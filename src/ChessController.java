
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ToolBar;

import javafx.scene.layout.StackPane;

import javafx.scene.layout.Pane;

public class ChessController implements Initializable {
	@FXML
	private Pane root;
	@FXML
	private StackPane sp_mainlayout;
	@FXML
	private ToolBar birdstufzBanner;

	private ChessCustomControl cc_custom;	//control which has a board and detects mouse and keyboard events

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		root.setStyle("-fx-background-color: #a50000");
		
		// initialize the chess layout, create a CustomControl and it to the layout
		cc_custom = new ChessCustomControl();
		sp_mainlayout.getChildren().add(cc_custom);
		
	}
			
}
