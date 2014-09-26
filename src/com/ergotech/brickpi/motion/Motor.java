/*
 *  Copyright ErgoTech Systems, Inc 2014
 *
 * This file is made available online through a Creative Commons Attribution-ShareAlike 3.0  license.
 * (http://creativecommons.org/licenses/by-sa/3.0/)
 *
 *  This is a library of functions for the RPi to communicate with the BrickPi.
 */
package com.ergotech.brickpi.motion;

import com.ergotech.brickpi.BrickPiCommunications;
import java.util.BitSet;

/**
 *
 * The class representing a motor.
 */
public class Motor {

    public enum Direction {

        CLOCKWISE, COUNTER_CLOCKWISE
    };

    /**
     * The current commanded output of the motor. (0-255)
     */
    protected int commandedOutput;

    /**
     * The number of ticks per revolution of the motor.
     */
    protected int ticksPerRevolution;

    /**
     * The current value as read from the encoder. This value may be
     * Integer.MAX_VALUE if the encoder has not been read, does not exist or is
     * otherwise invalid.
     *
     */
    protected volatile int currentEncoderValue;

    /**
     * The current speed as read from the encoder. This value may be
     * Integer.MAX_VALUE if the encoder has not been read, does not exist or is
     * otherwise invalid.
     *
     */
    protected volatile double currentSpeed;

    /**
     * Last reading time. The last time the encoder value was update. Used to
     * calculate the speed.
     */
    protected volatile long lastReadingTime;

    /**
     * The current commanded direction of the motor.
     */
    protected Direction direction;

    /**
     * Whether commanded enabled.
     */
    protected boolean enabled;

    /**
     * The encoder offset.
     */
    protected int encoderOffset;

    /**
     * The target of a requested motion or zero if no motion is requested.
     */
    protected double rotations;

    /**
     * The encoder reading at the start of a requested rotation.
     */
    protected int encoderAtStartOfRotation;

    /**
     * The brick pi communications instance to which this motor is associated.
     */
    protected BrickPiCommunications brickPi;

    /**
     * Create the motor.
     */
    public Motor() {
        currentSpeed = Double.MAX_VALUE;
        currentEncoderValue = Integer.MAX_VALUE;
        ticksPerRevolution = 1440;  // according to everything I've read, this should be 720;  The reality seems to be 1440 (for me)
    }

    /** returns the instance to which this Motor is associated.
     * @return  the current BrickPi instance to which this Motor is associated, or null if it
     * is not currently associated with a controller.
    */
    public BrickPiCommunications getBrickPi() {
        return brickPi;
    }
    
    /** Set the instance to which this Motor is associated.
     * @param brickPi the current BrickPi instance to which this Motor is associated, or null 
     * to remove the association with a controller.*/
    public void setBrickPi(BrickPiCommunications brickPi) {
        this.brickPi = brickPi;
    }

    /**
     * Reset the encoder reading.
     */
    public void resetEncoder() {
        encoderOffset = currentEncoderValue;
    }

    /**
     * The number of ticks per revolution of the motor. This is used to
     * calculate the motor speed in RPM.
     *
     * @return The number of ticks per revolution of the motor
     */
    public int getTicksPerRevolution() {
        return ticksPerRevolution;
    }

    /**
     * Sets the number of ticks per revolution of the motor
     *
     * @param ticksPerRevolution The number of ticks per revolution of the motor
     */
    public void setTicksPerRevolution(int ticksPerRevolution) {
        this.ticksPerRevolution = ticksPerRevolution;
    }

    /**
     * returns the last raw encoder value read from the brick pi.
     *
     * @return the current raw encoder value.
     */
    public int getCurrentEncoderValue() {
        return currentEncoderValue - encoderOffset;
    }

    /**
     * The calculated current speed. This will return -1 if the speed is
     * unknown. Counter clockwise speeds will be negative.
     *
     * @return the current calculated speed or -1 if the speed is unknown.
     */
    public double getCurrentSpeed() {
        // internally we keep a positive speed.
        double tmp = currentSpeed;
        if (currentSpeed == Double.MAX_VALUE) {
            tmp = -1;
        } else {
            if (getDirection() == Direction.COUNTER_CLOCKWISE) {
                tmp = -tmp;
            }
        }
        return tmp;
    }

    public long getLastReadingTime() {
        return lastReadingTime;
    }

//    /**
//     * Encode the data associated with the motor encoder offsets to the outgoing message.
//     * Encoder offsets are not yet supports so this encodes a single bit to the bitset
//     * 
//     * I'm not totally sure that the encoder values should be associated with the motors.
//     *
//     * @param message the BitSet representing the outgoing message.
//     * @param startLocation the starting bit location in the message at which to
//     * begin encoding
//     * @return the ending location. That is the startLocation for the next
//     * encoding.
//     */
//    public int encodeEncoderOffsetToValueRequest(BitSet message, int startLocation) {
//        return startLocation; // next bit..
//    }
    /**
     * Encode the data associated with the motor to the outgoing message. The
     * default method does nothing.
     *
     * @param message the BitSet representing the outgoing message.
     * @param startLocation the starting bit location in the message at which to
     * begin encoding
     * @return the ending location. That is the startLocation for the next
     * encoding.
     */
    public int encodeToValueRequest(BitSet message, int startLocation) {
        // here, I think, is how this bit-encoding works.
        // the value is encoded LSb first into the array
        // so, given the 4-bit value 3
        // the array would be 1100
        // that's sort of the same as saying that the value is bit-reversed
        // could be wrong here, writing the documentation before running it...
        // first the motor-enable
        message.set(startLocation++, enabled);
        // direction
        if (direction == Direction.CLOCKWISE) {  // could be reversed 
            message.set(startLocation++);
        } else {
            message.clear(startLocation++);
        }
        // speed
        int tmpSpeed = commandedOutput;
        for (int counter = 0; counter < 8; counter++) {
            message.set(startLocation++, (tmpSpeed & 0x1) == 1);
            tmpSpeed >>= 1;
        }
        return startLocation; // nothing to encode.
    }

    /**
     * Decode the encoder data associated with the motor from the incoming
     * message. This will set the currentSpeed variable.
     *
     * @param wordLength the number of bits to read
     * @param message the BitSet representing the outgoing message.
     * @param startLocation the starting bit location in the message at which to
     * begin decoding
     */
    public void decodeValues(int wordLength, byte[] message, int startLocation) {
        long currentTime = System.currentTimeMillis();
        int tmpEncoderValue = BrickPiCommunications.decodeInt(wordLength, message, startLocation);
        // if the encoder was reset before there was an encoder value, then this next clause 
        // kicks in as soon as we have a value.
        if (encoderOffset == Integer.MAX_VALUE) {
            encoderOffset = tmpEncoderValue;
        }
        if (isEnabled()) { // don't calculate the speed if we're not enabled...
            if (currentEncoderValue != Integer.MAX_VALUE) {
                double readingDifference = currentEncoderValue - tmpEncoderValue;
                long timeDifference = currentTime - lastReadingTime;
                //System.out.println(" Motor Speed " + readingDifference + " " + timeDifference);
                double immediateSpeed = Math.abs(readingDifference / timeDifference / ticksPerRevolution * 1000 * 60);
//            // could run a little low-pass filtering here, but it needs to be corrected
//            // for direction changes, etc.  - Just set currentEncoderValue and currentSpeed when speed or direction are set
//            if (currentSpeed == Double.MAX_VALUE) {
//                currentSpeed = immediateSpeed;
//            } else {
//                currentSpeed = Math.abs((currentSpeed * 4 + immediateSpeed) / 5);
//            }
                currentSpeed = immediateSpeed;
            }
        } else {
            currentSpeed = 0;
        }
        lastReadingTime = currentTime;
        currentEncoderValue = tmpEncoderValue;
        if (rotations != 0) {
            if (encoderAtStartOfRotation == Integer.MAX_VALUE) {
                encoderAtStartOfRotation = currentEncoderValue;
            } else {
                int difference = currentEncoderValue - encoderAtStartOfRotation;
                // let's hope the motors going in the correct direction or we'll 
                // never hit this point
                if (Math.abs(difference) > Math.abs(rotations * ticksPerRevolution)) {
                    // we made it, stop here
                    setCommandedOutput(0);
                }
            }
        }
    }

    /**
     * Returns the commanded output
     *
     * @return the commanded output +/-0-255 Negative indicates Counter
     * Clockwise (however you choose to define that).
     */
    public int getCommandedOutput() {
        int tmp = commandedOutput;
        if (getDirection() == Direction.COUNTER_CLOCKWISE) {
            tmp = -tmp;
        }
        return tmp;

    }

    /**
     * Set the commanded output. For convenience the speed is set as an int
     * although the max is still 255.
     *
     * @param commandedOutput +/- 0-255 Negative values indicate Counter
     * Clockwise (however you choose to define that).
     */
    public void setCommandedOutput(int commandedOutput) {
        // internally we'll keep a positive speed.
        if (commandedOutput < 0) {
            this.commandedOutput = -commandedOutput;
            setDirection(Direction.COUNTER_CLOCKWISE);
        } else {
            this.commandedOutput = commandedOutput;
            setDirection(Direction.CLOCKWISE);
        }
        if (brickPi != null) {
            // wake up the update thread so that the values are immediately send to the brick pi
            synchronized (brickPi) {
                brickPi.notify();
            }
        }

    }

    /**
     * Returns the direction.
     *
     * @return
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * Set the direction.
     *
     * @param direction
     */
    public void setDirection(Direction direction) {
        this.direction = direction;
        if (brickPi != null) {
            // wake up the update thread so that the values are immediately send to the brick pi
            synchronized (brickPi) {
                brickPi.notify();
            }
        }
    }

    /**
     * Returns the state of the enabled flag.
     *
     * @return true if the motor is enabled.
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Enables or disables the motor
     *
     * @param enabled set to true to enable the motor.
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (brickPi != null) {
            // wake up the update thread so that the values are immediately send to the brick pi
            synchronized (brickPi) {
                brickPi.notify();
            }
        }
    }

    /**
     * Rotate the motor a certain number of rotations and then stop. Fractional
     * rotation (eg 0.25) are acceptable. Negative values imply
     * counter-clockwise rotation.
     *
     * @param rotations the number of rotations to complete.
     * @param commandedOutput the speed at which to perform the rotation.
     *
     */
    public void rotate(double rotations, int commandedOutput) {
        this.rotations = rotations;
        encoderAtStartOfRotation = Integer.MAX_VALUE;
        brickPi.setUpdateDelay(5); // increase the scan time
        setCommandedOutput(commandedOutput);
        setEnabled(true);
    }

}
