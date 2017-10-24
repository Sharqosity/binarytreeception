import javafx.scene.layout.Pane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Panel extends JPanel {

    private static ArrayList<Node> nodes = new ArrayList<>();
    private Rectangle toolNode = new Rectangle(1155, 370, 100, 100);

    private Rectangle deleteNode = new Rectangle(1155, 570, 100, 100);
    private Rectangle mouse = new Rectangle(-999, -999, 12, 22);
    private Node currNode,snapNode,snapPreviewParent,lerpNode;

    private int defaultValue;
    private int range =  (int)(Math.random() * 250);

    private int c,z;
    private double total;

    private int score = 0;
    private int timeBonus, balanceBonus;

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
        initControls();
        Timer timer = new Timer(1000/FRAMES_PER_SECOND, e -> {
            if(!buttonName.equals("Play") && !buttonName.equals("Next Level")) {
                z++;
                if (z % FRAMES_PER_SECOND == 0) {
                    z = 0;
                    c++;
                }

            }


            if (lerpNode != null && snapPreviewParent != null) {
                if (lerpFrame < FRAMES_PER_SECOND) {
                    if (snapPreviewParent.getLeftChild() == lerpNode && !(lerpNode.getX() == snapPreviewParent.getX() - SNAP_OFFSET_X && lerpNode.getY() + SNAP_OFFSET_Y == snapPreviewParent.getY())) {
                        if (lerpNode != null && snapPreviewParent != null) {
                            animationPoint.x = (lerpNode.getX() + ((snapPreviewParent.getX() - SNAP_OFFSET_X - lerpNode.getX()) * lerpFrame / FRAMES_PER_SECOND));
                            animationPoint.y = (lerpNode.getY() + ((snapPreviewParent.getY() + SNAP_OFFSET_Y - lerpNode.getY()) * lerpFrame / FRAMES_PER_SECOND));
                        }
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

    public void initControls() {
        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (currNode != null) {
                    currNode.setX(e.getX());
                    currNode.setY(e.getY());

                    //Detect possible snap if in area, and show preview
                    for (Node n : nodes) {
                        if (n != currNode) {
                            if(n.getLeftChild() == null && new Point(e.getX(),e.getY()).distance(n.getX() - SNAP_OFFSET_X, n.getY()+SNAP_OFFSET_Y) < 50) {
//                            if (n.getLeftChild() == null && new Rectangle(n.getX() - (int)(SNAP_OFFSET_X*1.5), n.getY() + SNAP_OFFSET_Y, n.getDiameter(), n.getDiameter()).contains(e.getX(), e.getY())) {
                                if (currNode.getParent() == null && !currNode.isDescendantOf(n) && !currNode.isAncestorOf(n) && currNode.getLeftChild() != n && currNode.getRightChild() != n) {
                                    snapNode = new Node(0, n.getX() - SNAP_OFFSET_Y, n.getY() + SNAP_OFFSET_Y, null, null);
                                    snapPreviewParent = n;
                                    break;
                                }
                            } else if (n.getRightChild() == null && new Point(e.getX(),e.getY()).distance(n.getX() + SNAP_OFFSET_X, n.getY()+SNAP_OFFSET_Y) < 50) {
//                            } else if (n.getRightChild() == null && new Rectangle(n.getX() + (int)(SNAP_OFFSET_X*1.5), n.getY() + SNAP_OFFSET_Y, n.getDiameter(), n.getDiameter()).contains(e.getX(), e.getY())) {
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
            public void mouseMoved(MouseEvent e) {
            }
        });
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                mouse = new Rectangle(e.getX(), e.getY(), 12, 19);
                if(mouse.intersects(toolNode) && buttonName.equals("Play") /*nodes.size() == 0*/) {
                    range = (int)(Math.random() * 250);
                    while(range < 50) {
                        range = (int)(Math.random() * 250);
                    }
                    level();
                    currNode = nodes.get(nodes.size() - 1);
                    buttonName = "Check";
                    z = 0;
                    c = 0;
//                    x*=2;

                }
                else if(mouse.intersects(toolNode) && buttonName.equals("Check") /*nodes.size() != 0*/) {

                    System.out.println(startValidation());
                    total += c + (z/100.0);


                    //stores time value
                    if(total<10) {
                        new DecimalFormat("#.##").format(total);
                    }else if(total <100){
                        new DecimalFormat("##.##").format(total);
                    }else{
                        new DecimalFormat("###.##").format(total);
                    }
                   System.out.println(total);


                    if(startValidation()) {
//                        System.out.println(total);
                        timeBonus = (1000/(int)Math.round(total));
                        score = score + 100 + (x-2)*50 +timeBonus + balanceBonus;
                    }

                    buttonName = "Next Level";

                } else if (mouse.intersects(toolNode) && buttonName.equals("Next Level")) {
                    nodes.clear();
                    x*=2;
                    level();
//                    total += c + (z/100.0);
                    z = 0;
                    c = 0;
                }




//                if(mouse.intersects(deleteNode)){
//                    nodes.clear();
//                    x*=2;
//                    level();
//                }
                for(Node n : nodes) {
                    if(mouse.intersects(new Rectangle(n.getX()-n.getDiameter()/2, n.getY()-n.getDiameter()/2, n.getDiameter(), n.getDiameter()))) {
                        currNode = n;
                        break;
                    }
                }
            }



            @Override
            public void mouseReleased(MouseEvent e) {
                mouse = new Rectangle(e.getX(), e.getY(), 12, 19);

                //Place child on release if in correct snap area
                for(Node n :nodes) {
                    if (n != currNode) {
                        if(new Point(e.getX(), e.getY()).distance(n.getX() - SNAP_OFFSET_X, n.getY() + SNAP_OFFSET_Y) < 50) {
//                        if (mouse.intersects(new Rectangle(n.getX() - SNAP_OFFSET_X, n.getY() + SNAP_OFFSET_Y, n.getDiameter(), n.getDiameter()))) {
                            if (currNode != null && currNode.getParent() == null && !currNode.isDescendantOf(n) && !currNode.isAncestorOf(n) && n.getLeftChild() == null) {
                                n.setLeftChild(currNode);
                                currNode.setParent(n);
                                lerpNode = currNode;

                                snapNode = null;
                                break;
                            }
                        } else if (new Point(e.getX(), e.getY()).distance(n.getX() + SNAP_OFFSET_X, n.getY() + SNAP_OFFSET_Y) < 50) {

//                        } else if (mouse.intersects(new Rectangle(n.getX() + SNAP_OFFSET_X, n.getY() + SNAP_OFFSET_Y, n.getDiameter(), n.getDiameter()))) {
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

                mouse = new Rectangle(-999, -999, 12, 19);
                currNode = null;
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    System.exit(0);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
    }

    public Node gotHead() {
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


    public void levelBoss(){
        for (int i = 0; i < x; i++) {
            //nodes.add(new Node((int)Math.PI, 200, 200, null, null));

        }
    }

    public void level(){
        for (int i = 0; i < x; i++) {
            defaultValue = (int)(Math.random() * range);
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


    public boolean startValidation() {
        if(gotHead() != null) {
            System.out.println(gotHead().getValue());
            return validateTree(gotHead(), Integer.MIN_VALUE, Integer.MAX_VALUE);
        }else{
            System.out.println("null");
            return false;
        }
        //System.out.println(gotHead().getValue());

        //return validateTree(gotHead(), Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public boolean validateTree(Node n, int low, int high) {
        if(n == null) {
            return true;
        }
        return n.getValue() > low && n.getValue() < high && validateTree(n.getLeftChild(), low, n.getValue()) && validateTree(n.getRightChild(), n.getValue(), high);
    }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        //anti-aliasing
        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );
        g2.setRenderingHints(rh);



        if(snapNode!=null) {
            snapNode.drawPreviewLine(g2, snapPreviewParent);
            snapNode.displayPreview(g2);
        }
        if(lerpNode!=null) {
            lerpNode.setX(animationPoint.x);
            lerpNode.setY(animationPoint.y);
        }
        for(Node n : nodes) {
            n.drawLine(g2);
        }

        for(Node n : nodes) {
            n.display(g2);
        }

        drawToolbar(g2);

        if(buttonName.equals("Next Level") && startValidation()) {
            g2.setColor(Color.green);
            g2.drawString("Correct Tree: " + "+" + "100", 400,400);
            g2.drawString("Level Bonus: " +  "+" + (x-2)*50, 400, 450);
            g2.drawString("Time Bonus: " + "+" + timeBonus, 400, 500);
        }


    }

//    public void checkDeletion() {
//        if(currNode != null) {
//            if (new Rectangle(currNode.getX(), currNode.getY(), currNode.getDiameter(), currNode.getDiameter()).intersects(deleteNode)) {
//                nodes.remove(currNode);
//            }
//        }
//    }


    public void drawToolbar(Graphics2D g2) {

        g2.setColor(Color.RED);
        g2.fill(toolNode);
        g2.setColor(Color.BLUE);
        g2.fill(deleteNode);
        g2.setColor(Color.BLACK);

        g2.drawString(buttonName, (int)(toolNode.getX() + 20), (int)(toolNode.getY() + 55));
        g2.setColor(Color.WHITE);
        g2.drawString("Check", (int)(deleteNode.getX() + 30), (int)(deleteNode.getY() + 55));
        //g2.drawString("Delete", (int)(deleteNode.getX() + 30), (int)(deleteNode.getY() + 55));
        g2.setColor(Color.BLACK);
        Font currentFont = g2.getFont();
        Font newFont = currentFont.deriveFont(currentFont.getSize() * 2.4F);
        g2.setFont(newFont);
        g2.drawString(c + "", 55, 50);
        if(isValid == false){
            g2.drawString("Keep Trying!", getWidth()/2, getHeight()/2);
        }
        if (c < 10) {
            g2.drawString("." + z + "", 75, 50);
        } else if (c < 100) {
            g2.drawString("." + z, 95, 50);
        } else
            g2.drawString("." + z, 115, 50);
        }
        g2.drawString("Score: " + score, 50, 100);

    }


}
