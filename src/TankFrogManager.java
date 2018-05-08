
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class TankFrogManager {


	private final ArrayList<TankFrog> tankFrogs = new ArrayList<>();
	private final Group group = new Group();
	private final Maze maze;
	private int delaySeconds;
	private long frogTime;
	private final double width;
	private final double height;
	public boolean frogExists;
	private long delay;
	private long frogSpawnTime;
	private boolean hit;

	TankFrogManager(final Maze maze, double w, double h) {

		this.maze = maze;
		width = w;
		height = h;
		frogExists = false;
		hit = false;
		delaySeconds = (int) ((Math.random() * 6) + 5);
		delay = TimeUnit.SECONDS.toNanos(delaySeconds);
		frogSpawnTime = System.nanoTime() + delay;
	}

	// Used for adding the frogs to the scene.
	Node getNode() {
		return group;
	}

	// addFrog creates a frog at the launchPoint moving in the direction theta.
	// nanos is the current time and used
	// for removing the frog when it has expired.
	void addFrog(final Point2D launchPoint, final long nanos) {

		final TankFrog tankFrog = new TankFrog(launchPoint, nanos);
		group.getChildren().add(tankFrog.getShape());
		tankFrogs.add(tankFrog);
		frogExists = true;
	}

	// update updates the position of the frogs and removes expired ones.
	void update(final long nanos) {
		
		if (!tankFrogs.isEmpty()) {

			TankFrog frog = tankFrogs.get(0);

				if (nanos > frog.getExpiry() || hit) {

					group.getChildren().remove(frog.getShape());
					tankFrogs.remove(frog);
					frogSpawnTime = nanos;
					frogExists = false;
					hit = false;
					// reset delay time with a value between 5 and 10 seconds
					delaySeconds = (int) ((Math.random() * 6) + 5);
					delay = TimeUnit.SECONDS.toNanos(delaySeconds);

				}
			
		} else if (nanos >= frogSpawnTime + delay) {
			Point2D launchPoint;
			// randomly selects a neutral corner to spawn
			if (Math.random() > 0.5) {
				launchPoint = new Point2D(width * Math.random(), height * Math.random());
			} else {
				launchPoint = new Point2D(width * Math.random(), height * Math.random());
			}

			this.addFrog(launchPoint, nanos);
		}
	}

	// handleMazeCollisions handles collisions between all of the manager's frogs
	// and the maze.
	void handleMazeCollisions() {
		tankFrogs.forEach(frog -> {
			final ArrayList<TankRectangle> segs = maze.getCollisionCandidates(tankFrogs.get(0).getCenter());
			tankFrogs.get(0).handleMazeCollision(segs);
		});
	}

	void handle(final long nanos) {
		this.update(nanos);

		if (!tankFrogs.isEmpty()) {
			this.handleMazeCollisions();
		}
	}
	
	boolean isHit(Tank tank) {
    	for(TankFrog f : this.getTankFrogs()) {
    		if(TankPhysics.isIntersecting(tank.getShape(), f.getShape())) {
    			hit = true;
    			
    			return true;
    		}
    	}
    	return false;
    }

	public ArrayList<TankFrog> getTankFrogs() {
		return tankFrogs;
	}
}
