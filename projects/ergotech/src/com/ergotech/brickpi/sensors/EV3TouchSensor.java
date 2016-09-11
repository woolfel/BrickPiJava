/*
 *  Copyright ErgoTech Systems, Inc 2014
 *
 * This file is made available online through a Creative Commons Attribution-ShareAlike 3.0  license.
 * (http://creativecommons.org/licenses/by-sa/3.0/)
 *
 *  This is a library of functions for the RPi to communicate with the BrickPi.
 */
package com.ergotech.brickpi.sensors;

/**
 * Representation of a Touch Sensor.
 * @author sdaubin
 */
public class EV3TouchSensor extends Sensor {

    /**
     * Returns an instance of this sensor.
     */
    public EV3TouchSensor() {
        super(SensorType.EV3Touch);
    }

    /**
     * Returns the last value read from the sensor, or false if a value has not
     * been read.
     *
     * @return the last value read from the sensor.
     */
    public boolean isSet() {
        return getValue() > 1020;
    }
    
     /**
     * Returns the 1 or 0 for consistency with the sensor interface.
     */
    public int getValue() {
        return super.getValue() > 1020 ? 1 : 0;
    }


}
