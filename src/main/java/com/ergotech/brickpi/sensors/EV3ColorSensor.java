package com.ergotech.brickpi.sensors;

/**
 * The EV3 color sensor.
 * @author sdaubin
 *
 */
public class EV3ColorSensor extends Sensor {

    public EV3ColorSensor() {
        super(SensorType.EV3Color);
    }
    
    public Color getColor() {
        Color color = Color.COLORS.get(getValue());
        return color == null ? Color.Unknown : color;
    }

}
