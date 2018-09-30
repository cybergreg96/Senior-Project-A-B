package PacManLogic.Ghosts;

import PacManGUI.SceneInfo;
import PacManLogic.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;

import java.awt.*;

/**
 * Created by Arash on 4/22/2017.
 */
public class BlueGhost extends Ghosts implements GameObject {

    public BlueGhost(Point position, MapOutline map, Player player, Biscuits biscuits)
    {

    	super( position,  map,  player,  biscuits);
    }

    @Override
    public void update(KeyCode keyCode) {

    	if (getEscapeTimeCount() > 0){
            setEscape(true);

            setEscapeTimeCount(getEscapeTimeCount() - 1);
        }else{
            setEscape(false);
        } if(getBiscuits().getTotalEatenBigBiscuits()>previousBigBiscuitEaten){
            previousBigBiscuitEaten = getBiscuits().getTotalEatenBigBiscuits();
            setEscapeTimeCount(30);
        }
        
        if(isEscape()){
            if((keyCode == KeyCode.D)&&(getPlayer().getX()== getX()-20)&&(getPlayer().getY()==getY())){
                isEatenWhileEscape = true;
            }else if((keyCode == KeyCode.S)&&(getPlayer().getX()==getX())&&(getPlayer().getY()==getY()-20)){
                isEatenWhileEscape = true;
            }else if((keyCode == KeyCode.A)&&(getPlayer().getX()==getX()+20)&&(getPlayer().getY()==getY())){
                isEatenWhileEscape = true;
            }else if((keyCode == KeyCode.W)&&(getPlayer().getX()==getX())&&(getPlayer().getY()==getY()+20)){
                isEatenWhileEscape = true;
            }else if((getPlayer().getX()==getX())&&(getPlayer().getY()==getY())){
                isEatenWhileEscape = true;
            }
           
        }
            
        switch (keyCode)
        {

            case DOWN:

                moveDown();
                break;

            case LEFT:

                moveLeft();
                break;

            case RIGHT:

                moveRight();
                break;
            case UP:
                moveUp();

                break;
        }
         
        


    }

    @Override
    public void draw(GraphicsContext g, SceneInfo sceneInfo) {
        if (isEatenWhileEscape) {

            g.setFill(javafx.scene.paint.Color.rgb(170, 170, 170));

        } else if (!isEscape()) {
            g.setFill(javafx.scene.paint.Color.rgb(0, 30, 200));
        } else {
            g.setFill(javafx.scene.paint.Color.rgb(0, 155, 194));
        }
        g.fillRoundRect(this.getX(), this.getY(), 20, 20, 2, 2);

    }
    


}