package PacManLogic;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arash on 4/9/2017.
 */
public class Node<T> {

    private List<Node<T>> children = new ArrayList<Node<T>>();
    private Node<T> parent = null;
    private T data = null;
    private int hValue = 0;
    private int price;
    private int score;
    public Node(T data){

        this.data = data;

        //The function bellow is only usable in A* Algorithm
        this.setPrice(0);

    }
    public Node(T data, Node<T> parent){

        this.data = data;
        this.parent = parent;



    }

    // Using the bellow Constructor for the A* algorithm


    public Node(T data, int destinationX , int destinationY, int myX, int myY, Node<T> parent){

        this.data = data;
        this.parent = parent;
        this.sethValue(Math.abs(destinationY - myY) + Math.abs(destinationX - myX));
        this.setPrice(parent.getPrice()+1);
        this.setScore(this.getPrice() + this.hValue);

    }

    

    public List<Node<T>> getChildren(){

        return  children;
    }

    public void setParent(Node<T> parent){
        parent.addChild(this);
        this.parent = parent;
    }
    public Node<T> getParent(){
        return this.parent;
    }

    public void addChild(T data){

        Node<T> child = new Node<T>(data);
        child.setParent(this);
        this.children.add(child);
    }

    public void addChild(Node<T> child) {

        child.setParent(this);
        this.children.add(child);
        this.parent = parent;
    }

    public T getData(){
        return this.data;
    }
    public void setData(T data ,Node<T> parent){
        this.data = data;

    }

    public boolean isRoot(){

        return (this.parent == null);
    }

    public boolean isLeaf(){
        if (this.children.size() == 0){
            return true;
        }
        else {
            return false;
        }
    }
    public void removeParent(){

        this.parent = null;
    }


    public int gethValue() {
        return hValue;
    }

    public void sethValue(int hValue) {
        this.hValue = hValue;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
