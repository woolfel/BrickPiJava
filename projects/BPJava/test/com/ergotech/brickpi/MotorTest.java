package com.ergotech.brickpi;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ergotech.brickpi.motion.Motor;
import com.ergotech.brickpi.motion.MotorPort;

public class MotorTest {

	public MotorTest() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		BrickPi bpi = new BrickPi();
		Motor ma1 = new Motor();
		bpi.setMotor(ma1, MotorPort.MA);
        ma1.rotate(1, 50);
        ma1.setCommandedOutput(150);
        // there's a problem here since the code will exit before the rotation is complete...
        try {
            // get the updated values.
            Thread.sleep(5000); // wait for the values to be read....
        } catch (InterruptedException ex) {
            Logger.getLogger(BrickPiTests.class.getName()).log(Level.SEVERE, null, ex);
        }
        ma1.setCommandedOutput(0);
        System.out.println("Done ---");
	}

}
