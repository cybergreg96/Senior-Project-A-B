package BilliardGame;
/*
 * references: https://github.com/nhooyr/java-tanktank
 */
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;

import java.util.ArrayList;
import java.util.Iterator;

// BulletManager manages the creation and removal of the bullets of a tank.
class FireBallManager 
{
	
	private static final int MAX_BULLETS = 5;
	private final ArrayList<FireBall> tankBullets = new ArrayList<>(MAX_BULLETS);
	private final Group group = new Group();
	private final Maze maze;
	private Bunny playerTank;
	public Hero enemyTank;
	private boolean enemyTankCreated;
	
	// lock prevents the manager from firing any more bullet. Used to wait for the bullet firing key to release before
	// allowing another bullet to fire in Game.
	private int ammoCount = 50;
	boolean lock;
	FireBallManager(final Maze maze, Bunny t) 
	{
		this.maze = maze;
		playerTank = t;
		enemyTankCreated = false;
	}

	//sets the enemy tank as the other tank.
	public void setEnemyTank(Hero tank1)
	{
		enemyTank = tank1;
		enemyTankCreated = true;
	}

	// Used for adding the bullets to the scene.
	Node getNode() 
	{
		return group;
	}

	// addBullet creates a bullet at the launchPoint moving in the direction theta. nanos is the current time and used
	// for removing the bullet when it has expired.
	void addBullet(final Point2D launchPoint, final double theta, final long nanos)
	{
		if (lock || tankBullets.size() >= MAX_BULLETS || ammoCount == 0) 
		{
			return;
		}
		final FireBall tankBullet = new FireBall(launchPoint, theta, nanos);
		group.getChildren().add(tankBullet.getShape());
		tankBullets.add(tankBullet);
		ammoCount--;
	}

	// update updates the position of the bullets and removes expired ones.
	void update(final long nanos)
	{
		final Iterator<FireBall> it = tankBullets.iterator();
		while (it.hasNext()) {
			final FireBall tankBullet = it.next();
			if(enemyTankCreated && tankBullet.hitTank(enemyTank))
			{
				it.remove();
				group.getChildren().remove(tankBullet.getShape());
			}
			else
			{
				tankBullet.update();
			}
			if(tankBullet.getBounces()>10){
				tankBullet.resetBounces();
				it.remove();
				group.getChildren().remove(tankBullet.getShape());
				}
		}
	}
	// handleMazeCollisions handles collisions between all of the manager's bullets and the maze.
	void handleMazeCollisions() 
	{
		tankBullets.forEach(bullet -> {
			final ArrayList<PlayerRectangle> segs = maze.getCollisionCandidates(bullet.getCenter());
			bullet.handleMazeCollision(segs);
		});
	}


	// isDeadTank returns true if at least one bullet intersects with the tank 5 times without tank picking up health.
	boolean isDeadTank(final Hero tank1) 
	{
		if(tank1.getCurrentHealth() <= .1)
			return true;
		return false;
	}
	ArrayList<FireBall> tankShots()
	{
		return tankBullets;
	}
	//returns true when tank is reloading, flase when tank is in normal state
	boolean isReloading()
	{
		return tankBullets.size() == MAX_BULLETS;
	}
	//returns size of tanks remaining ammo count
	int getAmmo() 
	{
		return ammoCount;
	}
	
	//decrements ammo amount after tank fires a shot
	void decAmmo() 
	{
		ammoCount--;
	}
	
	//returns true if tank is out of ammo, false if tank still has bullets left.
	boolean outOfAmmo()
	{
		if(ammoCount <= 0)
			return true;
		return false;
	}
}
