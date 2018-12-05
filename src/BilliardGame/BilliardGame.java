package BilliardGame;

/*
 * references: https://github.com/nhooyr/java-tanktank
 */
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Timer;
// Game represents the state of the game and acts as the glue class between all of the other components.

public class BilliardGame
{
	// WIDTH and HEIGHT of the scene.
	// We add the thickness because at far right and bottom edges of the screen
	// we are going to place
	// the final segments of the grid and they need additional space because of
	// how the grid drawing algorithm works.
	// See the Maze class.
	private static final double WIDTH = Cell.LENGTH * Maze.COLUMNS + Maze.THICKNESS;
	private static final double HEIGHT = Cell.LENGTH * Maze.ROWS + Maze.THICKNESS;

	private static final ButtonType PLAY_AGAIN_BUTTON_TYPE = new ButtonType("PLAY AGAIN", ButtonBar.ButtonData.YES);
	private static final ButtonType MAIN_MENU_BUTTON_TYPE = new ButtonType("MAIN MENU", ButtonBar.ButtonData.YES);
	private static final ButtonType CONT_BUTTON_TYPE = new ButtonType("CONTINUE", ButtonBar.ButtonData.NO);

	private final Maze maze = new Maze();

	// health circles for blue tank
	Circle h1 = new Circle(17, 18, 8, Color.GREEN);
	Circle h2 = new Circle(17, 43, 8, Color.GREEN);
	Circle h3 = new Circle(17, 68, 8, Color.GREEN);
	Circle h4 = new Circle(17, 93, 8, Color.GREEN);
	Circle h5 = new Circle(17, 118, 8, Color.GREEN);
	Circle h6 = new Circle(17, 143, 8, Color.GREEN);
	Circle h7 = new Circle(17, 168, 8, Color.GREEN);
	Circle h8 = new Circle(17, 193, 8, Color.GREEN);
	Circle h9 = new Circle(17, 218, 8, Color.GREEN);
	Circle h10 = new Circle(17, 243, 8, Color.GREEN);
	// health circles for pink tank
	Circle h11 = new Circle(774, 675, 8, Color.RED);
	Circle h21 = new Circle(774, 700, 8, Color.RED);
	Circle h31 = new Circle(774, 725, 8, Color.RED);
	Circle h41 = new Circle(774, 750, 8, Color.RED);
	Circle h51 = new Circle(774, 775, 8, Color.RED);
	private final Hero tank1 = new Hero("blue", Color.SKYBLUE, Color.DARKBLUE, Color.LIGHTBLUE, maze, Hero.KEY_CODES_2,
			0, 1);
	private final Bunny tank2 = new Bunny("pink", Color.PINK, Color.DARKRED, Color.LIGHTPINK, maze, Bunny.KEY_CODES_1,
			Math.PI, 1);
	private final Stage stage;
	private final FPSMeter fpsMeter = new FPSMeter();

	private AnimationTimer timer;

	private final SeedBasketManager frogManager;
	final Group root = new Group();
	// tank game constructor. places all objects on pane and slowly eat away
	// maze wall objects until just health circles, tanks, and some maze walls
	// are present within the pane
	public BilliardGame(final Stage stage) 
	{

		frogManager = new SeedBasketManager(maze, WIDTH, HEIGHT);
		tank2.getBulletManager().setEnemyTank(tank1);

		this.stage = stage;

		final Scene scene = new Scene(root, WIDTH, HEIGHT);
		root.getChildren().add(maze.getNode());
		root.getChildren().addAll(h1, h2, h3, h4, h5, h6, h7, h8, h9, h10);
		root.getChildren().addAll(h11, h21, h31, h41, h51);
		root.getChildren().addAll(tank1.getNode(), tank2.getNode(),
				tank2.getBulletManager().getNode(), frogManager.getNode());

		scene.addEventHandler(KeyEvent.KEY_PRESSED, this::handlePressed);
		scene.addEventHandler(KeyEvent.KEY_RELEASED, this::handleReleased);

		scene.setOnKeyPressed(new EventHandler<KeyEvent>()
		{
			@Override
			public void handle(KeyEvent event) 
			{
				KeyCode code = event.getCode();
				if (code.toString() == "ESCAPE") 
				{

					timer.stop();

					final Alert alert = new Alert(Alert.AlertType.INFORMATION);
					alert.setTitle("TANK TANK Menu");
					alert.setHeaderText("Paused");

					alert.getButtonTypes().setAll(MAIN_MENU_BUTTON_TYPE, PLAY_AGAIN_BUTTON_TYPE, CONT_BUTTON_TYPE);
					final Optional<ButtonType> buttonType = alert.showAndWait();

					// If the alert had no result, then we default to showing
					// the main menu.
					if (buttonType.get() == MAIN_MENU_BUTTON_TYPE)
					{
						try 
						{
							Parent x = FXMLLoader.load(getClass().getClassLoader().getResource("StartScreen.fxml"));
							x.setStyle("-fx-background-color: #a50000");
							Scene y = new Scene(x);
							Stage w = stage;
							w.setResizable(false);
							w.setScene(y);
						} 
						catch (IOException e1) 
						{
							e1.printStackTrace();
						}
					} 
					else if (buttonType.get() == PLAY_AGAIN_BUTTON_TYPE) 
					{
						final BilliardGame tankGame = new BilliardGame(stage);
						tankGame.start();
					} 
					else if (buttonType.get() == CONT_BUTTON_TYPE) 
					{
						alert.hide();
						timer.start();
					}
				}

			}
		});
		scene.setRoot(root);
		stage.setScene(scene);
		// Must be called after the scene is set.
		stage.centerOnScreen();
	}

	// The game loop runs on an AnimationTimer which calls handle() about every 1/60 of a second.
	public void start() 
	{
		final BilliardGame g = this;
		timer = new AnimationTimer() 
		{
			@Override
			public void handle(final long now) 
			{
				g.handle(now);
			}
		};
		timer.start();
	}
	// handles state of a tanks death, setting winning message and winning alert
	// box.
	private void handle(final long nanos) 
	{
		fpsMeter.handle(nanos);
		
		if (tank1.isDead() || tank2.isDead()) 
		{
			timer.stop();

			final Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("TANK TANK");
			alert.setHeaderText("Game Over! TANK You Very Much For Playing!!!");

			String alertContent = "Wow, what a close game. It's a tie!";

			//Node graphic = tank1.getWinPose();
			Bunny winningTank = null;

			if (tank1.isDead()) 
			{
				if (!tank2.isDead()) 
				{
					winningTank = tank2;
					//graphic = tank2.getWinPose();
				}
			} 

			if (winningTank != null) 
			{
				alertContent = String.format("Congratulations to the %s tank for winning!",
						winningTank.getMainColorName());
			}
			//alert.setGraphic(graphic);
			alert.setContentText(alertContent);

			alert.getButtonTypes().setAll(MAIN_MENU_BUTTON_TYPE, PLAY_AGAIN_BUTTON_TYPE);

			Platform.runLater(() -> {

				final Optional<ButtonType> buttonType = alert.showAndWait();

				// If the alert had no result, then we default to showing the
				// main menu.
				if (!buttonType.isPresent() || buttonType.get() == MAIN_MENU_BUTTON_TYPE) 
				{

					try
					{
						Parent x = FXMLLoader.load(getClass().getResource("StartScreen.fxml"));
						x.setStyle("-fx-background-color: #a50000");
						Scene y = new Scene(x);
						Stage w = stage;
						w.setResizable(false);
						w.setScene(y);
					}
					catch (IOException e1)
					{
						e1.printStackTrace();
					}
					return;
				}

				final BilliardGame tankGame = new BilliardGame(stage);
				tankGame.start();
			});
			return;
		}

		// handles when a tank is hit by bullet object and determining what tank
		// to subtract health from.
		if (tank1.isHit(tank2.getBulletManager()))
		{
			tank1.subtractHealth();
		}
		
		// handles when a tank is hit by frog object and determining what tank
		// to add health to.
		if (frogManager.isHit(tank1))
		{
			tank1.addHealth();
		}
		

		// handles which health circles to be displayed based on current health
		// of tank1 or blue tank
		String health1 = Double.toString(tank1.getCurrentHealth());
		if(health1.contains("1.0")) {
			h1.setVisible(true);
			h2.setVisible(true);
			h3.setVisible(true);
			h4.setVisible(true);
			h5.setVisible(true);
			h6.setVisible(true);
			h7.setVisible(true);
			h8.setVisible(true);
			h9.setVisible(true);
			h10.setVisible(true);
		}
		if(health1.contains(".9")) {
			h1.setVisible(true);
			h2.setVisible(true);
			h3.setVisible(true);
			h4.setVisible(true);
			h5.setVisible(true);
			h6.setVisible(true);
			h7.setVisible(true);
			h8.setVisible(true);
			h9.setVisible(true);
			h10.setVisible(false);
		}
		if(health1.contains(".8")) {
			h1.setVisible(true);
			h2.setVisible(true);
			h3.setVisible(true);
			h4.setVisible(true);
			h5.setVisible(true);
			h6.setVisible(true);
			h7.setVisible(true);
			h8.setVisible(true);
			h9.setVisible(false);
		}
		if(health1.contains(".7")) {
			h1.setVisible(true);
			h2.setVisible(true);
			h3.setVisible(true);
			h4.setVisible(true);
			h5.setVisible(true);
			h6.setVisible(true);
			h7.setVisible(true);
			h8.setVisible(false);
		}
		if(health1.contains(".6")) {
			h1.setVisible(true);
			h2.setVisible(true);
			h3.setVisible(true);
			h4.setVisible(true);
			h5.setVisible(true);
			h6.setVisible(true);
			h7.setVisible(false);
		}
		if(health1.contains(".5")) {
			h1.setVisible(true);
			h2.setVisible(true);
			h3.setVisible(true);
			h4.setVisible(true);
			h5.setVisible(true);
			h6.setVisible(false);
		}
		if(health1.contains(".4")) {
			h1.setVisible(true);
			h2.setVisible(true);
			h3.setVisible(true);
			h4.setVisible(true);
			h5.setVisible(false);
		}
		if(health1.contains(".3")) {
			h1.setVisible(true);
			h2.setVisible(true);
			h3.setVisible(true);
			h4.setVisible(false);
		}
		if(health1.contains(".2")) {
			h1.setVisible(true);
			h2.setVisible(true);
			h3.setVisible(false);
		}
		if(health1.contains(".1")) {
			h1.setVisible(true);
			h2.setVisible(false);
		}
		if(tank1.getCurrentHealth() < .01) {
			h1.setVisible(false);
		}


		// handles which health circles to be displayed based on current health
		// of tank2 or pink tank
		String health2 = Double.toString(tank2.getCurrentHealth());
		if (health2.contains("1.0")) 
		{
			h51.setVisible(true);
			h41.setVisible(true);
			h31.setVisible(true);
			h21.setVisible(true);
			h11.setVisible(true);
		}

		if (health2.contains(".8")) 
		{
			h51.setVisible(true);
			h41.setVisible(true);
			h31.setVisible(true);
			h21.setVisible(true);
			h11.setVisible(false);
		}
		if (health2.contains(".6"))
		{
			h51.setVisible(true);
			h41.setVisible(true);
			h31.setVisible(true);
			h21.setVisible(false);
		}
		if (health2.contains(".4")) 
		{
			h51.setVisible(true);
			h41.setVisible(true);
			h31.setVisible(false);
		}
		if (health2.contains(".2")) 
		{
			h51.setVisible(true);
			h41.setVisible(false);
		}
		if (tank2.getCurrentHealth() < .01) 
		{
			h51.setVisible(false);
		}

		if (tank2.getBulletManager().isDeadTank(tank1)) 
		{
			tank1.kill();
		}
		
		if (tank1.isDead() || tank2.isDead())
		{
			// We draw the dead tanks before we announce to the players.
			// Otherwise if we try and prompt in this pulse, then there is a
			// slight freeze before
			// the dead tank can be drawn.
			return;
		}

		tank1.handle(nanos);
		tank2.handle(nanos);
		tank2.updateHero(tank1.getCenter());
		frogManager.handle(nanos);
	}
	private void handlePressed(final KeyEvent e)
	{
		tank1.handlePressed(e.getCode());
		tank2.handlePressed(e.getCode());
	}

	private void handleReleased(final KeyEvent e) 
	{
		tank1.handleReleased(e.getCode());
		tank2.handleReleased(e.getCode());
	}
}
