import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.beans.property.ObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.swing.*;

/**
 * citation: https://github.com/mitchell3/SWE443-A5
 * Created by mal97 on 3/20/2017.
 */
public class MancalaController implements Initializable{

    //Any necessary String buffer that may be needed
    private ArrayList<String> strBuffer = new ArrayList<String>();

    //UI Elements that will be inserted from the FXML files
    //Menu Screen JFX Elements
    @FXML
    private Pane paneMenu;
    @FXML
    private Text textTitleMenu;
    @FXML
    private Button btnStartMenu;
    @FXML
    private Button btnHelpMenu;
    @FXML
    private Button btnExitMenu;

    /*
        Description:    Initializes all necessary UI elements
        Purpose:        Programmatically creates the initial Mancala Menu screen.
    */
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
    	
	    
        //Verify all Menu UI elements were inserted
        assert paneMenu != null : "fx:id=\"paneMenu\" was not inserted: check FXML file 'mancala_menu.fxml'";
        assert textTitleMenu != null : "fx:id=\"textTitleMenu\" was not inserted: check FXML file 'mancala_menu.fxml'";
        assert btnStartMenu != null : "fx:id=\"btnStartMenu\" was not inserted: check FXML file 'mancala_menu.fxml'";
        assert btnHelpMenu != null : "fx:id=\"btnHelpMenu\" was not inserted: check FXML file 'mancala_menu.fxml'";
        assert btnExitMenu != null : "fx:id=\"btnExitMenu\" was not inserted: check FXML file 'mancala_menu.fxml'";

        //Clicks the Start Button
        btnStartMenu.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                public void handle(MouseEvent e) {
                    try {
                        //Close the menu screen
                        Stage menu = (Stage) paneMenu.getScene().getWindow();
                        menu.close();

                        //Open the Player screen
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("mancala_player.fxml"));
                        loader.getClass().getResource("styles.css");

                        Stage stage = new Stage();
                        stage.setTitle("Mancala Player Screen");
                        stage.initModality(Modality.APPLICATION_MODAL);
                        stage.setScene(new Scene((Pane) loader.load(), 600, 400));
                        stage.setResizable(false);
                        stage.show();
                    } catch (Exception f) {
                        System.out.println("Error moving to Player Screen.");
                        f.printStackTrace();
                    }
                }
            });

        //Clicks the Help Button
        btnHelpMenu.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            public void handle(MouseEvent e) {
                try {
                    //Create a modal window popup
                    Stage popup = new Stage();
                    popup.setTitle("Help Mancala");
                    popup.initModality(Modality.APPLICATION_MODAL);

                    //Create controls and containers to hold the controls/labels
                    HBox popupBox = new HBox(20);
                    popupBox.setAlignment(Pos.CENTER);
                    Label lblText = new Label("This feature is currently in development and is not expected for release until stable release!!");

                    //Add controls and containers to the newly made stage
                    popupBox.getChildren().addAll(lblText);
                    popup.setScene(new Scene(popupBox,500, 100));

                    //Display the help popup
                    popup.show();
                } catch (Exception f) {
                    System.out.println("Error displaying the Help screen.");
                    f.printStackTrace();
                }
            }
        });

        //Clicks the Exit Button
        btnExitMenu.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
           public void handle(MouseEvent e) {
                close();
           }
        });
    }



    // Handles closing of the application
    public void exit(ActionEvent event) {
        event.consume();
        close();

    }

    private void close() {
        //Create a modal window popup
        Stage popup = new Stage();
        popup.setTitle("Exit Mancala");
        popup.initModality(Modality.APPLICATION_MODAL);

        //Create controls and containers to hold the controls/labels
        VBox popupVbox = new VBox(20);
        popupVbox.setAlignment(Pos.CENTER);
        Label label = new Label("Would you like  exit?");

        HBox popupBox = new HBox(20);
        popupBox.setAlignment(Pos.CENTER);
        Button btnYes = new Button("Yes");
        Button btnNo = new Button("No");

        //Add controls and containers to the newly made stage
        popupBox.getChildren().addAll(btnYes,btnNo);
        popupVbox.getChildren().addAll(label, popupBox);

        popup.setScene(new Scene(popupVbox,250, 100));

        //handle accepting of closing
        btnYes.setOnAction(e->{
            popup.close();
            Stage stage = (Stage) paneMenu.getScene().getWindow();
            stage.close();
        });

        //handle denial of closing
        btnNo.setOnAction(e->{
            popup.close();
        });

        //Display the close popup
        popup.show();
    }

}
