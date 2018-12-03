
package BilliardGame;
/*
 * references: https://github.com/nhooyr/java-tanktank
 */
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

// Tank represents the tanks in the game.
class Hero 
{
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

	private final Color headColor;
	private final Color outOfAmmoHeadColor;
	private final String mainColorName;
	private final PlayerRectangle head = new PlayerRectangle(HEAD_WIDTH, HEAD_HEIGHT);
	private final PlayerRectangle body = new PlayerRectangle(BODY_WIDTH, BODY_HEIGHT);

	private final Maze maze;
	// Map from the keycodes to ops, see the KEY_CODES_1, KEY_CODES_2 and the
	// handle method.
	private final HashMap<KeyCode, Op> keycodes;
	// Keys pressed since the last frame.
	private final HashSet<Op> activeOps = new HashSet<>();
	// Shape holds the union between the body and head. It is used for collision
	// detection.
	private Shape shape;
	private Shape shapeOfTank;
	// Middle of body.
	private Point2D pivot = new Point2D(body.getWidth() / 2, body.getHeight() / 2);
	private double theta;
	private Point2D decomposedVelocity;
	private Point2D negativeDecomposedVelocity;
	private Op lastMovementOp;
	private boolean dead;
	private double currentHealth;
	public boolean bunnyExists;
	public boolean frogExists;

	/*
	 * Tank object constructor. When called creates a tank object with
	 * parameters of tank name, body colors, its keycodes, intitial angle and
	 * the max health.
	 */
	Hero(final String mainColorName, final Color bodyColor, final Color headColor, final Color outOfAmmoColor,
			final Maze maze, final HashMap<KeyCode, Op> keycodes, final double initialAngle, double maxHealth) 
	{
		this.maze = maze;
		this.keycodes = keycodes;
		this.mainColorName = mainColorName;
		this.currentHealth = maxHealth;
		this.bunnyExists = false;
		this.frogExists = false;

		final Point2D headPoint = new Point2D(body.getWidth() - head.getWidth() / 2, body.getHeight() / 2 - head.getHeight() / 2);
		head.moveTo(headPoint);

		this.headColor = headColor;
		this.outOfAmmoHeadColor = outOfAmmoColor;
		head.getPolygon().setFill(this.headColor);
		body.getPolygon().setFill(bodyColor);

		//rotates tank to its beginning angle.
		rotate(initialAngle);

		// Move to the middle of some random cell.
		final Random rand = new Random();
		int row = 0;
		int col = 0;
		if (mainColorName.equals("blue")) 
		{
			col = 0;
			row = 0;
		} 
		else 
		{
			col = Maze.COLUMNS - 1;
			row = Maze.ROWS - 1;
		}
		moveBy(new Point2D(col * Cell.LENGTH, row * Cell.LENGTH));
		moveBy(new Point2D(Maze.THICKNESS, Maze.THICKNESS));
		moveBy(new Point2D((Cell.LENGTH - Maze.THICKNESS) / 2, (Cell.LENGTH - Maze.THICKNESS) / 2));
		moveBy(new Point2D(-body.getWidth() / 2, -body.getHeight() / 2));
		syncShape();
	}

	Node getNode() 
	{
		// head added after so that you can see it in front.
		return new Group(body.getPolygon(), head.getPolygon());
	}

	// The pose used by winners!
	Node getWinPose() 
	{
		final PlayerRectangle headCopy = new PlayerRectangle(head);
		final PlayerRectangle bodyCopy = new PlayerRectangle(body);

		// TODO should the tank be pointing out or into the alert needs more
		// thought. right now it faces inward.
		// feels more symmetric
		headCopy.rotate(pivot, -theta + Math.PI);
		bodyCopy.rotate(pivot, -theta + Math.PI);

		return new Group(bodyCopy.getPolygon(), headCopy.getPolygon());
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
		body.rotate(pivot, theta);
		head.rotate(pivot, theta);
		decomposedVelocity = Physics.decomposeVector(VELOCITY * currentHealth, this.theta);
		negativeDecomposedVelocity = Physics.decomposeVector(-VELOCITY * currentHealth, this.theta);
		syncShape();
	}

	//updates tanks shape
	private void syncShape() 
	{
		shape = Shape.union(head.getPolygon(), body.getPolygon());
		shapeOfTank = Shape.union(head.getPolygon(), body.getPolygon());
	}

	//moves tank forward
	private void forward()
	{
		lastMovementOp = Op.FORWARD;
		decomposedVelocity = Physics.decomposeVector(VELOCITY * currentHealth, this.theta);
		negativeDecomposedVelocity = Physics.decomposeVector(-VELOCITY * currentHealth, this.theta);
		// multiply by tankHealth to reduce speed
		moveBy(decomposedVelocity);
	}

	//moves tank backwards
	private void back() 
	{
		lastMovementOp = Op.REVERSE;
		decomposedVelocity = Physics.decomposeVector(VELOCITY * currentHealth, this.theta);
		negativeDecomposedVelocity = Physics.decomposeVector(-VELOCITY * currentHealth, this.theta);
		// multiply by tankHealth to reduce speed
		moveBy(negativeDecomposedVelocity);
	}

	//moves tank shape by passed parameter point
	private void moveBy(final Point2D point) 
	{
		if(body.getBottomLeft().getY() < 0) {
			Point2D newPoint = new Point2D(point.getX(), point.getY() + 800);
			head.moveBy(newPoint);
			body.moveBy(newPoint);
			pivot = pivot.add(newPoint);
			syncShape();
		}else if(body.getTopLeft().getY() > 800) {
			Point2D newPoint = new Point2D(point.getX(), point.getY() - 800);
			head.moveBy(newPoint);
			body.moveBy(newPoint);
			pivot = pivot.add(newPoint);
			syncShape();
		}else if(body.getBottomLeft().getX() < 0) {
			Point2D newPoint = new Point2D(point.getX() + 800, point.getY() );
			head.moveBy(newPoint);
			body.moveBy(newPoint);
			pivot = pivot.add(newPoint);
			syncShape();
		}
		else if(body.getBottomRight().getX() > 800) {
			Point2D newPoint = new Point2D(point.getX() - 800, point.getY() );
			head.moveBy(newPoint);
			body.moveBy(newPoint);
			pivot = pivot.add(newPoint);
			syncShape();
		}else {
		head.moveBy(point);
		body.moveBy(point);
		pivot = pivot.add(point);
		syncShape();
		}
	}

	private double getTheta() 
	{
		return theta;
	}

	//returns center or pivot point of tank
	private Point2D getCenter() 
	{
		return pivot;
	}

	Shape getShape()
	{
		return shape;
	}

	//returns shape of tank
	public Shape getTankShape()
	{
		return shapeOfTank;
	}

	//checks if tank is hitting another object such as a maze wall or circle bullet
	private boolean checkCollision(final Shape shape) 
	{
		return Physics.isIntersecting(getShape(), shape);
	}

	//detects if a tank is hit by circle shaped bullet object
	boolean isHit(FireBallManager tbm)
	{
		for (FireBall t : tbm.tankShots())
		{
			if (Physics.isIntersecting(getShape(), t.getShape())) 
			{
				return true;
			}
		}
		return false;
	}

	//detects if a tank hits or runs into health frog
	boolean isHit(TankFrogManager frogs)
	{
		for (TankFrog f : frogs.getTankFrogs()) 
		{
			if (Physics.isIntersecting(getShape(), f.getShape()))
			{
				return true;
			}
		}
		return false;
	}

	// TODO add edge mechanics, e.g. instead of just stopping the tank, we lower
	// velocity/slide.
	// The way this works is that we first grab possible collision candidates
	// from the maze.
	// Then we ensure there is actually a collision. Once we know there is a
	// collision, we
	// backtrack the tank until there is no collision.
	private void handleMazeCollisions() 
	{
		final ArrayList<PlayerRectangle> segs = maze.getCollisionCandidates(getCenter());

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
		final Hero hero = this;
		// Need to declare this up here instead of in each case because java's
		// switch cases share scope. So java would think
		// we are redeclaring a variable.
		final Point2D decomposedVelocity;
		switch (lastMovementOp) {
		case FORWARD:
			decomposedVelocity = Physics.decomposeVector(-1, theta);
			reverseOp = () -> hero.moveBy(decomposedVelocity);
			break;
		case REVERSE:
			decomposedVelocity = Physics.decomposeVector(1, theta);
			reverseOp = () -> hero.moveBy(decomposedVelocity);
			break;
		case RIGHT:
			reverseOp = () -> hero.rotate(-TURNING_ANGLE / 12);
			break;
		case LEFT:
			reverseOp = () -> hero.rotate(TURNING_ANGLE / 12);
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

	//handles release of button in tank keycode
	void handleReleased(final KeyCode keyCode)
	{
		final Op op = keycodes.get(keyCode);
		activeOps.remove(op);
	}

	// handle updates the state of the tank and the tank's bullets.
	void handle(final long nanos) 
	{

		if (!bunnyExists)
		{
			if (activeOps.contains(Op.RIGHT))
			{
				right();
			}
			
			if (activeOps.contains(Op.LEFT)) 
			{
				left();
			}
			handleMazeCollisions();

			if (activeOps.contains(Op.FORWARD))
			{
				forward();
			}
			
			if (activeOps.contains(Op.REVERSE))
			{
				back();
			}
		}
		else 
		{
			if (activeOps.contains(Op.RIGHT)) 
			{
				left();
			}
			
			if (activeOps.contains(Op.LEFT))
			{
				right();
			}
			handleMazeCollisions();

			if (activeOps.contains(Op.FORWARD)) 
			{
				back();
			}
			
			if (activeOps.contains(Op.REVERSE)) 
			{
				forward();
			}
		}

		handleMazeCollisions();
	}

	//method to kill tank and turn its colors to dead colors
	void kill()
	{
		dead = true;
		head.getPolygon().setFill(DEATH_COLOR);
		body.getPolygon().setFill(DEATH_COLOR);
	}

	//returns true if tank died, false if it is still alive.
	boolean isDead() 
	{
		return dead;
	}

	//returns string value of tanks main color, pink or blue
	String getMainColorName()
	{
		return mainColorName;
	}

	//when tank is hit by any bullet subtracts health from tank in increments of 20%. 
	void subtractHealth()
	{
		this.currentHealth -= .1;
	}

//	//when tank runs into a frog adds health in increments of 20%.
//	void addHealth()
//	{
//		if (this.getCurrentHealth() < 1) 
//		{
//			this.currentHealth += .2;
//		}
//	}

	//returns double value of tanks current health
	double getCurrentHealth()
	{
		return currentHealth;
	}

	private enum Op 
	{
		FORWARD, RIGHT, LEFT, REVERSE, FIRE,
	}
}
