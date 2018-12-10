package BilliardGame;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
public class HealthUI {
	
	ImageView[] healthElements = new ImageView[10];
	Group hGroup = new Group();
	Image healthImg;
	int actualHealth = 10;
	public HealthUI(double startX, double startY) 
	{
		try {
			healthImg = new Image(new FileInputStream("src/resources/heart.png"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		initImageViews(startX, startY);
		System.out.println(healthElements[0].getImage());
		hGroup = new Group(healthElements);
	}
	void initImageViews(double x, double y) {
		int counter = 0;
		int increment = 25;
		for(int i = 0; i < healthElements.length;i++) {
			healthElements[i] = new ImageView(healthImg);
			healthElements[i].setX(x);
			healthElements[i].setY(y + counter * increment);
			counter++;
		}
	}
	public void manageHealth(double value) 
	{
		actualHealth = (int)value * 10;
		ObservableList<Node> hChildren = hGroup.getChildren();
		//System.out.println("Health left: " + actualHealth);
		for(int i = 0; i < actualHealth;i++) {
			hChildren.get(i).setVisible(true);
		}
		for(int j = actualHealth; j < 10; j++) {
			hChildren.get(j).setVisible(false);
		}
	}
	Node getNode() {
		return hGroup;
	}
	public ImageView[] getHealthElements() {
		return healthElements;
	}
	public int getActualHealth() {
		return actualHealth;
	}
}
