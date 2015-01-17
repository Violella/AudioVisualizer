package ch.fhnw.meco.processor;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Strategie um Pixels dem Image einzeln zu manipulieren.
 *
 */
public class VideoProcessor implements IVideoProcessor {
    private float[] frequencyData;

    /**
     *  {@inheritDoc}
     */
    @Override
    public BufferedImage processImage(BufferedImage image) {
        if (frequencyData == null) {
            return image;
        } else {

            Color color;
            int red;
            int green;
            int blue;
            Color editedColor;

            // Schleife über alle Pixel im Bild basierend auf Breite und Höhe (w = width, h = height)
            for (int w = 0; w < image.getWidth(); ++w) {
                for (int h = 0; h < image.getHeight(); ++h) {

                    color = new Color(image.getRGB(w, h));
                    red = color.getRed();
                    green = color.getGreen();
                    blue = color.getBlue();

                    if(frequencyData[0] != 0){
                        red*=frequencyData[0];
                    }
                    if(frequencyData[1] != 0){
                        green*=frequencyData[1];
                    }
                    if(frequencyData[2] != 0){
                        blue*=frequencyData[2];
                    }


                    editedColor = new Color(red, green, blue);

                    // Pixelwert mit neuer Farbe an Position setzen
                    image.setRGB(w, h, editedColor.getRGB());
                }
            }
            return image;
        }
    }

    /**
     *  {@inheritDoc}
     */
    @Override
    public void processAudio(byte[] audio) {
        float[] floaties = new float[audio.length];
        for (int i = 0; i <audio.length; i++) floaties[i] = audio[i];
        frequencyData = AudioAnalyzer.getSumData(floaties);
    }

    /**
     * Wendet ein Vergrauungs-Schema auf der Pixelfarbe an.
     *
     * @param color     Pixelfarbe
     * @return          Farbe mit angewandtem Vergrauungs-Schema
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
     * Wendet eine Rot-Schema auf der Pixelfarbe an.
     *
     * @param color     Pixelfarbe
     * @return          Farbe mit angewandtem Rot-Schema
     */
    private static Color redish(Color color) {
        return new Color(color.getRed(), 0, 0);
    }

    /**
     * Wendet eine Schwarzweiss-Schema auf der Pixelfarbe an.
     *
     * @param color     Pixelfarbe
     * @return          Farbe mit angewandtem Schwarzweiss-Schema
     */
    private static Color blackWhite(Color color) {
        if ((color.getRed() + color.getGreen() + color.getBlue()) < (255 * 3) / 3) {
            return new Color(0, 0, 0);
        } else {
            return new Color(255, 255, 255);
        }
    }
}