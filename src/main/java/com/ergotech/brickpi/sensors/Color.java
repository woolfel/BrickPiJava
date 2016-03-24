package com.ergotech.brickpi.sensors;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * The colors that an EV3 sensor reads.
 * @author sdaubin
 * @see EV3ColorSensor
 */
public enum Color {
    
    None(0),
    Black(1),
    Blue(2),
    Green(3),
    Yellow(4),
    Red(5),
    White(6),
    Brown(7),
    Unknown(666);
    
    static final Map<Integer, Color> COLORS = createColorMap();

    private final int value;

    private Color(int value) {
        this.value = value;
    }

    private static Map<Integer, Color> createColorMap() {
        Map<Integer,Color> map = new HashMap<>();
        for (Color color : Color.values()) {
            map.put(color.value, color);
        }
        return Collections.unmodifiableMap(map);
    }
    
    
}
