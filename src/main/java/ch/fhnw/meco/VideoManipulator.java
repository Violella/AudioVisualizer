package ch.fhnw.meco;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Manuel Wehrli on 09.01.2015.
 */
public class VideoManipulator {
    private BufferedImage frame;
    private double[] frequencyTable;

    public VideoManipulator(BufferedImage frame, double[] frequencyTable) {
        this.frame = frame;
        this.frequencyTable = frequencyTable;
        executeManipulation();
    }

    private void executeManipulation() {
        for (int x = 0; x < frame.getHeight(); ++x) {
            for (int y = 0; y < frame.getHeight(); ++y) {
                Color color = new Color(frame.getRGB(x, y));
                Color pixel = color.brighter().brighter();
                frame.setRGB(x, y, pixel.getRGB());
            }
        }

    }

    public BufferedImage getManipulatedFrame() {
        return frame;
    }
}
