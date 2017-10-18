import javafx.scene.layout.Pane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class Panel extends JPanel {

    static ArrayList<Node> nodes = new ArrayList<>();
    Rectangle toolNode = new Rectangle(1155, 370, 100, 100);

    Rectangle deleteNode = new Rectangle(1155, 570, 100, 100);
    Rectangle mouse = new Rectangle(-999, -999, 12, 22);
    Node currNode;
    Node snapNode;
    Node snapPreviewParent;
    Node lerpNode;
    int defaultValue = 0;
    private int c,z;

    private int frame = 1;
    private Point animationPoint = new Point();


    final int SNAP_OFFSET_X = 100;
    final int SNAP_OFFSET_Y = 100;


    public Panel() {
        initControls();
        Timer timer = new Timer(1000/60, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                z++;
                if (z % 10 == 0) {
                    z = 0;
                    c++;
                }

                if(lerpNode!=null) {
                    if (frame < 60) {
                        if (snapPreviewParent.getLeftChild() == lerpNode && !(lerpNode.getX() == snapPreviewParent.getX() - SNAP_OFFSET_X && lerpNode.getY() + SNAP_OFFSET_Y == snapPreviewParent.getY())) {
                            if (lerpNode != null && snapPreviewParent != null) {
                                animationPoint.x = (lerpNode.getX() + ((snapPreviewParent.getX() - SNAP_OFFSET_X - lerpNode.getX()) * frame / 60));
                                animationPoint.y = (lerpNode.getY() + ((snapPreviewParent.getY() + SNAP_OFFSET_Y - lerpNode.getY()) * frame / 60));
                            }
                        } else if (snapPreviewParent.getRightChild() == lerpNode && !(lerpNode.getX() == snapPreviewParent.getX() + SNAP_OFFSET_X && lerpNode.getY() + SNAP_OFFSET_Y == snapPreviewParent.getY())) {
                            if (lerpNode != null && snapPreviewParent != null) {
                                animationPoint.x = (lerpNode.getX() + ((snapPreviewParent.getX() + SNAP_OFFSET_X - lerpNode.getX()) * frame / 60));
                                animationPoint.y = (lerpNode.getY() + ((snapPreviewParent.getY() + SNAP_OFFSET_Y - lerpNode.getY()) * frame / 60));
                            }
                        }
                        frame++;

                    } else {
                        lerpNode = null;
                    }
                } else {
                    frame = 1;
                }

                repaint();
            }
        });
        timer.start();
    }

    public void initControls() {
        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if(currNode!=null) {
                    currNode.setX(e.getX() - (currNode.getDiameter() / 2));
                    currNode.setY(e.getY() - (currNode.getDiameter() / 2));
                }

                //Detect possible snap if in area, and show preview
                for(Node n :nodes) {

                    if(n.getLeftChild() == null && new Rectangle(n.getX() - SNAP_OFFSET_X, n.getY() + SNAP_OFFSET_Y, n.getDiameter()+SNAP_OFFSET_X, n.getDiameter()+SNAP_OFFSET_Y).contains(e.getX(),e.getY())) {
                        if(currNode.hasParent() == null && currNode.getLeftChild() != n && currNode.getRightChild() != n) {
                            snapNode = new Node(0,n.getX()-SNAP_OFFSET_Y,n.getY()+SNAP_OFFSET_Y,null,null);
                            snapPreviewParent = n;
                            break;
                        }
                    } else if (n.getRightChild() == null && new Rectangle(n.getX() + SNAP_OFFSET_X, n.getY() + SNAP_OFFSET_Y, n.getDiameter()+SNAP_OFFSET_X, n.getDiameter()+SNAP_OFFSET_Y).contains(e.getX(),e.getY())) {
                        if(currNode.hasParent() == null && currNode.getLeftChild() != n && currNode.getRightChild() != n) {
                            snapNode = new Node(0,n.getX()+SNAP_OFFSET_Y,n.getY()+SNAP_OFFSET_Y,null,null);
                            snapPreviewParent = n;
                            break;
                        }
                    } else {
                        snapNode = null;
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
                if(mouse.intersects(toolNode)) {
                    nodes.add(new Node(defaultValue, e.getX() - 50, e.getY() - 50, null, null));
                    currNode = nodes.get(nodes.size() - 1);
                    defaultValue = (int)(Math.random() * 50);
                }
                for(Node n : nodes) {
                    if(mouse.intersects(new Rectangle(n.getX(), n.getY(), n.getDiameter(), n.getDiameter()))) {
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
                    if(mouse.intersects(new Rectangle(n.getX() - SNAP_OFFSET_X, n.getY() + SNAP_OFFSET_Y,n.getDiameter()+SNAP_OFFSET_X, n.getDiameter()+SNAP_OFFSET_Y))) {
                        if(currNode.hasParent() == null && n.getLeftChild() == null) {
                            n.setLeftChild(currNode);
                            currNode.setParent(n);
                            lerpNode = currNode;
//                            currNode.setX(n.getX()-SNAP_OFFSET_X);
//                            currNode.setY(n.getY()+SNAP_OFFSET_Y);
                            snapNode = null;
                        }
                    } else if (mouse.intersects(new Rectangle(n.getX() + SNAP_OFFSET_X, n.getY() + SNAP_OFFSET_Y,n.getDiameter()+SNAP_OFFSET_X, n.getDiameter()+SNAP_OFFSET_Y))) {
                        if(currNode.hasParent() == null && n.getRightChild() == null) {
                            n.setRightChild(currNode);
                            currNode.setParent(n);
                            lerpNode = currNode;
//                            currNode.setX(n.getX()+SNAP_OFFSET_X);
//                            currNode.setY(n.getY()+SNAP_OFFSET_Y);
                            snapNode = null;
                        }
                    }
                }

                mouse = new Rectangle(-999, -999, 12, 19);
                checkDeletion();
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
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
//            lerpNode.display(g2);
        }
        for(Node n : nodes) {
            n.display(g2);
        }

        drawToolbar(g2);
    }

    public void checkDeletion() {
        if(currNode != null) {
            if (deleteNode.contains(currNode.getX() + currNode.getDiameter()/2, currNode.getY() + currNode.getDiameter()/2)) {
                nodes.remove(currNode);
                currNode = null;
            }
//            if (new Rectangle(currNode.getX(), currNode.getY(), currNode.getDiameter(), currNode.getDiameter()).intersects(deleteNode)) {
//                nodes.remove(currNode);
//            }
        }
    }

    public void drawToolbar(Graphics2D g2) {
        g2.setColor(Color.RED);
        g2.fill(toolNode);
        g2.setColor(Color.BLUE);
        g2.fill(deleteNode);
        g2.setColor(Color.BLACK);
        g2.drawString("New Node", (int)(toolNode.getX() + 20), (int)(toolNode.getY() + 55));
        g2.setColor(Color.WHITE);
        g2.drawString("Delete", (int)(deleteNode.getX() + 30), (int)(deleteNode.getY() + 55));
        g2.setColor(Color.BLACK);
        Font currentFont = g2.getFont();
        Font newFont = currentFont.deriveFont(currentFont.getSize() * 2.4F);
        g2.setFont(newFont);
        g2.drawString(c + "", 55, 50);
        if (c < 10) {
            g2.drawString("." + z + "", 75, 50);
        } else if (c < 100) {
            g2.drawString("." + z, 95, 50);

        } else
            g2.drawString("." + z, 115, 50);

    }


}
