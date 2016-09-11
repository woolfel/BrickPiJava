/*
 *  Copyright ErgoTech Systems, Inc 2014
 *
 * This file is made available online through a Creative Commons Attribution-ShareAlike 3.0  license.
 * (http://creativecommons.org/licenses/by-sa/3.0/)
 *
 *  This is a library of functions for the RPi to communicate with the BrickPi.
 */
package com.ergotech.brickpi;

import com.pi4j.io.serial.Serial;
import com.pi4j.io.serial.SerialFactory;
import com.pi4j.io.serial.SerialPortException;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class provides utility method for communication with the brick pi.
 */
public class BrickPi extends BrickPiCommunications {

    private static final Logger LOGGER = 
            LoggerFactory.getLogger(BrickPi.class.getName());

    public static final String DEFAULT_DEVICE = "/dev/ttyAMA0";

    /**
     * The singleton instance of this class.
     */
    protected static BrickPi brickPi;

    /**
     * Serial port instance.
     */
    protected final Serial serial;

    /**
     * Return the brick pi singleton.
     *
     * @return the brick pi instance.
     */
    public static BrickPi getBrickPi() {
        if (brickPi == null) {
            try {
                // we'll try/catch the exception and log it here.
                // the "getBrickPi" could be called often and should not
                // fail (at least after initial debugging) and catch the 
                // exception externally might be irritating after a while...
                brickPi = new BrickPi();

            } catch (IOException ex) {
                LOGGER.error(ex.getMessage(), ex);
            }
        }
        return brickPi;
    }
    
    public BrickPi() throws IOException {
        this(DEFAULT_DEVICE, 500000);
    }

    /**
     * Create the brick pi instance. This will only occur on the "getBrickPi"
     * call, and only if it has not already been created.
     *
     * @throws java.io.IOException thrown if serial open throws a
     * SerialPortException
     */
    public BrickPi(String device, int baudRate) throws IOException {
        try {
            serial = SerialFactory.createInstance();
            //System.out.println ("Port opening... "  + com.pi4j.wiringpi.Serial.serialOpen("/dev/ttyAMA0", 500000));
            System.out.println("Opening Serial Port " + device + ':' + baudRate);
            serial.open(device, baudRate);
            System.out.println("port open: " + serial.isOpen());
            // the listener thread in PI4J currently waits 100ms to see if data is available.
            // That's a little long for BrickPi applications.
        } catch (SerialPortException se) {
            // never let a runtime exception pass.  It can crash a whole application
            // since you won't necessarily see it until it fails...
            LOGGER.error(se.getMessage(), se);
            throw new IOException("Failed to open communications to BrickPi");
        }
    }

    /**
     * Send a packet to the brick pi.
     *
     * @param destinationAddress
     * @param packet
     */
    protected void sendToBrickPi(byte destinationAddress, byte[] packet) {
        // clear the read buffer before writing...
        serial.flush();
        // the checksum is the sum of all the bytes in the entire packet EXCEPT the checksum
        int checksum = destinationAddress + packet.length;
        for (byte toAdd : packet) {
            checksum += (int) (toAdd & 0xFF);
        }
        byte[] toSend = new byte[packet.length + 3];
        System.arraycopy(packet, 0, toSend, 3, packet.length);
        toSend[0] = destinationAddress;
        toSend[1] = (byte) (checksum & 0xFF);  // checksum...
        toSend[2] = (byte) (packet.length & 0xFF);
        if (DEBUG_LEVEL > 0) {
            StringBuffer output = new StringBuffer();
            output.append("Sending");
            for (byte toAdd : toSend) {
                output.append(" ");
                output.append(Integer.toHexString(toAdd & 0xFF));
            }
            System.out.println(output.toString());
        }
        serial.write(toSend);
        //serial.write(packet);
    }

    /**
     * Read a packet from the brick pi.
     *
     * @param timeout total read timeout in ms
     * @return the packet read from the serial port/brickpi
     * @throws java.io.IOException thrown if there's a timeout reading the port.
     */
    protected byte[] readFromBrickPi(int timeout) throws IOException { // timeout in mS

        int delay = timeout / 5;  // we'll wait a maximum of timeout
        while (serial.availableBytes() < 2 && delay-- >= 0) { // we need at least the checksum and bytecount (2 bytes)
            LOGGER.debug("Available: {}", serial.availableBytes());
            try {
                Thread.sleep(5);  // 5ms

            } catch (InterruptedException ex) {
                LOGGER.error(ex.getMessage(), ex);
            }
        }

        if (serial.availableBytes() < 1) {
            throw new IOException("Read timed out - Header");
        }

        // the first byte of the received packet in the checksum.
        // the second is the number of bytes in the packet.
        byte checksum = (byte) serial.read();
        byte packetSize = (byte) serial.read();  // the packet size does not include this two byte header.
        int inCheck = packetSize;  // the incoming checksum does not include the checksum...

        // so, we have packetSize bytes left to read.
        // delay should still be good.  If we had to wait above, it will be less than timeout/5
        // but the overall timeout in the method should still max out at timeout.
        while (serial.availableBytes() < packetSize && delay-- >= 0) { // we need at least the checksum and bytecount (2 bytes)
            try {
                Thread.sleep(5);  // 5ms

            } catch (InterruptedException ex) {
                LOGGER.error(ex.getMessage(), ex);
            }
        }

        if (serial.availableBytes() < packetSize) {
            throw new IOException("Read timed out - Packet");
        }

        byte[] packet = new byte[packetSize];
        for (int counter = 0; counter < packetSize; counter++) {
            packet[counter] = (byte) (serial.read() & 0xFF);
            inCheck += (int) (packet[counter] & 0xFF);
        }
        if (DEBUG_LEVEL > 0) {
            StringBuffer input = new StringBuffer();
            input.append("Received ");
            input.append(Integer.toHexString(checksum & 0xFF));
            input.append(" ");
            input.append(Integer.toHexString(packetSize & 0xFF));
            for (byte received : packet) {
                input.append(" ");
                input.append(Integer.toHexString(received & 0xFF));
            }
            System.out.println(input.toString());
        }

        if ((inCheck & 0xFF) != (checksum & 0xFF)) {
            throw new IOException("Bad Checksum " + inCheck + " expected " + checksum);
        }
        // if we get to here, all is well.
        return packet;
    }

 }
