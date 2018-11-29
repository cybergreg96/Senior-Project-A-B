package BilliardGame;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import TankGame.TankGame;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ToolBar;

import javafx.scene.input.KeyEvent;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class BilliardController   {
	
	private int WIDTH = 850;
	private int HEIGHT = 550;

	final Group root = new Group();
	private AnimationTimer timer;
	private final Maze maze = new Maze();

	public BilliardController(final Stage stage) {
		
		final Scene scene = new Scene(root, WIDTH, HEIGHT);
		root.getChildren().add(maze.getNode());
		Player player = new Player(maze, Player.KEY_CODES_1, 0.0, 20.0);
		root.getChildren().add(player.getNode());
		
		scene.setRoot(root);
		stage.setScene(scene);
		// Must be called after the scene is set.
		stage.centerOnScreen();
	}
	
	// The game loop runs on an AnimationTimer which calls handle() about every 1/60 of a second.
		public void start() 
		{
			final BilliardController g = this;
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
		
		
		
		private void handle(final long nanos) 
		{
			
		}
}
