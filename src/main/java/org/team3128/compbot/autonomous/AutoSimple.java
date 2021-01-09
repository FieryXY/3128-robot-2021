package org.team3128.compbot.autonomous;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

import org.team3128.compbot.autonomous.CmdArmInitialize;
import org.team3128.compbot.autonomous.CmdDrive;


import org.team3128.common.utility.math.Pose2D;
import org.team3128.common.utility.math.Rotation2D;
import org.team3128.common.drive.Drive;
import org.team3128.common.hardware.limelight.Limelight;
import org.team3128.common.drive.DriveCommandRunning;
import org.team3128.compbot.autonomous.*;
import org.team3128.compbot.commands.*;
import org.team3128.compbot.subsystems.Constants;
import org.team3128.compbot.subsystems.Arm.ArmState;
import org.team3128.compbot.subsystems.*;
import org.team3128.common.utility.Log;
import com.kauailabs.navx.frc.AHRS;
import org.team3128.common.generics.ThreadScheduler;


public class AutoSimple extends SequentialCommandGroup {

    public AutoSimple(FalconDrive drive, Shooter shooter, Arm arm, Hopper hopper, AHRS ahrs, Limelight limelight, DriveCommandRunning cmdRunning, double timeoutMs, ThreadScheduler scheduler) {       
        Log.info("AutoSimple", "started");
        hopper.setBallCount(3);

        addCommands (
            new CmdSetIntake(hopper, Constants.IntakeConstants.INTAKE_MOTOR_REVERSE_VALUE),
            new CmdSetArm(arm, ArmState.STARTING_DOWN, 500),
            new CmdSetArm(arm, ArmState.STARTING, 500),
            new CmdSetIntake(hopper, Constants.IntakeConstants.INTAKE_MOTOR_OFF_VALUE),
            new CmdDrive(drive, 1),
            // addSequential(new CmdAutoTrajectory(drive, 130, 0.5, 10000, 
            //     new Pose2D(0, 0, Rotation2D.fromDegrees(0)),
            //     new Pose2D(60 * Constants.MechanismConstants.inchesToMeters, 0 * Constants.MechanismConstants.inchesToMeters, Rotation2D.fromDegrees(0))));
            // Log.info("6", String.valueOf(hopper.hopper_update_count));
            //addSequential(new CmdSetIndexer(hopper, Constants.IntakeConstants.INTAKE_MOTOR_ON_VALUE));
            new CmdAlignShoot(drive, shooter, arm, hopper, ahrs, limelight, cmdRunning, Constants.VisionConstants.TX_OFFSET, 3)
            );
    }
}