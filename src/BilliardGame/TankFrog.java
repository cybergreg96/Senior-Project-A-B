package BilliardGame;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class TankFrog 
{

	static final double VELOCITY = Bunny.VELOCITY * 1.5; // exported for use in
	// Maze.

	private static final double RADIUS = Bunny.HEAD_HEIGHT * 8;
	private static final Paint COLOR = Color.GREEN;
	private static final long DURATION = TimeUnit.SECONDS.toNanos(30);
	private static final int durationSeconds = (int) ((Math.random() * 6) + 5);
	private final Circle circle;
	private final long expiry;
	private boolean hit;

	// tankfrog object constructor. creates a circle with frog img overlay that
	// has a specific starting point on the maze and an expiration time.
	TankFrog(Point2D launchPoint, final long nanos) 
	{
		// We add the velocity and radius to the launchPoint so the Tank does
		// not instantly die from its own bullet.
		// Since the bullet is defined to be 1.5 times faster than the tank,
		// this guarantees that the tank will not die
		// as the tank cannot move into it.
		Image carrot = new Image("/resources/frog.png");
		circle = new Circle(launchPoint.getX(), launchPoint.getY(), RADIUS, COLOR);
		circle.setFill(new ImagePattern(carrot));
		hit = false;

		expiry = nanos + DURATION;
	}

	// returns when the frog will disappear from a maze during one of its
	// instances.
	long getExpiry() 
	{
		return expiry;
	}

	// returns whther the frog has been hit by a tank. returns true if it has
	// and false if it is still alive
	void hit() 
	{
		hit = true;
	}

	// Used for collision detection and adding/removing the bullets to/from the
	// scene.
	// TODO would be cleaner to have getNode() for adding to scene and
	// getShape() for collision detection because they have a difference. See
	// Tank class.
	Shape getShape() 
	{
		return circle;
	}

	// The way this works is that first we check if at least one of the
	// candidate segments is intersecting with the bullet. If so,
	// then we need to figure out which segment the bullet collided with first
	// and on which edge of that seg.
	// So we move the bullet back until there is no collision. The last seg left
	// before there being no collision
	// is the seg the bullet collided with first. Then, we check if the bullet's
	// center is between the minx-manx
	// or between miny-maxy of the seg. Depending on which is true, a horizontal
	// or vertical bounce needs to occur.
	// If neither is true, then the bullet collided with a corner and we have to
	// handle that specially. See the comments
	// below.
	void handleMazeCollision(final ArrayList<PlayerRectangle> segments)
	{
		// TODO this code is copied in Tank too, we could use a shared method
		// that accepts a Shape or something.
		for (int i = 0; i < segments.size(); i++) 
		{
			if (!Physics.isIntersecting(circle, segments.get(i).getPolygon()))
			{
				// The bullet does not intersect the seg.
				segments.remove(i);
				i--;
			}
		}

		if (segments.size() == 0) 
		{
			// The bullet does not intersect any of the segments.
			return;
		}

		// seg will hold the final seg the object ended up colliding with, aka
		// the first collision.
		PlayerRectangle seg = null;

		final Point2D center = getCenter();

		assert seg != null;

	}

	// This is used by BulletManager to get the possible segments of Maze that
	// the bullet could have collided with.
	Point2D getCenter() {
		return new Point2D(circle.getCenterX(), circle.getCenterY());
	}

}
