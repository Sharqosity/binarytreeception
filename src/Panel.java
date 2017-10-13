import javafx.scene.layout.Pane;

import javax.swing.*;
import java.awt.*;


/**
 * Created by student on 10/13/17.
 */
public class Panel extends JPanel{

    private Graphics2D g2;

    public Panel() {


    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g2 = (Graphics2D) g;
    }


    }
