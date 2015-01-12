package ch.fhnw.meco.processor;

import java.awt.image.BufferedImage;

public interface IVideoProcessor {

    public void processAudio(float[] audio);

    public BufferedImage processImage(BufferedImage frame);
}
