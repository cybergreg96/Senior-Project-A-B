
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
class Tank {
	static final int VELOCITY = 3; // exported for use in Bullet.
	static final double BODY_HEIGHT = 15; // exported for use in Cell.
	static final double HEAD_HEIGHT = BODY_HEIGHT / 4; // exported for use in Bullet.

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
	private final TankRectangle head = new TankRectangle(HEAD_WIDTH, HEAD_HEIGHT);
	private final TankRectangle body = new TankRectangle(BODY_WIDTH, BODY_HEIGHT);
	private final TankBulletManager tankBulletManager;
	private final Maze maze;
	// Map from the keycodes to ops, see the KEY_CODES_1, KEY_CODES_2 and the handle method.
	private final HashMap<KeyCode, Op> keycodes;
	// Keys pressed since the last frame.
	private final HashSet<Op> activeOps = new HashSet<>();
	// Shape holds the union between the body and head. It is used for collision detection.
	private Shape shape;
	// Middle of body.
	private Point2D pivot = new Point2D(body.getWidth() / 2, body.getHeight() / 2);
	private double theta;
	private Point2D decomposedVelocity;
	private Point2D negativeDecomposedVelocity;
	private Op lastMovementOp;
	private boolean dead;
	public boolean bunnyExists;

	Tank(final String mainColorName, final Color bodyColor, final Color headColor, final Color outOfAmmoColor, final Maze maze, final HashMap<KeyCode, Op> keycodes, final double initialAngle) {
		this.maze = maze;
		this.keycodes = keycodes;
		this.mainColorName = mainColorName;
		this.bunnyExists = false;


		tankBulletManager = new TankBulletManager(maze);

		final Point2D headPoint = new Point2D(
				body.getWidth() - head.getWidth() / 2, // half of head sticks out.
				body.getHeight() / 2 - head.getHeight() / 2 // head is in the vertical middle of tank.
				);
		head.moveTo(headPoint);

		this.headColor = headColor;
		this.outOfAmmoHeadColor = outOfAmmoColor;
		head.getPolygon().setFill(this.headColor);
		body.getPolygon().setFill(bodyColor);

		rotate(initialAngle);
		// Move to the middle of some random cell.
		final Random rand = new Random();
		int row = 0;
		int col = 0;
		if(mainColorName.equals("blue"))
		{        
			col = Maze.COLUMNS - 1;
			row = Maze.ROWS - 1;
		}
		else
		{
			col = 0;
			row = 0;
		}

		moveBy(new Point2D(col * TankCell.LENGTH, row * TankCell.LENGTH));
		moveBy(new Point2D(Maze.THICKNESS, Maze.THICKNESS));
		moveBy(new Point2D((TankCell.LENGTH - Maze.THICKNESS) / 2, (TankCell.LENGTH - Maze.THICKNESS) / 2));
		moveBy(new Point2D(-body.getWidth() / 2, -body.getHeight() / 2));

		syncShape();
	}

	TankBulletManager getBulletManager() {
		return tankBulletManager;
	}

	Node getNode() {
		// head added after so that you can see it in front.
		return new Group(body.getPolygon(), head.getPolygon());
	}

	// The pose used by winners!
	Node getWinPose() {
		final TankRectangle headCopy = new TankRectangle(head);
		final TankRectangle bodyCopy = new TankRectangle(body);

		// TODO should the tank be pointing out or into the alert ¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿¿ needs more thought. right now it faces inward. feels more symmetric
		headCopy.rotate(pivot, -theta + Math.PI);
		bodyCopy.rotate(pivot, -theta + Math.PI);

		return new Group(bodyCopy.getPolygon(), headCopy.getPolygon());
	}

	// The direction of angles is reversed because the coordinate system is reversed.
	private void right() {
		lastMovementOp = Op.RIGHT;
		rotate(TURNING_ANGLE);
	}

	private void left() {
		lastMovementOp = Op.LEFT;
		rotate(-TURNING_ANGLE);
	}

	private void rotate(final double theta) {
		this.theta += theta;
		body.rotate(pivot, theta);
		head.rotate(pivot, theta);
		decomposedVelocity = TankPhysics.decomposeVector(VELOCITY, this.theta);
		negativeDecomposedVelocity = TankPhysics.decomposeVector(-VELOCITY, this.theta);
		syncShape();
	}

	private void syncShape() {
		shape = Shape.union(head.getPolygon(), body.getPolygon());
	}

	private void forward() {
		lastMovementOp = Op.FORWARD;
		moveBy(decomposedVelocity);
	}

	private void back() {
		lastMovementOp = Op.REVERSE;
		moveBy(negativeDecomposedVelocity);
	}

	private void moveBy(final Point2D point) {
		head.moveBy(point);
		body.moveBy(point);
		pivot = pivot.add(point);
		syncShape();
	}

	private Point2D getBulletLaunchPoint() {
		if(!bunnyExists)
		{
		final Point2D topRight = head.getTopRight();
		final Point2D bottomRight = head.getBottomRight();
		return topRight.midpoint(bottomRight);
		}
		else
		{
			final Point2D topRight = body.getTopLeft();
			final Point2D bottomRight = body.getBottomLeft();
			return topRight.midpoint(bottomRight);
		}
	}

	private double getTheta() {
		return theta;
	}

	private Point2D getCenter() {
		return pivot;
	}

	Shape getShape() {
		return shape;
	}

	private boolean checkCollision(final Shape shape) {
		return TankPhysics.isIntersecting(getShape(), shape);
	}

	// TODO add edge mechanics, e.g. instead of just stopping the tank, we lower velocity/slide.
	// The way this works is that we first grab possible collision candidates from the maze.
	// Then we ensure there is actually a collision. Once we know there is a collision, we
	// backtrack the tank until there is no collision.
	private void handleMazeCollisions() {
		final ArrayList<TankRectangle> segs = maze.getCollisionCandidates(getCenter());

		for (int i = 0; i < segs.size(); i++) {
			if (!checkCollision(segs.get(i).getPolygon())) {
				// The tank does not intersect the seg.
				segs.remove(i);
				i--;
			}
		}

		if (segs.size() == 0) {
			// The tank does not intersect any of the segs.
			return;
		}

		Runnable reverseOp = null;
		// Backtrack.
		final Tank tank = this;
		// Need to declare this up here instead of in each case because java's switch cases share scope. So java would think
		// we are redeclaring a variable.
		final Point2D decomposedVelocity;
		switch (lastMovementOp) {
		case FORWARD:
			decomposedVelocity = TankPhysics.decomposeVector(-1, theta);
			reverseOp = () -> tank.moveBy(decomposedVelocity);
			break;
		case REVERSE:
			decomposedVelocity = TankPhysics.decomposeVector(1, theta);
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

			for (int i = 0; i < segs.size(); i++) {
				if (!checkCollision(segs.get(i).getPolygon())) {
					segs.remove(i);
					i--;
				}
			}
		} while (segs.size() > 0);
	}

	void handlePressed(final KeyCode keyCode) {
		activeOps.add(keycodes.get(keyCode));
	}

	void handleReleased(final KeyCode keyCode) {
		final Op op = keycodes.get(keyCode);
		if (op == Op.FIRE) {
			tankBulletManager.lock = false;
		}
		activeOps.remove(op);
	}

	// handle updates the state of the tank and the tank's bullets.
	void handle(final long nanos) {
		tankBulletManager.update(nanos);

		if (activeOps.contains(Op.FIRE)) 
		{
			//shoots backwards if bunny exists
			if(!bunnyExists)
			{
			tankBulletManager.addBullet(
					getBulletLaunchPoint(),
					getTheta(),
					nanos
					);
			}
			else
			{
				//TODO make this fire backwards
				tankBulletManager.addBullet(
						getBulletLaunchPoint(),
						getTheta() + Math.PI,
						nanos
						);
			}
			tankBulletManager.lock = true;
		}
		tankBulletManager.handleMazeCollisions();

		if (tankBulletManager.isReloading()) {
			head.getPolygon().setFill(outOfAmmoHeadColor);
		} else {
			head.getPolygon().setFill(headColor);
		}

		if(!bunnyExists)
		{
			if (activeOps.contains(Op.RIGHT)) {
				right();
			}
			if (activeOps.contains(Op.LEFT)) {
				left();
			}
			handleMazeCollisions();

			if (activeOps.contains(Op.FORWARD)) {
				forward();
			}
			if (activeOps.contains(Op.REVERSE)) {
				back();
			}
		}
		else
		{
			if (activeOps.contains(Op.RIGHT)) {
				left();
			}
			if (activeOps.contains(Op.LEFT)) {
				right();
			}
			handleMazeCollisions();

			if (activeOps.contains(Op.FORWARD)) {
				back();
			}
			if (activeOps.contains(Op.REVERSE)) {
				forward();
			}
		}

		handleMazeCollisions();
	}

	void kill() {
		dead = true;
		head.getPolygon().setFill(DEATH_COLOR);
		body.getPolygon().setFill(DEATH_COLOR);
	}

	boolean isDead() {
		return dead;
	}

	String getMainColorName() {
		return mainColorName;
	}

	private enum Op {
		FORWARD,
		RIGHT,
		LEFT,
		REVERSE,
		FIRE,
	}
}
