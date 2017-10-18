import java.awt.*;

public class Node {
    private int value;
    private Node leftChild, rightChild;
    private int x, y;
    private int diameter = 100;
    private Node hasParent;

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


    public void display(Graphics2D g2) {
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

    public Node hasParent() {
        return hasParent;
    }

    public void setParent(Node parent) {
        hasParent = parent;
    }
}

