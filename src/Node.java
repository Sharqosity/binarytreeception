import java.awt.Graphics2D;
import java.awt.Color;

public class Node {
    private int value;
    private Node leftChild, rightChild;
    private int x, y;
    private int diameter = 100;

    public Node(int value, int x, int y, Node leftChild, Node rightChild) {
        this.value = value;
        this.x = x;
        this.y = y;
        this.leftChild = leftChild;
        this.rightChild = rightChild;
    }

    public Node(int value){
        this.value = value;
    }


    public String toString(){
        String ans = "";

        if(getLeftChild() != null){
            ans += leftChild.toString();
        }
        ans+= value + " ";

        if(rightChild != null){
            ans += rightChild.toString();
        }
        return ans;
    }


    public void add(Node newValue){
        if(newValue.getValue() < value) { //go down left branch
            if (leftChild == null) {
                leftChild = newValue;
            }
        }else {
            if(rightChild == null){
                rightChild = newValue;
            }
        }
    }

    public void display(Graphics2D g2) {
        if(leftChild!=null) {
            g2.setColor(Color.YELLOW);
            g2.drawLine(x + diameter / 2, y + diameter / 2, leftChild.x + diameter / 2, leftChild.y + diameter / 2);
        }
        if(rightChild!=null) {
            g2.setColor(Color.GREEN);
            g2.drawLine(x + diameter / 2, y + diameter / 2, rightChild.x + diameter / 2, rightChild.y + diameter / 2);
        }

        g2.setColor(Color.BLACK);
        g2.fillOval(x, y, diameter, diameter);
        g2.setColor(Color.WHITE);
        g2.drawString("" + value, x + 45, y+55);
    }

    public int getValue() {
        return value;
    }

    public Node getLeftChild() {
        return leftChild;
    }

    public Node getRightChild() {
        return rightChild;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setLeftChild(Node leftChild) {
        this.leftChild = leftChild;
    }

    public void setRightChild(Node rightChild) {
        this.rightChild = rightChild;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int newPos) {
        x = newPos;
    }

    public void setY(int newPos) {
        y = newPos;
    }

    public int getDiameter() {
        return diameter;
    }
}

