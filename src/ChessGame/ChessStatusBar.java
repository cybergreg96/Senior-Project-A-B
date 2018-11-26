package ChessGame;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;

/*
 * This class references the project at this link: https://github.com/GuiBon/ChessGame
 * 
 * This class formats the status bar of the chess game
 */
public class ChessStatusBar extends HBox
{
	private Button 	resetButton;
	public Label	whitePlayerAlert;
	public Label	blackPlayerAlert;
	public Label	whitePlayerTimer;
	public Label	blackPlayerTimer;
	public Label	winner;
	public Label	loser;
	private GridPane statusBarGp;
	
	//initializes all alerts and timers on the status bar
	public ChessStatusBar()
	{
		statusBarGp = new GridPane();
		whitePlayerAlert = new Label("");
		blackPlayerAlert = new Label("");
		whitePlayerTimer = new Label("");
		blackPlayerTimer = new Label("");
		winner = new Label("");
		loser = new Label("");
		
		//adds left, center, and right column to status bar
		ColumnConstraints column = new ColumnConstraints();
		column.setPercentWidth(33.33);
		statusBarGp.getColumnConstraints().add(column);
		column = new ColumnConstraints();
		column.setPercentWidth(33.33);
		statusBarGp.getColumnConstraints().add(column);
		column = new ColumnConstraints();
		column.setPercentWidth(33.33);
		statusBarGp.getColumnConstraints().add(column);
		
		statusBarGp.setPrefSize(2000, 100);
		statusBarGp.getRowConstraints().add(new RowConstraints(70/2));
		statusBarGp.getRowConstraints().add(new RowConstraints(70/2));
		statusBarGp.addRow(0, whitePlayerAlert, loser, blackPlayerAlert);
		statusBarGp.addRow(1, whitePlayerTimer, winner, blackPlayerTimer);
		
		//sets style for each label
		for (Node n: statusBarGp.getChildren()) 
		{
			GridPane.setHalignment(n, HPos.CENTER);
			GridPane.setValignment(n, VPos.CENTER);
			if (n instanceof Label) 
			{
				n.setStyle("-fx-font-size: 16pt; -fx-font-weight: bold; -fx-text-fill: white; -fx-opacity: 10.0;");
			}
		}
		
		statusBarGp.setVgap(10);
		statusBarGp.setHgap(10);
		statusBarGp.setPadding(new Insets(10, 10, 10, 10));
		statusBarGp.setStyle("-fx-background-color: #a50000; ");
		statusBarGp.setSnapToPixel(false);		
		getChildren().add(statusBarGp);
	}
	
	public void resize(double width, double height)
	{
		super.resize(width, height); 
		setWidth(width);
		setHeight(height);
	}
	
	public Button getResetButton() 
	{
		return resetButton;
	}

	public void setResetButton(Button resetButton) 
	{
		this.resetButton = resetButton;
	}	
}