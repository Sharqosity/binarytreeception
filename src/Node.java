import java.awt.*;

public class Node {
    private int value;
    private Node leftChild, rightChild;
    private int x, y;
    private int diameter = 100;
    private Node parent;

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

    public void add(int newValue) {
        if(newValue < this.value) {
            if(leftChild == null) {
                leftChild = new Node(newValue);
            } else {
                leftChild.add(newValue);
            }
        } else if (newValue > this.value) {
            if(rightChild == null) {
                rightChild = new Node(newValue);
            } else {
                rightChild.add(newValue);
            }
        } else {
            System.out.println("Number already exists in tree");
        }
    }

    /*
    epic tree functions --------------------------------------------------------
    */
    //recursively checks if input node is above this node in the tree
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

    //recursively checks if input node is below this node in the tree
    public boolean isAncestorOf(Node check) {
        if(this == check) {
            return true;
        } else if(leftChild ==null && rightChild == null) {
            return false;
        } else {
            return leftChild != null && leftChild.isAncestorOf(check) || rightChild != null && rightChild.isAncestorOf(check);
        }
    }

    //finds max depth of tree with initial input being the head. used for checking balance
    public int maxDepth(Node node) {
        if (node == null) {
            return 0;
        }
        else {
            /* compute the depth of each subtree */
            int lDepth = maxDepth(node.getLeftChild());
            int rDepth = maxDepth(node.getRightChild());

            /* use the larger one */
            if (lDepth > rDepth)
                return (lDepth + 1);
            else
                return (rDepth + 1);
        }
    }

    /* Drawing Functions *-------------------------------------------------------
     */

    //display green hover circle
    public void displayPreview(Graphics2D g2) {
        g2.setColor(new Color(110, 255, 110));
        g2.fillOval(x-diameter/2, y-diameter/2, diameter, diameter);
        g2.setColor(Color.GREEN);
        g2.setStroke(new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0));
        g2.drawOval(x-diameter/2, y-diameter/2, diameter, diameter);
    }
    //draw green review line from hover circle to intended parent
    public void drawPreviewLine(Graphics2D g2, Node parent) {
        g2.setStroke(new BasicStroke(5));
        g2.setColor(new Color(110, 255, 110));
        g2.drawLine(parent.getX(), parent.getY(), this.x, this.y);
    }
    //draw connecting lines from this to its children
    public void drawLine(Graphics2D g2) {
        if(leftChild!=null) {
            g2.setStroke(new BasicStroke(10));
            g2.setColor(Color.BLUE);
            g2.drawLine(x, y, leftChild.x, leftChild.y);
        }
        if(rightChild!=null) {
            g2.setStroke(new BasicStroke(10));
            g2.setColor(Color.RED);
            g2.drawLine(x, y, rightChild.x, rightChild.y);
        }
    }
    //draw the circle itself
    public void display(Graphics2D g2) {
        g2.setStroke(new BasicStroke(1));
        g2.setColor(Color.BLACK);
        g2.fillOval(x-diameter/2, y-diameter/2, diameter, diameter);
        g2.setColor(Color.WHITE);
        if(value < 100) { //value is 2 digits
            g2.drawString("" + value, x - 7, y + 5);
        } else { //value is 3 digits, draw more to the left
            g2.drawString("" + value, x - 10, y + 5);
        }
    }



    /* Getters and setters */

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getDiameter() {
        return diameter;
    }



    public Node getLeftChild() {
        return leftChild;
    }

    public Node getRightChild() {
        return rightChild;
    }

    public void setLeftChild(Node leftChild) {
        this.leftChild = leftChild;
    }

    public void setRightChild(Node rightChild) {
        this.rightChild = rightChild;
    }


    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
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




}

