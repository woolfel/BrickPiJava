package com.ergotech.brickpi.motion;

/**
 * An enumeration of BrickPi motor ports.
 * @author sdaubin
 *
 */
public enum MotorPort {
    
    MA(0),
    MB(1),
    MC(2),
    MD(3);

    private final int port;
    
    private MotorPort(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }
}
