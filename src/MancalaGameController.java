import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

import static java.lang.System.exit;

import java.io.IOException;

/**
 * Created by mal97 on 3/25/2017.
 */

public class MancalaGameController implements Initializable {

    MancalaPlayer player1, player2;
    MancalaHouse h1, h2, h3, h4, h5, h6, h7, h8, h9, h10, h11, h12;
    MancalaBoard mancalaBoard;
    MancalaHouse kalahP1, kalahP2;
    Boolean goagain;
    int turn = 1;
    Boolean clicked = false;
    //Mancala Screen JFX Elements
    @FXML
    private Pane paneMancala;
    @FXML
    private HBox hboxLeftKalahMancala;
    @FXML
    private Button btnLeftKalahMancala; //Will be disabled
    @FXML
    private HBox hboxRightKalahMancala;
    @FXML
    private Button btnRightKalahMancala; //will be disabled
    @FXML
    private Pane panePitsMancala;
    @FXML
    private Button btnPit1;
    @FXML
    private Button btnPit2;
    @FXML
    private Button btnPit3;
    @FXML
    private Button btnPit4;
    @FXML
    private Button btnPit5;
    @FXML
    private Button btnPit6;
    @FXML
    private Button btnPit7;
    @FXML
    private Button btnPit8;
    @FXML
    private Button btnPit9;
    @FXML
    private Button btnPit10;
    @FXML
    private Button btnPit11;
    @FXML
    private Button btnPit12;
    @FXML
    private Label lblTurn;
    @FXML
	private Button goHome = new Button("Go home");
    
    

    //Starts the next screen
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
    	
    	paneMancala.setStyle("-fx-background-color: #a50000");
    	
	    Scene home = new Scene(goHome);
	    home.snapshot(null);       
	    goHome.setTranslateX((850/2) - (goHome.getWidth()/2));
	    goHome.setTranslateY(4);  
	    paneMancala.getChildren().add(goHome);
	    goHome.setOnAction((ActionEvent e) -> {
	    	try {
				Parent x = FXMLLoader.load(getClass().getResource("StartScreen.fxml"));
				x.setStyle("-fx-background-color: #a50000");
	            Scene y = new Scene(x);
	            Stage w = (Stage)((Node)e.getSource()).getScene().getWindow();
	            w.setResizable(false);
	            w.setScene(y);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
	        
	    });
	    
        //Verify all Menu UI elements were inserted
        assert paneMancala != null : "fx:id=\"paneMancala\" was not inserted: check FXML file 'mancala_scene.fxml'";
        assert hboxLeftKalahMancala != null : "fx:id=\"hboxLeftKalahMancala\" was not inserted: check FXML file 'mancala_scene.fxml'";
        assert btnLeftKalahMancala != null : "fx:id=\"btnLeftKalahMancala\" was not inserted: check FXML file 'mancala_scene.fxml'";
        assert hboxRightKalahMancala != null : "fx:id=\"hboxRightKalahMancala\" was not inserted: check FXML file 'mancala_scene.fxml'";
        assert panePitsMancala != null : "fx:id=\"panePitsMancala\" was not inserted: check FXML file 'mancala_scene.fxml'";
        assert btnPit1 != null : "fx:id=\"btnPit1\" was not inserted: check FXML file 'mancala_scene.fxml'";
        assert btnPit2 != null : "fx:id=\"btnPit2\" was not inserted: check FXML file 'mancala_scene.fxml'";
        assert btnPit3 != null : "fx:id=\"btnPit3\" was not inserted: check FXML file 'mancala_scene.fxml'";
        assert btnPit4 != null : "fx:id=\"btnPit4\" was not inserted: check FXML file 'mancala_scene.fxml'";
        assert btnPit5 != null : "fx:id=\"btnPit5\" was not inserted: check FXML file 'mancala_scene.fxml'";
        assert btnPit6 != null : "fx:id=\"btnPit6\" was not inserted: check FXML file 'mancala_scene.fxml'";
        assert btnPit7 != null : "fx:id=\"btnPit7\" was not inserted: check FXML file 'mancala_scene.fxml'";
        assert btnPit8 != null : "fx:id=\"btnPit8\" was not inserted: check FXML file 'mancala_scene.fxml'";
        assert btnPit9 != null : "fx:id=\"btnPit9\" was not inserted: check FXML file 'mancala_scene.fxml'";
        assert btnPit10 != null : "fx:id=\"btnPit10\" was not inserted: check FXML file 'mancala_scene.fxml'";
        assert btnPit11 != null : "fx:id=\"btnPit11\" was not inserted: check FXML file 'mancala_scene.fxml'";
        assert btnPit12 != null : "fx:id=\"btnPit12\" was not inserted: check FXML file 'mancala_scene.fxml'";
        assert lblTurn != null : "fx:id=\"lblTurn\" was not inserted: check FXML file 'mancala_scene.fxml'";

        //Disable the Kalahs so they are not buttons.
        btnLeftKalahMancala.setDisable(true);
        btnRightKalahMancala.setDisable(true);

        //Change row colors
        setColorRows();

        //Play the Game
        playGame();
    }
    //update button texts
    public void updateBtns() {
        btnPit1.setText("h1: "+String.valueOf(h1.getNumPebbles()));
        btnPit2.setText("h2: "+String.valueOf(h2.getNumPebbles()));
        btnPit3.setText("h3: "+String.valueOf(h3.getNumPebbles()));
        btnPit4.setText("h4: "+String.valueOf(h4.getNumPebbles()));
        btnPit5.setText("h5: "+String.valueOf(h5.getNumPebbles()));
        btnPit6.setText("h6: "+String.valueOf(h6.getNumPebbles()));
        btnPit7.setText("h7: "+String.valueOf(h7.getNumPebbles()));
        btnPit8.setText("h8: "+String.valueOf(h8.getNumPebbles()));
        btnPit9.setText("h9: "+String.valueOf(h9.getNumPebbles()));
        btnPit10.setText("h10: "+String.valueOf(h10.getNumPebbles()));
        btnPit11.setText("h11: "+String.valueOf(h11.getNumPebbles()));
        btnPit12.setText("h12: "+String.valueOf(h12.getNumPebbles()));
        btnLeftKalahMancala.setText("P1: "+String.valueOf(kalahP1.getNumPebbles()));
        btnRightKalahMancala.setText("P2: "+String.valueOf(kalahP2.getNumPebbles()));
    }
    public boolean done(){
        if(player1.isActive()){
            int sum = h1.getNumPebbles()+
                    h2.getNumPebbles()+
                    h3.getNumPebbles()+
                    h4.getNumPebbles()+
                    h5.getNumPebbles()+
                    h6.getNumPebbles();
            return (sum == 0);
        }
        else{
            int sum = h7.getNumPebbles()+
                    h8.getNumPebbles()+
                    h9.getNumPebbles()+
                    h10.getNumPebbles()+
                    h11.getNumPebbles()+
                    h12.getNumPebbles();
            return (sum == 0);
        }
    }
    public void takeTurn(){
        if(done()){
            if(kalahP1.getNumPebbles() > kalahP2.getNumPebbles()){
                System.out.println("Player 1 won!");
                lblTurn.setText("Player 1 Won!");
            }
            else if(kalahP2.getNumPebbles() > kalahP1.getNumPebbles()){
                System.out.println("Player 2 won!");
                lblTurn.setText("Player 2 Won!");
            }
            else{
                System.out.println("Tie!");
                lblTurn.setText("Tie");
            }
            
        }else {
        goagain = false;
        if (turn % 2 != 0) {
            //Set the proper enablements
            lblTurn.setText("Player 1's Turn");
            player1.setActivePlayer(true);
            player2.setActivePlayer(false);

            btnPit1.setDisable(false);
            btnPit2.setDisable(false);
            btnPit3.setDisable(false);
            btnPit4.setDisable(false);
            btnPit5.setDisable(false);
            btnPit6.setDisable(false);
            btnPit7.setDisable(true);
            btnPit8.setDisable(true);
            btnPit9.setDisable(true);
            btnPit10.setDisable(true);
            btnPit11.setDisable(true);
            btnPit12.setDisable(true);
            btnLeftKalahMancala.setStyle("-fx-background-color: rgba(0, 0, 0)");
            btnLeftKalahMancala.setOpacity(1);
            btnPit1.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                public void handle(MouseEvent e) {
                    goagain = player1.selectHouse(h1);
                    updateBtns();
                    if(!goagain){
                        turn++;
                    }
                    takeTurn();
                }
            });
            btnPit2.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                public void handle(MouseEvent e) {
                    goagain = player1.selectHouse(h2);
                    updateBtns();
                    if(!goagain){
                        turn++;
                    }
                    takeTurn();
                }
            });
            btnPit3.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                public void handle(MouseEvent e) {
                    goagain = player1.selectHouse(h3);
                    updateBtns();
                    if(!goagain){
                        turn++;
                    }
                    takeTurn();
                }
            });
            btnPit4.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                public void handle(MouseEvent e) {
                    goagain = player1.selectHouse(h4);
                    updateBtns();
                    if(!goagain){
                        turn++;
                    }
                    takeTurn();
                }
            });
            btnPit5.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                public void handle(MouseEvent e) {
                    goagain = player1.selectHouse(h5);
                    updateBtns();
                    if(!goagain){
                        turn++;
                    }
                    takeTurn();
                }
            });
            btnPit6.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                public void handle(MouseEvent e) {
                    goagain = player1.selectHouse(h6);
                    updateBtns();
                    if(!goagain){
                        turn++;
                    }
                    takeTurn();
                }
            });
        }else{
            //Set the proper enablements
            lblTurn.setText("Player 2's Turn");
            player2.setActivePlayer(true);
            player1.setActivePlayer(false);

            btnPit1.setDisable(true);
            btnPit2.setDisable(true);
            btnPit3.setDisable(true);
            btnPit4.setDisable(true);
            btnPit5.setDisable(true);
            btnPit6.setDisable(true);
            btnPit7.setDisable(false);
            btnPit8.setDisable(false);
            btnPit9.setDisable(false);
            btnPit10.setDisable(false);
            btnPit11.setDisable(false);
            btnPit12.setDisable(false);
            btnLeftKalahMancala.setOpacity(.5);
            btnPit7.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                public void handle(MouseEvent e) {
                    goagain = player2.selectHouse(h7);
                    updateBtns();
                    if(!goagain){
                        turn++;
                    }
                    takeTurn();
                }
            });
            btnPit8.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                public void handle(MouseEvent e) {
                    goagain = player2.selectHouse(h8);
                    updateBtns();
                    if(!goagain){
                        turn++;
                    }
                    takeTurn();

                }
            });
            btnPit9.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                public void handle(MouseEvent e) {
                    goagain = player2.selectHouse(h9);
                    updateBtns();
                    if(!goagain){
                        turn++;
                    }
                    takeTurn();

                }
            });
            btnPit10.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                public void handle(MouseEvent e) {
                    goagain = player2.selectHouse(h10);
                    updateBtns();
                    if(!goagain){
                        turn++;
                    }
                    takeTurn();

                }
            });
            btnPit11.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                public void handle(MouseEvent e) {
                    goagain = player2.selectHouse(h11);
                    updateBtns();
                    if(!goagain){
                        turn++;
                    }
                    takeTurn();

                }
            });
            btnPit12.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                public void handle(MouseEvent e) {
                    goagain = player2.selectHouse(h12);
                    updateBtns();
                    if(!goagain){
                        turn++;
                    }
                    takeTurn();

                }
            });
        }
        }
    }

    //Plays the Mancala Game
    public void playGame() {
        boolean done = false;

        //Create the board
        mancalaBoard = MancalaBoard.createGame(6, 2, "The Game", 4);
        System.out.println("Board created!");

        //hook up objects to UI
        player1 = mancalaBoard.getPlayers().get(0);
        player2 = mancalaBoard.getPlayers().get(1);
        h1 = player1.getFirstHouse();
        MancalaHouse temp = h1.successor();
        h2 = temp;
        temp = temp.successor();
        h3 = temp;
        temp = temp.successor();
        h4 = temp;
        temp = temp.successor();
        h5 = temp;
        temp = temp.successor();
        h6 = temp;
        temp = temp.successor();
        System.out.println("Player 1's houses ready!");
        temp = temp.successor();
        h7 = temp;
        temp = temp.successor();
        h8 = temp;
        temp = temp.successor();
        h9 = temp;
        temp = temp.successor();
        h10 = temp;
        temp = temp.successor();
        h11 = temp;
        temp = temp.successor();
        h12 = temp;
        System.out.println("Player 1's houses ready!");
        kalahP1 = player1.getStore();
        kalahP2 = player2.getStore();

        //set house labels
        btnPit1.setText("h1: "+String.valueOf(mancalaBoard.getPebblesPerHouse()));
        btnPit2.setText("h2: "+String.valueOf(mancalaBoard.getPebblesPerHouse()));
        btnPit3.setText("h3: "+String.valueOf(mancalaBoard.getPebblesPerHouse()));
        btnPit4.setText("h4: "+String.valueOf(mancalaBoard.getPebblesPerHouse()));
        btnPit5.setText("h5: "+String.valueOf(mancalaBoard.getPebblesPerHouse()));
        btnPit6.setText("h6: "+String.valueOf(mancalaBoard.getPebblesPerHouse()));
        btnPit7.setText("h7: "+String.valueOf(mancalaBoard.getPebblesPerHouse()));
        btnPit8.setText("h8: "+String.valueOf(mancalaBoard.getPebblesPerHouse()));
        btnPit9.setText("h9: "+String.valueOf(mancalaBoard.getPebblesPerHouse()));
        btnPit10.setText("h10: "+String.valueOf(mancalaBoard.getPebblesPerHouse()));
        btnPit11.setText("h11: "+String.valueOf(mancalaBoard.getPebblesPerHouse()));
        btnPit12.setText("h12: "+String.valueOf(mancalaBoard.getPebblesPerHouse()));
        System.out.println("About to enter loop!");
        takeTurn();
    }

    public void setColorRows() {
        //Graphically show row ownership
        //First row is Player 1's
        btnPit1.setStyle("-fx-background-color: rgba(230,199,111);" +
                "    -fx-background-radius: 10, 5;" +
                "    -fx-background-insets: 0, 1;");
        btnPit2.setStyle("-fx-background-color: rgba(230,199,111);" +
                "    -fx-background-radius: 10, 5;" +
                "    -fx-background-insets: 0, 1;");
        btnPit3.setStyle("-fx-background-color: rgba(230,199,111);" +
                "    -fx-background-radius: 10, 5;" +
                "    -fx-background-insets: 0, 1;");
        btnPit4.setStyle("-fx-background-color: rgba(230,199,111);" +
                "    -fx-background-radius: 10, 5;" +
                "    -fx-background-insets: 0, 1;");
        btnPit5.setStyle("-fx-background-color: rgba(230,199,111);" +
                "    -fx-background-radius: 10, 5;" +
                "    -fx-background-insets: 0, 1;");
        btnPit6.setStyle("-fx-background-color: rgba(230,199,111);" +
                "    -fx-background-radius: 10, 5;" +
                "    -fx-background-insets: 0, 1;");

        //Second row is Player 2's
        btnPit7.setStyle("-fx-background-color: rgba(123,176,215);" +
                "    -fx-background-radius: 10, 5;" +
                "    -fx-background-insets: 0, 1;");
        btnPit8.setStyle("-fx-background-color: rgba(123,176,215);" +
                "    -fx-background-radius: 10, 5;" +
                "    -fx-background-insets: 0, 1;");
        btnPit9.setStyle("-fx-background-color: rgba(123,176,215);" +
                "    -fx-background-radius: 10, 5;" +
                "    -fx-background-insets: 0, 1;");
        btnPit10.setStyle("-fx-background-color: rgba(123,176,215);" +
                "    -fx-background-radius: 10, 5;" +
                "    -fx-background-insets: 0, 1;");
        btnPit11.setStyle("-fx-background-color: rgba(123,176,215);" +
                "    -fx-background-radius: 10, 5;" +
                "    -fx-background-insets: 0, 1;");
        btnPit12.setStyle("-fx-background-color: rgba(123,176,215);" +
                "    -fx-background-radius: 10, 5;" +
                "    -fx-background-insets: 0, 1;");
    }

}