import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("tree game with excellent gui");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setBounds(0,0,1280,720);

        Panel p = new Panel();
        frame.add(p);
        p.setLayout(null);
        p.setFocusable(true);
        p.grabFocus();

        frame.setVisible(true);
        frame.setResizable(false);
    }


}
