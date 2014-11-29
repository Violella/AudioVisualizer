package ch.fhnw.meco;

import ddf.minim.AudioPlayer;
import ddf.minim.Minim;

import java.io.InputStream;

/**
 * @author Josiane Manera
 */
public class MinimTester {

    public static void main(String[] args) throws InterruptedException {

        final String fileName = "Yamaha-SY-35-Clarinet-C5.wav";
        Minim minim = new Minim(new MinimTester());
        final AudioPlayer audioPlayer = minim.loadFile(fileName);
        audioPlayer.play();
        System.out.println("Sound is played async");
        while (audioPlayer.isPlaying()) {
            Thread.sleep(255);
        }
        audioPlayer.close();
    }

    public InputStream createInput(final String name) {
        return MinimTester.class.getResourceAsStream("/audio/"+name);
    }

    public String sketchPath( String fileName ) {
        return "";
    }


}
