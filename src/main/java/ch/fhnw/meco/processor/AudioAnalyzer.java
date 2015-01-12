package ch.fhnw.meco.processor;

import ddf.minim.AudioSample;
import ddf.minim.Minim;
import ddf.minim.analysis.FFT;
import sun.audio.AudioPlayer;

import javax.sound.sampled.AudioFormat;
import java.util.ArrayList;

/**
 * Created by Manuel on 11.01.2015.
 */
public class AudioAnalyzer {

    public static ArrayList<Float> getFreqData(float[] audio) {
        Minim minim = new Minim(new AudioAnalyzer());
        AudioSample sample = minim.createSample(audio, new AudioFormat(44100, 16, 2, false, false));

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

        ArrayList<Float> result = new ArrayList<Float>();
        result.add((tot / 100) * band1);
        result.add((tot / 100) * band2);
        result.add((tot / 100) * band3);
        result.add((tot / 100) * band4);
        result.add((tot / 100) * band5);
        result.add((tot / 100) * band6);

        return result;
    }
}
