package org.usfirst.frc.team948.robot;

import org.usfirst.frc.team948.robot.subsystems.Drive;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This is a demo program showing the use of OpenCV to do vision processing. The
 * image is acquired from the USB camera, then a rectangle is put on the image
 * and sent to the dashboard. OpenCV has many methods for different types of
 * processing.
 */
public class Robot extends IterativeRobot {
	private static visionProc proccesor;
	private static Timer clock = new Timer();
	public final double tickDistance = 30;
	public static DS2016 oi;
	public static Drive drive;
	public static RobotMap robotMap;

	@Override
	public void robotInit() {
		clock.start();
		UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
		camera.setExposureManual(-11);
		// camera.setResolution(640, 380);
		proccesor = new visionProc().start();
		SmartDashboard.putNumber("Time", clock.get());
		robotMap = new RobotMap();
		drive = new Drive();
	}

	public void teleopPeriodic() {
		periodicAll();
		Scheduler.getInstance().run();
	}

	public void disabledPeriodic() {
		periodicAll();
	}
	
	public void periodicAll(){
		SmartDashboard.putNumber("leftEncoder", robotMap.leftEncoder.get());
		SmartDashboard.putNumber("rightEncoder", robotMap.rightEncoder.get());
		SmartDashboard.putNumber("Time", clock.get());
		if (proccesor.dataExists()) {
			SmartDashboard.putBoolean("NoDataOut", false);
			visionField data = proccesor.getData();
			SmartDashboard.putNumber("Theta", (data.theta * 180.0) / Math.PI);
			SmartDashboard.putNumber("V", data.v);
			SmartDashboard.putNumber("Gamma", (data.gamma * 180.0) / Math.PI);
			SmartDashboard.putNumber("Zeta", data.zeta);
			SmartDashboard.putNumber("Omega", data.omega);
		} else {
			SmartDashboard.putBoolean("NoDataOut", true);
		}
	}
}
