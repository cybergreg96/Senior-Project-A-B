package BilliardGame;

/*
 * references: https://github.com/nhooyr/java-tanktank
 */
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Polygon;

// This class creates a rectangle that will be either the body or barrel of the tank
// Also, this class uses radians instead of degrees as the unit for rotation.
class PlayerRectangle 
{
	private final double width;
	private final double height;
	private final Polygon polygon = new Polygon();
	private Point2D[] points = new Point2D[4];
	private Point2D origin = Point2D.ZERO;
	private boolean diagonal = false;
	public int angle;

	// Cloning constructor used for cloning the winning Tank to place into the
	// alert when a game is over.
	PlayerRectangle(final PlayerRectangle rect) 
	{
		this.points = rect.points.clone();
		this.origin = rect.origin; // ok because Point2D is immutable.
		this.width = rect.width;
		this.height = rect.height;
		this.polygon.getPoints().setAll(getDoubles());
		this.polygon.setFill(rect.polygon.getFill());
		this.angle = 0;
	}

	// tank shape constructor. creates body of tank with a specified height and
	// width and moves it to a starting point on pane.
	PlayerRectangle(final double x, final double y, final double width, final double height)
	{
		this(width, height);
		moveTo(new Point2D(x, y));
	}

	// tank shape constructor. creates body of tank with a specified height and
	// width.
	PlayerRectangle(final double width, final double height) 
	{
		this.width = width;
		this.height = height;
		points[0] = new Point2D(0, 0);
		points[1] = new Point2D(width, 0);
		points[2] = new Point2D(width, height);
		points[3] = new Point2D(0, height);
	}

	// returns width of tank shape
	double getWidth() 
	{
		return width;
	}

	// returns height of tank shape
	double getHeight() 
	{
		return height;
	}

	// moveBy translates all of the points in the rectangle by the given point.
	void moveBy(final Point2D p) 
	{
		for (int i = 0; i < points.length; i++) 
		{
			points[i] = points[i].add(p);
		}
		origin.add(p);
		syncPolygon();
	}

	// moveTo translates the Rectangle such that the the top left point of the
	// rectangle is the given point.
	void moveTo(final Point2D p) 
	{
		// This origin stuff is not strictly necessary because before this
		// method is called, origin is always (0, 0)
		// but whatever, lets do it for completeness and safety.
		final Point2D dif = p.subtract(origin);
		moveBy(dif);
	}

	// Rotate rotates all of the points of the rectangle around the pivot by
	// theta.
	void rotate(final Point2D pivot, final double theta)
	{
		for (int i = 0; i < points.length; i++) 
		{
			points[i] = Physics.rotate(points[i], pivot, theta);
			
			if(points[i].getX() < 0)
			{
				//make negative value positive
				double xHelper = points[i].getX() * -2;
				points[i] = points[i].add(xHelper, 0);
			}
		}
		diagonal = true;
		syncPolygon();
	}

	public boolean isDiagonal() {
		return diagonal;
	}
	// Used for converting the Rectangle to a javafx Polygon for adding to the
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
		polygon.getPoints().setAll(getDoubles());
	}

	Polygon getPolygon()
{
		return polygon;
	}
}
