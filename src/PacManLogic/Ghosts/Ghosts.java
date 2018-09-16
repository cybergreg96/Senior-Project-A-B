package PacManLogic.Ghosts;

import PacManLogic.Biscuits;
import PacManLogic.MapOutline;
import PacManLogic.Player;

import java.awt.*;
import java.util.Random;

/**
 * Created by Arash on 5/4/2017.
 */
public abstract class Ghosts {

    private int X;
    private int Y;
    private int stepX;
    private int stepY;
    private boolean isEscape = false;
    private int escapeTimeCount = 0;
    int previousBigBiscuitEaten = 0;
    boolean isEatenWhileEscape = false;
    private int currentDirection = 0;
    private int previousDirection = 0;
    private int totalSearched;
    private Player player;
    private Biscuits biscuits;
    private MapOutline map;
    public  Ghosts () {

    }

    public Ghosts(Point position, MapOutline map, Player player, Biscuits biscuits)
    {

        setX(position.x);
        setY(position.y);
        this.setMap(map);
        this.setPlayer(player);
        this.setBiscuits(biscuits);
        setStepX(getX()/20);
        setStepY(getY()/20);
    }



    public void moveDown(){
        if(getMap().points[getStepY()+1][getStepX()]!=0){
            setY(getY() + 20);
            setStepY(getStepY()+1);

        }
    }
    public void moveLeft(){

        if(getMap().points[getStepY()][getStepX()-1]!=0){
            setX(getX() - 20);
            setStepX(getStepX()-1);

            if ((this.getX()==0) && (this.getY()==300)){
                this.setX(520);
                setStepX(26);
            }
        }
    }
    public void moveRight(){
        if(getMap().points[getStepY()][getStepX()+1]!=0) {

            setX(getX() + 20);
            setStepX(getStepX()+1);

            if ((this.getX()==540) && (this.getY()==300)){
                this.setX(20);
                setStepX(1);
            }
        }

    }
    public void moveUp(){
        if(getMap().points[getStepY()-1][getStepX()]!=0){
            setY(getY() -20);
            setStepY(getStepY()-1);

        }
    }
    //Random Mode starts here
    public  void randomMovement(int direction){
        if(getCurrentDirection() == 0) {
            switch (direction) {
                case 1:

                    if (getMap().points[getStepY() + 1][getStepX()] != 0) {
                        moveDown();
                        setCurrentDirection(1);
                        break;
                    } else {
                        setCurrentDirection(0);
                        break;
                    }

                case 2:

                    if (getMap().points[getStepY()][getStepX() - 1] != 0) {
                        moveLeft();
                        setCurrentDirection(2);
                        break;
                    } else {
                        setCurrentDirection(0);
                        break;
                    }

                case 3:

                    if (getMap().points[getStepY()][getStepX() + 1] != 0) {

                        moveRight();
                        setCurrentDirection(3);
                        break;
                    } else {
                        setCurrentDirection(0);
                        break;
                    }

                case 4:

                    if (getMap().points[getStepY() - 1][getStepX()] != 0) {
                        moveUp();
                        setCurrentDirection(4);
                        break;
                    } else {
                        setCurrentDirection(0);
                        break;
                    }
            }
        } else if(getCurrentDirection() == 1){

            if((getMap().points[getStepY()][getStepX() - 1] == 0) && (getMap().points[getStepY()][getStepX() + 1] == 0)) {
                if (getMap().points[getStepY() + 1][getStepX()] != 1) {
                    moveDown();
                } else {
                    setPreviousDirection(getCurrentDirection());
                    setCurrentDirection(0);
                }
            }else{
                setPreviousDirection(getCurrentDirection());
                setCurrentDirection(0);
            }

        }  else if(getCurrentDirection() == 2){

            if((getMap().points[getStepY() + 1][getStepX()] == 0)&&(getMap().points[getStepY() - 1][getStepX()] == 0) ){
                if (getMap().points[getStepY()][getStepX() - 1] != 1) {
                    moveLeft();
                }else{
                    setPreviousDirection(getCurrentDirection());
                    setCurrentDirection(0);
                }
            }else{
                setPreviousDirection(getCurrentDirection());
                setCurrentDirection(0);
            }

        } else if(getCurrentDirection() == 3){

            if((getMap().points[getStepY() + 1][getStepX()] == 0)&&(getMap().points[getStepY() - 1][getStepX()] == 0) ){
                if (getMap().points[getStepY()][getStepX() + 1] != 1) {
                    moveRight();
                }else{
                    setPreviousDirection(getCurrentDirection());
                    setCurrentDirection(0);
                }
            }else{
                setPreviousDirection(getCurrentDirection());
                setCurrentDirection(0);
            }
        }else if(getCurrentDirection() == 4) {
            if ((getMap().points[getStepY()][getStepX() - 1] == 0) && (getMap().points[getStepY()][getStepX() + 1] == 0)) {
                if (getMap().points[getStepY() - 1][getStepX()] != 1) {
                    moveUp();
                } else {
                    setPreviousDirection(getCurrentDirection());
                    setCurrentDirection(0);
                }
            }else{
                setPreviousDirection(getCurrentDirection());
                setCurrentDirection(0);
            }
        }
    }


    public int randomDirection(){
        Random random = new Random();
        int dir = random.nextInt(3);
        if (getPreviousDirection() == 1){
            int[] myNums = {1,2,3};
            return myNums[dir];
        }else if (getPreviousDirection() == 2){
            int[] myNums = {1,2,4};
            return myNums[dir];
        }else if (getPreviousDirection() == 3){
            int[] myNums = {1,3,4};
            return myNums[dir];
        }else if (getPreviousDirection() == 4){
            int[] myNums = {2,3,4};
            return myNums[dir];
        }else  {
            dir = random.nextInt(4)+1;
            return dir;
        }

    }

//Random Mode Ends here





    public int getX() {
        return X;
    }

    public void setX(int x) {
        X = x;
    }

    public int getY() {
        return Y;
    }

    public void setY(int y) {
        Y = y;
    }


    public int getStepX() {
        return stepX;
    }

    public void setStepX(int stepX) {
        this.stepX = stepX;
    }

    public int getStepY() {
        return stepY;
    }

    public void setStepY(int stepY) {
        this.stepY = stepY;
    }

    public int getEscapeTimeCount() {
        return escapeTimeCount;
    }

    public void setEscapeTimeCount(int escapeTimeCount) {
        this.escapeTimeCount = escapeTimeCount;
    }

    public boolean isEscape() {
        return isEscape;
    }

    public void setEscape(boolean escape) {
        this.isEscape = escape;
    }

    public int getTotalSearched() {
        return totalSearched;
    }

    public void setTotalSearched(int totalSearched) {
        this.totalSearched = totalSearched;
    }

    public Biscuits getBiscuits() {
        return biscuits;
    }

    public void setBiscuits(Biscuits biscuits) {
        this.biscuits = biscuits;
    }
    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
    public MapOutline getMap() {
        return map;
    }

    public void setMap(MapOutline map) {
        this.map = map;
    }


    public int getCurrentDirection() {
        return currentDirection;
    }

    public void setCurrentDirection(int currentDirection) {
        this.currentDirection = currentDirection;
    }

    public int getPreviousDirection() {
        return previousDirection;
    }

    public void setPreviousDirection(int previousDirection) {
        this.previousDirection = previousDirection;
    }

}
