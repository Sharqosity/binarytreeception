import java.awt.*;

/**
 * Created by student on 10/13/17.
 */
public class Node {
    private int value;
    private Node leftChild, rightChild;
    //Point position;
    private int x, y;
    private int diameter = 100;
    private Node parent;

    public Node(int value, int x, int y, Node leftChild, Node rightChild) {

        this.value = value;
        this.x = x;
        this.y = y;
        this.leftChild = leftChild;
        this.rightChild = rightChild;
        //this.position = position;

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


    //checks if input node is above this node in the tree
    public boolean isDescendantOf(Node check) {
        if(this == check) {
            return true;
        }
        if (parent == null) {
            return false;
        } else {
            return parent == check || parent.isDescendantOf(check);
        }
    }

    public boolean isAncestorOf(Node check) {

        if(this == check) {
            return true;
        } else if(leftChild ==null && rightChild == null) {
            return false;
        } else {
            return leftChild != null && leftChild.isAncestorOf(check) || rightChild != null && rightChild.isAncestorOf(check);
        }

    }

    public void drawPreviewLine(Graphics2D g2, Node parent) {
        g2.setStroke(new BasicStroke(5));
        g2.setColor(new Color(110, 255, 110));
        g2.drawLine(parent.getX() + diameter / 2, parent.getY() + diameter / 2, this.x + diameter / 2, this.y + diameter / 2);

    }

    public void displayPreview(Graphics2D g2) {
        g2.setColor(new Color(110, 255, 110));
        g2.fillOval(x, y, diameter, diameter);
        g2.setColor(Color.GREEN);
        g2.setStroke(new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0));
        g2.drawOval(x, y, diameter, diameter);

    }

    public void drawLine(Graphics2D g2) {
        if(leftChild!=null) {
            g2.setStroke(new BasicStroke(10));
            g2.setColor(Color.RED);
            g2.drawLine(x + diameter / 2, y + diameter / 2, leftChild.x + diameter / 2, leftChild.y + diameter / 2);
        }
        if(rightChild!=null) {
            g2.setStroke(new BasicStroke(10));
            g2.setColor(Color.RED);
            g2.drawLine(x + diameter / 2, y + diameter / 2, rightChild.x + diameter / 2, rightChild.y + diameter / 2);
        }
    }

    public void display(Graphics2D g2) {

        g2.setStroke(new BasicStroke(1));

        g2.setColor(Color.BLACK);
        g2.fillOval(x, y, diameter, diameter);
        g2.setColor(Color.WHITE);
        g2.drawString("" + value, x + 45, y + 55);
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

//    public int getPosition(){return position;}
//
//    public void setPosition(int newPosition){this.position = newPosition;}


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

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }
}

