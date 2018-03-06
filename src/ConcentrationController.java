
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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

	private Card selectedCard;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	
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

                Card card = new Card( String.format("file:src/birds/%s.png", num) );

                card.setOnMouseClicked(x -> {
                    if(card.isFlipped())
                        return;
                    
                    if(selectedCard != null && !selectedCard.isFlipped()) {
                        if (selectedCard != card && selectedCard.equals(card)) {
                            selectedCard.flip();
                            card.flip();
                        } else {
                        	
                            selectedCard.hide();
                            selectedCard.setEffect(null);
                            selectedCard = null;
                           
                            /* TODO: alert player too */
                        }
                        
                    } else {
                    	card.show();
                        selectedCard = card;
                        selectedCard.show();
                    }

                    if (allFlipped(root))
                        alertWin(); // win
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

        // TODO: now flip a few

    
		
	}
	
	public boolean allFlipped(GridPane g) {
        for(Node x : g.getChildren())
            if(!((Card)x).isFlipped())
                return false;
        return true;
    }

    public void alertWin()  {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("You Win!");
        alert.setHeaderText("You Win!");
        alert.setContentText("Congratulations!");

        alert.showAndWait();

     
    }
	
}
