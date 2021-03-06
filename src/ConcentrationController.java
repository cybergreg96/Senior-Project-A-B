import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.TimerTask;

import ConcentrationGame.ConcentrationCard;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/*
 * This class references the project at this link: https://github.com/EvanTich/Memory
 * 
 * This class handles the concentration game
 */
public class ConcentrationController implements Initializable 
{
	@FXML
	private GridPane root;
	@FXML
	private Pane rootPane;
	@FXML
	private Text timerText;

	@FXML
	private Text best;

	private ConcentrationCard selectedCard;
	private ConcentrationCard aCard;
	private int selectNum = 0;
	private int numberOfMoves = 0;
	private int howManyMatches = 0;
	private java.util.Timer timer;
	private int interval = 89;
	public static final int CANVAS_WIDTH = 850;
	public static final int CANVAS_HEIGHT = 650;

	@FXML
	public void goHome(ActionEvent event) throws IOException 
	{
		Parent root = FXMLLoader.load(getClass().getResource("StartScreen.fxml"));
		root.setStyle("-fx-background-color: #a50000");
		Scene LoginScene = new Scene(root);
		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		window.setResizable(false);
		window.setScene(LoginScene);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) 
	{
		rootPane.setStyle("-fx-background-color: #a50000");
		root.setStyle("-fx-background-color: #FFFFFF");
		rootPane.getStylesheets().add("styles.css");
		setTimer();

		// Initialize Array of Images and random generation for game
		best();
		byte[] imgs = new byte[10];
		Random rnd = new Random();

		ColorAdjust adjust = new ColorAdjust();
		adjust.setBrightness(-0.25);

		for (int i = 0; i < 4; i++) 
		{
			for (int j = 0; j < 5; j++) 
			{
				int num;
				// get valid random image number
				while (imgs[num = rnd.nextInt(10)] == 2)
					;
				imgs[num]++;

				ConcentrationCard concentrationCard = new ConcentrationCard(
						String.format("file:src/birds/%s.png", num + 1));

				concentrationCard.setOnMouseClicked(x -> {
					if (selectNum == 2) 
					{
						return;
					}
					concentrationCard.flipIt();
					selectNum++;
					if (selectNum == 1) 
					{
						//first card has been chosen
						aCard = concentrationCard;
					} else if (selectNum == 2) 
					{
						//second card has been chosen
						selectedCard = concentrationCard;
						numberOfMoves++;
						if (aCard.equals(selectedCard)) 
						{
							aCard.isMatched();
							selectedCard.isMatched();
							selectNum = 0;
							howManyMatches++;
							if (allFlipped(root)) 
							{
							}
						} 
						else
						{
							//allows time for the user to see an incorrect card before it is flipped back over
							if (interval != 0) 
							{
								javax.swing.Timer time = new javax.swing.Timer(500, null);
								time.addActionListener(e -> {
									if (interval != 0) 
									{
										aCard.flipIt();
										aCard.setEffect(null);
										selectedCard.flipIt();
										selectedCard.setEffect(null);
										selectNum = 0;
										time.stop();
									}
								});
								time.start();
							}
						}
					}
				});

				concentrationCard.setOnMouseEntered(x -> {
					if (selectedCard != concentrationCard && !concentrationCard.isFlipped())
						concentrationCard.setEffect(adjust);
				});

				concentrationCard.setOnMouseExited(x -> {
					if (selectedCard != concentrationCard && !concentrationCard.isFlipped())
						concentrationCard.setEffect(null);
				});

				root.add(concentrationCard, j, i); // node, col, row
			}
		}

		Button goHome = new Button("Go home");
		Scene home = new Scene(goHome);
		home.snapshot(null);
		goHome.setTranslateX((CANVAS_WIDTH / 2) - (goHome.getWidth() / 2)  - 50);
		goHome.setTranslateY(5);
		rootPane.getChildren().add(goHome);
		
		//this sends the user back to the start screen when they click the "Go Home" button
		goHome.setOnAction((ActionEvent e) -> {
			try 
			{
				Parent x = FXMLLoader.load(getClass().getResource("StartScreen.fxml"));
				x.setStyle("-fx-background-color: #a50000");
				Scene y = new Scene(x);
				Stage w = (Stage) ((Node) e.getSource()).getScene().getWindow();
				w.setResizable(false);
				w.setScene(y);
			}
			catch (IOException e1)
			{
				e1.printStackTrace();
			}
		});

		Button reset = new Button("New Game");
		Scene newgame = new Scene(reset);
		newgame.snapshot(null);
		reset.setTranslateX((CANVAS_WIDTH / 2) - reset.getWidth() / 2 + 50);
		reset.setTranslateY(5);
		rootPane.getChildren().add(reset);
		
		//this resets the game when the user presses the "New Game" button
		reset.setOnAction((ActionEvent e) -> {
			timer.cancel();
			interval = 0;
			try 
			{
				Parent concentration = FXMLLoader.load(getClass().getResource("Concentration.fxml"));
				Scene concentrationScene = new Scene(concentration);
				Stage window = (Stage) ((Node)e.getSource()).getScene().getWindow();
				window.setScene(concentrationScene);
				window.setResizable(false);
				window.show();
			} 
			catch (Exception e1)
			{
				e1.printStackTrace();
			}

		});
	}

	//checks if all cards have been flipped and the game is over
	public boolean allFlipped(GridPane g)
	{
		for (Node x : g.getChildren())
			if (!((ConcentrationCard) x).isFlipped())
				return false;
		timer.cancel();

		int someInt = 100000;
		String inputName = "concentration_best_score.txt";
		String workingDir = System.getProperty("user.dir");
		File workingDirFile = new File(workingDir);
		File testfile = new File(workingDirFile, inputName);
		
		//saves high score
		if (!testfile.exists())
		{
			PrintWriter writer;
			try 
			{
				writer = new PrintWriter("concentration_best_score.txt", "UTF-8");
				writer.println(interval + 1);
				writer.close();
			} 
			catch (FileNotFoundException e2) 
			{
				e2.printStackTrace();
			} 
			catch (UnsupportedEncodingException e2) 
			{
				e2.printStackTrace();
			}
		} 
		else 
		{
			Scanner scanner = null;
			try
			{
				scanner = new Scanner(new File("concentration_best_score.txt"));
			} 
			catch (FileNotFoundException e1)
			{
				e1.printStackTrace();
			}
			
			int[] tall = new int[100];
			int i = 0;
			while (scanner.hasNextInt())
			{
				tall[i++] = scanner.nextInt();
			}

			String value = "";
			for (int j = 0; j < i; j++) 
			{
				value += tall[j];
			}

			someInt = Integer.parseInt(value);
		}
		
		if (someInt <= interval)
		{
			PrintWriter writer2 = null;
			try 
			{
				writer2 = new PrintWriter("concentration_best_score.txt", "UTF-8");
			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
			} 
			catch (UnsupportedEncodingException e)
			{
				e.printStackTrace();
			}
			writer2.println(interval + 1);
			writer2.close();
		}
		interval = 0;
		best();
		
		return true;
	}

	public void setTimer()
	{
		timer = new java.util.Timer();
		timer.scheduleAtFixedRate(new TimerTask() 
		{
			public void run() 
			{
				if (interval >= 0) 
				{
					timerText.setText(String.valueOf(interval));
					interval--;
				} 
				else
					timer.cancel();
				timer.purge();
				if (interval == 0)
				{
					for (Node c : root.getChildren())
					{
						((ConcentrationCard) c).reveal();
					}
				}

			}
		}, 1000, 1000);
	}

	//sets the high score to be displayed on the banner
	private void best() 
	{
		String inputName = "concentration_best_score.txt";
		String workingDir = System.getProperty("user.dir");
		File workingDirFile = new File(workingDir);
		File testfile = new File(workingDirFile, inputName);
		if (testfile.exists()) 
		{
			Scanner scanner = null;
			try 
			{
				scanner = new Scanner(new File("concentration_best_score.txt"));
			} 
			catch (FileNotFoundException e1) 
			{
				e1.printStackTrace();
			}
			
			int[] tall = new int[100];
			int i = 0;
			while (scanner.hasNextInt()) 
			{
				tall[i++] = scanner.nextInt();
			}

			String value = "";
			for (int j = 0; j < i; j++) 
			{
				value += tall[j];
			}
			
			best.setText(value);
		} 
		else 
		{
			best.setText("90");
		}
	}

}
