import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;
import javax.swing.*;
import java.io.File;
import java.util.ArrayList;

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
        //Sound.sound1.loop();
        playSound("Titanic on Recorder");
    }
    public static void play(String files) {
        byte[] buffer = new byte[4096];
        //for (String filePath : files) {
            File file = new File("./res/" + files + ".wav");
            try {
                AudioInputStream is = AudioSystem.getAudioInputStream(file);
                AudioFormat format = is.getFormat();
                SourceDataLine line = AudioSystem.getSourceDataLine(format);
                line.open(format);
                line.start();
                while (is.available() > 0) {
                    int len = is.read(buffer);
                    line.write(buffer, 0, len);
                }
                line.drain();
                line.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    public static void playSound(String files){
        Runnable r = new Runnable(){
            public void run(){
                play(files);
            }
        };
        Thread thread1 = new Thread(r);
        thread1.start();
    }





}
