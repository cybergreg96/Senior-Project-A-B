
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Timer;

// Game represents the state of the game and acts as the glue class between all of the other components.
//
// NOTE: All methods that will be called from JavaFX onto user code run on a
// single thread so no synchronization is necessary.
// https://docs.oracle.com/javase/8/javafx/get-started-tutorial/jfx-architecture.htm
class TankGame {
	// WIDTH and HEIGHT of the scene.
	// We add the thickness because at far right and bottom edges of the screen
	// we are going to place
	// the final segments of the grid and they need additional space because of
	// how the grid drawing algorithm works.
	// See the Maze class.
	private static final double WIDTH = TankCell.LENGTH * Maze.COLUMNS + Maze.THICKNESS;
	private static final double HEIGHT = TankCell.LENGTH * Maze.ROWS + Maze.THICKNESS;

	private static final ButtonType PLAY_AGAIN_BUTTON_TYPE = new ButtonType("PLAY AGAIN", ButtonBar.ButtonData.YES);
	// This is unfortunate but javafx sucks. One of the buttons need to be a
	// cancel button otherwise you cant X the dialog...
	// I'd rather not add a third button so this is how its going to work
	// unfortunately. Worse part is that it treats
	// closing the window as clicking the cancel button, which is certainly not
	// necessarily the case. We would prefer the
	// process exit if the alert window is Xed. Maybe this is a misuse of alerts
	// but whatever.
	private static final ButtonType MAIN_MENU_BUTTON_TYPE = new ButtonType("MAIN MENU", ButtonBar.ButtonData.YES);
	private static final ButtonType CONT_BUTTON_TYPE = new ButtonType("CONTINUE", ButtonBar.ButtonData.NO);

	private final Maze maze = new Maze();
	Circle h1 = new Circle(17, 18, 8, Color.GREEN);
	Circle h2 = new Circle(17, 43, 8, Color.GREEN);
	Circle h3 = new Circle(17, 68, 8, Color.GREEN);
	Circle h4 = new Circle(17, 93, 8, Color.GREEN);
	Circle h5 = new Circle(17, 118, 8, Color.GREEN);
	
	Circle h11 = new Circle(774, 675, 8, Color.RED);
	Circle h21 = new Circle(774, 700, 8, Color.RED);
	Circle h31 = new Circle(774, 725, 8, Color.RED);
	Circle h41 = new Circle(774, 750, 8, Color.RED);
	Circle h51 = new Circle(774, 775, 8, Color.RED);
	private final Tank tank1 = new Tank("blue", Color.SKYBLUE, Color.DARKBLUE, Color.LIGHTBLUE, maze, Tank.KEY_CODES_2, 0, 1);
	private final Tank tank2 = new Tank("pink", Color.PINK, Color.DARKRED, Color.LIGHTPINK, maze, Tank.KEY_CODES_1, Math.PI, 1);
	private final Stage stage;
	private final TankFPSMeter fpsMeter = new TankFPSMeter();
	private final TankBunnyFritzManager bunnyManager;

	private AnimationTimer timer;

	TankGame(final Stage stage) {
        bunnyManager = new TankBunnyFritzManager(maze, WIDTH, HEIGHT);
		this.stage = stage;
		final Group root = new Group();
		final Scene scene = new Scene(root, WIDTH, HEIGHT);
		root.getChildren().add(maze.getNode());
		root.getChildren().addAll(h1,h2,h3,h4,h5);
		root.getChildren().addAll(h11,h21,h31,h41,h51);
		root.getChildren().addAll(tank1.getNode(), tank2.getNode(), tank1.getBulletManager().getNode(),
				tank2.getBulletManager().getNode(), bunnyManager.getNode());

		scene.addEventHandler(KeyEvent.KEY_PRESSED, this::handlePressed);
		scene.addEventHandler(KeyEvent.KEY_RELEASED, this::handleReleased);

		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				KeyCode code = event.getCode();
				if (code.toString() == "ESCAPE") {

					timer.stop();

					final Alert alert = new Alert(Alert.AlertType.INFORMATION);
					alert.setTitle("Tank Tank Menu");
					alert.setHeaderText("Paused");

					alert.getButtonTypes().setAll(MAIN_MENU_BUTTON_TYPE, PLAY_AGAIN_BUTTON_TYPE, CONT_BUTTON_TYPE);
					final Optional<ButtonType> buttonType = alert.showAndWait();

					// If the alert had no result, then we default to showing
					// the main menu.
					if (buttonType.get() == MAIN_MENU_BUTTON_TYPE) {
						try {
							Parent x = FXMLLoader.load(getClass().getResource("StartScreen.fxml"));
							x.setStyle("-fx-background-color: #a50000");
							Scene y = new Scene(x);
							Stage w = stage;
							w.setResizable(false);
							w.setScene(y);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					} else if(buttonType.get() == PLAY_AGAIN_BUTTON_TYPE) {
						final TankGame tankGame = new TankGame(stage);
						tankGame.start();
					} else if(buttonType.get() == CONT_BUTTON_TYPE){
						alert.hide();
						timer.start();
					}
				}

			}
		});

		stage.setScene(scene);
		// Must be called after the scene is set.
		stage.centerOnScreen();
	}

	// The game loop runs on an AnimationTimer which calls handle() about every
	// 1/60 of a second.
	// Rendering and updating are handled separately in JavaFX so this is the
	// standard design of a game loop.
	// https://gafferongames.com/post/fix_your_timestep/
	// https://gamedev.stackexchange.com/questions/1589/when-should-i-use-a-fixed-or-variable-time-step
	// There are many other articles recommending this design.
	// Though, I am not positive it works the way I think it does and the docs
	// are not very clear. So whatever,
	// no big deal.
	void start() {
		final TankGame g = this;
		timer = new AnimationTimer() {
			@Override
			public void handle(final long now) {
				g.handle(now);
			}
		};
		timer.start();
		Timer timerr = new Timer();
		ArrayList<String> time = new ArrayList<>();
		time.add("0");time.add("1");time.add("2");time.add("3");time.add("4");time.add("5");time.add("6");time.add("7");time.add("8");time.add("9");time.add("10");
		while(time.contains(timer.toString())){
			final Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("Tank Tank Menu");
			alert.setHeaderText("Paused");
		}
	}

	private void handle(final long nanos) {
		fpsMeter.handle(nanos);

		if (tank1.isDead() || tank2.isDead()) {
			timer.stop();

			final Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("Tank Tank");
			alert.setHeaderText("Game Over!");

			String alertContent = "Wow, what a close game. It's a tie!";
			// If later we allow the game to keep continuing in the background,
			// we will need to ensure we keep
			// the alert's graphic in sync with the winning tank.
			// We could use either tank here for a tie because we just need to
			// show a black tank.
			// Probably cleaner to create a brand new one. But whatever, this
			// means when tank1 does win, we do not need
			// to update the graphic because its already correct.
			Node graphic = tank1.getWinPose();
			Tank winningTank = null;

			if (tank1.isDead()) {
				if (!tank2.isDead()) {
					winningTank = tank2;
					graphic = tank2.getWinPose();
				}
			} else {
				winningTank = tank1;
			}

			if (winningTank != null) {
				alertContent = String.format("Congratulations to the %s tank for winning!",
						winningTank.getMainColorName());
			}
			alert.setGraphic(graphic);
			alert.setContentText(alertContent);

			alert.getButtonTypes().setAll(MAIN_MENU_BUTTON_TYPE, PLAY_AGAIN_BUTTON_TYPE);

			// Must run later because we cannot call alert.showAndWait() during
			// animation processing. See its docs.
			// And we might want animation to continue down the road anyhow.
			Platform.runLater(() -> {

				// This is optional because the alert can be abnormally closed
				// and return no result.
				// See
				// https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/Dialog.html
				final Optional<ButtonType> buttonType = alert.showAndWait();

				// If the alert had no result, then we default to showing the
				// main menu.
				if (!buttonType.isPresent() || buttonType.get() == MAIN_MENU_BUTTON_TYPE) {

					try {
						Parent x = FXMLLoader.load(getClass().getResource("StartScreen.fxml"));
						x.setStyle("-fx-background-color: #a50000");
						Scene y = new Scene(x);
						Stage w = stage;
						w.setResizable(false);
						w.setScene(y);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					return;
				}

				final TankGame tankGame = new TankGame(stage);
				tankGame.start();
			});
			return;
		}

		// TODO in future use a single bullet manager and a separate bullet
		// limiter.
		// TODO in the future another possibility would be to allow the winning
		// tank to move. Not a big deal right now.
		
		if(tank1.isHit(tank1.getBulletManager()) || tank1.isHit(tank2.getBulletManager())) {
			tank1.subtractHealth();
		}
		if(tank2.isHit(tank2.getBulletManager()) || tank2.isHit(tank1.getBulletManager())) {
			tank2.subtractHealth();
		}
		
		String health1 = Double.toString(tank1.getCurrentHealth());
		
		if(health1.contains(".8")){
			h5.setVisible(false);
		}
		if(health1.contains(".6")){
			h4.setVisible(false);
		}
		if(health1.contains(".4")){
			h3.setVisible(false);
		}
		if(health1.contains(".2")){
			h2.setVisible(false);
		}
		if(tank1.getCurrentHealth()<.01){
			h1.setVisible(false);
		}
		
		String health2 = Double.toString(tank2.getCurrentHealth());
		if(health2.contains(".8")){
			h11.setVisible(false);
		}
		if(health2.contains(".6")){
			h21.setVisible(false);
		}
		if(health2.contains(".4")){
			h31.setVisible(false);
		}
		if(health2.contains(".2")){
			h41.setVisible(false);
		}
		if(tank2.getCurrentHealth()<.01){
			h51.setVisible(false);
		}
		
		
		if (tank1.getBulletManager().isDeadTank(tank1) || tank2.getBulletManager().isDeadTank(tank1)) {
			tank1.kill();
		}
		if (tank1.getBulletManager().isDeadTank(tank2) || tank2.getBulletManager().isDeadTank(tank2)) {
			tank2.kill();
		}
		if (tank1.isDead() || tank2.isDead()) {
			// We draw the dead tanks before we announce to the players.
			// Otherwise if we try and prompt in this pulse, then there is a
			// slight freeze before
			// the dead tank can be drawn. Its awkward and ruins the fluidity.
			return;
		}

        tank1.bunnyExists = bunnyManager.bunnyExists;
        tank2.bunnyExists = bunnyManager.bunnyExists;

        tank1.handle(nanos);
        tank2.handle(nanos);
        bunnyManager.handle(nanos);
	}

	private void handlePressed(final KeyEvent e) {
		tank1.handlePressed(e.getCode());
		tank2.handlePressed(e.getCode());
	}

	private void handleReleased(final KeyEvent e) {
		tank1.handleReleased(e.getCode());
		tank2.handleReleased(e.getCode());
	}
}
