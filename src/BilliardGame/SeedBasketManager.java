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
	private boolean win;

	SeedBasketManager(final Maze maze, double w, double h) 
	{
		this.maze = maze;
		width = w;
		height = h;
		frogExists = false;
		
		initialized = false;
		win = false;
	}

	// Used for adding the frogs to the scene.
	Node getNode() 
	{
		return group;
	}

	void addFrog(final Point2D launchPoint, final long nanos) 
	{

		final SeedBasket basket = new SeedBasket(launchPoint, nanos);
		group.getChildren().add(basket.getShape());
		group.getChildren().add(basket.getImageView());
		seedsArray.add(basket);
		frogExists = true;
	}

	// update updates the position of the frogs and removes expired ones.
	void update(final long nanos)
	{
		// check to see if seedsArray is not empty (not all collected yet)
		if(!seedsArray.isEmpty()) {
			// initialize a temp array for storing
			ArrayList<SeedBasket> seedsTemp = new ArrayList<>();
			// iterate thru seedsArray
			for(SeedBasket basket: seedsArray) {
				// check to see if seed has been collected
				if(basket.isHit()) {
					// remove from view
					group.getChildren().remove(basket.getShape());
				} else {
					// seed hasn't been collected, so save it in temp
					seedsTemp.add(basket);
				}
			}
			// reassign temp to seedsArray (seeds haven't been collected yet)
			seedsArray = seedsTemp;
		} else {
			// has the seeds been created yet?
			if(!initialized) {
				// create seeds in quadrants
				// Top Left Quadrant
				this.addFrog(new Point2D((width/2) * Math.random() + 1, (height/2) * Math.random() + 1), nanos);
				// Top Right Quadrant
				this.addFrog(new Point2D((width/2) * Math.random() + (width/2), (height/2) * Math.random() + 1), nanos);
				// Bottom Left Quadrant
				this.addFrog(new Point2D((width/2) * Math.random() + 1, (height/2) * Math.random() + (height/2)), nanos);
				// Bottom Right Quadrant
				this.addFrog(new Point2D((width/2) * Math.random() + (width/2), (height/2) * Math.random() + (height/2)), nanos);
				initialized = true;
			} else {				
				win = true;
			}
		}
	}

	void handleMazeCollisions()
	{
		seedsArray.forEach(frog -> {
			final ArrayList<PlayerRectangle> segs = maze.getCollisionCandidates(seedsArray.get(0).getCenter());
			seedsArray.get(0).handleMazeCollision(segs);
		});
	}
	
	boolean getWinCondition() {
		return win;
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
