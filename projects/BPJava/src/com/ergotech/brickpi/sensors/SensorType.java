package com.ergotech.brickpi.sensors;

/**
 * An enumeration of sensor types.  The main thing we need to know to operate a sensor
 * is its type id and the number of bits that the sensor decodes on update.
 * @author sdaubin
 *
 */
public enum SensorType {
    
    Raw(                    0, 10),
    LightOff(               0, 10),
    LightOn(                (Sensor.MASK_D0_M | Sensor.MASK_D0_S), 10),
    Touch(                  32, 1),
    Ultrasonic(             33, 8),
    UltrasonicSS(           34, 8),
    ColorFull(              36, 3),
    /**
     * Reflected light intensity (0 to 100).
     */
    EV3ColorReflected(      50, 16),
    /**
     * Ambient light intensity (0 to 100)
     */
    EV3ColorAmbient(        51, 16),
    /**
     * Detected color.
     * @see Color
     */
    EV3Color(               52, 16),
    EV3ColorRawReflected(   53, 32),
    /**
     * Raw Color Components.
     */
    EV3ColorRawColor(       54, 16),
    EV3Touch(               67, 16);      
    
    private final byte type;
    private final int decodeBitCount;
    
    private SensorType(int type, int decodeBitCount) {
        this.type = (byte)type;
        this.decodeBitCount = decodeBitCount;
    }

    byte getType() {
        return type;
    }

    int getDecodeBitCount() {
        return decodeBitCount;
    }
}
