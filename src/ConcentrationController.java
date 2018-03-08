/*
 * citations: https://github.com/ThriftyNick/maze_generator
 */
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

import java.util.TimerTask;



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

public class ConcentrationController implements Initializable {
	@FXML
	private GridPane root;
	@FXML
	private Pane rootPane;
	@FXML
	private Text timerText;
	
	private Card selectedCard;
	private Card aCard;
	 private int selectNum = 0;
	 private int numberOfMoves = 0;
	 private int howManyMatches = 0;
	 private java.util.Timer timer;
	 private int interval = 10;



	
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
		root.setStyle("-fx-background-color: #FFFFFF");
		setTimer();
		
		 
		//Initialize Array of Images and random generation for game

        byte[] imgs = new byte[10];
        Random rnd = new Random();

        ColorAdjust adjust = new ColorAdjust();
        adjust.setBrightness(-0.25);

        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 5; j++) {
                int num;
                // get valid random image number
                while(imgs[num = rnd.nextInt(10)] == 2);
                imgs[num]++;

                Card card = new Card( String.format("file:src/birds/%s.png", num + 1) );

                card.setOnMouseClicked(x -> {
                	
                	 if (selectNum == 2) {
                         return;
                     }
                	 card.flipIt();
                	 selectNum++;
                	 if (selectNum == 1) {
                         aCard = card;
                     } else if (selectNum == 2) {
                         selectedCard = card;
                         numberOfMoves++;
                         if (aCard.equals(selectedCard)) {
                             aCard.isMatched();
                             selectedCard.isMatched();
                             selectNum = 0;
                             howManyMatches++;
                             if (allFlipped(root)) {
                                 
                             }
                         } else {
                        	 javax.swing.Timer time = new javax.swing.Timer(500, null);
                             time.addActionListener(e -> {
                                 //for cards to be compared, flip all cards
                                 aCard.flipIt();
                                 aCard.setEffect(null);
                                 selectedCard.flipIt();
                                 selectedCard.setEffect(null);
                                 selectNum = 0;
                                 time.stop();
                             });
                             time.start();
                         }
                     }
                });


                	
                	
                	
                	
                	
                	
                	
                	
                	

                card.setOnMouseEntered(x -> {
                    if(selectedCard != card && !card.isFlipped())
                        card.setEffect(adjust);
                });

                card.setOnMouseExited(x -> {
                    if(selectedCard != card && !card.isFlipped())
                        card.setEffect(null);
                });

                root.add(card, j, i); // node, col, row
                
            }
        }

        Button goHome = new Button("Go home");
        Scene home = new Scene(goHome);
        home.snapshot(null);       
        goHome.setTranslateX(382);
        goHome.setTranslateY(550); 
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
	
	public boolean allFlipped(GridPane g) {
        for(Node x : g.getChildren())
            if(!((Card)x).isFlipped())
                return false;
        return true;
    }
	
	public void setTimer() {
	    timer = new java.util.Timer();
	    timer.scheduleAtFixedRate(new TimerTask() {
	        public void run() {
	            if(interval >= 0)
	            {
	              timerText.setText(String.valueOf(interval));
	                interval--;
	            }
	            else

	                timer.cancel();
	            if(interval == 0) {
	            	for(Node c: root.getChildren()) {
	                  	 ((Card) c).reveal();
	                   }
	            }
                
	           
	        }
	    }, 1000,1000);
	}

	
}
