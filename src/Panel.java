import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Panel extends JPanel {

    private static ArrayList<Node> nodes = new ArrayList<>();
    private Rectangle toolNode = new Rectangle(1155, 370, 100, 100);

    private Node currNode,snapNode,snapPreviewParent,lerpNode;

    private int range =  (int)(Math.random() * 250);

    private int c,z;
    private double total;
    private Timer timer;

    private int score = 0;
    private int levelBonus, timeBonus, balanceBonus;
    private int lives = 2;

    //snap animation stuff
    private int lerpFrame = 1;
    private Point animationPoint = new Point();


    private final int FRAMES_PER_SECOND = 60;
    private final int SNAP_OFFSET_X = 100;
    private final int SNAP_OFFSET_Y = 100;

    private int x = 3;
    private String buttonName = "Play";

    private boolean isValid = true;


    public Panel() {
        //adds all mouse events!
        initControls();

        timer = new Timer(1000 / FRAMES_PER_SECOND, e -> {
            //steps timer
            if (buttonName.equals("Check")) {
                z++;
                if (z % FRAMES_PER_SECOND == 0) {
                    z = 0;
                    c++;
                }
            }

            //animation calculation
            if (lerpNode != null && snapPreviewParent != null) {
                if (lerpFrame < FRAMES_PER_SECOND) { //left
                    if (snapPreviewParent.getLeftChild() == lerpNode && !(lerpNode.getX() == snapPreviewParent.getX() - SNAP_OFFSET_X && lerpNode.getY() + SNAP_OFFSET_Y == snapPreviewParent.getY())) {
                        if (lerpNode != null && snapPreviewParent != null) {
                            animationPoint.x = (lerpNode.getX() + ((snapPreviewParent.getX() - SNAP_OFFSET_X - lerpNode.getX()) * lerpFrame / FRAMES_PER_SECOND));
                            animationPoint.y = (lerpNode.getY() + ((snapPreviewParent.getY() + SNAP_OFFSET_Y - lerpNode.getY()) * lerpFrame / FRAMES_PER_SECOND));
                        } //right
                    } else if (snapPreviewParent.getRightChild() == lerpNode && !(lerpNode.getX() == snapPreviewParent.getX() + SNAP_OFFSET_X && lerpNode.getY() + SNAP_OFFSET_Y == snapPreviewParent.getY())) {
                        if (lerpNode != null && snapPreviewParent != null) {
                            animationPoint.x = (lerpNode.getX() + ((snapPreviewParent.getX() + SNAP_OFFSET_X - lerpNode.getX()) * lerpFrame / FRAMES_PER_SECOND));
                            animationPoint.y = (lerpNode.getY() + ((snapPreviewParent.getY() + SNAP_OFFSET_Y - lerpNode.getY()) * lerpFrame / FRAMES_PER_SECOND));
                        }
                    }
                    lerpFrame++;

                } else {
                    lerpNode = null;
                }
            } else {
                lerpFrame = 1;
            }

            repaint();
        });
        timer.start();
    }

    private void initControls() {
        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {

                if (currNode != null) {
                    //update position of current node to mouse, if it exists
                    currNode.setX(e.getX());
                    currNode.setY(e.getY());

                    //Detect possible snap if in area, and show preview
                    for (Node n : nodes) {
                        if (n != currNode) {
                            if(n.getLeftChild() == null && new Point(e.getX(),e.getY()).distance(n.getX() - SNAP_OFFSET_X, n.getY()+SNAP_OFFSET_Y) < 60) {
                                if (currNode.getParent() == null && !currNode.isDescendantOf(n) && !currNode.isAncestorOf(n) && currNode.getLeftChild() != n && currNode.getRightChild() != n) {
                                    snapNode = new Node(0, n.getX() - SNAP_OFFSET_Y, n.getY() + SNAP_OFFSET_Y, null, null);
                                    snapPreviewParent = n;
                                    break;
                                }
                            } else if (n.getRightChild() == null && new Point(e.getX(),e.getY()).distance(n.getX() + SNAP_OFFSET_X, n.getY()+SNAP_OFFSET_Y) < 60) {
                                if (currNode.getParent() == null && !currNode.isDescendantOf(n) && !currNode.isAncestorOf(n) && currNode.getLeftChild() != n && currNode.getRightChild() != n) {
                                    snapNode = new Node(0, n.getX() + SNAP_OFFSET_Y, n.getY() + SNAP_OFFSET_Y, null, null);
                                    snapPreviewParent = n;
                                    break;
                                }
                            } else {
                                snapNode = null;
                            }
                        }
                    }
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {}
        });
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {}

            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    //Right click: finds clicked node and disconnects from its parent
                    for (Node n : nodes) {
                        if (new Rectangle(n.getX() - n.getDiameter() / 2, n.getY() - n.getDiameter() / 2, n.getDiameter(), n.getDiameter()).contains(e.getX(),e.getY())) {
                            currNode = n;
                            break;
                        }
                    }
                    if (currNode!=null && currNode.getParent() != null) {
                        if (currNode.getParent().getLeftChild() == currNode) {
                            currNode.getParent().setLeftChild(null);
                        } else {
                            currNode.getParent().setRightChild(null);
                        }
                        currNode.setParent(null);

                    }

                } else { //--------------------LEFT CLICK---------------------

                    if (buttonName.equals("Play")) {
                        range = (int) (Math.random() * 250);
                        while (range < 50) {
                            range = (int) (Math.random() * 250);
                        }
                        level();
                        currNode = nodes.get(nodes.size() - 1);
                        buttonName = "Check";
                        z = 0;
                        c = 0;

                    } else if (toolNode.contains(e.getX(),e.getY()) && buttonName.equals("Check")) {
                        total += c + (z / 100.0);
                        if (total < 10) {
                            new DecimalFormat("#.##").format(total);
                        } else if (total < 100) {
                            new DecimalFormat("##.##").format(total);
                        } else {
                            new DecimalFormat("###.##").format(total);
                        }

                        if (startValidation() == 1) {
                            levelBonus = x * 10;
                            timeBonus = (int)(10000*(1/(x + ((total*5)/3))));

                            if (gotHead().maxDepth(gotHead()) > ((int) (Math.log(x) / Math.log(2))) + 1) {
                                balanceBonus = 0;

                            } else if (gotHead().maxDepth(gotHead()) == 0) {
                                balanceBonus = 0;
                            } else {
                                balanceBonus = 500;
                            }
                            score = score + 100 + levelBonus + timeBonus + balanceBonus;

                        buttonName = "Next Level";

                        } else if(startValidation() == 0){ //incomplete tree
                            isValid = false;

                        } else { //the user failed
                            lives--;
                            isValid = false;
                        }

                    } else if (toolNode.contains(e.getX(),e.getY()) && buttonName.equals("Next Level")) {
                        nodes.clear();
                        x += 2;
                        level();
                        z = 0;
                        c = 0;
                        buttonName = "Check";
                    }

                    //-----------------------Finds and sets the current node if a node is clicked
                    for (Node n : nodes) {
                        if (new Rectangle(n.getX() - n.getDiameter() / 2, n.getY() - n.getDiameter() / 2, n.getDiameter(), n.getDiameter()).contains(e.getX(),e.getY())) {
                            currNode = n;
                            break;
                        }
                    }
                }
            }


            @Override
            public void mouseReleased(MouseEvent e) {
                if (!SwingUtilities.isRightMouseButton(e)) {
                    //Place child on release if in correct snap area
                    for (Node n : nodes) {
                        if (n != currNode) {
                            //left
                            if (new Point(e.getX(), e.getY()).distance(n.getX() - SNAP_OFFSET_X, n.getY() + SNAP_OFFSET_Y) < 60) {
                                if (currNode != null && currNode.getParent() == null && !currNode.isDescendantOf(n) && !currNode.isAncestorOf(n) && n.getLeftChild() == null) {
                                    n.setLeftChild(currNode);
                                    currNode.setParent(n);
                                    lerpNode = currNode;

                                    snapNode = null;
                                    break;
                                }
                                //right
                            } else if (new Point(e.getX(), e.getY()).distance(n.getX() + SNAP_OFFSET_X, n.getY() + SNAP_OFFSET_Y) < 60) {
                                if (currNode != null && currNode.getParent() == null && !currNode.isDescendantOf(n) && !currNode.isAncestorOf(n) && n.getRightChild() == null) {
                                    n.setRightChild(currNode);
                                    currNode.setParent(n);
                                    lerpNode = currNode;

                                    snapNode = null;
                                    break;
                                }
                            }
                        }
                    }
                    currNode = null;
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {}

            @Override
            public void mouseExited(MouseEvent e) {}
        });

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    System.exit(0);
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {}
        });
    }

    //returns head node. if null, there is more than 1 head
    private Node gotHead() {
        ArrayList<Node> heads = new ArrayList<>();

        for (int i = 0; i < nodes.size(); i++) {
            if(nodes.get(i).getParent() == null){
                heads.add(nodes.get(i));
            }
        }
        if(heads.size() == 1){
           return heads.get(0);
        } else{
            return null;
        }
    }

    //final level?
    public void levelBoss(){
//        for (int i = 0; i < x; i++) {
//            nodes.add(new Node((int)Math.PI, 200, 200, null, null));
//
//        }
    }

    //sets values for nodes
    private void level(){
        for (int i = 0; i < x; i++) {
            int defaultValue = (int) (Math.random() * range);
            for(int j = 0; j < nodes.size(); j++) {
                if(defaultValue == nodes.get(j).getValue()) {
                    defaultValue = (int)(Math.random() * 100);
                    j = 0;
                }
            }
            nodes.add(new Node(defaultValue, (int)(Math.random()*(getWidth()-200)), (int)(Math.random()*(getHeight()-100)), null, null));
            currNode = null;
        }
    }

    //-1: Incorrect Tree
    //0: Unfinished Tree
    //1: Correct Tree
    private int startValidation() {
        if (gotHead() != null) {
            if (validateTree(gotHead(), Integer.MIN_VALUE, Integer.MAX_VALUE)) {
                return 1;
            } else {
                return -1;
            }
        } else {
            return 0;
        }
    }

    //checks if tree is correct
    private boolean validateTree(Node n, int low, int high) {
        if(n == null) {
            return true;
        }
        return n.getValue() > low && n.getValue() < high && validateTree(n.getLeftChild(), low, n.getValue()) && validateTree(n.getRightChild(), n.getValue(), high);
    }

    /*                                                                           *
     * * * * * * * * * * * * * * * * Draw function * * * * * * * * * * * * * * * *
     *                                                                          */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        //anti-aliasing
        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );
        g2.setRenderingHints(rh);

        //draw hover preview
        if(snapNode!=null) {
            snapNode.drawPreviewLine(g2, snapPreviewParent);
            snapNode.displayPreview(g2);
        }

        //set points for animating node. this shouldnt even be in the draw function
        if(lerpNode!=null) {
            lerpNode.setX(animationPoint.x);
            lerpNode.setY(animationPoint.y);
        }

        //draw all connecting lines
        for(Node n : nodes) {
            n.drawLine(g2);
        }

        //draw all normal nodes
        for(Node n : nodes) {
            n.display(g2);
        }
        //draw toolbar, time, score
        drawToolbar(g2);

        //draw scoring if in scoring phase
        if(buttonName.equals("Next Level") && startValidation()==1) {
            g2.setColor(Color.green);
            g2.drawString("Correct Tree: " + "+" + "100", 400,400);
            g2.drawString("Level Bonus: " +  "+" + (x-2)*50, 400, 450);
            g2.drawString("Time Bonus: " + "+" + timeBonus, 400, 500);
            if(balanceBonus > 0) {
                g2.drawString("Correctly Balanced: " + "+" + balanceBonus, 400, 550);
            }
        }

        if(lives < 1) {
            g2.setColor(Color.red);
            g2.drawString("You ran out of lives! Your final score is: " + score, 300, 360);
            timer.stop();
        }
    }

    private void drawToolbar(Graphics2D g2) {

        //"menu"
        if (buttonName.equals("Play")) {
            g2.setFont(new Font("Roboto", Font.BOLD, 50));
            g2.setColor(Color.GREEN);
            g2.drawString("Binary Tree Building Game", 640-336, 100);
            g2.setColor(Color.CYAN);
            g2.setFont(new Font("Roboto", Font.BOLD, 100));
            g2.drawString("PLAY", 640-125, 360);
            g2.setColor(Color.black);
            g2.setFont(new Font("Roboto", Font.PLAIN, 36));
            g2.drawString("Drag nodes to make a tree. Right click on a node to disconnect it",80,650);

        } else { //check/next level button
            g2.setColor(Color.RED);
            g2.fill(toolNode);

            g2.setColor(Color.BLACK);
            g2.drawString(buttonName, (int) (toolNode.getX() + 20), (int) (toolNode.getY() + 55));

            Font currentFont = g2.getFont();
            Font newFont = currentFont.deriveFont(currentFont.getSize() * 2.4F);
            g2.setFont(newFont);

            g2.drawString(c + "", 55, 50);
            if (!isValid) {
                g2.drawString("Keep Trying!", 1075, 330);
            }

            //----TIMER----//
            if (c < 10) {
                g2.drawString("." + z + "", 75, 50);
            } else if (c < 100) {
                g2.drawString("." + z, 95, 50);
            } else {
                g2.drawString("." + z, 115, 50);
            }
            //----SCORE----//
            g2.drawString("Score: " + score, 50, 100);
            //----LIVES----//
            g2.drawString("Lives: " + lives, 50, 150);

        }
    }
}



