package TankGame;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

// BulletManager manages the creation and removal of the bunny.
class TankBunnyFritzManager 
{

	public boolean bunnyExists;

	private static final int MAX_BUNNIES = 1;

	// bunny will be spawned every 5 to 10 seconds
	private long delay;
	private int delaySeconds;

	private final ArrayList<TankBunnyFritz> bunnies = new ArrayList<>();

	private long bunnySpawnTime;

	private final double width;
	private final double height;

	private final Group group = new Group();
	private final Maze maze;

	// creates a tankbunnyfritzmanager object for a tank when called. Sets
	// bunny's width and height as well as if it exits at an instance
	TankBunnyFritzManager(final Maze maze, double w, double h)
	{
		this.maze = maze;
		width = w;
		height = h;
		bunnyExists = false;

		delaySeconds = (int) ((Math.random() * 6) + 5);
		delay = TimeUnit.SECONDS.toNanos(delaySeconds);

		bunnySpawnTime = System.nanoTime() + delay;
	}

	// Used for adding the bunnies to the scene.
	Node getNode()
	{
		return group;
	}

	// addBunny creates a bunny at the launchPoint moving in the direction
	// theta. nanos is the current time and used
	// for removing the bullet when it has expired.
	void addBunny(final Point2D launchPoint, final double theta, final long nanos) 
	{
		final TankBunnyFritz bunny = new TankBunnyFritz(launchPoint, theta, nanos);
		group.getChildren().add(bunny.getShape());
		bunnies.add(bunny);
		bunnyExists = true;
	}

	// update updates the position of the bunny and removes it when expired
	void update(final long nanos) 
	{
		if (!bunnies.isEmpty()) 
		{
			TankBunnyFritz bunny = bunnies.get(0);

			if (nanos > bunny.getExpiry()) 
			{
				// remove bunny after expiration
				group.getChildren().remove(bunny.getShape());
				bunnies.remove(0);
				bunnySpawnTime = nanos;
				bunnyExists = false;

				// reset delay time with a value between 5 and 10 seconds
				delaySeconds = (int) ((Math.random() * 6) + 5);
				delay = TimeUnit.SECONDS.toNanos(delaySeconds);
			} 
			else 
			{
				bunny.update();
			}
		} 
		else if (nanos >= bunnySpawnTime + delay) 
		{
			int theta = (int) (Math.random() * 360);
			Point2D launchPoint;

			// randomly selects a neutral corner to spawn
			if (Math.random() > 0.5) 
			{
				launchPoint = new Point2D(width * 0.1, height * 0.9);
			} else {
				launchPoint = new Point2D(width * 0.9, height * 0.1);
			}

			this.addBunny(launchPoint, theta, nanos);
		}
	}

	// handleMazeCollisions handles collisions between the bunny and the maze.
	void handleMazeCollisions() 
	{
		final ArrayList<TankRectangle> segs = maze.getCollisionCandidates(bunnies.get(0).getCenter());
		bunnies.get(0).handleMazeCollision(segs);
	}

	// handle updates the state of the bunny
	void handle(final long nanos) 
	{
		this.update(nanos);

		if (!bunnies.isEmpty()) 
		{
			this.handleMazeCollisions();
		}
	}
}
