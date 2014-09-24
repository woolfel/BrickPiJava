/*
 *  Copyright ErgoTech Systems, Inc 2014
 *
 * This file is made available online through a Creative Commons Attribution-ShareAlike 3.0  license.
 * (http://creativecommons.org/licenses/by-sa/3.0/)
 *
 *  This is a library of functions for the RPi to communicate with the BrickPi.
 */
package com.ergotech.brickpi;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class provides utility method for communication remotely with the brick pi.
 * 
 * This approach assumes that a network to serial driver of some sort is running
 * on the RPi.  It has been tested with ser2net.  The 500k baud setting requires
 * ser2net version 2.8 or higher. 
 * 
 * Add this line to /etc/ser2net.conf
 * 
 * 3333:raw:0:/dev/ttyAMA0:500000 8DATABITS NONE 1STOPBIT
 * 
 * unless you have a reason to leave them there you should delete, or comment
 * out all the other configuration lines.
 * 
 */
public class RemoteBrickPi extends BrickPiCommunications {

    /**
     * The socket connection to the BrickPi.
     */
    protected Socket socket;

    /**
     * The IP address or hostname of the Pi.
     */
    protected String piAddress;

    /**
     * The port that ser2net is listening on. This defaults to 3333.
     */
    protected int ser2netPort;

    /**
     * Create an instance. Since this is a remote call, it is possible
     * to have multiple instance of a this class.  No singleton here...
     */
    public RemoteBrickPi() {
        this(null, 3333);
    }

    /**
     * Create an instance with the pi address.  The ser2net port will default to 3333.
     * @param piAddress the address of the RPi
     */
    public RemoteBrickPi(String piAddress) {
        this(piAddress, 3333);
   }

     /**
     * Create an instance with the pi address and the ser2net port.
     * @param piAddress the address of the RPi
     * @param ser2netPort the port number for 
     */
    public RemoteBrickPi(String piAddress, int ser2netPort) {
        super();
        this.ser2netPort = ser2netPort;
        this.piAddress = piAddress;
   }

    /**
     * Returns the IP address or hostname of the Pi.
     *
     * @return the IP address or hostname of the Pi.
     */
    public String getPiAddress() {
        return piAddress;
    }

    /**
     * Sets the IP address or hostname of the Pi.
     *
     * @param piAddress the IP address or hostname of the Pi.
     */
    public void setPiAddress(String piAddress) {
        closeSocket();
        this.piAddress = piAddress;
    }

    /**
     * Returns the port that ser2net is listening on.
     *
     * @return the port on which ser2net is listening.
     */
    public int getSer2netPort() {
        return ser2netPort;
    }

    /**
     * Sets the port that ser2net is listening on.
     *
     * @param ser2netPort the port on which ser2net is listening.
     */
    public void setSer2netPort(int ser2netPort) {
        closeSocket();
        this.ser2netPort = ser2netPort;
    }

    /**
     * Initialize the socket connection. This will succeed only if the Pi
     * address has been set (and the ser2net port address if not 3333).
     *
     * @return the TCP connection to the Pi
     * @throws java.io.IOException thrown if there is a network connection
     * connecting to the Pi on the ser2net port.
     */
    protected Socket getSocket() throws IOException {
        if (socket == null && piAddress != null && piAddress.trim().length() > 0 && ser2netPort > 0) {
            // open the socket connection. 
            socket = new Socket(piAddress, ser2netPort);
        }
        return socket;
    }

    // I really don't like duplicating the code from send and read.  The Socket
    // could be implemented as the "Serial" interface, but I don't really like that
    // because it would add dependencies on p4j to this class.
    // The serial instance could be wrapped in an input/output stream.  This
    // is just more effort than I'm willing to put in at the moment, so copied code it is...
    /**
     * Send a packet to the brick pi.
     *
     * @param destinationAddress
     * @param packet
     */
    @Override
    protected void sendToBrickPi(byte destinationAddress, byte[] packet) {
        try {
            Socket localSocket = getSocket();
            if (localSocket != null) {
                OutputStream out = localSocket.getOutputStream();
                InputStream in = localSocket.getInputStream();
                // clear the read buffer before writing...
                while (in.available() > 0) {
                    in.read();
                }
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
                out.write(toSend);
            }
        } catch (IOException ex) {
            closeSocket();
        }
    }

    protected void closeSocket() {
        // close and null the socket on any error - it will be reopened.
        try {
            socket.close();
        } catch (Exception any) {
            // ignore.
        }
        socket = null;
    }

    /**
     * Read a packet from the brick pi.
     *
     * @param timeout total read timeout in ms
     * @return the packet read from the localSocket port/brickpi
     * @throws java.io.IOException thrown if there's a timeout reading the port.
     */
    @Override
    protected byte[] readFromBrickPi(int timeout) throws IOException { // timeout in mS
        byte[] packet = null;
        try {
            if (getSocket() != null) {
                InputStream in = getSocket().getInputStream();
                //System.out.println("Available Input " + in.available());

                int delay = timeout / 5;  // we'll wait a maximum of timeout
                while (in.available() < 2 && delay-- >= 0) { // we need at least the checksum and bytecount (2 bytes)
                    try {
                        Thread.sleep(5);  // 5ms

                    } catch (InterruptedException ex) {
                        Logger.getLogger(RemoteBrickPi.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                if (in.available() < 1) {
                    throw new IOException("Read timed out - Header");
                }

                // the first byte of the recieved packet in the checksum.
                // the second is the number of bytes in the packet.
                byte checksum = (byte) in.read();
                byte packetSize = (byte) in.read();  // the packet size does not include this two byte header.
                int inCheck = packetSize;  // the incoming checksum does not include the checksum...

                // so, we have packetSize bytes left to read.
                // delay should still be good.  If we had to wait above, it will be less than timeout/5
                // but the overall timeout in the method should still max out at timeout.
                while (in.available() < packetSize && delay-- >= 0) { // we need at least the checksum and bytecount (2 bytes)
                    try {
                        Thread.sleep(5);  // 5ms

                    } catch (InterruptedException ex) {
                        Logger.getLogger(RemoteBrickPi.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                if (in.available() < packetSize) {
                    throw new IOException("Read timed out - Packet");
                }

                packet = new byte[packetSize];
                for (int counter = 0; counter < packetSize; counter++) {
                    packet[counter] = (byte) (in.read() & 0xFF);
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
            }
        } catch (IOException ex) {
            closeSocket();
        }
        // if we get to here, all is well.
        return packet;
    }

}
