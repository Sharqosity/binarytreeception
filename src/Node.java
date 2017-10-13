/**
 * Created by student on 10/13/17.
 */
public class Node {
    private int value;
    private Node leftChild, rightChild;

    public Node(int value, Node leftChild, Node rightChild) {
        this.value = value;
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


    public void add(int newValue){
        if(newValue < value) { //go down left branch
            if (leftChild == null) {
                leftChild = new Node(newValue);
            } else {
                leftChild.add(newValue);
            }
        }else{
            if(rightChild == null){
                rightChild = new Node(newValue);

            }else{
                rightChild.add(newValue);
            }


        }


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
}

