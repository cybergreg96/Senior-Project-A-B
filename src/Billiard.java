import java.net.URL;
import java.util.ResourceBundle;

import BilliardGame.BilliardController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ToolBar;

import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class Billiard implements Initializable
{
	@FXML
	private AnchorPane root;
	@FXML
	private ToolBar birdstufzBanner;
	@FXML
	private ToolBar birdstufzBanner1;
	private Stage stage;

	@Override

	public void initialize(URL arg0, ResourceBundle arg1) 
	{
		final Group groupRoot = new Group();
		final Scene scene = new Scene(groupRoot, 850, 650);

		Stage stage = (Stage) root.getScene().getWindow();

		final BilliardController tankGame = new BilliardController(stage);
		tankGame.start();

	}

	public void setStage(Stage stage) 
	{
		this.stage = stage;
	}

}