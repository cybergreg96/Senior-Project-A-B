package BilliardGame;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class SeedBasket 
{

	static final double VELOCITY = Bunny.VELOCITY * 1.5; // exported for use in
	// Maze.

	private static final double RADIUS = 20;
	private final Circle circle;
	private boolean hit;
	private Image seedImg;
	private ImageView seedImgView;

	SeedBasket(Point2D launchPoint, final long nanos) 
	{

		Image seedImg = new Image("/resources/seeds.png");
		initImgView(seedImg, 40, 40, launchPoint);
		circle = new Circle(launchPoint.getX(), launchPoint.getY(), RADIUS);
		circle.setFill(Color.TRANSPARENT);
		hit = false;
	}

	// returns whther the frog has been hit by a tank. returns true if it has
	// and false if it is still alive
	void hit() 
	{
		hit = true;
	}
	
	boolean isHit() {
		if(hit) {
			return true;
		}
		return false;
	}
	public void initImgView(Image img, double width, double height, Point2D position) {
		seedImgView = new ImageView(img);
		seedImgView.setFitWidth(width);
		seedImgView.setFitHeight(height);
		seedImgView.setX(position.getX() - RADIUS);
		seedImgView.setY(position.getY() - RADIUS);
	}
	Shape getShape() 
	{
		return circle;
	}
	public ImageView getImageView() {
		return seedImgView;
	}
	void handleMazeCollision(final ArrayList<PlayerRectangle> segments)
	{
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
