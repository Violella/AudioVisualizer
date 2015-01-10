package ch.fhnw.meco.processor;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.logging.Logger;

/**
 * Bearbeitet einzelne Bilder mit einem spezifischen Filter.
 */
public class ImageProcessing {

    private static final Logger log = Logger.getLogger(VideoBuilder.class.getName());

    public static BufferedImage process(BufferedImage image) {

        // Schleife über alle Pixel im Bild basierend auf Breite und Höhe (w = width, h = height)
        for (int w = 0; w < image.getWidth(); w++) {
            for (int h = 0; h < image.getHeight(); h++) {

                // BufferedImage.getRGB() speichert Farben eines Pixels als einzelne Integer Werte
                // Man kann das Color(int) Objekt verwenden, um die RGB Werte anzupassen.
                Color color = new Color(image.getRGB(w, h));

                int r = color.getRed();
                int g = color.getGreen();
                int b = color.getBlue();

                // Farbig (default)
                Color newColor = new Color(r, g, b);

                // newColor = blackWhite(newColor);
                newColor = redish(newColor);
                // newColor = gray(newColor);

                // Pixelwert mit neuer Farbe an Position setzen
                image.setRGB(w, h, newColor.getRGB());
            }
        }
        return image;
    }

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

    private static Color redish(Color newColor) {
        return new Color(newColor.getRed(), 0, 0);
    }

    /**
     * Schwarzweiss Bild
     *
     * @param newColor
     * @return
     */
    private static Color blackWhite(Color newColor) {
        if ((newColor.getRed() + newColor.getGreen() + newColor.getBlue()) < (255 * 3) / 3) {
            return new Color(0, 0, 0);
        } else {
            return new Color(255, 255, 255);
        }
    }

}