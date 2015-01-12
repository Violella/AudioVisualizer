package ch.fhnw.meco.processor;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Bietet eine Plattform um das Image anhand des AudioSamples zu bearbeiten.
 */
public class VideoProcessor implements IVideoProcessor {
    private ArrayList<Float> frequencyData;

    /**
     * @param image Images
     * @return Image
     */
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

                    if(frequencyData.get(0)!= 0){
                        red*=frequencyData.get(0);
                    }
                    if(frequencyData.get(1)!= 0){
                        green*=frequencyData.get(1);
                    }
                    if(frequencyData.get(2)!= 0){
                        blue*=frequencyData.get(2);
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
     * Weist dem Imageprocessing eine neue Strategie je nach Audiosstream.
     *
     * @param audio Audio
     */
    public void processAudio(float[] audio) {
        frequencyData = AudioAnalyzer.getSumData(audio);
//        for (int i = 0; i < frequencyData.size(); ++i) {
//            System.out.println(frequencyData.get(i));
//        }
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
}