/*
 *
 * This file is made available online through a Creative Commons Attribution-ShareAlike 3.0  license.
 * (http://creativecommons.org/licenses/by-sa/3.0/)
 *
 *  This is a library of functions for the RPi to communicate with the BrickPi.
 */
package com.ergotech.brickpi.sensors;

import com.ergotech.brickpi.BrickPiCommunications;

/**
 *
 * @author kenrik
 */
public class ColorSensor extends Sensor {

    public ColorSensor() {
    }

    @Override
    public byte getSensorType() {
        return TYPE_SENSOR_COLOR_FULL;
    }

    @Override
    public int decodeValues(byte[] message, int startLocation) {
        value = BrickPiCommunications.decodeInt(3, message, startLocation);
        return value;
    }
}
