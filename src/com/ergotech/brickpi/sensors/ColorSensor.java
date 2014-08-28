/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ergotech.brickpi.sensors;

import com.ergotech.brickpi.BrickPi;

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
        value = BrickPi.decodeInt(3, message, startLocation);
        return value;
    }
}
