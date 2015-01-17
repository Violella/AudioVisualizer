package ch.fhnw.meco.processor;

import java.awt.image.BufferedImage;

/**
 *
 * Bietet eine Schnittstelle um das Image anhand des AudioSamples unterschiedlich bearbeiten zu können.
 */
public interface IVideoProcessor {

    /**
     * Wird aufgerufen, sobald ein neuer Audiostream zur Verfügung steht.
     * Analysiert das Audisample und leitet die Daten an die Bildbearbeitung weiter.
     *
     * @param audio     Audiostream
     */
    public void processAudio(byte[] audio);

    /**
     * Wird aufgerufen, sobald ein neues Image zur Verfügung steht.
     * Maipuliert das Bild anhand einer spezifischen Strategie.
     *
     * @param image     Image
     * @return          Manipuliertes Image
     */
    public BufferedImage processImage(BufferedImage image);
}
