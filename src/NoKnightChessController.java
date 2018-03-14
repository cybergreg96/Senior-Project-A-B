

import java.awt.Point;
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
import javafx.scene.input.MouseEvent;
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
	private GridPane chessPieces;


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

        int cellColorSwitch = 0;
        
        for(int i = 0; i < 6; i++) {
            for(int j = 0; j < 6; j++) {

            	Pane boardCell = new Pane();
            	
            	//determines color of the board cell to be added
            	if(cellColorSwitch % 2 == 0)
            	{
            		//light color 
                	boardCell.setStyle("-fx-background-color: #c2a470");
            	}
            	else
            	{
            		//dark color
                	boardCell.setStyle("-fx-background-color: #754201");
            	}            	
            	
                chessBoard.add(boardCell, j, i); // node, col, row
                
                cellColorSwitch++;
            }
            //alternate color when going to next row
            cellColorSwitch++;
        }
        
        
        Button goHome = new Button("Go home");
        Scene home = new Scene(goHome);
        home.snapshot(null);       
        goHome.setTranslateX(200);
        goHome.setTranslateY(10); 
        rootPane.getChildren().add(goHome);
        goHome.setOnAction((ActionEvent e) -> {
        	
        	
			try {
				Parent x = FXMLLoader.load(getClass().getResource("StartScreen.fxml"));
				x.setStyle("-fx-background-color: #a50000");
	            Scene y = new Scene(x);
	            Stage w = (Stage)((Node)e.getSource()).getScene().getWindow();
	            w.setResizable(false);
	            w.setScene(y);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        });
	}	
}
