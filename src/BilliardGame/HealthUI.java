package BilliardGame;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
public class HealthUI {
	
	ImageView[] healthElements = new ImageView[10];
	Group hGroup;
	Image healthImg;
	public HealthUI(double startX, double startY) 
	{
		try {
			healthImg = new Image(new FileInputStream("src/resources/heart.png"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		int counter = 0;
		int increment = 25;
		for(ImageView i: healthElements) {
			i.setImage(healthImg);
			i.setX(startX);
			i.setY(startY + counter * increment);
			counter++;
		}
		hGroup = new Group(healthElements);
	}
	public void manageHealth(double value) 
	{
		int actualHealth = (int)value * 10;
		Node[] hChildren = (Node[])hGroup.getChildren().toArray();
		for(int i = 0; i < actualHealth;i++) {
			hChildren[i].setVisible(true);
		}
		for(int j = actualHealth; j < 10; j++) {
			hChildren[j].setVisible(false);
		}
	}
	Node getNode() {
		return hGroup;
	}
	public ImageView[] getHealthElements() {
		return healthElements;
	}
}
