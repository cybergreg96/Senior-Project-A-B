package PacManLogic;

import PacManGUI.SceneInfo;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

/**
 * Created by Arash on 4/7/2017.
 */
public class Biscuits implements GameObject{

    private int x;
    private int y;
    private MapOutline map;
    private Player player;
    private NonPlayerCharacter npc;
    private int totalBiscuits;
    private int totalBigBiscuits;
    private int eatenBiscuits[][] = new int[1000][2];
    private int eatenBigBiscuits[][] = new int[1000][2];
    private static int totalEatenBiscuits =0;
    private static int totalEatenBigBiscuits =0;
    boolean hasCountedBiscuitNums = false;
    boolean hasCountedBigBiscuitNums = false;

    public Biscuits(Player player, MapOutline map, NonPlayerCharacter npc){
        this.setX(x);
        this.setY(y);
        this.player = player;
        this.map = map;
        this.npc = npc;
    }

    public static void setTotalEatenBiscuits(int totalEatenBiscuits) {
        Biscuits.totalEatenBiscuits = totalEatenBiscuits;
    }

    public static int getTotalEatenBigBiscuits() {
        return totalEatenBigBiscuits;
    }

    public static void setTotalEatenBigBiscuits(int totalEatenBigBiscuits) {
        Biscuits.totalEatenBigBiscuits = totalEatenBigBiscuits;
    }


    @Override
    public void update(KeyCode keypressed) {


    }

    @Override
    public void draw(GraphicsContext g, SceneInfo sceneInfo) {


        for(int i =0; i< map.points.length; i++){
            for(int j=0; j<map.points[i].length;j++) {

            	// condition for big biscuit
                if((map.points[i][j]==1)&&(i%9==0)&&(j% 11==1)){
                    g.setFill( Color.rgb(250, 130, 0));
                    g.fillRoundRect((j * 20) + 5, (i * 20) + 5, 10, 10, 0, 0);
                    if (!hasCountedBigBiscuitNums) {
                        setTotalBigBiscuits(getTotalBigBiscuits() + 1);
                    }
                    // check if player is at current location
                    if((player.getStepX()==j)&&(player.getStepY()==i)) {

                        boolean repeatedMove = false;
                        // check to see if big biscuit at that position has been eaten
                        for (int m = 0; m< eatenBigBiscuits.length; m++){
                            if ((player.getStepX()== eatenBigBiscuits[m][0]) && (player.getStepY()== eatenBigBiscuits[m][1])){
                                repeatedMove = true;
                            }
                        }
                        // condition if big biscuit has not been eaten
                        if(!repeatedMove){
                        	// Remember what big biscuit has been eaten
                            eatenBigBiscuits[getTotalEatenBiscuits()][0] = player.getStepX();
                            eatenBigBiscuits[getTotalEatenBiscuits()][1] = player.getStepY();
                            // Reduce big biscuit count
                            setTotalBigBiscuits(getTotalBigBiscuits() - 1);
                            // Increment total eaten big biscuit
                            setTotalEatenBigBiscuits(getTotalEatenBigBiscuits() + 1);

                        }
                    }
                    // remove any eaten big biscuit
                    for (int k = 0; k< getTotalEatenBiscuits(); k++){
                        g.clearRect((eatenBigBiscuits[k][0] * 20) +5, (eatenBigBiscuits[k][1] * 20) + 5, 10, 10);

                    }

                }
            // set small biscuits
            if(map.points[i][j]==1) {

                        g.setFill( Color.rgb(250, 130, 0));
                        g.fillRoundRect((j * 20) + 7, (i * 20) + 7, 5, 5, 0, 0);
                        if (!hasCountedBiscuitNums) {
                            setTotalBiscuits(getTotalBiscuits() + 1);
                        }

                    if((player.getStepX()==j)&&(player.getStepY()==i)) {

                            boolean repeatedMove = false;
                            // check to see if regular biscuit has been eaten at that position
                            for (int m = 0; m< eatenBiscuits.length; m++){
                                if ((player.getStepX()== eatenBiscuits[m][0]) && (player.getStepY()== eatenBiscuits[m][1])){
                                    repeatedMove = true;
                                }
                            }
                            // condition if regular biscuit has not been eaten
                            if(!repeatedMove){
                            	// remember the regular biscuit thats been eaten
                                eatenBiscuits[getTotalEatenBiscuits()][0] = player.getStepX();
                                eatenBiscuits[getTotalEatenBiscuits()][1] = player.getStepY();
                                setTotalBiscuits(getTotalBiscuits() - 1);
                                setTotalEatenBiscuits(getTotalEatenBiscuits() + 1);
                                // Condition for if NPC gave power up for double points
                                if(this.npc.getPower() == NonPlayerCharacter.Powerup.DOUBLE_POINTS) {
                                	setTotalEatenBiscuits(getTotalEatenBiscuits() + 1);
                                }
                            }


                    }
                    // remove any eaten regular biscuits
                    for (int k = 0; k< getTotalEatenBiscuits(); k++){
                        g.clearRect((eatenBiscuits[k][0] * 20) +7, (eatenBiscuits[k][1] * 20) + 7, 10, 10);

                }

                }



            }
        }
        hasCountedBiscuitNums = true;

    }



    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public static  int getTotalEatenBiscuits() {
        return totalEatenBiscuits;
    }


    public int getTotalBiscuits() {
        return totalBiscuits;
    }

    public void setTotalBiscuits(int totalBiscuits) {
        this.totalBiscuits = totalBiscuits;
    }

    public int getTotalBigBiscuits() {
        return totalBigBiscuits;
    }

    public void setTotalBigBiscuits(int totalBigBiscuits) {
        this.totalBigBiscuits = totalBigBiscuits;
    }
}
