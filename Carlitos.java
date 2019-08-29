package etec;
import robocode.*;
import robocode.util.Utils;
import java.awt.Color;
/**
 * Carlitos - a robot by (Gabriel S. Martins && Valter Muniz)
 */
public class Carlitos extends AdvancedRobot
{
	int movementDirection = 1;
	double previousEnergy = 100;
	double changeInEnergy = 0;
	public void run() {
 
		setAdjustGunForRobotTurn(true);
		setAdjustRadarForGunTurn(true);
		setBodyColor(Color.lightGray);
		while(true) {
			if (getRadarTurnRemaining() == 0.0)
		            	setTurnRadarRightRadians(Double.POSITIVE_INFINITY);
	        	execute();
		}
	}
	public void onScannedRobot(ScannedRobotEvent e) {
		double turn = e.getBearing() + 90 - (e.getDistance()/10);
		setMaxVelocity(12 - turn);
		setTurnRight(turn);
		setAhead(100*movementDirection);
		double bulletSpeed = 20;
		double absBearing = e.getBearingRadians() + getHeadingRadians();
		double enemyLatVel = e.getVelocity()*Math.sin(e.getHeadingRadians() - absBearing);
		double enemyDirection = Math.signum(enemyLatVel);
		double escapeAngle = Math.asin(4.0 / bulletSpeed);
		double angleOffset = escapeAngle * enemyDirection * Math.random();
		changeInEnergy =previousEnergy-e.getEnergy();
		setTurnGunRightRadians(Utils.normalRelativeAngle(absBearing + angleOffset - getGunHeadingRadians()));
		if (changeInEnergy>0 && changeInEnergy<=3) {
		movementDirection = -movementDirection;
		setAhead((e.getDistance()/4+25)*movementDirection);
		setTurnLeft(90);
		}
		if(e.getDistance() < 200){
		fire (1);
		}
		else if(e.getDistance() < 50){
		fire(2);
		}
		double radarTurn = getHeadingRadians() + e.getBearingRadians() - getRadarHeadingRadians();
 
    	setTurnRadarRightRadians(Utils.normalRelativeAngle(radarTurn));
	}
	
	public void onHitByBullet(HitByBulletEvent e) {
		back(10);
	}
	
	public void onHitWall(HitWallEvent e) {
		setTurnRight(90);
		movementDirection = -movementDirection;
	}	
}
