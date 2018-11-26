package MancalaGame;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * citation: https://github.com/mitchell3/SWE443-A5
 * 
 * This class initializes the window that mancala is displayed on
 */
public class MancalaPlayerController implements Initializable 
{

	String name1, name2; //stores the players' names
	int f; //tracks the current player number

	//Player Screen JFX Elements
	@FXML
	private Pane panePlayer;
	@FXML
	private Text playerNumberTitlePlayer;
	@FXML
	private Text labelPlayer;
	@FXML
	private TextField textFieldNamePlayer;
	@FXML
	private Button btnNextStartPlayer;

	//starts the game screen
	public void initialize(URL fxmlFileLocation, ResourceBundle resources)
	{
		//Verify all Menu UI elements were inserted
		assert panePlayer != null : "fx:id=\"panePlayer\" was not inserted: check FXML file 'mancala_player.fxml'";
		assert playerNumberTitlePlayer != null : "fx:id=\" playerNumberTitlePlayer\" was not inserted: check FXML file 'mancala_player.fxml'";
		assert labelPlayer != null : "fx:id=\"labelPlayer\" was not inserted: check FXML file 'mancala_player.fxml'";
		assert textFieldNamePlayer != null : "fx:id=\"textFieldNamePlayer\" was not inserted: check FXML file 'mancala_player.fxml'";
		assert btnNextStartPlayer != null : "fx:id=\"btnNextStartPlayer\" was not inserted: check FXML file 'mancala_player.fxml'";

		//First player set-up
		btnNextStartPlayer.setText("Next Player");
		playerNumberTitlePlayer.setText("Player 1");
		textFieldNamePlayer.setText("Please enter your name, player 1.");

		//Clicks the next button
		btnNextStartPlayer.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() 
		{
			public void handle(MouseEvent e) 
			{
				try
				{
					if (f==0)
					{
						//First Player
						name1 = textFieldNamePlayer.getText();
						textFieldNamePlayer.setText("Please enter your name, player 2.");
						playerNumberTitlePlayer.setText("Player 2");
						btnNextStartPlayer.setText("Start Game");
						f=1;
					}
					else if (f==1)
					{
						//Second Player
						name2 = textFieldNamePlayer.getText();

						//Move to Game Screen
						//Close the menu screen
						Stage player = (Stage) panePlayer.getScene().getWindow();
						player.close();

						//Open the Player screen
						FXMLLoader loader = new FXMLLoader(getClass().getResource("mancala_scene.fxml"));
						loader.getClass().getResource("styles.css");

						Stage stage = new Stage();
						stage.setTitle("Mancala");
						stage.initModality(Modality.APPLICATION_MODAL);
						stage.setScene(new Scene((Pane) loader.load(), 912, 400));
						stage.setResizable(false);
						stage.show();

					}
				} 
				catch(Exception f) 
				{
					f.printStackTrace();
				}
			}
		});
	}
}

