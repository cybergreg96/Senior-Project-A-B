

import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.TimerTask;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;

public class NoKnightChessController implements Initializable {
	@FXML
	private AnchorPane rootPane;
	@FXML
	private Text titleTxt;
	@FXML
	private GridPane chessBoard;
	@FXML
	private Pane chessPane;

	@FXML
	public void goHome(ActionEvent event) throws IOException {
		Parent loginParent = FXMLLoader.load(getClass().getResource("StartScreen.fxml"));
		loginParent.setStyle("-fx-background-color: #a50000");
        Scene LoginScene = new Scene(loginParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setResizable(false);
        window.setScene(LoginScene);
	}
       
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		rootPane.setStyle("-fx-background-color: #a50000");
		chessBoard.setStyle("-fx-background-color: #FFFFFF");
		
		 
		//Initialize Array of Images and random generation for game

        ColorAdjust adjust = new ColorAdjust();
        adjust.setBrightness(-0.25);

        //sets width and height dimensions for gridpane in chessBoard
        for (int i = 0; i < 6; i++) {
            RowConstraints rowSize = new RowConstraints(90);
           
            ColumnConstraints columnSize = new ColumnConstraints(90);
           
            chessBoard.getRowConstraints().add(rowSize);
            chessBoard.getColumnConstraints().add(columnSize);
        }
        
        int cellColorSwitch = 0;
        
        for(int i = 0; i < 6; i++) {
            for(int j = 0; j < 6; j++) {

            	Pane boardCell = new Pane();
            	
            	//determines color of the board cell to be added
            	if(cellColorSwitch % 2 == 0)
            	{
                	boardCell.setStyle("-fx-background-color: #eec98a");
            	}
            	else
            	{
                	boardCell.setStyle("-fx-background-color: #754201");

            	}            	
            	
                chessBoard.add(boardCell, j, i); // node, col, row
                
                cellColorSwitch++;
            }
        }
        
        rootPane.getChildren().add(chessBoard);
          

      
		
	}
}
