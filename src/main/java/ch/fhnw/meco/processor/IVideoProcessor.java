package ch.fhnw.meco.processor;

import java.awt.image.BufferedImage;

public interface IVideoProcessor {

    public void processAudio(byte[] byteArray);

    public BufferedImage processImage(BufferedImage image);
}
