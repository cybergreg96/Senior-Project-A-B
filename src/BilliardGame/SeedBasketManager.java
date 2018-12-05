package BilliardGame;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class SeedBasketManager 
{

	private ArrayList<SeedBasket> seedsArray = new ArrayList<>();
	private final Group group = new Group();
	private final Maze maze;
	private final double width;
	private final double height;
	public boolean frogExists;
	private boolean initialized;

	// spawns the tankfrog manager object to managed when frogs are visible and
	// where on the maze they are spawned with a given width and height and a
	// random amount of time to live for.
	SeedBasketManager(final Maze maze, double w, double h) 
	{
		this.maze = maze;
		width = w;
		height = h;
		frogExists = false;
		
		initialized = false;
	}

	// Used for adding the frogs to the scene.
	Node getNode() 
	{
		return group;
	}

	// addFrog creates a frog at the launchPoint moving in the direction theta.
	// nanos is the current time and used
	// for removing the frog when it has expired.
	void addFrog(final Point2D launchPoint, final long nanos) 
	{

		final SeedBasket tankFrog = new SeedBasket(launchPoint, nanos);
		group.getChildren().add(tankFrog.getShape());
		seedsArray.add(tankFrog);
		frogExists = true;
	}

	// update updates the position of the frogs and removes expired ones.
	void update(final long nanos)
	{
		if(!seedsArray.isEmpty()) {
			ArrayList<SeedBasket> seedsTemp = new ArrayList<>();
			for(SeedBasket basket: seedsArray) {
				if(basket.isHit()) {
					group.getChildren().remove(basket.getShape());
				} else {
					seedsTemp.add(basket);
				}
			}
			seedsArray = seedsTemp;
		} else {
			if(!initialized) {
				this.addFrog(new Point2D((width/2) * Math.random() + 1, (height/2) * Math.random() + 1), nanos);
				this.addFrog(new Point2D((width/2) * Math.random() + (width/2), (height/2) * Math.random() + 1), nanos);
				this.addFrog(new Point2D((width/2) * Math.random() + 1, (height/2) * Math.random() + (height/2)), nanos);
				this.addFrog(new Point2D((width/2) * Math.random() + (width/2), (height/2) * Math.random() + (height/2)), nanos);
				initialized = true;
			} else {				
				// TODO: display the winning screen
				System.out.println("win");
			}
		}
	}

	// handleMazeCollisions handles collisions between all of the manager's
	// frogs
	// and the maze.
	void handleMazeCollisions()
	{
		seedsArray.forEach(frog -> {
			final ArrayList<PlayerRectangle> segs = maze.getCollisionCandidates(seedsArray.get(0).getCenter());
			seedsArray.get(0).handleMazeCollision(segs);
		});
	}

	void handle(final long nanos)
	{
		this.update(nanos);

		if (!seedsArray.isEmpty())
		{
			this.handleMazeCollisions();
		}
	}

	// checks whether a frog object has been hit by a tank object by look at
	// whether frog shape is been intersected by another shape type such as tank
	boolean isHit(Hero tank1) 
	{
		for (SeedBasket f : this.getSeedBaskets())
		{
			if (Physics.isIntersecting(tank1.getShape(), f.getShape())) 
			{
				f.hit();

				return true;
			}
		}
		return false;
	}

	//returns arraylist of frog objects to be added to maze
	public ArrayList<SeedBasket> getSeedBaskets() 
	{
		return seedsArray;
	}
}
