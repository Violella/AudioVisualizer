package ch.fhnw.meco.processor;

import java.awt.image.BufferedImage;

public interface IVideoProcessor {

    public void processAudio(float[] byteArray);

    public BufferedImage processImage(BufferedImage image);
}
