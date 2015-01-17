package ch.fhnw.meco.processor;

import ddf.minim.AudioSample;
import ddf.minim.Minim;
import ddf.minim.analysis.FFT;

import javax.sound.sampled.AudioFormat;
import java.awt.*;

/**
 * Besitzt verschiedene Funktionen zur Analyse eines Audio Streams.
 *
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
     * @param audio     Audiostream als Float Array
     * @return          Farbe
     */
    public static Color getFftColor(float[] audio) {

        final FFT fft = new FFT(audio.length, DOUBLED_SAMPLE_RATE);
        fft.forward(audio);

        float avgBand  = 0;
        float highestBand = 0;
        for (int i = 0; i < fft.specSize(); i++) {
            final float band = fft.getBand(i);
            avgBand += band;
            if (band > highestBand) highestBand = band;
        }
        avgBand /= (float) fft.specSize();
        float colorMultiplier = (avgBand / highestBand) * MAGNIFIER;

        return new Color(colorMultiplier, 1 - colorMultiplier, 0,  ALPHA_VALUE_OF_COLOR);
    }

    /**
     * Zu Testzwecken werden aus dem Audiosammple Werte bezogen.
     * Aus dem Float Array kann danach eine Farbe erzeugt werden.
     *
     * @param   audio    Audiostream als Float Array
     * @return
     */
    public static float[] getSumData(float[] audio) {

        float band1 = Math.abs(audio[50]);
        float band2 = Math.abs(audio[100]);
        float band3 = Math.abs(audio[150]);
        float band4 = Math.abs(audio[200]);
        float band5 = Math.abs(audio[250]);
        float band6 = Math.abs(audio[300]);

        float tot = band1 + band2 + band3 + band4 + band5 + band6;

        float[] result = new float[6];
        result[0] = (band1 / tot);
        result[1] = (band2 / tot);
        result[2] = (band3 / tot);
        result[3] = (band4 / tot);
        result[4] = (band5 / tot);
        result[5] = (band6 / tot);

        return result;
    }

    /**
     * Analyse der Audiodaten mit Minim.
     *
     * @param audio
     * @return
     */
    public static float[] getFreqData(float[] audio) {


        Minim minim = new Minim(new AudioAnalyzer());
        AudioSample sample = minim.createSample(audio, new AudioFormat(DOUBLED_SAMPLE_RATE, 16, 2, false, false));

        FFT fourier = new FFT(sample.bufferSize(), sample.sampleRate());

        sample.trigger();
        fourier.forward(sample.mix);

        float band1 = fourier.getBand(1) + fourier.getBand(2);

        float band2 = fourier.getBand(3) + fourier.getBand(4) + fourier.getBand(5);

        float band3 = 0;
        for (int i = 6; i <= 12; i++) {
            band3 += fourier.getBand(i);
        }

        float band4 = 0;
        for (int i = 13; i <= 80; i++) {
            band4 += fourier.getBand(i);
        }

        float band5 = 0;
        for (int i = 81; i <= 120; i++) {
            band5 += fourier.getBand(i);
        }

        float band6 = 0;
        for (int i = 160; i <= 400; i++) {
            band3 += fourier.getBand(i);
        }

        float tot = band1 + band2 + band3 + band4 + band5 + band6;

        float[] result = new float[6];
        result[0] = (tot / 100) * band1;
        result[1] = (tot / 100) * band2;
        result[2] = (tot / 100) * band3;
        result[3] = (tot / 100) * band4;
        result[4] = (tot / 100) * band5;
        result[5] = (tot / 100) * band6;

        minim.dispose();

        return result;
    }
}
