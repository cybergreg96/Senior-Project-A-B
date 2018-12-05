package BilliardGame;
/*
 * references: https://github.com/nhooyr/java-tanktank
 */
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

// Bullet represents a bullet emitted by a tank.
class FireBall
{
    static final double VELOCITY = Bunny.VELOCITY * 1.5; // exported for use in Maze.

    private static final double RADIUS = Bunny.HEAD_HEIGHT * 2;
    private static final Paint COLOR = Color.RED;
    private static final long DURATION = TimeUnit.SECONDS.toNanos(15);

    private final Circle circle;
    private final long expiry;
    private Point2D velocity;

    //tank bullet constructor
    FireBall(Point2D launchPoint, final double theta, final long nanos) 
    {
        // We add the velocity and radius to the launchPoint so the Tank does not instantly die from its own bullet.
        // Since the bullet is defined to be 1.5 times faster than the tank, this guarantees that the tank will not die
        // as the tank cannot move into it.
        final Point2D radiusForward = Physics.decomposeVector(RADIUS + VELOCITY, theta);
        launchPoint = launchPoint.add(radiusForward);
        Image carrot = new Image("/resources/Fireball.png");
        circle = new Circle(launchPoint.getX(), launchPoint.getY(), RADIUS, COLOR);
        circle.setFill(new ImagePattern(carrot));
        velocity = Physics.decomposeVector(VELOCITY, theta);

        expiry = nanos + DURATION;
    }

    //method that specifies how fast bullet moves
    private void moveBy(final Point2D velocity) 
    {
        circle.setCenterX(circle.getCenterX() + velocity.getX());
        circle.setCenterY(circle.getCenterY() + velocity.getY());
    }

    //updates bullet shape
    void update() 
    {
        moveBy(velocity);
    }

    //returns time bullet expires and is deleted from pane
    long getExpiry() 
    {
        return expiry;
    }

    // Used for collision detection and adding/removing the bullets to/from the scene.
    // TODO would be cleaner to have getNode() for adding to scene and getShape() for collision detection because they have a difference. See Tank class.
    Shape getShape() 
    {
        return circle;
    }
    
    //handles how the bullet reacts to bouncing off of horizontal objects
    private void horizontalBounce() 
    {
        velocity = new Point2D(velocity.getX(), -velocity.getY());
    }

  //handles how the bullet reacts to bouncing off of vertical objects
    private void verticalBounce() 
    {
        velocity = new Point2D(-velocity.getX(), velocity.getY());
    }

    // The way this works is that first we check if at least one of the candidate segments is intersecting with the bullet. If so,
    // then we need to figure out which segment the bullet collided with first and on which edge of that seg.
    // So we move the bullet back until there is no collision. The last seg left before there being no collision
    // is the seg the bullet collided with first. Then, we check if the bullet's center is between the minx-manx
    // or between miny-maxy of the seg. Depending on which is true, a horizontal or vertical bounce needs to occur.
    // If neither is true, then the bullet collided with a corner and we have to handle that specially. See the comments
    // below.
    void handleMazeCollision(final ArrayList<PlayerRectangle> segments) 
    {
        // TODO this code is copied in Tank too, we could use a shared method that accepts a Shape or something.
        for (int i = 0; i < segments.size(); i++) {
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

        // seg will hold the final seg the object ended up colliding with, aka the first collision.
        PlayerRectangle seg = null;

        // Backtrack.
        // Very fine because it has a big impact when it comes to hitting corners. See source below. Also, you can test
        // this by editing BulletManager to allow for a stream of bullets and then move forward and back as you hit a corner.
        // This should not affect the trajectory of reflected bullets but it does, and it does more if smallVelocity is larger.
        // There are improvements that can be made to this but whatever.
        final Point2D smallVelocity = velocity.multiply(-1.0 / 64.0);
        do
        {
            moveBy(smallVelocity);

            for (int i = 0; i < segments.size(); i++) 
            {
                if (!Physics.isIntersecting(circle, segments.get(i).getPolygon())) 
                {
                    seg = segments.remove(i);
                    i--;
                }
            }
        } while (segments.size() > 0);

        final Point2D center = getCenter();

        assert seg != null;
        final Point2D topLeft = seg.getTopLeft();
        final Point2D topRight = seg.getTopRight();
        final Point2D botRight = seg.getBottomRight();
        final Point2D botLeft = seg.getBottomLeft();

        if (center.getX() >= topLeft.getX() && center.getX() <= topRight.getX())
        {
            horizontalBounce();
            return;
        } 
        else if (center.getY() >= topLeft.getY() && center.getY() <= botLeft.getY()) 
        {
            verticalBounce();
            return;
        }

        final Point2D corner;

        if (center.getX() < topLeft.getX() && center.getY() < topLeft.getY())
        {
            corner = topLeft;
        }
        else if (center.getX() < botLeft.getX() && center.getY() > botLeft.getY()) {
            corner = botLeft;
        } 
        else if (center.getX() > topRight.getX() && center.getY() < topRight.getY()) 
        {
            corner = topRight;
        } 
        else
        {
            corner = botRight;
        }

        // Normal points from the corner to the center of the circle.
        final Point2D normal = center.subtract(corner).normalize();
        velocity = reflect(velocity, normal);
    }
    
    //returns true if the bullet hits or is intersecting with tank shape, false if otherwise
    boolean hitTank(Hero enemyTank) 
    {
    	
    	if(Physics.isIntersecting(circle,  enemyTank.getTankShape()))
    	{
    		return true;
    	}
    	return false;
    }
    // reflect reflects the velocity across the normal.
    private Point2D reflect(final Point2D velocity, final Point2D normal) 
    {
        return velocity.subtract(normal.multiply(velocity.dotProduct(normal)).multiply(2));
    }

    // This is used by BulletManager to get the possible segments of Maze that the bullet could have collided with.
    Point2D getCenter() 
    {
        return new Point2D(circle.getCenterX(), circle.getCenterY());
    }

}
