package frc.robot;
import edu.wpi.first.wpilibj.Timer;
import frc.lib.Calibration.Calibration;
import frc.lib.DataServer.Signal;
import frc.lib.WebServer.CasseroleDriverView;

public class TestJSONDataSource {
	
	public int TestData1;
	public double TestData2;
	public double TestData3;
	public boolean TestBool;
	
	public double counter;
	
	public Calibration cal1;
    public Calibration cal2;
    
    Signal testVal1;
    Signal testVal2;
    Signal batteryVolts;
    Signal batteryCurrent;
    Signal dtLeftMotorSpeed;
    Signal dtRightMotorSpeed;
    Signal shooterMotorSpeed;

    public TestJSONDataSource(){
        initDataGeneration();
        startDataGeneration();
    }
	
	public void initDataGeneration(){
				
		CasseroleDriverView.newDial("Test Val1 RPM", 0, 200, 25, 55, 130);
		CasseroleDriverView.newDial("Test Val2 ft/s", -20, 20, 5, -3, 3);
		CasseroleDriverView.newDial("Battery Volts", 0, 15, 1, 10.5, 13.5);
		CasseroleDriverView.newWebcam("Test WebCam", "http://plazacam.studentaffairs.duke.edu/mjpg/video.mjpg", 50.0, 25.0, 90.0);
		CasseroleDriverView.newWebcam("Test WebCam2", "http://86.41.192.75:1024/mjpg/video.mjpg", 25.0, 75.0, 90.0);
		CasseroleDriverView.newBoolean("Test Bool Display 1", "red");
		CasseroleDriverView.newBoolean("Test Bool Display 2", "green");
		CasseroleDriverView.newBoolean("Test Bool Display 3", "yellow");
		CasseroleDriverView.newStringBox("Test String");
		
		testVal1 =new Signal("Test Val1", "RPM");
		testVal2 =new Signal("Test Val2", "ft/s");
		batteryVolts =new Signal("Battery Volts", "V");
		batteryCurrent =new Signal("Battery Current", "A");
		dtLeftMotorSpeed =new Signal("DT Left Motor Speed", "RPM");
		dtRightMotorSpeed =new Signal("DT Right Motor Speed", "RPM");
		shooterMotorSpeed =new Signal("Shooter Motor Speed", "RPM");
		
	}
	
	public void startDataGeneration(){
		cal1 = new Calibration("Cal1", 10,-5,100);
		cal2 = new Calibration("Cal2",15.0);
		counter = 0;
		
		Thread dataGenThread = new Thread(new Runnable() {
			@Override
			public void run(){
				while(true){
                    double loopTime_ms = Timer.getFPGATimestamp()*1000;
					TestData1 = TestData1 - 3 + (int)cal1.get();
					TestData2 = TestData1/2.0 + 4.0 + cal2.get();
					TestData3 = cal1.get()*Math.sin(counter/cal2.get())+50;
					TestBool = TestData3 > 87.0;
					
					testVal1.addSample(loopTime_ms, TestData3);
					testVal2.addSample(loopTime_ms, TestData3 * TestData3);
					batteryVolts.addSample(loopTime_ms, (counter/5.0) % 12);
					batteryCurrent.addSample(loopTime_ms,TestData3*TestData1 % 3000);
					dtLeftMotorSpeed.addSample(loopTime_ms,(counter/5.0) % 12 * TestData3 * 0.1);
					dtRightMotorSpeed.addSample(loopTime_ms,Math.floor(TestData3/5)*5);
					shooterMotorSpeed.addSample(loopTime_ms,Math.random()+TestData3);
					
					
					CasseroleDriverView.setDialValue("Test Val1 RPM", TestData3);
					CasseroleDriverView.setDialValue("Test Val2 ft/s", 5.0);
					CasseroleDriverView.setDialValue("Battery Volts", (counter/5.0) % 12);
					CasseroleDriverView.setStringBox("Test String", "Test value " + Double.toString(counter));
					CasseroleDriverView.setBoolean("Test Bool Display 1", TestData3 > 45.0);
					CasseroleDriverView.setBoolean("Test Bool Display 2", TestData3 > 50.0);
					CasseroleDriverView.setBoolean("Test Bool Display 3", TestData3 > 55.0);
					
					counter++;
					
					try {
						Thread.sleep(20);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			}
		});
		
		TestData1 = 0;
		TestData2 = 0;
		TestBool = false;
		dataGenThread.setName("CasseroleTestDataGenerator");
		dataGenThread.start();
	}

}