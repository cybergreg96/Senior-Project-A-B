package ;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;

import javafx.scene.layout.GridPane;

import javafx.scene.layout.Pane;

public class NoKnightChessController implements Initializable {
	@FXML
	private Pane rootPane;
	@FXML
	private GridPane chessBoard;
	@FXML
	private Text titleTxt;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
        int cellColorSwitch = 0;
        
        for(int i = 0; i < 5; i++) {
            for(int j = 0; j < 5; j++) {

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
