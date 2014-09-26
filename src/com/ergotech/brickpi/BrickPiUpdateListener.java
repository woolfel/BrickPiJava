/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ergotech.brickpi;

/**
 *
 * This listener can be attached to a BrickPi instance and will be notified
 * each time values are update over the serial (or remote serial) link.
 */
public interface BrickPiUpdateListener {
    
    /**
     * Called whenever a poll of values is complete. 
     * @param <T> a BrickPi or remote BrickPi instance.
     * @param source the source of this event.
     */
    public <T extends BrickPiCommunications> void updateReceived (T source);
    
}
