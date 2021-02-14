package org.team3128.athos.subsystems;


import static edu.wpi.first.wpilibj.XboxController.Button;
import org.team3128.common.utility.Log;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.XboxController.Button;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.controller.RamseteController;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import org.team3128.sim.subsystems.DriveSubsystem;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj.trajectory.constraint.DifferentialDriveVoltageConstraint;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import java.util.List;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.trajectory.TrajectoryUtil;
import java.nio.file.*;
import java.io.*;

import edu.wpi.first.wpilibj2.command.SubsystemBase;


public class PathFinding {
    
    public PathFinding(){
        
    }




    public Trajectory getTrajectory(String trajPath) {
        String trajectoryJSON = trajPath;
        Trajectory exampleTrajectory = new Trajectory();

        try {
            Path trajectoryPath = Filesystem.getDeployDirectory().toPath().resolve(trajectoryJSON);
            exampleTrajectory = TrajectoryUtil.fromPathweaverJson(trajectoryPath);
        } catch (IOException ex) {
            System.out.println(ex);
        }

        return exampleTrajectory;
    }

    public Command getAutonomousCommand(String trajPath, NEODrive m_robotDrive) {
        Log.info("MainAthos","3");
        // Create a voltage constraint to ensure we don't accelerate too fast
        var autoVoltageConstraint = new DifferentialDriveVoltageConstraint(
                new SimpleMotorFeedforward(Constants.ksVolts,
                        Constants.kvVoltSecondsPerMeter,
                        Constants.kaVoltSecondsSquaredPerMeter),
                Constants.kDriveKinematics, 7);
        String trajectoryJSON = trajPath;
        Trajectory exampleTrajectory = new Trajectory();

        try {
            Path trajectoryPath = Filesystem.getDeployDirectory().toPath().resolve(trajectoryJSON);
            exampleTrajectory = TrajectoryUtil.fromPathweaverJson(trajectoryPath);
        } catch (IOException ex) {
            System.out.println(ex);
        }

        RamseteCommand ramseteCommand = new RamseteCommand(exampleTrajectory, m_robotDrive::getPose,
                new RamseteController(Constants.kRamseteB, Constants.kRamseteZeta),
                new SimpleMotorFeedforward(Constants.ksVolts,
                        Constants.kvVoltSecondsPerMeter,
                        Constants.kaVoltSecondsSquaredPerMeter),
                Constants.kDriveKinematics, m_robotDrive::getWheelSpeeds,
                new PIDController(Constants.kPDriveVel, 0, 0),
                new PIDController(Constants.kPDriveVel, 0, 0),
                // RamseteCommand passes volts to the callback
                m_robotDrive::tankDriveVolts, (Subsystem) m_robotDrive);
        return ramseteCommand.andThen(() -> m_robotDrive.tankDriveVolts(0, 0));
    }


    public Command getAutonomousCommandSlalom(NEODrive m_robotDrive) {
        Log.info("MainAthos","3");
        // Create a voltage constraint to ensure we don't accelerate too fast
        var autoVoltageConstraint = new DifferentialDriveVoltageConstraint(
                new SimpleMotorFeedforward(Constants.ksVolts,
                        Constants.kvVoltSecondsPerMeter,
                        Constants.kaVoltSecondsSquaredPerMeter),
                Constants.kDriveKinematics, 7);
        //String trajectoryJSON = trajPath;
        TrajectoryConfig config = new TrajectoryConfig(Constants.maxVelocity,
        Constants.maxAcceleration)
                        // Add kinematics to ensure max speed is actually obeyed
                        .setKinematics(Constants.kDriveKinematics)
                        // Apply the voltage constraint
                        .addConstraint(autoVoltageConstraint).setReversed(false);
        
                        Trajectory exampleTrajectory = TrajectoryGenerator.generateTrajectory(
                                new Pose2d(0, 0, new Rotation2d(0)),
                                List.of(
                                new Translation2d(1.4, 0.7),
                                new Translation2d(3.5, 1.7),
                                new Translation2d(5.5, 0.7),
                                new Translation2d(6.4, -0.1),
                                new Translation2d(7.4, 0.8),
                                new Translation2d(6.4, 1.7),
                                new Translation2d(5.5, 0.7),
                                new Translation2d(3.3, 0.2),
                                new Translation2d(1.4, 0.7)),
                                new Pose2d(-0.1, 1.7, new Rotation2d(3.14)),
                                config);

        RamseteCommand ramseteCommand = new RamseteCommand(exampleTrajectory, m_robotDrive::getPose,
                new RamseteController(Constants.kRamseteB, Constants.kRamseteZeta),
                new SimpleMotorFeedforward(Constants.ksVolts,
                        Constants.kvVoltSecondsPerMeter,
                        Constants.kaVoltSecondsSquaredPerMeter),
                Constants.kDriveKinematics, m_robotDrive::getWheelSpeeds,
                new PIDController(Constants.kPDriveVel, 0, 0),
                new PIDController(Constants.kPDriveVel, 0, 0),
                // RamseteCommand passes volts to the callback
                m_robotDrive::tankDriveVolts, (Subsystem) m_robotDrive);
        return ramseteCommand.andThen(() -> m_robotDrive.tankDriveVolts(0, 0));
    }

    public Command getAutonomousCommandBarrel(NEODrive m_robotDrive) {
        Log.info("MainAthos","3");
        // Create a voltage constraint to ensure we don't accelerate too fast
        var autoVoltageConstraint = new DifferentialDriveVoltageConstraint(
                new SimpleMotorFeedforward(Constants.ksVolts,
                        Constants.kvVoltSecondsPerMeter,
                        Constants.kaVoltSecondsSquaredPerMeter),
                Constants.kDriveKinematics, 7);
        //String trajectoryJSON = trajPath;
        TrajectoryConfig config = new TrajectoryConfig(Constants.maxVelocity,
        Constants.maxAcceleration)
                        // Add kinematics to ensure max speed is actually obeyed
                        .setKinematics(Constants.kDriveKinematics)
                        // Apply the voltage constraint
                        .addConstraint(autoVoltageConstraint).setReversed(false);
        
                        Trajectory exampleTrajectory = TrajectoryGenerator.generateTrajectory(
                                new Pose2d(0, 0, new Rotation2d(0)),
                                List.of(
                                new Translation2d(2.9, -0.2),
                                new Translation2d(2.7, -1.2),
                                new Translation2d(2.2, -0.5),
                                new Translation2d(4.1, 0),
                                new Translation2d(5.6, 0.6),
                                new Translation2d(5.2, 1.5),
                                new Translation2d(4.6, 0),
                                new Translation2d(6.5, -1.2),
                                new Translation2d(7.1, -0.5),
                                new Translation2d(6.2, 0.4)),
                                new Pose2d(-0.1, 0.5, new Rotation2d(3.14)),
                                config);

        RamseteCommand ramseteCommand = new RamseteCommand(exampleTrajectory, m_robotDrive::getPose,
                new RamseteController(Constants.kRamseteB, Constants.kRamseteZeta),
                new SimpleMotorFeedforward(Constants.ksVolts,
                        Constants.kvVoltSecondsPerMeter,
                        Constants.kaVoltSecondsSquaredPerMeter),
                Constants.kDriveKinematics, m_robotDrive::getWheelSpeeds,
                new PIDController(Constants.kPDriveVel, 0, 0),
                new PIDController(Constants.kPDriveVel, 0, 0),
                // RamseteCommand passes volts to the callback
                m_robotDrive::tankDriveVolts, (Subsystem) m_robotDrive);
        return ramseteCommand.andThen(() -> m_robotDrive.tankDriveVolts(0, 0));
    }

    public Command getAutonomousCommandBounce1(NEODrive m_robotDrive) {
        Log.info("MainAthos","3");
        // Create a voltage constraint to ensure we don't accelerate too fast
        var autoVoltageConstraint = new DifferentialDriveVoltageConstraint(
                new SimpleMotorFeedforward(Constants.ksVolts,
                        Constants.kvVoltSecondsPerMeter,
                        Constants.kaVoltSecondsSquaredPerMeter),
                Constants.kDriveKinematics, 7);
        //String trajectoryJSON = trajPath;
        TrajectoryConfig config = new TrajectoryConfig(Constants.maxVelocity,
        Constants.maxAcceleration)
                        // Add kinematics to ensure max speed is actually obeyed
                        .setKinematics(Constants.kDriveKinematics)
                        // Apply the voltage constraint
                        .addConstraint(autoVoltageConstraint).setReversed(false);
        
                        Trajectory exampleTrajectory = TrajectoryGenerator.generateTrajectory(
                                new Pose2d(0, 0, new Rotation2d(0)),
                                List.of(
                                new Translation2d(1.1, 0.3)),
                                new Pose2d(1.3, 1.2, new Rotation2d(1.57)),
                                config);

        RamseteCommand ramseteCommand = new RamseteCommand(exampleTrajectory, m_robotDrive::getPose,
                new RamseteController(Constants.kRamseteB, Constants.kRamseteZeta),
                new SimpleMotorFeedforward(Constants.ksVolts,
                        Constants.kvVoltSecondsPerMeter,
                        Constants.kaVoltSecondsSquaredPerMeter),
                Constants.kDriveKinematics, m_robotDrive::getWheelSpeeds,
                new PIDController(Constants.kPDriveVel, 0, 0),
                new PIDController(Constants.kPDriveVel, 0, 0),
                // RamseteCommand passes volts to the callback
                m_robotDrive::tankDriveVolts, (Subsystem) m_robotDrive);
        return ramseteCommand.andThen(() -> m_robotDrive.tankDriveVolts(0, 0));
    }

    public Command getAutonomousCommandBounce2(NEODrive m_robotDrive) {
        Log.info("MainAthos","3");
        // Create a voltage constraint to ensure we don't accelerate too fast
        var autoVoltageConstraint = new DifferentialDriveVoltageConstraint(
                new SimpleMotorFeedforward(Constants.ksVolts,
                        Constants.kvVoltSecondsPerMeter,
                        Constants.kaVoltSecondsSquaredPerMeter),
                Constants.kDriveKinematics, 7);
        //String trajectoryJSON = trajPath;
        TrajectoryConfig config = new TrajectoryConfig(Constants.maxVelocity,
        Constants.maxAcceleration)
                        // Add kinematics to ensure max speed is actually obeyed
                        .setKinematics(Constants.kDriveKinematics)
                        // Apply the voltage constraint
                        .addConstraint(autoVoltageConstraint).setReversed(true);
        
                        Trajectory exampleTrajectory = TrajectoryGenerator.generateTrajectory(
                                new Pose2d(1.3, 1.2, new Rotation2d(1.57)),
                                List.of(
                                new Translation2d(2.3, -1.2),
                                new Translation2d(3.4, -0.9)),
                                new Pose2d(3.5, 1.3, new Rotation2d(4.71)),
                                config);

        RamseteCommand ramseteCommand = new RamseteCommand(exampleTrajectory, m_robotDrive::getPose,
                new RamseteController(Constants.kRamseteB, Constants.kRamseteZeta),
                new SimpleMotorFeedforward(Constants.ksVolts,
                        Constants.kvVoltSecondsPerMeter,
                        Constants.kaVoltSecondsSquaredPerMeter),
                Constants.kDriveKinematics, m_robotDrive::getWheelSpeeds,
                new PIDController(Constants.kPDriveVel, 0, 0),
                new PIDController(Constants.kPDriveVel, 0, 0),
                // RamseteCommand passes volts to the callback
                m_robotDrive::tankDriveVolts, (Subsystem) m_robotDrive);
        return ramseteCommand.andThen(() -> m_robotDrive.tankDriveVolts(0, 0));
    }

    public Command getAutonomousCommandBounce3(NEODrive m_robotDrive) {
        Log.info("MainAthos","3");
        // Create a voltage constraint to ensure we don't accelerate too fast
        var autoVoltageConstraint = new DifferentialDriveVoltageConstraint(
                new SimpleMotorFeedforward(Constants.ksVolts,
                        Constants.kvVoltSecondsPerMeter,
                        Constants.kaVoltSecondsSquaredPerMeter),
                Constants.kDriveKinematics, 7);
        //String trajectoryJSON = trajPath;
        TrajectoryConfig config = new TrajectoryConfig(Constants.maxVelocity,
        Constants.maxAcceleration)
                        // Add kinematics to ensure max speed is actually obeyed
                        .setKinematics(Constants.kDriveKinematics)
                        // Apply the voltage constraint
                        .addConstraint(autoVoltageConstraint).setReversed(false);
        
                        Trajectory exampleTrajectory = TrajectoryGenerator.generateTrajectory(
                                new Pose2d(3.5, 1.3, new Rotation2d(4.71)),
                                List.of(
                                new Translation2d(3.7, -1),
                                new Translation2d(4.9, -1.4),
                                new Translation2d(5.5, -0.6)),
                                new Pose2d(5.6, 1.2, new Rotation2d(1.57)),
                                config);

        RamseteCommand ramseteCommand = new RamseteCommand(exampleTrajectory, m_robotDrive::getPose,
                new RamseteController(Constants.kRamseteB, Constants.kRamseteZeta),
                new SimpleMotorFeedforward(Constants.ksVolts,
                        Constants.kvVoltSecondsPerMeter,
                        Constants.kaVoltSecondsSquaredPerMeter),
                Constants.kDriveKinematics, m_robotDrive::getWheelSpeeds,
                new PIDController(Constants.kPDriveVel, 0, 0),
                new PIDController(Constants.kPDriveVel, 0, 0),
                // RamseteCommand passes volts to the callback
                m_robotDrive::tankDriveVolts, (Subsystem) m_robotDrive);
        return ramseteCommand.andThen(() -> m_robotDrive.tankDriveVolts(0, 0));
    }

    public Command getAutonomousCommandBounce4(NEODrive m_robotDrive) {
        Log.info("MainAthos","3");
        // Create a voltage constraint to ensure we don't accelerate too fast
        var autoVoltageConstraint = new DifferentialDriveVoltageConstraint(
                new SimpleMotorFeedforward(Constants.ksVolts,
                        Constants.kvVoltSecondsPerMeter,
                        Constants.kaVoltSecondsSquaredPerMeter),
                Constants.kDriveKinematics, 7);
        //String trajectoryJSON = trajPath;
        TrajectoryConfig config = new TrajectoryConfig(Constants.maxVelocity,
        Constants.maxAcceleration)
                        // Add kinematics to ensure max speed is actually obeyed
                        .setKinematics(Constants.kDriveKinematics)
                        // Apply the voltage constraint
                        .addConstraint(autoVoltageConstraint).setReversed(true);
        
                        Trajectory exampleTrajectory = TrajectoryGenerator.generateTrajectory(
                                new Pose2d(5.6, 1.2, new Rotation2d(1.57)),
                                List.of(
                                new Translation2d(6.4, 0.1)),
                                new Pose2d(7.1, 0.1, new Rotation2d(3.14)),
                                config);

        RamseteCommand ramseteCommand = new RamseteCommand(exampleTrajectory, m_robotDrive::getPose,
                new RamseteController(Constants.kRamseteB, Constants.kRamseteZeta),
                new SimpleMotorFeedforward(Constants.ksVolts,
                        Constants.kvVoltSecondsPerMeter,
                        Constants.kaVoltSecondsSquaredPerMeter),
                Constants.kDriveKinematics, m_robotDrive::getWheelSpeeds,
                new PIDController(Constants.kPDriveVel, 0, 0),
                new PIDController(Constants.kPDriveVel, 0, 0),
                // RamseteCommand passes volts to the callback
                m_robotDrive::tankDriveVolts, (Subsystem) m_robotDrive);
        return ramseteCommand.andThen(() -> m_robotDrive.tankDriveVolts(0, 0));
    }
    

























    
    public Command getAutonomousCommand1(NEODrive m_robotDrive) {
        Log.info("MainAthos","3");
        // Create a voltage constraint to ensure we don't accelerate too fast
        var autoVoltageConstraint = new DifferentialDriveVoltageConstraint(
                new SimpleMotorFeedforward(Constants.ksVolts,
                        Constants.kvVoltSecondsPerMeter,
                        Constants.kaVoltSecondsSquaredPerMeter),
                Constants.kDriveKinematics, 7);
        //String trajectoryJSON = trajPath;
        TrajectoryConfig config = new TrajectoryConfig(Constants.maxVelocity,
        Constants.maxAcceleration)
                        // Add kinematics to ensure max speed is actually obeyed
                        .setKinematics(Constants.kDriveKinematics)
                        // Apply the voltage constraint
                        .addConstraint(autoVoltageConstraint).setReversed(false);
        
                        Trajectory exampleTrajectory = TrajectoryGenerator.generateTrajectory(
                                new Pose2d(0, 0, new Rotation2d(0)),
                                List.of(new Translation2d(3, 3), new Translation2d(7, 0), new Translation2d(3, -3)),
                                new Pose2d(-1, -0.5, new Rotation2d(3.14)),
                                config);

        // try {
        //     Path trajectoryPath = Filesystem.getDeployDirectory().toPath().resolve(trajectoryJSON);
        //     exampleTrajectory = TrajectoryUtil.fromPathweaverJson(trajectoryPath);
        // } catch (IOException ex) {
        //     System.out.println(ex);
        // }

        RamseteCommand ramseteCommand = new RamseteCommand(exampleTrajectory, m_robotDrive::getPose,
                new RamseteController(Constants.kRamseteB, Constants.kRamseteZeta),
                new SimpleMotorFeedforward(Constants.ksVolts,
                        Constants.kvVoltSecondsPerMeter,
                        Constants.kaVoltSecondsSquaredPerMeter),
                Constants.kDriveKinematics, m_robotDrive::getWheelSpeeds,
                new PIDController(Constants.kPDriveVel, 0, 0),
                new PIDController(Constants.kPDriveVel, 0, 0),
                // RamseteCommand passes volts to the callback
                m_robotDrive::tankDriveVolts, (Subsystem) m_robotDrive);
        return ramseteCommand.andThen(() -> m_robotDrive.tankDriveVolts(0, 0));
    }

    public Command getAutonomousCommand2(NEODrive m_robotDrive) {
        Log.info("MainAthos","3");
        // Create a voltage constraint to ensure we don't accelerate too fast
        var autoVoltageConstraint = new DifferentialDriveVoltageConstraint(
                new SimpleMotorFeedforward(Constants.ksVolts,
                        Constants.kvVoltSecondsPerMeter,
                        Constants.kaVoltSecondsSquaredPerMeter),
                Constants.kDriveKinematics, 7);
        //String trajectoryJSON = trajPath;
        TrajectoryConfig config = new TrajectoryConfig(Constants.maxVelocity,
        Constants.maxAcceleration)
                        // Add kinematics to ensure max speed is actually obeyed
                        .setKinematics(Constants.kDriveKinematics)
                        // Apply the voltage constraint
                        .addConstraint(autoVoltageConstraint).setReversed(false);
        
                        Trajectory exampleTrajectory = TrajectoryGenerator.generateTrajectory(
                                new Pose2d(0, 0, new Rotation2d(0)),
                                List.of(
                                new Translation2d(1.424, 1.62),
                                new Translation2d(3.81, 1.286),
                                new Translation2d(6.096, 0.762),
                                new Translation2d(6.858, 0),
                                new Translation2d(7.62, 0.762),
                                new Translation2d(6.858, 1.524),
                                new Translation2d(6.096, 0.762),
                                new Translation2d(3.81, 0),
                                new Translation2d(1.524, 0.762)),
                                new Pose2d(-2, 1, new Rotation2d(3.14)),
                                config);

        // try {
        //     Path trajectoryPath = Filesystem.getDeployDirectory().toPath().resolve(trajectoryJSON);
        //     exampleTrajectory = TrajectoryUtil.fromPathweaverJson(trajectoryPath);
        // } catch (IOException ex) {
        //     System.out.println(ex);
        // }

        RamseteCommand ramseteCommand = new RamseteCommand(exampleTrajectory, m_robotDrive::getPose,
                new RamseteController(Constants.kRamseteB, Constants.kRamseteZeta),
                new SimpleMotorFeedforward(Constants.ksVolts,
                        Constants.kvVoltSecondsPerMeter,
                        Constants.kaVoltSecondsSquaredPerMeter),
                Constants.kDriveKinematics, m_robotDrive::getWheelSpeeds,
                new PIDController(Constants.kPDriveVel, 0, 0),
                new PIDController(Constants.kPDriveVel, 0, 0),
                // RamseteCommand passes volts to the callback
                m_robotDrive::tankDriveVolts, (Subsystem) m_robotDrive);
        return ramseteCommand.andThen(() -> m_robotDrive.tankDriveVolts(0, 0));
    }



}
