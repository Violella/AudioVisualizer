package ch.fhnw.meco.processor;

import ddf.minim.analysis.FFT;

import java.awt.*;

/**
 * Besitzt verschiedene Funktionen zur Analyse eines Audio Streams.
 */
public class AudioAnalyzer {

    private static final int DOUBLED_SAMPLE_RATE = 44100;
    private static final float MAGNIFIER = 2.5f;
    private static final float ALPHA_VALUE_OF_COLOR = 0.5f;

    /**
     * Der Audiostream wird mit einer Fast Fourier Transformation analysiert.
     * Die Höhe der Amplitude bestimmt die ausgegebene Farbe.
     * Je Höher desto rötlicher, je tiefer desto grüner wird die ausgegebene Farbe.
     *
     * @param audio Audiostream als Float Array
     * @return Farbe
     */
    public static Color getFftColor(float[] audio) {

        final FFT fft = new FFT(audio.length, DOUBLED_SAMPLE_RATE);
        fft.forward(audio);

        float avgBand = 0;
        float highestBand = 0;
        for (int i = 0; i < fft.specSize(); i++) {
            final float band = fft.getBand(i);
            avgBand += band;
            if (band > highestBand) highestBand = band;
        }
        avgBand /= (float) fft.specSize();
        float colorMultiplier = (avgBand / highestBand) * MAGNIFIER;

        return new Color(colorMultiplier, 1 - colorMultiplier, 0, ALPHA_VALUE_OF_COLOR);
    }
}
