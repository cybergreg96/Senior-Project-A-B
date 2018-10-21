package PacManLogic.Ghosts;
import PacManGUI.SceneInfo;
import PacManLogic.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;

import java.awt.*;

/**
 * Created by Arash on 4/8/2017.
 */
public class PinkGhost extends Ghosts implements GameObject {




    int pauseCounter = 2;


    public PinkGhost(Point position, MapOutline map, Player player, Biscuits biscuits)
    {
        super( position,  map,  player,  biscuits);

    }



// Breadth first search starts here



    public void breadthFirstSearch(int x, int y){

        int checkingAdjacent = 0;
        int totalVisited = 0;
        int[] currentReached = new int[2];
        Node<int[]>[] visitedPlaces =  new Node[700];
        for (int i= 0; i<visitedPlaces.length;i++){

            visitedPlaces[i] =  new Node<int[]>(null);
        }


        visitedPlaces[totalVisited] = new Node<int[]>(new int[]{getStepX(), getStepY()});



        totalVisited++;







        while( true ){



            //check the upon adjacent point
            if ((getMap().points[visitedPlaces[checkingAdjacent].getData()[1] - 1][visitedPlaces[checkingAdjacent].getData()[0]] == 1)||(getMap().points[visitedPlaces[checkingAdjacent].getData()[1] - 1][visitedPlaces[checkingAdjacent].getData()[0]] == 2)){
                boolean allreadyChecked = false;
                for (int k = 0; k < totalVisited; k++) {
                    if((visitedPlaces[checkingAdjacent].getData()[0]==visitedPlaces[k].getData()[0])&&(visitedPlaces[checkingAdjacent].getData()[1] - 1 == visitedPlaces[k].getData()[1])) {

                        allreadyChecked = true;
                        break;
                    }
                }
                if (!allreadyChecked) {


                    visitedPlaces[totalVisited] =  new Node<int[]>(new int[]{visitedPlaces[checkingAdjacent].getData()[0],visitedPlaces[checkingAdjacent].getData()[1]-1},visitedPlaces[checkingAdjacent]);


                    currentReached[0] = visitedPlaces[checkingAdjacent].getData()[0];
                    currentReached[1] = visitedPlaces[checkingAdjacent].getData()[1] - 1;
                    if (( currentReached[0] == x) && (currentReached[1]== y)){
                        //   visitedPlaces[totalVisited] =  new Node<int[]>(new int[]{visitedPlaces[checkingAdjacent].getData()[0],visitedPlaces[checkingAdjacent].getData()[1]-1},visitedPlaces[totalVisited - adjacantCounter]);

                        break;
                    }
                    totalVisited++;

                }
            }


            //check the right adjacent point
            if ((getMap().points[visitedPlaces[checkingAdjacent].getData()[1]][visitedPlaces[checkingAdjacent].getData()[0]+1] == 1)||(getMap().points[visitedPlaces[checkingAdjacent].getData()[1]][visitedPlaces[checkingAdjacent].getData()[0]+1] == 2)){
                boolean allreadyChecked = false;
                for (int k = 0; k < totalVisited; k++) {
                    if((visitedPlaces[checkingAdjacent].getData()[0]+1==visitedPlaces[k].getData()[0])&&(visitedPlaces[checkingAdjacent].getData()[1] == visitedPlaces[k].getData()[1])) {

                        allreadyChecked = true;
                        break;
                    }
                }
                if (!allreadyChecked) {

                    // Adding the found empty place to the array
                    visitedPlaces[totalVisited] =  new Node<int[]>(new int[]{visitedPlaces[checkingAdjacent].getData()[0]+1,visitedPlaces[checkingAdjacent].getData()[1]},visitedPlaces[checkingAdjacent]);


                    currentReached[0] = visitedPlaces[checkingAdjacent].getData()[0]+1;
                    currentReached[1] = visitedPlaces[checkingAdjacent].getData()[1];
                    if (( currentReached[0] == x) && (currentReached[1]== y)){
                        //   visitedPlaces[totalVisited] =  new Node<int[]>(new int[]{visitedPlaces[checkingAdjacent].getData()[0]+1,visitedPlaces[checkingAdjacent].getData()[1]},visitedPlaces[totalVisited - adjacantCounter]);
                        break;
                    }
                    totalVisited++;

                }
            }
            //check the bottom adjacent point
            if ((getMap().points[visitedPlaces[checkingAdjacent].getData()[1]+1][visitedPlaces[checkingAdjacent].getData()[0]] == 1)||(getMap().points[visitedPlaces[checkingAdjacent].getData()[1]+1][visitedPlaces[checkingAdjacent].getData()[0]] == 2)) {
                boolean allreadyChecked = false;
                for (int k = 0; k < totalVisited; k++) {
                    if((visitedPlaces[checkingAdjacent].getData()[0]==visitedPlaces[k].getData()[0])&&(visitedPlaces[checkingAdjacent].getData()[1]+1 == visitedPlaces[k].getData()[1])) {

                        allreadyChecked = true;
                        break;
                    }
                }
                if (!allreadyChecked) {


                    visitedPlaces[totalVisited] =  new Node<int[]>(new int[]{visitedPlaces[checkingAdjacent].getData()[0],visitedPlaces[checkingAdjacent].getData()[1]+1},visitedPlaces[checkingAdjacent]);


                    currentReached[0] = visitedPlaces[checkingAdjacent].getData()[0];
                    currentReached[1] = visitedPlaces[checkingAdjacent].getData()[1]+1;


                    if (( currentReached[0] == x) && (currentReached[1]== y)){
                        //    visitedPlaces[totalVisited] =  new Node<int[]>(new int[]{visitedPlaces[checkingAdjacent].getData()[0],visitedPlaces[checkingAdjacent].getData()[1]+1},visitedPlaces[totalVisited - adjacantCounter]);
                        break;
                    }

                    totalVisited++;
                }
            }
            //check the left adjacent point
            if ((getMap().points[visitedPlaces[checkingAdjacent].getData()[1]][visitedPlaces[checkingAdjacent].getData()[0]-1] == 1)||(getMap().points[visitedPlaces[checkingAdjacent].getData()[1]][visitedPlaces[checkingAdjacent].getData()[0]-1] == 2)){
                boolean allreadyChecked = false;
                for (int k = 0; k < totalVisited; k++) {
                    if((visitedPlaces[checkingAdjacent].getData()[0]-1==visitedPlaces[k].getData()[0])&&(visitedPlaces[checkingAdjacent].getData()[1] == visitedPlaces[k].getData()[1])) {

                        allreadyChecked = true;
                        break;
                    }
                }
                if (!allreadyChecked) {

                    visitedPlaces[totalVisited] =  new Node<int[]>(new int[]{visitedPlaces[checkingAdjacent].getData()[0]-1,visitedPlaces[checkingAdjacent].getData()[1]}, visitedPlaces[checkingAdjacent]);


                    currentReached[0] = visitedPlaces[checkingAdjacent].getData()[0]-1;
                    currentReached[1] = visitedPlaces[checkingAdjacent].getData()[1];
                    if (( currentReached[0] == x) && (currentReached[1]== y)){
                        //    visitedPlaces[totalVisited] =  new Node<int[]>(new int[]{visitedPlaces[checkingAdjacent].getData()[0]-1,visitedPlaces[checkingAdjacent].getData()[1]},visitedPlaces[totalVisited - adjacantCounter]);

                        break;
                    }

                    totalVisited++;
                }
            }

            checkingAdjacent++;

        }


        Node<int[]> myNode = visitedPlaces[totalVisited];
        int[][] myNodesArray = new int[400][2];
        int countMyNodes = 0;
        int countZeros = 0;

        while (myNode != null){
            myNodesArray[countMyNodes][0] =  myNode.getData()[0];
            myNodesArray[countMyNodes][1] =  myNode.getData()[1];
            myNode = myNode.getParent();
            countMyNodes ++;
        }

        int[][] myNodesArray2 = new int[countMyNodes][2];
        for (int j =0; j<myNodesArray.length;j++){


            if((myNodesArray[j][0]!=0)){

                myNodesArray2[j][0]= myNodesArray[j][0];
                myNodesArray2[j][1]= myNodesArray[j][1];
            }

        }
        int[][] temp = new int[1][2];
        for (int i = 0; i < myNodesArray2.length/2; i++)
        {
            temp[0] = myNodesArray2[i];
            myNodesArray2[i] = myNodesArray2[myNodesArray2.length-1 - i];
            myNodesArray2[myNodesArray2.length-1 - i] = temp[0];
        }
        myNodesArray = myNodesArray2;

        setTotalSearched(totalVisited);

        if((!isEscape())|| (isEatenWhileEscape)) {




            this.setStepX(myNodesArray[1][0]);
            this.setStepY(myNodesArray[1][1]);
            this.setX((myNodesArray[1][0]) * 20);
            this.setY((myNodesArray[1][1]) * 20);



        }else if ((isEscape())&&(!isEatenWhileEscape)){


            // Run Away
            if(pauseCounter==2){
                this.setStepX(myNodesArray[0][0]);
                this.setStepY(myNodesArray[0][1]);
                if (myNodesArray[0][0]-myNodesArray[1][0]==1){

                    if(getMap().points[getStepY()][getStepX()+1]!=0) {
                        setX(getX() + 20);
                        setStepX(getStepX()+1);
                        if ((this.getX()==540) && (this.getY()==300)){
                            this.setX(20);
                            setStepX(1);
                        }}else if(getMap().points[getStepY()+1][getStepX()]!=0){
                        setY(getY() + 20);
                        setStepY(getStepY()+1);
                    }else  {
                        setY(getY() -20);
                        setStepY(getStepY()-1);
                    }

                }else if (myNodesArray[1][0]-myNodesArray[0][0]==1){
                    if(getMap().points[getStepY()][getStepX()-1]!=0){
                        setX(getX() - 20);
                        setStepX(getStepX()-1);
                        if ((this.getX()==0) && (this.getY()==300)){
                            this.setX(520);
                            setStepX(26);
                        }} else  if(getMap().points[getStepY()-1][getStepX()]!=0){
                        setY(getY() -20);
                        setStepY(getStepY()-1);
                    }else {
                        setY(getY() + 20);
                        setStepY(getStepY()+1);
                    }
                }
                else if (myNodesArray[1][1]-myNodesArray[0][1]==1){
                    if(getMap().points[getStepY()-1][getStepX()]!=0){
                        setY(getY() -20);
                        setStepY(getStepY()-1);

                    }else   if(getMap().points[getStepY()][getStepX()+1]!=0) {

                        setX(getX() + 20);
                        setStepX(getStepX()+1);
                        if ((this.getX()==540) && (this.getY()==300)){
                            this.setX(20);
                            setStepX(1);
                        }
                    }else {
                        setX(getX() - 20);
                        setStepX(getStepX()-1);

                        if ((this.getX()==0) && (this.getY()==300)){
                            this.setX(520);
                            setStepX(26);
                        }}

                }else if (myNodesArray[0][1]-myNodesArray[1][1]==1){
                    if(getMap().points[getStepY()+1][getStepX()]!=0){
                        setY(getY() + 20);
                        setStepY(getStepY()+1);

                    }else  if(getMap().points[getStepY()][getStepX()+1]!=0) {

                        setX(getX() + 20);
                        setStepX(getStepX()+1);
                        if ((this.getX()==540) && (this.getY()==300)){
                            this.setX(20);
                            setStepX(1);
                        }
                    }else {
                        setX(getX() - 20);
                        setStepX(getStepX()-1);
                        if ((this.getX()==0) && (this.getY()==300)){
                            this.setX(520);
                            setStepX(26);
                        }
                    }
                }}
            pauseCounter--;
            if (pauseCounter ==0){
                pauseCounter =2;
            }
        }

    }


// Breadth first search ends here



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


        if((getBiscuits().getTotalEatenBiscuits() < 40)&&(!isEscape())) {
            randomMovement(randomDirection());
        }else if (!isEatenWhileEscape) {
            breadthFirstSearch(getPlayer().getStepX(), getPlayer().getStepY());

        } else if(isEatenWhileEscape){

            breadthFirstSearch(14, 15);
            if ((getStepX()==14)&&(getStepY()==15))
                isEatenWhileEscape = false;
        }
        if(isEscape()){
            if((keyCode == KeyCode.RIGHT)&&(getPlayer().getX()== getX()-20)&&(getPlayer().getY()==getY())){
                isEatenWhileEscape = true;
            }else if((keyCode == KeyCode.DOWN)&&(getPlayer().getX()==getX())&&(getPlayer().getY()==getY()-20)){
                isEatenWhileEscape = true;
            }else if((keyCode == KeyCode.LEFT)&&(getPlayer().getX()==getX()+20)&&(getPlayer().getY()==getY())){
                isEatenWhileEscape = true;
            }else if((keyCode == KeyCode.UP)&&(getPlayer().getX()==getX())&&(getPlayer().getY()==getY()+20)){
                isEatenWhileEscape = true;
            }else if((getPlayer().getX()==getX())&&(getPlayer().getY()==getY())){
                isEatenWhileEscape = true;
            }

        }


    }

    @Override
    public void draw(GraphicsContext g, SceneInfo sceneInfo) {
        if (isEatenWhileEscape){

            g.setFill( javafx.scene.paint.Color.rgb(170, 170, 170));

        }
        else if(!isEscape()){
            g.setFill( javafx.scene.paint.Color.rgb(255, 7, 102));
        }else{
            g.setFill( javafx.scene.paint.Color.rgb(0, 155, 194));
        }
        g.fillRoundRect(this.getX(), this.getY() ,20,20, 2, 2);
    }


}
