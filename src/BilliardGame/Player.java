package BilliardGame;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;


public class Player {

	static final int VELOCITY = 3; // exported for use in Bullet.
	static final double BODY_HEIGHT = 15; // exported for use in Cell.
	static final double HEAD_HEIGHT = BODY_HEIGHT / 4; // exported for use in
	// Bullet.

	static final HashMap<KeyCode, Op> KEY_CODES_1 = new HashMap<>();
	static final HashMap<KeyCode, Op> KEY_CODES_2 = new HashMap<>();

	private static final double TURNING_ANGLE = Math.PI / 36;
	private static final double BODY_WIDTH = 20;
	private static final double HEAD_WIDTH = BODY_WIDTH / 2;
	private static final Color DEATH_COLOR = Color.BLACK;
	private Shape shape;
	private Shape shapeOfTank;
	private Op lastMovementOp;
	
	static {
		KEY_CODES_1.put(KeyCode.UP, Op.FORWARD);
		KEY_CODES_1.put(KeyCode.RIGHT, Op.RIGHT);
		KEY_CODES_1.put(KeyCode.DOWN, Op.REVERSE);
		KEY_CODES_1.put(KeyCode.LEFT, Op.LEFT);
		KEY_CODES_1.put(KeyCode.PERIOD, Op.FIRE);
	}

	static {
		KEY_CODES_2.put(KeyCode.W, Op.FORWARD);
		KEY_CODES_2.put(KeyCode.D, Op.RIGHT);
		KEY_CODES_2.put(KeyCode.S, Op.REVERSE);
		KEY_CODES_2.put(KeyCode.A, Op.LEFT);
		KEY_CODES_2.put(KeyCode.V, Op.FIRE);
	}

	private final TankRectangle head = new TankRectangle(HEAD_WIDTH, HEAD_HEIGHT);
	private final TankRectangle body = new TankRectangle(BODY_WIDTH, BODY_HEIGHT);

	private final Maze maze;
	
	private Point2D pivot = new Point2D(body.getWidth() / 2, body.getHeight() / 2);
	private double theta;
	private Point2D decomposedVelocity;
	private Point2D negativeDecomposedVelocity;
	
	// Map from the keycodes to ops, see the KEY_CODES_1, KEY_CODES_2 and the
		// handle method.
		private final HashMap<KeyCode, Op> keycodes;
		// Keys pressed since the last frame.
		private final HashSet<Op> activeOps = new HashSet<>();
		
		private Image image;
		private Rectangle rectangle;
		
		Player(final Maze maze, final HashMap<KeyCode, Op> keycodes, final double initialAngle, double maxHealth) 
		{
			this.maze = maze;
			this.keycodes = keycodes;
			//this.currentHealth = maxHealth;
			
			Image player = new Image("/resources/bunny face.png");
			rectangle = new Rectangle(20,20, 40, 40);
			rectangle.setFill(new ImagePattern(player));
			
			
			
		}
		
		//rotate tank clockwise if stopped and turns tank right when moving
		private void right() 
		{
			lastMovementOp = Op.RIGHT;
			rotate(TURNING_ANGLE);
		}

		//rotate tank counter clockwise if stopped and turns tank left when moving
		private void left() 
		{
			lastMovementOp = Op.LEFT;
			rotate(-TURNING_ANGLE);
		}

		// The direction of angles is reversed because the coordinate system is
		// reversed.
		//rotates tank on axis
		private void rotate(final double theta) 
		{
			this.theta += theta;
			rectangle.setRotate(theta);
			
			decomposedVelocity = Physics.decomposeVector(VELOCITY, this.theta);
			negativeDecomposedVelocity = Physics.decomposeVector(-VELOCITY , this.theta);
			syncShape();
		}

		//updates tanks shape
		private void syncShape() 
		{
			shape = Shape.union(rectangle, rectangle);
			shapeOfTank = Shape.union(rectangle, rectangle);
		}

		//moves tank forward
		private void forward()
		{
			lastMovementOp = Op.FORWARD;
			decomposedVelocity = Physics.decomposeVector(VELOCITY, this.theta);
			negativeDecomposedVelocity = Physics.decomposeVector(-VELOCITY, this.theta);
			// multiply by tankHealth to reduce speed
			moveBy(decomposedVelocity);
		}

		//moves tank backwards
		private void back() 
		{
			lastMovementOp = Op.REVERSE;
			decomposedVelocity = Physics.decomposeVector(VELOCITY , this.theta);
			negativeDecomposedVelocity = Physics.decomposeVector(-VELOCITY, this.theta);
			// multiply by tankHealth to reduce speed
			moveBy(negativeDecomposedVelocity);
		}

		//moves tank shape by passed parameter point
		private void moveBy(final Point2D point) 
		{
			rectangle.setTranslateX(point.getX());
			rectangle.setTranslateY(point.getY());
			pivot = pivot.add(point);
			syncShape();
		}
		
		//checks if tank is hitting another object such as a maze wall or circle bullet
		private boolean checkCollision(final Shape shape) 
		{
			return Physics.isIntersecting(getShape(), shape);
		}
		
		Shape getShape()
		{
			return shape;
		}
		
		//returns center or pivot point of tank
		private Point2D getCenter() 
		{
			return pivot;
		}
		
		private void handleMazeCollisions() 
		{
			final ArrayList<TankRectangle> segs = maze.getCollisionCandidates(getCenter());

			for (int i = 0; i < segs.size(); i++)
			{
				if (!checkCollision(segs.get(i).getPolygon()))
				{
					// The tank does not intersect the seg.
					segs.remove(i);
					i--;
				}
			}

			if (segs.size() == 0) 
			{
				// The tank does not intersect any of the segs.
				return;
			}

			Runnable reverseOp = null;
			// Backtrack.
			final Player tank = this;
			// Need to declare this up here instead of in each case because java's
			// switch cases share scope. So java would think
			// we are redeclaring a variable.
			final Point2D decomposedVelocity;
			switch (lastMovementOp) {
			case FORWARD:
				decomposedVelocity = Physics.decomposeVector(-1, theta);
				reverseOp = () -> tank.moveBy(decomposedVelocity);
				break;
			case REVERSE:
				decomposedVelocity = Physics.decomposeVector(1, theta);
				reverseOp = () -> tank.moveBy(decomposedVelocity);
				break;
			case RIGHT:
				reverseOp = () -> tank.rotate(-TURNING_ANGLE / 12);
				break;
			case LEFT:
				reverseOp = () -> tank.rotate(TURNING_ANGLE / 12);
				break;
			}
			do {
				assert reverseOp != null;
				reverseOp.run();

				for (int i = 0; i < segs.size(); i++) 
				{
					if (!checkCollision(segs.get(i).getPolygon()))
					{
						segs.remove(i);
						i--;
					}
				}
			} while (segs.size() > 0);
		}

		//handles press of a key in tank keycode
		void handlePressed(final KeyCode keyCode)
		{
			activeOps.add(keycodes.get(keyCode));
		}
		
	
	private enum Op 
	{
		FORWARD, RIGHT, LEFT, REVERSE, FIRE,
	}
	
	Node getNode() 
	{
		// head added after so that you can see it in front.
		return new Group(rectangle);
	}
	
}
