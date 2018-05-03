
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;


// BulletManager manages the creation and removal of the bullets of a tank.
class BunnyFritzManager {
	private static final int MAX_BUNNIES = 1;

	private static final long SECOND = TimeUnit.SECONDS.toNanos(1);

	// bunny will be spawned every 15 seconds
	private static final long delay = SECOND * 3;

	private final ArrayList<BunnyFritz> bunnies = new ArrayList<>();

	private long bunnySpawnTime;

	private final double width;
	private final double height;

	private final Group group = new Group();
	private final Maze maze;

	BunnyFritzManager(final Maze maze, double w, double h) {
		this.maze = maze;
		width = w;
		height = h;		
		bunnySpawnTime = System.currentTimeMillis();
	}

	// Used for adding the bunnies to the scene.
	Node getNode() {
		return group;
	}

	// addBunny creates a bunny at the launchPoint moving in the direction theta. nanos is the current time and used
	// for removing the bullet when it has expired.
	void addBunny(final Point2D launchPoint, final double theta, final long nanos) {
		if (!bunnies.isEmpty()) {
			return;
		}
		final BunnyFritz bunny = new BunnyFritz(launchPoint, theta, nanos); //TODO change launch point to neutral corner
		group.getChildren().add(bunny.getShape());
		bunnies.add(bunny);
	}

	// update updates the position of the bullets and removes expired ones.
	void update(final long nanos) 
	{
		if(!bunnies.isEmpty())
		{
			BunnyFritz bunny = bunnies.get(0);

			if (nanos > bunny.getExpiry()) {
				//remove bunny after expiration
				group.getChildren().remove(bunny.getShape());
				bunnies.remove(0);
				bunnySpawnTime = System.currentTimeMillis();
			} else {
				bunny.update();
			}
		}
		else if(nanos >= bunnySpawnTime + delay)
		{
			Point2D launchPoint = new Point2D(width * 0.1, height * 0.9);
			this.addBunny(launchPoint, 90, nanos);
		}
	}

	// handleMazeCollisions handles collisions between all of the manager's bullets and the maze.
	void handleMazeCollisions() {
		bunnies.forEach(bullet -> {
			final ArrayList<TankRectangle> segs = maze.getCollisionCandidates(bullet.getCenter());
			bullet.handleMazeCollision(segs);
		});
	}

	// handle updates the state of the tank and the tank's bullets.
	void handle(final long nanos) {
		this.update(nanos);

		this.handleMazeCollisions();
	}
}

