
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;

import java.util.ArrayList;
import java.util.Iterator;

// BulletManager manages the creation and removal of the bullets of a tank.
class TankBulletManager {
	private static final int MAX_BULLETS = 5;
	private final ArrayList<TankBullet> tankBullets = new ArrayList<>(MAX_BULLETS);
	private final Group group = new Group();
	private final Maze maze;
	private Tank playerTank;
	public Tank enemyTank;
	private boolean enemyTankCreated;
	// lock prevents the manager from firing any more bullet. Used to wait for the bullet firing key to release before
	// allowing another bullet to fire in Game.
	private int ammoCount = 50;
	boolean lock;
	TankBulletManager(final Maze maze, Tank t) {
		this.maze = maze;
		playerTank = t;
		enemyTankCreated = false;
	}

	public void setEnemyTank(Tank t)
	{
		enemyTank = t;
		enemyTankCreated = true;
	}

	// Used for adding the bullets to the scene.
	Node getNode() {
		return group;
	}

	// addBullet creates a bullet at the launchPoint moving in the direction theta. nanos is the current time and used
	// for removing the bullet when it has expired.
	void addBullet(final Point2D launchPoint, final double theta, final long nanos) {
		if (lock || tankBullets.size() >= MAX_BULLETS || ammoCount == 0) {
			return;
		}
		final TankBullet tankBullet = new TankBullet(launchPoint, theta, nanos);
		group.getChildren().add(tankBullet.getShape());
		tankBullets.add(tankBullet);
		ammoCount--;
	}

	// update updates the position of the bullets and removes expired ones.
	void update(final long nanos) {
		final Iterator<TankBullet> it = tankBullets.iterator();
		while (it.hasNext()) {
			final TankBullet tankBullet = it.next();
			if (nanos > tankBullet.getExpiry() || tankBullet.hitTank(playerTank)) {
				it.remove();
				group.getChildren().remove(tankBullet.getShape());
			} else if(enemyTankCreated && tankBullet.hitTank(enemyTank))
			{
				it.remove();
				group.getChildren().remove(tankBullet.getShape());
			}
			else
			{
				tankBullet.update();
			}
		}
	}
	// handleMazeCollisions handles collisions between all of the manager's bullets and the maze.
	void handleMazeCollisions() {
		tankBullets.forEach(bullet -> {
			final ArrayList<TankRectangle> segs = maze.getCollisionCandidates(bullet.getCenter());
			bullet.handleMazeCollision(segs);
		});
	}


	// isDeadTank returns true if at least one bullet intersects with the tank.
	boolean isDeadTank(final Tank tank) {
		if(tank.getCurrentHealth() <= .1)
			return true;
		return false;
	}
	ArrayList<TankBullet> tankShots(){
		return tankBullets;
	}
	boolean isReloading() {
		return tankBullets.size() == MAX_BULLETS;
	}
	int getAmmo() {
		return ammoCount;
	}
	void decAmmo() {
		ammoCount--;
	}
	boolean outOfAmmo() {
		if(ammoCount <= 0)
			return true;
		return false;
	}
}
