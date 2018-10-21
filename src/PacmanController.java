/*
 * This class references the project at this link: https://github.com/arashabedin/PacMan
 *
 */

import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.TreeMap;

import PacManGUI.SceneInfo;
import PacManLogic.Biscuits;
import PacManLogic.GameObject;
import PacManLogic.MapOutline;
import PacManLogic.MazePlayGround;
import PacManLogic.NonPlayerCharacter;
import PacManLogic.Player;
import PacManLogic.Ghosts.BlueGhost;
import PacManLogic.Ghosts.OrangeGhost;
import PacManLogic.Ghosts.PinkGhost;
import PacManLogic.Ghosts.RedGhost;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.DialogEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToolBar;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;

public class PacmanController {
	@FXML
	private Pane rootPane;
	@FXML
	private Group rootGroup;
	@FXML
	private ToolBar birdstufzBanner;
	@FXML
	private ToolBar birdstufzBanner1;
	@FXML
	private StackPane stackpane;

	@FXML
	Label labelStatus;
	@FXML
	Canvas root;

	@FXML
	Label myPoints = new Label();

	@FXML
	Button restartBtn = new Button();

	@FXML
	Label blueGhostSearches = new Label();

	@FXML
	Label pinkGhostSearches = new Label();

	@FXML
	Label orangeGhostSearches = new Label();

	@FXML
	Label redGhostSearches = new Label();

	@FXML
	Pane highScoreBackground;

	@FXML
	Pane highScorePane;

	@FXML
	Label highScoreLabel;

	@FXML
	Label highScoreRank, highScoreName, highScoreDisplay;


	private SceneInfo sceneInfo;
	private float refreshRate = 50;
	private KeyCode keyPressed = KeyCode.ALT;
	private KeyCode ghostKeyPressed = KeyCode.ALT;

	ArrayList < GameObject > gameObjects = new ArrayList < GameObject > ();
	private Player player;
	private ArrayList<PacmanScore> highScores;

	private MapOutline map = new MapOutline();
	private Biscuits biscuits;
	private PinkGhost pinkGhost;
	private BlueGhost blueGhost;
	private OrangeGhost orangeGhost;
	private RedGhost redGhost;
	private NonPlayerCharacter npc;
	
	//TODO experimenting
	AnimationTimer animation;

	MazePlayGround mazePlayGround = new MazePlayGround(javafx.scene.paint.Color.rgb(0, 0, 0), 1, 1, player, map);

	public void addStuff() {

		sceneInfo = new SceneInfo(root);
		player = new Player(new Point(20, 60), map);
		// NPC initializer
		npc = new NonPlayerCharacter(player, map);

		biscuits = new Biscuits(player, map, npc);
		biscuits.setTotalEatenBiscuits(0);
		biscuits.setTotalEatenBigBiscuits(0);
		pinkGhost = new PinkGhost(new Point(280, 300), map, player, biscuits);
		blueGhost = new BlueGhost(new Point(220, 300), map, player, biscuits);
		orangeGhost = new OrangeGhost(new Point(260, 260), map, player, biscuits);
		redGhost = new RedGhost(new Point(260, 300), map, player, biscuits);

		gameObjects.add(biscuits);
		gameObjects.add(player);
		gameObjects.add(pinkGhost);
		//gameObjects.add(blueGhost); don't add blue ghost (player 2) to separate its update and drawing
		gameObjects.add(orangeGhost);
		gameObjects.add(redGhost);
		gameObjects.add(mazePlayGround);
		gameObjects.add(npc);

	}
	public void restartGame() {
		pinkGhost.setEscapeTimeCount(0);
		pinkGhost.setEscape(false);
		gameObjects = new ArrayList < GameObject > ();
		keyPressed = KeyCode.ALT;
		ghostKeyPressed = KeyCode.ALT;

		//TODO start
		saveHighScores();
		
		// go back to high score screen and update scores
		highScoreBackground.setVisible(true);
		highScoreBackground.setMouseTransparent(false);
		//TODO end
		
		addStuff();

	}
	public void initialize() {

		//initialize game with high score screen
		highScoreBackground.setStyle("-fx-background-color: rgba(0, 0, 0, .85)");
		highScorePane.setStyle("-fx-background-color: rgba(200, 200, 200, 1)");

		// set value of highScore ArrayList
		readHighScoresFile();

		//update high score dislpay
		updateScoreDisplay();

		restartBtn.setOnAction(new EventHandler < ActionEvent > () {
			@Override
			public void handle(ActionEvent event) {
				restartGame();

			}
		});



		addStuff();

		Button goHome = new Button("Go home");
		Scene home = new Scene(goHome);
		home.snapshot(null);       
		goHome.setTranslateX(4);
		goHome.setTranslateY((650 / 2) - (goHome.getHeight() / 2)  - 50); 
		goHome.setRotate(-90);
		rootPane.getChildren().add(goHome);

		//handles the "Go Home" button
		goHome.setOnAction((ActionEvent e) -> {
			try 
			{
				saveHighScores();
				
				Parent x = FXMLLoader.load(getClass().getResource("StartScreen.fxml"));
				x.setStyle("-fx-background-color: #a50000");
				Scene y = new Scene(x);
				Stage w = (Stage)((Node)e.getSource()).getScene().getWindow();
				w.setResizable(false);
				w.setScene(y);
			} 
			catch (IOException e1) 
			{
				e1.printStackTrace();
			}
		});

		drawCanvas();
	}


	//set different keyPressed variables based on what type of key was pressed, WASD (letter key) for P1 and arrow keys for P2
	public void keyPressed(KeyCode keyCode) {
		if(keyCode.isLetterKey()) {
			this.keyPressed = keyCode;
		}
		if(keyCode.isArrowKey()) {
			this.ghostKeyPressed = keyCode;
		}
		if(keyCode == keyCode.ENTER) {
			this.ghostKeyPressed = keyCode.ENTER;
		}
	}


	private void update(long now) {

		//separated the 2nd player ghost being updated with a different keyCode to fix problems with one player not being able to move when the other presses a different key
		blueGhost.update(ghostKeyPressed);

		for (int i = 0; i < gameObjects.size(); i++) {

			gameObjects.get(i).update(keyPressed);


		}

		// keyPressed = KeyCode.ALT;
		if (pinkGhost.isEscape() == false)
			if (((player.getX() == pinkGhost.getX()) && (player.getY() == pinkGhost.getY())) || ((player.getX() == blueGhost.getX()) && (player.getY() == blueGhost.getY())) || ((player.getX() == orangeGhost.getX()) && (player.getY() == orangeGhost.getY())) || ((player.getX() == redGhost.getX()) && (player.getY() == redGhost.getY())))

			{				
				System.out.println("You lost");
				
				animation.stop();
				
				//TODO implement double points to score
				int score = (biscuits.getTotalEatenBiscuits() * 100) - 100;
				
				updateScores(score);
				
				restartGame();
			}
		drawCanvas();
		myPoints.setText("Points : " + String.valueOf((biscuits.getTotalEatenBiscuits() * 100) - 100));
		pinkGhostSearches.setText((pinkGhost.getTotalSearched() > 0 ? String.valueOf(pinkGhost.getTotalSearched()) : "Random mode"));
		orangeGhostSearches.setText((orangeGhost.getTotalSearched() > 0 ? String.valueOf(orangeGhost.getTotalSearched()) : "Random mode"));
		redGhostSearches.setText((redGhost.getTotalSearched() > 0 ? String.valueOf(redGhost.getTotalSearched()) : "Random mode"));
	}


	/**
	 * Draw the canvas - used in the gameloop
	 */
	private void drawCanvas() {
		GraphicsContext g = root.getGraphicsContext2D();

		// clear canvas
		g.clearRect(0, 0, sceneInfo.getWidth() * sceneInfo.getFieldWidth(), sceneInfo.getHeight() * sceneInfo.getFieldHeight());

		// draw gameObjects
		for (GameObject item: gameObjects) {
			item.draw(g, sceneInfo);
		}
		blueGhost.draw(g, sceneInfo);

		// biscuits.clear(g,sceneInfo);
	}

	@FXML
	public void onGoHomePressed(ActionEvent event) 
	{
		try 
		{
			saveHighScores();

			Parent x = FXMLLoader.load(getClass().getResource("StartScreen.fxml"));
			x.setStyle("-fx-background-color: #a50000");
			Scene y = new Scene(x);
			Stage w = (Stage)((Node)event.getSource()).getScene().getWindow();
			w.setResizable(false);
			w.setScene(y);
		} 
		catch (IOException e1) 
		{
			e1.printStackTrace();
		}
	}

	@FXML 
	public void onPlayGamePressed(ActionEvent event)
	{
		highScoreBackground.setVisible(false);
		highScoreBackground.setMouseTransparent(true);

		animation = new AnimationTimer() {
			long lastUpdate;
			public void handle(long now) {
				if (now > lastUpdate + refreshRate * 1690000) {
					lastUpdate = now;
					update(now);
				}
			}
		};
				
		animation.start();
	}

	public void updateScoreDisplay()
	{
		String rank = "";
		String scores = "";
		String names = "";

		//sort high scores from highest score to lowest score
		Collections.sort(highScores, Collections.reverseOrder());

		// display rank with name and score
		for(int i = 0; i < 10; i++)
		{
			rank = rank + (i + 1) + ". \n";

			if(highScores.size() > i)
			{
				//high score exists
				names = names + highScores.get(i).getPlayerName() + " \n";
				scores = scores + highScores.get(i).getScore() + " \n";
			}
			else
			{
				names = names + "aaa" + " \n";
				scores = scores + "0" + " \n";
			}
		}

		highScoreRank.setText(rank);
		highScoreName.setText(names);
		highScoreDisplay.setText(scores);
	}

	/**
	 * retrieve high score arraylist from a file
	 */
	public void readHighScoresFile()
	{
		String inputName = "pacman_scores.dat";
		String workingDir = System.getProperty("user.dir");
		File workingDirFile = new File(workingDir);
		File testFile = new File(workingDirFile, inputName);

		if (testFile.exists()) 
		{
			try 
			{
				ObjectInputStream in = new ObjectInputStream(new FileInputStream(testFile));

				highScores = (ArrayList<PacmanScore>) in.readObject();

				//add dummy scores to fill the rest of the high score page
				while(highScores.size() < 10)
				{
					highScores.add(new PacmanScore());
				}

				in.close();
			} 
			catch (FileNotFoundException e1)
			{
				e1.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		} 
		else 
		{
			highScores = new ArrayList<PacmanScore>();
		}
	}

	/**
	 * save high scores object to a file
	 */
	public void saveHighScores()
	{
		String inputName = "pacman_scores.dat";
		String workingDir = System.getProperty("user.dir");
		File workingDirFile = new File(workingDir);
		File testFile = new File(workingDirFile, inputName);

		try 
		{
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(testFile));

			out.writeObject(highScores);
			out.close();
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

	public void updateScores(int score)
	{
		if(isHighScore(score))
		{
			//user got a high score
			TextInputDialog dialog = new TextInputDialog("");
			dialog.setTitle("High Score!!");
			dialog.setHeaderText("Congratulations! New High Score!");
			dialog.setContentText("Enter Player Name:");

			dialog.setOnHidden(new EventHandler<DialogEvent>(){
				
				@Override
				public void handle(DialogEvent event) {
					String result = dialog.getResult();
					
					if(!result.trim().equals(""))
					{
						PacmanScore newScore = new PacmanScore();
						newScore.setScore(score);
						newScore.setPlayerName(dialog.getResult());
						
						//add new score to highScores
						highScores.add(newScore);
						updateScoreDisplay();
					}
					else
					{
						dialog.show();
					}
					
				}
				
			});

			//print pop up window
		    dialog.show(); 
		}
	}
	
	public boolean isHighScore(int score)
	{
		int index = 0;
		
		while(index < 10)
		{
			if(highScores.get(index).getScore() == 0 && highScores.get(index).getPlayerName().equals("aaa"))
			{
				//empty score slot
				break;
			}
			else if(score > highScores.get(index).getScore())
			{
				//high score has been achieved
				break;
			}
			else if(index == 9)
			{
				// score is not high enough to be in high scores
				return false;
			}

			//increment index
			index++;
		}
		
		return true;
	}
}
