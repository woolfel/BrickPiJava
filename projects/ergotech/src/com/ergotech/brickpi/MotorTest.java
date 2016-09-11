package com.ergotech.brickpi;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ergotech.brickpi.motion.Motor;
import com.ergotech.brickpi.motion.MotorPort;

public class MotorTest {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		BrickPi bpi = new BrickPi();
        try {
            // configure the sensors
            bpi.setupSensors();
        } catch (Exception ex) {
            Logger.getLogger(BrickPiTests.class.getName()).log(Level.SEVERE, null, ex);
        }
		Motor ma1 = new Motor();
		Motor ma2 = new Motor();
		Motor ma3 = new Motor();
		Motor ma4 = new Motor();
		bpi.setMotor(ma1, MotorPort.MA);
		bpi.setMotor(ma2, MotorPort.MB);
		bpi.setMotor(ma3, MotorPort.MC);
		bpi.setMotor(ma4, MotorPort.MD);
        // there's a problem here since the code will exit before the rotation is complete...
        try {
            ma1.rotate(5, 250);
            ma4.rotate(5, 250);
            // get the updated values.
            Thread.sleep(15000); // wait for the values to be read....
        } catch (Exception ex) {
            Logger.getLogger(BrickPiTests.class.getName()).log(Level.SEVERE, null, ex);
        }
        ma1.setCommandedOutput(0);
        ma4.setCommandedOutput(0);
        System.out.println("Done ---");
	}

}
