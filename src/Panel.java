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
//    Rectangle toolNode = new Rectangle(675, 250, 100, 100);
    Rectangle toolNode = new Rectangle(1155, 370, 100, 100);

    Rectangle deleteNode = new Rectangle(1155, 570, 100, 100);
    Rectangle mouse = new Rectangle(-999, -999, 12, 22);
    Node currNode;
    int defaultValue = 0;

    public Panel() {
        initControls();
        Timer timer = new Timer(40, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
    }


}
