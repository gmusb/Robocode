package roboCodeEtec266;
import robocode.*;
import robocode.util.Utils;
import java.awt.Color;
/**
 * Carlitos - a robot by (Gabriel S. Martins && Valter Muniz)
 */
public class CarlitosC extends AdvancedRobot
{
	//criando variáveis para o robo
	int movementDirection = 1;//direção de movimento
	double previousEnergy = 100;//energia do inimigo
	double changeInEnergy = 0;//mudança em energia do inimigo
	double turn = 0;//A quantidade em graus que o robo vai virar
	public void run() {
 		//iniciando o robo
		setAdjustGunForRobotTurn(true);
		setAdjustRadarForGunTurn(true);
		setBodyColor(Color.lightGray);
		while(true) {
			//girando o radar infinitamente
			if (getRadarTurnRemaining() == 0.0)//se o radar não estiver girando
		            	setTurnRadarRightRadians(Double.POSITIVE_INFINITY);//gira infinitamente o radar
	        	execute();// executa o radar
		}
	}
	public void onScannedRobot(ScannedRobotEvent e) {
		setAhead(100*movementDirection);//vai andar para frente e virar de acordo com a váriavel "movementDirection"
		// criando mais váriáveis
		double bulletSpeed = 20;//velocidade da bala
		double absBearing = e.getBearingRadians() + getHeadingRadians();//direção do inimigo
		double enemyLatVel = e.getVelocity()*Math.sin(e.getHeadingRadians() - absBearing);//velocidade do inimigo
		double enemyDirection = Math.signum(enemyLatVel);// direção do inimigo
		double escapeAngle = Math.asin(4.0 / bulletSpeed);// ângulo de escape do inimigo
		double angleOffset = escapeAngle * enemyDirection * (Math.random() * ((1 - 0) + 1)) + 0;// angulo de escape do inimigo
		changeInEnergy =previousEnergy-e.getEnergy();//definindo a mudança de energia
		setTurnGunRightRadians(Utils.normalRelativeAngle(absBearing + angleOffset - getGunHeadingRadians()));//virando o robo
		if (changeInEnergy>0 && changeInEnergy<=3) { //se tiver mudança de energia. (para desviar de balas
		movementDirection = -movementDirection;//muda a direção de movimento
		setAhead(10*movementDirection);//vai para a frente
		}
		//o bloco a baixo é para se adaptar á distância do inimigo
		if(e.getDistance() < 200 && e.getDistance() >49){//se a distancia for menor que 200 e maior que 50
		fire (1);
		turn = e.getBearing() + 90;
		}
		else if(e.getDistance() < 50 && e.getDistance() >39){
		fire(2);
		turn = e.getBearing() + 90 + (e.getDistance()/10);
		}
		else if(e.getDistance() < 40){
		turn = e.getBearing();
		fire(3);
		}
		else{
		turn = (e.getBearing() - 20);
		}
		setTurnRight(turn);
		setMaxVelocity(12 - turn);
		double radarTurn = getHeadingRadians() + e.getBearingRadians() - getRadarHeadingRadians();
 
    	setTurnRadarRightRadians(Utils.normalRelativeAngle(radarTurn));
	}
	
	public void onHitByBullet(HitByBulletEvent e) {
		setTurnRight(turn * movementDirection);
	}
	
	public void onHitWall(HitWallEvent e) {
		setTurnRight(90);
		movementDirection = -movementDirection;
	}	
}
