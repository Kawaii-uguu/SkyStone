package org.firstinspires.ftc.robotcontroller.external.samples.TEAMSTUFF;
//more stuff
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.AnalogOutput;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.I2cDevice;

//basic stuff
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

/**
 * removed instructions to make this easier to navigate
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */
 
@TeleOp(name="Made in Heaven", group="Linear Opmode")
//@Disabled
public class madeinheaven extends LinearOpMode {
    //extra stuff for auto
    double lpower = 0;
    static final int    CYCLE_MS    =   50;
    static final double INCREMENT   = 0.01;
    static final double MAX_FWD     =  5.0;
    boolean turnleft  = false;
    String modestring;
    double cordist;
    
    //lists driving modes:
    int drivemode[]={1,2,3};
    int drivindexer = 0;
    
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftBack = null;
    private DcMotor leftFront = null;
    private DcMotor rightBack = null;
    private DcMotor rightFront = null;
    
    //map the dist. sensor
    DistanceSensor sensorDistance;
    
    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        leftBack  = hardwareMap.get(DcMotor.class, "three");
        leftFront = hardwareMap.get(DcMotor.class, "one");
        rightBack = hardwareMap.get(DcMotor.class, "two");
        rightFront = hardwareMap.get(DcMotor.class, "zero");

//the statement below can be changed if we need to do so
        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        leftBack.setDirection(DcMotor.Direction.FORWARD);
        leftFront.setDirection(DcMotor.Direction.FORWARD);
        rightBack.setDirection(DcMotor.Direction.REVERSE);
        rightFront.setDirection(DcMotor.Direction.REVERSE);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            if drivemode[drivindexer] == 1{
              modestring = "Manual drive mode";
              // Setup a variable for each drive wheel to save power level for telemetry
              double leftPower;
              double rightPower;
              leftPower    = Range.clip(1, -1.0, 1.0) ;
              rightPower   = Range.clip(-1, -1.0, 1.0) ;
                  boolean mode;
                  double speed;
                
                  //multiplies speed
                  //cool concept but dpad-up will be hard to reach while using triggers and joysticks
                  //try bumpers or x button
                  if (gamepad1.dpad_up == true){
                    //toggle speed on
                      mode = true;
                    }else if (gamepad1.dpad_down == true){
                     //toggle speed off
                       mode = false;
                    }

                  if (mode == false){
                      speed = 0.5;
                  }else {
                      speed = 1;
                  }
                  /*
                  wait
                  this could be improved upon for ease of use
                  for instance, mapping acceleration to (a)
                  if we're already assigning speed modifiers to dpad, we shouldn't
                  also map speed modifiers to the joysticks
                  sample code for doing so:
                  ---------------------------------------
                  int accel;
                  if (gamepad.a == true){
                    accel = 1;
                  }else {
                    accel = 0;
                  }
                  leftBack.setPower(accel * speed);
                  leftFront.setPower(accel * speed);
                  rightBack.setPower(accel * speed);
                  rightFront.setPower(accel * speed);
                  ---------------------------------------
                  (something like that)
                  */
                
                  leftBack.setPower(gamepad1.left_stick_y * speed);
                  leftFront.setPower(gamepad1.left_stick_y * speed);
                  rightBack.setPower(gamepad1.right_stick_y * speed);
                  rightFront.setPower(gamepad1.right_stick_y * speed);
                  if (gamepad1.a == true){
                      //reverses direction
                      //could be mapped to y or x if a is remapped to acceleration
                      leftBack.setPower(-speed);
                      leftFront.setPower(-speed);
                      rightBack.setPower(-speed);
                      rightFront.setPower(-speed);
                  }
                  if (gamepad1.b == true){
                      //multiplies speed, think (B)-dashing
                      leftBack.setPower(speed * 2);
                      leftFront.setPower(speed * 2);
                      rightBack.setPower(speed * 2);
                      rightFront.setPower(speed * 2);  
                  }
                  if (gamepad1.left_stick_x < 0){
                       leftBack.setPower(-speed);
                       leftFront.setPower(-speed);//pushing L3 left
                       rightBack.setPower(speed);//turns it left
                       rightFront.setPower(speed);

                    } else if (gamepad1.left_stick_x > 0){
                        leftBack.setPower(speed);// so pushing L3 right
                        leftFront.setPower(speed);//should turn it right
                        rightBack.setPower(-speed);
                        rightFront.setPower(-speed);
                    } else if (gamepad1.right_stick_x > 0){
                        leftBack.setPower(speed);
                        leftFront.setPower(-speed);
                        rightBack.setPower(-speed);
                        rightFront.setPower(speed);
                        //please remove these^ v
                        //they're unnecessary and could be used for more useful commands
                        //for instance, they could be used to move appendages that will be added in the near future.
                   }else if (gamepad1.right_stick_x < 0){
                        leftBack.setPower(-speed);
                        leftFront.setPower(speed);
                        rightBack.setPower(speed);
                        rightFront.setPower(-speed);
                    }
                    //just a test
                    //turn the robot a set amount
                    if (gamepad1.dpad_left == true){
                      turnleft = true;
                    }
                  while(turnleft == true){
                    lpower += INCREMENT ;
                    if (lpower >= MAX_LFT ) {
                        lpower = MAX_LFT;
                        turnleft = false;
                        leftBack.setPower(0);
                        leftFront.setPower(0);
                        rightFront.setPower(0);
                        rightBack.setPower(0);
                    }
                  rightFront.setPower(lpower);
                  rightBack.setPower(lpower);
                  leftBack.setPower(-lpower);
                  leftFront.setPower(-lpower);
                  sleep(CYCLE_MS);
                }
            }//mode 1 ends here
            if (drivmode[drivindexer] == 2){
              modestring = "Distance Sensor mode";
              cordist = dist.getDistance(DistanceUnit.CM) - 3;
              //distance sensor stuff
              
              telemetry.addData("Distance (cm)" ,cordist);
              /*
              This recalibrates the distance because our distance sensor is inaccurate. 
              You can remove this if you don't need it.
              */
             //test loop
             if (cordist <= 6) {
              leftBack.setPower(0);
              leftFront.setPower(0);
              rightFront.setPower(0);
              rightBack.setPower(0);
             } else {
              leftBack.setPower(1);
              leftFront.setPower(1);
              rightFront.setPower(1);
              rightBack.setPower(1);
             }
              cordist = dist.getDistance(DistanceUnit.CM) - 3;
               //     String.format(Locale.US, "%.02f", dist.getDistance(DistanceUnit.CM)));
                telemetry.update();
            
            }
            if (drivmode[drivindexer] == 3){
              modestring = "Empty Mode"
            
            //space for new mode
            
            }
            
            //toggle drive modes
            if (gamepad.right_bumper == true){
              if drivindexer == 2{
                drivindexer = 0;
              }else{
                drivindexer++;
              }
            }
            if (gamepad.right_bumper == true){
              if drivindexer == 0{
                drivindexer = 2;
              }else{
                drivindexer--;
              }
            }
            
            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Mode", modestring);
            telemetry.addData("Motors", "left (%.2f), right (%.2f)"); //leftBack, rightBack, leftFront, rightFront);
            telemetry.update();
        }
    }
}
/*
TODO:
-find a way to add premapped automated tasks that can be run with a hotkey
-make room for sensor/servo controls
-draw out the keymappings
-stuff
*/
