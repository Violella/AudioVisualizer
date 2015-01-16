package ch.fhnw.meco.processor;

import java.awt.image.BufferedImage;

public interface IVideoProcessor {

    public void processAudio(byte[] audio);

    public BufferedImage processImage(BufferedImage image);
}
