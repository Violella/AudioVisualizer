package ch.fhnw.meco.processor;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Strategie um das Bild mit einem halbtransparenten Farb-Overlay zu manipulieren.
 */
public class FilterProcessor implements IVideoProcessor {

    private Color color = new Color(0.1f, 0.1f, 0.1f, 0.1f);

    /**
     * {@inheritDoc}
     */
    @Override
    public void processAudio(byte[] audio) {
        float[] floaties = new float[audio.length];
        for (int i = 0; i < audio.length; i++) floaties[i] = audio[i];
        color = AudioAnalyzer.getFftColor(floaties);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BufferedImage processImage(BufferedImage image) {
        return colorImage(image, color);
    }

    /**
     * Erstellt ein neues Bild und zeichnet das Video-Image zusammen mit einem halbtransparenten, Farb-Overlay darüber.
     *
     * @param image    Video-Image
     * @param newColor Farbe für das Overlay
     * @return Manipuliertes Bild
     */
    private BufferedImage colorImage(BufferedImage image, Color newColor) {

        BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TRANSLUCENT);
        Graphics2D graphics = result.createGraphics();
        graphics.drawImage(image, null, 0, 0);
        graphics.setComposite(AlphaComposite.SrcOver);
        graphics.setColor(newColor);
        graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
        graphics.dispose();

        // Bringt Bild in das richtige Ausgabeformat
        BufferedImage finalResult = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        graphics = finalResult.createGraphics();
        graphics.drawImage(result, null, 0, 0);
        graphics.dispose();

        return finalResult;
    }
}
