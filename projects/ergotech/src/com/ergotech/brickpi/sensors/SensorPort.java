package com.ergotech.brickpi.sensors;

/**
 * An enumeration of BrickPi sensor ports.
 * @author sdaubin
 *
 */
public enum SensorPort {
    
    S1(0),
    S2(1),
    S3(2),
    S4(3);

    private final int port;
    
    private SensorPort(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }
}
