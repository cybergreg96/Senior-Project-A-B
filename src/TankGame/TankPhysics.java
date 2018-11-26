package TankGame;
/*
 * references: https://github.com/nhooyr/java-tanktank
 */
import javafx.geometry.Point2D;
import javafx.scene.shape.Shape;

class TankPhysics {
    static Point2D decomposeVector(final double v, final double theta) {
        return new Point2D(Math.cos(theta) * v, Math.sin(theta) * v);
    }

    // Obtained from research. See https://stackoverflow.com/a/2259502/4283659
    // and https://academo.org/demos/rotation-about-point/.
    static Point2D rotate(Point2D point, final Point2D pivot, final double theta) {
        point = point.subtract(pivot);

        final double s = Math.sin(theta);
        final double c = Math.cos(theta);
        final double x = point.getX() * c - point.getY() * s;
        final double y = point.getX() * s + point.getY() * c;

        point = new Point2D(x, y);
        return point.add(pivot);
    }

    static boolean isIntersecting(final Shape shape1, final Shape shape2) {
        // This is incredibly inefficient but doing it more efficiently involves a lot of math and for this game
        // it is not worth the effort. Just do not run this on a complete toaster.
        // We do not use javafx's bounds as described at https://docs.oracle.com/javase/8/javafx/api/javafx/geometry/Bounds.html
        // because when a shape is rotated, the bounding box does not rotate, instead it is formed the min/max x/y values which
        // means collision detection becomes highly inaccurate.
        //
        // If in the future, this becomes a bottleneck, use the method described in
        // https://stackoverflow.com/questions/401847/circle-rectangle-collision-detection-intersection.
        final Shape intersection = Shape.intersect(shape1, shape2);
        return intersection.getBoundsInLocal().getWidth() > 0;
        // This is the inaccurate method that javafx provides.
//        return shape1.getBoundsInParent().intersects(shape2.getBoundsInParent());
    }
}
