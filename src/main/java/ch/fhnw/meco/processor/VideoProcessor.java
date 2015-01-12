package ch.fhnw.meco.processor;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Bietet eine Plattform um das Image anhand des AudioSamples zu bearbeiten.
 */
public class VideoProcessor implements IVideoProcessor {
    private ArrayList<Float> frequencyData =new ArrayList<Float>();

    /**
     * @param image Images
     * @return Image
     */
    public BufferedImage processImage(BufferedImage image) {
        if (frequencyData == null) {
            return image;
        } else {

        Color color;
        Color newColor;

        // Schleife über alle Pixel im Bild basierend auf Breite und Höhe (w = width, h = height)
        for (int w = 0; w < image.getWidth(); w++) {
            for (int h = 0; h < image.getHeight(); h++) {

                color = new Color(image.getRGB(w, h));

                newColor = redish(color);
                // newColor = blackWhite(newColor);
                // newColor = gray(newColor);

                // Pixelwert mit neuer Farbe an Position setzen
                image.setRGB(w, h, newColor.getRGB());
            }
        }
            return image;
        }
    }

    /**
     * Vergrauung an dunklen Stellen.
     *
     * @param color
     * @return
     */
    private static Color gray(Color color) {
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        int gray = 255 / 2;

        int light = ((255 * 3) / 3);
        if ((r + b + g) < light) {
            int light2 = ((255 * 3) / 2);
            if ((r + b + g) < light2) {
                gray = 255 / 3;
            }
            return new Color(gray, gray, gray);
        }
        return color;
    }

    /**
     * Rotfilter auf Bild.
     *
     * @param color
     * @return
     */
    private static Color redish(Color color) {
        return new Color(color.getRed(), 0, 0);
    }

    /**
     * Schwarzweiss Bild
     *
     * @param color
     * @return
     */
    private static Color blackWhite(Color color) {
        if ((color.getRed() + color.getGreen() + color.getBlue()) < (255 * 3) / 3) {
            return new Color(0, 0, 0);
        } else {
            return new Color(255, 255, 255);
        }
    }

    /**
     * Weist dem Imageprocessing eine neue Strategie je nach Audiosstream.
     *
     * @param audio Audio
     */
    public void processAudio(float[] audio) {
        frequencyData = AudioAnalyzer.getFreqData(audio);
    }
}