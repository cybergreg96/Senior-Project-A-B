
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Polygon;

// Rectangle class represents a rectangle like the javafx Rectangle class but allows access/modification of arbitrary
// points within the rectangle. This is not essential but a cleaner approach and will allow for accurate and fast
// collision detection in the future or other operations too without the jankyness/inflexibility of the javafx Rectangle class.
// It also uses radians and not degrees as the unit for rotation.
class TankRectangle {
    private final double width;
    private final double height;
    private final Polygon polygon = new Polygon();
    private Point2D[] points = new Point2D[4];
    private Point2D origin = Point2D.ZERO;

    // Cloning constructor used for cloning the winning Tank to place into the alert when a game is over.
    TankRectangle(final TankRectangle rect) {
        this.points = rect.points.clone();
        this.origin = rect.origin; // ok because Point2D is immutable.
        this.width = rect.width;
        this.height = rect.height;
        this.polygon.getPoints().setAll(getDoubles());
        this.polygon.setFill(rect.polygon.getFill());
    }

    TankRectangle(final double x, final double y, final double width, final double height) {
        this(width, height);
        moveTo(new Point2D(x, y));
    }

    TankRectangle(final double width, final double height) {
        this.width = width;
        this.height = height;
        points[0] = new Point2D(0, 0);
        points[1] = new Point2D(width, 0);
        points[2] = new Point2D(width, height);
        points[3] = new Point2D(0, height);
    }

    double getWidth() {
        return width;
    }

    double getHeight() {
        return height;
    }

    // moveBy translates all of the points in the rectangle by the given point.
    void moveBy(final Point2D p) {
        for (int i = 0; i < points.length; i++) {
            points[i] = points[i].add(p);
        }
        origin.add(p);
        syncPolygon();
    }

    // moveTo translates the Rectangle such that the the top left point of the rectangle is the given point.
    void moveTo(final Point2D p) {
        // This origin stuff is not strictly necessary because before this method is called, origin is always (0, 0)
        // but whatever, lets do it for completeness and safety.
        final Point2D dif = p.subtract(origin);
        moveBy(dif);
    }

    // Rotate rotates all of the points of the rectangle around the pivot by theta.
    void rotate(final Point2D pivot, final double theta) {
        for (int i = 0; i < points.length; i++) {
            points[i] = TankPhysics.rotate(points[i], pivot, theta);
        }
        syncPolygon();
    }

    // Used for converting the Rectangle to a javafx Polygon for adding to the scene.
    private Double[] getDoubles() {
        final Double[] doubles = new Double[points.length * 2];
        for (int i = 0; i < points.length; i++) {
            final int j = i * 2;
            doubles[j] = points[i].getX();
            doubles[j + 1] = points[i].getY();
        }
        return doubles;
    }

    Point2D getTopLeft() {
        return points[0];
    }

    Point2D getTopRight() {
        return points[1];
    }

    Point2D getBottomRight() {
        return points[2];
    }

    Point2D getBottomLeft() {
        return points[3];
    }

    // Syncs the current points array with the polygon.
    private void syncPolygon() {
        polygon.getPoints().setAll(getDoubles());
    }

    Polygon getPolygon() {
        return polygon;
    }
}
