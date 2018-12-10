package BilliardGame;

/*
 * references: https://github.com/nhooyr/java-tanktank
 */
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

import PacManGUI.SceneInfo;

// Tank represents the tanks in the game.
class Bunny implements Viewable {
	static final int VELOCITY = 3; // exported for use in Bullet.
	static final double BODY_HEIGHT = 15; // exported for use in Cell.
	static final double HEAD_HEIGHT = BODY_HEIGHT / 4; // exported for use in
	// Bullet.

	static final HashMap<KeyCode, Op> KEY_CODES_1 = new HashMap<>();
	static final HashMap<KeyCode, Op> KEY_CODES_2 = new HashMap<>();

	private static final double TURNING_ANGLE = Math.PI / 36;
	private static final double BODY_WIDTH = 10;
	private static final double HEAD_WIDTH = BODY_WIDTH / 2;
	private static final Color DEATH_COLOR = Color.TRANSPARENT;
	public double[] heroCord = new double[2];

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
	// private final PlayerRectangle head = new PlayerRectangle(HEAD_WIDTH,
	// HEAD_HEIGHT);
	// private final PlayerRectangle billiardBunny = new
	// PlayerRectangle(billiardBunny_WIDTH, billiardBunny_HEIGHT);
	private final PlayerCircle billiardBunny = new PlayerCircle(BODY_WIDTH);
	private FireBallManager tankBulletManager;
	private final Maze maze;
	// Map from the keycodes to ops, see the KEY_CODES_1, KEY_CODES_2 and the
	// handle method.
	private final HashMap<KeyCode, Op> keycodes;
	// Keys pressed since the last frame.
	private final HashSet<Op> activeOps = new HashSet<>();
	// Shape holds the union between the billiardBunny and head. It is used for
	// collision
	// detection.
	private Shape shape;
	private Shape shapeOfTank;
	// Middle of billiardBunny.
	private Point2D pivot = new Point2D(billiardBunny.getCenter().getX(), billiardBunny.getCenter().getY());
	private double theta;
	private Point2D decomposedVelocity;
	private Point2D negativeDecomposedVelocity;
	private Op lastMovementOp;
	private boolean dead;
	private double currentHealth;
	private Image bunnyImage;
	private Image ratImg;
	private ImageView bunnyImgView;
	public boolean bunnyExists;
	public boolean frogExists;

	public boolean bunnyFreeze = false;
	public boolean playerBunny = false;
	public boolean split = false;
	public boolean isGiantRat = false;
	public boolean autoMove = true;
	public boolean autoMoveWait = true;

	Timer waitTimer = new Timer();

	/*
	 * Tank object constructor. When called creates a tank object with parameters of
	 * tank name, billiardBunny colors, its keycodes, intitial angle and the max
	 * health.
	 */
	Bunny(final String mainColorName, final Color billiardBunnyColor, final Color headColor, final Color outOfAmmoColor,
			final Maze maze, final HashMap<KeyCode, Op> keycodes, final double initialAngle, double maxHealth) {
		this.maze = maze;
		this.keycodes = keycodes;
		this.mainColorName = mainColorName;
		this.currentHealth = maxHealth;
		this.bunnyExists = false;
		this.frogExists = false;
		try {
			bunnyImage = new Image(new FileInputStream("src/resources/bunny face.png"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			ratImg = new Image(new FileInputStream("src/resources/giant-rat.png"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		initImgView(bunnyImage, 40, 40);
		tankBulletManager = new FireBallManager(maze, this);

		// final Point2D headPoint = new Point2D(billiardBunny.getWidth() -
		// head.getWidth() / 2, billiardBunny.getHeight() / 2 - head.getHeight()
		// / 2);
		// head.moveTo(headPoint);

		this.headColor = headColor;
		this.outOfAmmoHeadColor = outOfAmmoColor;
		// head.getPolygon().setFill(this.headColor);
		// billiardBunny.getPolygon().setFill(billiardBunnyColor);
		billiardBunny.getCircle().setFill(Color.TRANSPARENT);
		// rotates tank to its beginning angle.
		rotate(initialAngle);

		// Move to the middle of some random cell.
		final Random rand = new Random();
		int row = 0;
		int col = 0;
		if (mainColorName.equals("blue")) {
			col = 0;
			row = 0;
		} else {
			col = Maze.COLUMNS - rand.nextInt(Maze.COLUMNS);
			row = Maze.ROWS - rand.nextInt(Maze.COLUMNS);
		}
		moveBy(new Point2D(col * Cell.LENGTH, row * Cell.LENGTH));
		moveBy(new Point2D(Maze.THICKNESS, Maze.THICKNESS));
		moveBy(new Point2D((Cell.LENGTH - Maze.THICKNESS) / 2, (Cell.LENGTH - Maze.THICKNESS) / 2));
		moveBy(new Point2D(-billiardBunny.getRadius() / 2, -billiardBunny.getRadius() / 2));
		syncShape();
	}

	public boolean freeze() {
		return bunnyFreeze;
	}

	public void setFreeze(boolean shouldFreeze) {
		bunnyFreeze = shouldFreeze;
	}

	public boolean split() {
		return split;
	}

	public void setSplit(boolean shouldSplit) {
		split = shouldSplit;
	}

	public void setPlayerBunny(boolean isPlayer) {
		playerBunny = isPlayer;
	}

	public boolean isPlayerBunny() {
		return playerBunny;
	}

	public void setAutoMove(boolean shouldAuto) {
		autoMove = shouldAuto;
	}

	public boolean shouldAutoMove() {
		return autoMove;
	}

	/*
	 * creates TankBulletManager object for each tank object created
	 */
	FireBallManager getBulletManager() {
		return tankBulletManager;
	}

	Node getNode() {
		return new Group(billiardBunny.getCircle(), bunnyImgView);
	}

	public void initImgView(Image img, double width, double height) {
		bunnyImgView = new ImageView(img);
		bunnyImgView.setFitWidth(width);
		bunnyImgView.setFitHeight(height);
	}
	// The pose used by winners!
	/*
	 * Node getWinPose() { //final PlayerRectangle headCopy = new
	 * PlayerRectangle(head); //final PlayerRectangle billiardBunnyCopy = new
	 * PlayerRectangle(billiardBunny);
	 * 
	 * // TODO should the tank be pointing out or into the alert needs more //
	 * thought. right now it faces inward. // feels more symmetric
	 * headCopy.rotate(pivot, -theta + Math.PI); billiardBunnyCopy.rotate(pivot,
	 * -theta + Math.PI);
	 * 
	 * return new Group(billiardBunnyCopy.getPolygon(), headCopy.getPolygon()); }
	 */

	// rotate tank clockwise if stopped and turns tank right when moving
	private void right() {
		/*
		 * lastMovementOp = Op.RIGHT; rotate(TURNING_ANGLE);
		 */

		lastMovementOp = Op.RIGHT;
		decomposedVelocity = new Point2D(3, 0);// Physics.decomposeVector(VELOCITY,
												// 0);
		// multiply by tankHealth to reduce speed
		moveBy(decomposedVelocity);
	}

	// rotate tank counter clockwise if stopped and turns tank left when moving
	private void left() {
		/*
		 * lastMovementOp = Op.LEFT; rotate(-TURNING_ANGLE);
		 */

		lastMovementOp = Op.LEFT;
		decomposedVelocity = new Point2D(-3, 0);// Physics.decomposeVector(VELOCITY,
												// 180);
		// multiply by tankHealth to reduce speed
		moveBy(decomposedVelocity);
	}

	// The direction of angles is reversed because the coordinate system is
	// reversed.
	// rotates tank on axis
	private void rotate(final double theta) {
		this.theta += theta;
		billiardBunny.rotate(pivot, theta);
		decomposedVelocity = Physics.decomposeVector(VELOCITY, this.theta);
		negativeDecomposedVelocity = Physics.decomposeVector(-VELOCITY, this.theta);
		// bunnyImgView.setRotate(bunnyImgView.getRotate() +
		// Math.toDegrees(theta));
		syncShape();
	}

	// updates tanks shape
	private void syncShape() {
		shape = billiardBunny.getCircle();
		shapeOfTank = billiardBunny.getCircle();
		bunnyImgView.setX(getX() - 2 * billiardBunny.getRadius());
		bunnyImgView.setY(getY() - 3 * billiardBunny.getRadius());
	}

	// moves tank forward
	private void forward() {
		lastMovementOp = Op.FORWARD;
		decomposedVelocity = new Point2D(0, -3);// Physics.decomposeVector(-VELOCITY,
												// 90 / 3);
		// multiply by tankHealth to reduce speed
		moveBy(decomposedVelocity);
	}

	// moves tank backwards
	private void back() {
		lastMovementOp = Op.REVERSE;
		decomposedVelocity = new Point2D(0, 3);// Physics.decomposeVector(VELOCITY,
												// 90);
		// multiply by tankHealth to reduce speed
		moveBy(decomposedVelocity);
	}

	/**
	 * This function updates the velocity of the Bunny to have consistent speed with
	 * the other main directions
	 * 
	 * @param x x direction
	 * @param y y direction
	 */
	private void diagonal(String x, String y) {
		double xVal, yVal;

		if (y.equals("FORWARD")) {
			// bunny is moving forward
			lastMovementOp = Op.DIAGONAL_FORWARD;
			yVal = -2.12;
		} else {
			// bunny is moving in reverse
			lastMovementOp = Op.DIAGONAL_REVERSE;
			yVal = 2.12;
		}

		// move y position TODO
		decomposedVelocity = new Point2D(0, yVal);
		moveBy(decomposedVelocity);

		// handle collisions for first movement
		handleMazeCollisions();

		if (x.equals("RIGHT")) {
			xVal = 2.12;
			lastMovementOp = Op.DIAGONAL_RIGHT;
		} else {
			xVal = -2.12;
			lastMovementOp = Op.DIAGONAL_LEFT;
		}

		// move x position
		decomposedVelocity = new Point2D(xVal, 0);
		moveBy(decomposedVelocity);
	}

	private void moveBy(final Point2D point) {
		if (billiardBunny.getCenter().getY() < 0) {
			Point2D newPoint = new Point2D(point.getX(), point.getY() + 5);
			// head.moveBy(newPoint);
			billiardBunny.moveBy(newPoint);
			pivot = pivot.add(newPoint);
			syncShape();
		} else if (billiardBunny.getCenter().getY() > 800) {
			Point2D newPoint = new Point2D(point.getX(), point.getY() - 5);
			// head.moveBy(newPoint);
			billiardBunny.moveBy(newPoint);
			pivot = pivot.add(newPoint);
			syncShape();
		} else if (billiardBunny.getCenter().getX() < 0) {
			Point2D newPoint = new Point2D(point.getX() + 5, point.getY());
			// head.moveBy(newPoint);
			billiardBunny.moveBy(newPoint);
			pivot = pivot.add(newPoint);
			syncShape();
		} else if (billiardBunny.getCenter().getX() > 800) {
			Point2D newPoint = new Point2D(point.getX() - 5, point.getY());
			// head.moveBy(newPoint);
			billiardBunny.moveBy(newPoint);
			pivot = pivot.add(newPoint);
			syncShape();
		} else {
			billiardBunny.moveBy(point);
			pivot = pivot.add(point);
			syncShape();
		}
	}

	// gets location of point where bullet should exit. returns location of end
	// of tank barrel on the pane.
	private Point2D getBulletLaunchPoint() {
		if (!bunnyExists) {
			/*
			 * final Point2D topRight = billiardBunny.getTopRight(); final Point2D
			 * bottomRight = billiardBunny.getBottomRight(); return
			 * topRight.midpoint(bottomRight);
			 */
			return billiardBunny.getCenter();
		} else {
			/*
			 * final Point2D topLeft = billiardBunny.getTopLeft(); final Point2D bottomLeft
			 * = billiardBunny.getBottomLeft(); return topLeft.midpoint(bottomLeft);
			 */
			return billiardBunny.getCenter();
		}

	}

	private double getTheta() {
		return theta;
	}

	// returns center or pivot point of tank
	private Point2D getCenter() {
		return pivot;
	}

	Shape getShape() {
		return shape;
	}

	// returns shape of tank
	public Shape getTankShape() {
		return shapeOfTank;
	}

	// checks if tank is hitting another object such as a maze wall or circle
	// bullet
	private boolean checkCollision(final Shape shape) {
		return Physics.isIntersecting(getShape(), shape);
	}

	// detects if a tank is hit by circle shaped bullet object
	boolean isHit(FireBallManager tbm) {
		for (FireBall t : tbm.tankShots()) {
			if (Physics.isIntersecting(getShape(), t.getShape())) {
				return true;
			}
		}
		return false;
	}

	// detects if a tank hits or runs into health frog
	boolean isHit(SeedBasketManager frogs) {
		for (SeedBasket f : frogs.getSeedBaskets()) {
			if (Physics.isIntersecting(getShape(), f.getShape())) {
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
	private void handleMazeCollisions() {
		final ArrayList<PlayerRectangle> segs = maze.getCollisionCandidates(getCenter());

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
		final Bunny tank = this;
		// Need to declare this up here instead of in each case because java's
		// switch cases share scope. So java would think
		// we are redeclaring a variable.
		final Point2D decomposedVelocity;
		switch (lastMovementOp) {
		case FORWARD:
			decomposedVelocity = new Point2D(0,

					3);
			reverseOp = () -> tank.moveBy

			(decomposedVelocity);
			changeDir(lastMovementOp);
			break;
		case REVERSE:
			decomposedVelocity = new Point2D(0,

					-3);
			reverseOp = () -> tank.moveBy

			(decomposedVelocity);
			changeDir(lastMovementOp);
			break;
		case RIGHT:
			decomposedVelocity = new Point2D(-

			3, 0);
			reverseOp = () -> tank.moveBy

			(decomposedVelocity);
			changeDir(lastMovementOp);
			break;
		case LEFT:
			decomposedVelocity = new Point2D(3,

					0);
			reverseOp = () -> tank.moveBy

			(decomposedVelocity);
			changeDir(lastMovementOp);
			break;
		case DIAGONAL_FORWARD:
			decomposedVelocity = new Point2D(0,

					2.12);
			reverseOp = () -> tank.moveBy

			(decomposedVelocity);
			break;
		case DIAGONAL_REVERSE:
			decomposedVelocity = new Point2D(0,

					-2.12);
			reverseOp = () -> tank.moveBy

			(decomposedVelocity);
			break;
		case DIAGONAL_RIGHT:
			decomposedVelocity = new Point2D(-

			2.12, 0);
			reverseOp = () -> tank.moveBy

			(decomposedVelocity);
			break;
		case DIAGONAL_LEFT:
			decomposedVelocity = new Point2D

			(2.12, 0);
			reverseOp = () -> tank.moveBy

			(decomposedVelocity);
			break;
		}
		do {
			assert reverseOp != null;
			reverseOp.run();

			for (int i = 0; i < segs.size(); i

			++) {
				if (!checkCollision

				(segs.get(i).getPolygon())) {
					segs.remove(i);
					i--;
				}
			}
		} while (segs.size() > 0);
	}

	// handles press of a key in tank keycode
	void handlePressed(final KeyCode keyCode) {
		setAutoMove(false);
		activeOps.add(keycodes.get(keyCode));
		waitTimer.cancel();
		waitTimer.purge();
	}

	// handles release of button in tank keycode
	void handleReleased(final KeyCode keyCode) {
		final Op op = keycodes.get(keyCode);
		if (op == Op.FIRE) {
			tankBulletManager.lock = false;
		}
		activeOps.remove(op);
		waitTimer = new Timer();
		waitTimer.schedule(new java.util.TimerTask() {
			@Override
			public void run() {
				setAutoMove(true);
			}
		},
				// execute after three seconds
				2000);
	}

	void updateHero(Point2D point) {
		heroCord[0] = point.getX();
		heroCord[1] = point.getY();
	}

	public double distX() {
		double x = heroCord[0] - billiardBunny.getCenter().getX();
		return x;
	}

	public double distY() {
		double y = heroCord[1] - billiardBunny.getCenter().getY();
		return y;
	}

	public void fixTheta() {
		if (theta > 2 * Math.PI) {
			theta -= 2 * Math.PI;
		} else if (theta < 0) {
			theta += 2 * Math.PI;
		}
	}

	public void autoAim() {
		double angle = Math.toDegrees(Math.atan2(distY(), distX()));
		if (angle < 0) {
			angle += 360;
		}
		double it = theta - Math.toRadians(angle);
		if (theta != Math.toRadians(angle))
			rotate(-1 * it);
	}

	public void BUNNYaim() {
		double angle = Math.toDegrees(Math.atan2

		(distY(), distX()));
		if (angle < 0) {
			angle += 360;
		}
		double random = Math.random() * .314;
		Random rand = new Random();
		int n = rand.nextInt(2) + 1;
		if (n == 1) {

		} else {
			random = -random;
		}
		double it = theta - Math.toRadians(angle) +

				random;
		if (theta != Math.toRadians(angle))
			rotate(-1 * it);
	}

	public void GRATaim() {
		double angle = Math.toDegrees(Math.atan2

		(distY(), distX()));
		if (angle < 0) {
			angle += 360;
		}
		double it = theta - Math.toRadians(angle);
		if (theta != Math.toRadians(angle))
			rotate(-1 * it);
	}

	private int c = 0;
	private long expiry;
	private long elapsed;
	private static final long DURATION = TimeUnit.SECONDS.toNanos(5);
	private static final long DURATION2 = TimeUnit.SECONDS.toNanos(20);
	long first;
	long second;

	void autoMove(long nanos) {
		if (nanos < second) {
			randMove();
			handleMazeCollisions();
		} else if (nanos >= second && nanos <= elapsed) {
			dirMove();
			handleMazeCollisions();
		} else if (nanos > elapsed) {
			elapsed = nanos + DURATION2;
			first = (long) ((elapsed - nanos) * .9);
			second = (nanos + first);
		}
	}

	void dirMove() {
		double x = distX();
		double y = distY();
		if (Math.abs(x) > Math.abs(y)) {
			if (x < 0) {
				left();
			} else {
				right();
			}
		} else {
			if (y < 0) {
				forward();
			} else {
				back();
			}
		}
	}

	private int c2 = 0;
	private int n = 0;

	void randMove() {
		Random rand = new Random();
		if (c2 == 0) {
			n = rand.nextInt(4) + 1;
			c2++;
		} else {
			c2++;
		}
		if (c2 == 75) {
			c2 = 0;
		}
		if (n == 1) {
			if (getY() <= 20) {
				n = 2;
				back();
			} else {
				forward();
			}

		} else if (n == 2) {
			if (getY() >= 770) {
				n = 1;
				forward();
			} else {
				back();
			}
		} else if (n == 3) {
			if (getX() <= 20) {
				n = 4;
				right();
			} else {
				left();
			}
		} else if (n == 4) {
			if (getX() >= 770) {
				n = 3;
				left();
			} else {
				right();
			}
		}

	}

	private void changeDir(Op lastMovementOp2) {
		// TODO Auto-generated method stub
		if (lastMovementOp2 == Op.FORWARD) {
			if (distX() < 0) {
				left();
			} else {
				right();
			}
		} else if (lastMovementOp2 == Op.LEFT) {
			if (distY() < 0) {
				forward();
			} else {
				back();
			}
		} else if (lastMovementOp2 == Op.RIGHT) {
			if (distY() < 0) {
				forward();
			} else {
				back();
			}
		} else if (lastMovementOp2 == Op.REVERSE) {
			if (distX() < 0) {
				left();
			} else {
				right();
			}
		}
	}

	public double dist() {
		double z = Math.pow(distX(), 2) + Math.pow(distY(), 2);
		double ans = Math.sqrt(z);
		return ans;
	}

	// handle updates the state of the tank and the tank's bullets.
	void handle(final long nanos) {
		if (c == 0) {
			expiry = nanos + DURATION;
			elapsed = nanos + DURATION2;
			first = (long) ((elapsed - nanos) * .9);
			second = (nanos + first);
			c++;
		}
		fixTheta();

		if (isGiantRat) {
			GRATaim();
		} else {
			BUNNYaim();
		}
		if (shouldAutoMove()) {
			autoMove(nanos);
		}

		if (nanos >= expiry) {
			tankBulletManager.addBullet(getBulletLaunchPoint(), getTheta(), nanos);
			expiry = nanos + DURATION;
		}
		tankBulletManager.update(nanos);
		// prevents even attempting to fire if out of ammo
		if (activeOps.contains(Op.FIRE) && !tankBulletManager.outOfAmmo() && bunnyFreeze == false) {
			// shoots backwards if bunny exists
			if (!bunnyExists) {
				tankBulletManager.addBullet(getBulletLaunchPoint(), getTheta(), nanos);
			} else {
				tankBulletManager.addBullet(getBulletLaunchPoint(), getTheta() + Math.PI, nanos);
			}
			// ammo check
			// System.out.println(mainColorName + " AmmoCount: " +
			// tankBulletManager.getAmmo());
			tankBulletManager.lock = true;
		}
		tankBulletManager.handleMazeCollisions();

		if (tankBulletManager.isReloading() || tankBulletManager.outOfAmmo()) {
			billiardBunny.getCircle().setFill(outOfAmmoHeadColor);
		} else {
			billiardBunny.getCircle().setFill(headColor);
		}

		// TODO test

		if (bunnyFreeze == false && isPlayerBunny()) {
			if (activeOps.size() > 1) {
				// multiple control buttons are being pressed
				if (activeOps.contains(Op.RIGHT) && activeOps.contains(Op.FORWARD)) {
					diagonal("RIGHT", "FORWARD");
				} else if (activeOps.contains(Op.LEFT) && activeOps.contains(Op.FORWARD)) {
					diagonal("LEFT", "FORWARD");
				} else if (activeOps.contains(Op.RIGHT) && activeOps.contains(Op.REVERSE)) {
					diagonal("RIGHT", "REVERSE");
				} else if (activeOps.contains(Op.LEFT) && activeOps.contains(Op.REVERSE)) {
					diagonal("LEFT", "REVERSE");
				}

				handleMazeCollisions();
			} else {

				if (activeOps.contains(Op.RIGHT)) {
					right();
				} else if (activeOps.contains(Op.LEFT)) {
					left();
				} else if (activeOps.contains(Op.FORWARD)) {
					forward();
				} else if (activeOps.contains(Op.REVERSE)) {
					back();
				}

				handleMazeCollisions();
			}
		}

		if (isHit(getBulletManager())) {
			double randomVariable = Math.random();
			if (randomVariable <= 0.1) {
				System.out.println("giant rat");
				if (!isGiantRat) {
					isGiantRat = true;
					bunnyImgView.setImage(ratImg);
					System.out.println("Setting new Image...");
					syncShape();
				} else {
					isGiantRat = false;
					bunnyImgView.setImage(bunnyImage);
					System.out.println("Setting old Image...");
					syncShape();
				}
			} else if (randomVariable <= 0.2 && randomVariable > 0.1) {
				// split bunny
				System.out.println("bunny split");
				setSplit(true);
			} else if (randomVariable <= 0.7 && randomVariable > 0.2) {
				System.out.println("bunny freeze");
				setFreeze(true);
				new java.util.Timer().schedule(new java.util.TimerTask() {
					@Override
					public void run() {
						setFreeze(false);
					}
				},
						// execute after three seconds
						3000);
			} else {
				System.out.println("do nothing");
				// nothing
			}
		}
	}

	// method to kill tank and turn its colors to dead colors
	void kill() {
		dead = true;
		// head.getPolygon().setFill(DEATH_COLOR);
		billiardBunny.getCircle().setFill(DEATH_COLOR);
	}

	// returns true if tank died, false if it is still alive.
	boolean isDead() {
		return dead;
	}

	// returns string value of tanks main color, pink or blue
	String getMainColorName() {
		return mainColorName;
	}

	// when tank is hit by any bullet subtracts health from tank in increments
	// of 20%.
	void subtractHealth() {
		this.currentHealth -= .2;
	}

	// when tank runs into a frog adds health in increments of 20%.
	void addHealth() {
		if (this.getCurrentHealth() < 1) {
			this.currentHealth += .2;
		}
	}

	// returns double value of tanks current health
	double getCurrentHealth() {
		return currentHealth;
	}

	private enum Op {
		FORWARD, RIGHT, LEFT, REVERSE, FIRE, DIAGONAL_FORWARD, DIAGONAL_REVERSE, DIAGONAL_RIGHT, DIAGONAL_LEFT,
	}

	public double getX() {
		return billiardBunny.getCenter().getX();
	}

	public double getY() {
		return billiardBunny.getCenter().getY();
	}
}
