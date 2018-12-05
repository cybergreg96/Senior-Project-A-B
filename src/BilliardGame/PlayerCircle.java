package BilliardGame;

/*
 * references: https://github.com/nhooyr/java-tanktank
 */
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

// This class creates a circle that will be either the body or barrel of the tank
// Also, this class uses radians instead of degrees as the unit for rotation.
class PlayerCircle
{
	private final double radius;
	private final Circle pCirc = new Circle();
	private Point2D[] points = new Point2D[4];
	private Point2D origin = Point2D.ZERO;
	private Point2D center = origin;
	// Cloning constructor used for cloning the winning Tank to place into the
	// alert when a game is over.
	PlayerCircle(final PlayerCircle circle) 
	{
		this.points = circle.points.clone();
		this.origin = circle.origin; // ok because Point2D is immutable.
		this.center = circle.center;
		this.radius = circle.radius;
		this.pCirc.setCenterX(origin.getX());
		this.pCirc.setCenterY(origin.getY());
		this.pCirc.setRadius(radius);
		this.pCirc.setFill(circle.pCirc.getFill());
	}
	// tank shape constructor. creates body of tank with a specified height and
	// width and moves it to a starting point on pane.
	PlayerCircle(final double x, final double y, final double radius)
	{
		this(radius);
		moveTo(new Point2D(x, y));
	}


	PlayerCircle(final double radius) 
	{
		this.radius = radius;
		points[0] = new Point2D(radius * Math.sin(2.356), radius * Math.cos(2.356)); //"Top Left"
		points[1] = new Point2D(radius * Math.sin(.7854), radius * Math.cos(.7854)); //"Top Right"
		points[2] = new Point2D(radius * Math.sin(5.672), radius * Math.cos(5.672)); //"Bottom Right"
		points[3] = new Point2D(radius * Math.sin(4.887), radius * Math.cos(4.887)); //"Bottom Left"
		
		//System.out.println("Location of Circle: " + this.origin + "\n " + "Radius: " + this.radius);
		//for(Point2D p: points) {
		//	System.out.println("Point: ( " + p.getX()+ " , " + p.getY() + " )");
		//}
	}

	double getRadius() 
	{
		return radius;
	}
	Point2D getCenter() {
		return center;
	}
	// moveBy translates all of the points in the circle by the given point.
	void moveBy(final Point2D p) 
	{
		center = center.add(p);
		syncPolygon();
	}

	// moveTo translates the circle such that the the top left point of the
	// circle is the given point.
	void moveTo(final Point2D p) 
	{
		// This origin stuff is not strictly necessary because before this
		// method is called, origin is always (0, 0)
		// but whatever, lets do it for completeness and safety.
		final Point2D dif = p.subtract(center);
		moveBy(dif);
	}

	// Rotate rotates all of the points of the circle around the pivot by
	// theta.
	void rotate(final Point2D pivot, final double theta)
	{
		for (int i = 0; i < points.length; i++) 
		{
			points[i] = Physics.rotate(points[i], pivot, theta);
		}
		syncPolygon();
	}

	// Used for converting the circle to a javafx circle for adding to the
	// scene.
	private Double[] getDoubles() 
	{
		final Double[] doubles = new Double[points.length * 2];
		for (int i = 0; i < points.length; i++) 
		{
			final int j = i * 2;
			doubles[j] = points[i].getX();
			doubles[j + 1] = points[i].getY();
		}
		return doubles;
	}
	// returns top left corner coordinate of tank shape.
	Point2D getTopLeft() 
	{
		return points[0];
	}

	// returns top right corner coordinate of tank shape.
	Point2D getTopRight() 
	{
		return points[1];
	}

	// returns bottom right corner coordinate of tank shape.
	Point2D getBottomRight()
	{
		return points[2];
	}

	// returns bottom left corner coordinate of tank shape.
	Point2D getBottomLeft() 
	{
		return points[3];
	}

	// Syncs the current points array with the polygon.
	private void syncPolygon() 
	{
		pCirc.setCenterX(center.getX());
		pCirc.setCenterY(center.getY());
		pCirc.setRadius(radius);
		//System.out.println(center);
	}

	Circle getCircle()
{
		return pCirc;
	}
}
