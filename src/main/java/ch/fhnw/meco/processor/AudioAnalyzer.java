package ch.fhnw.meco.processor;

import ddf.minim.AudioSample;
import ddf.minim.Minim;
import ddf.minim.analysis.FFT;

import javax.sound.sampled.AudioFormat;
import java.awt.*;

/**
 * Created by Manuel on 11.01.2015.
 */
public class AudioAnalyzer {

    public static float[] getSumData(float[] audioSample) {

        float band1 = Math.abs(audioSample[50]);
        float band2 = Math.abs(audioSample[100]);
        float band3 = Math.abs(audioSample[150]);
        float band4 = Math.abs(audioSample[200]);
        float band5 = Math.abs(audioSample[250]);
        float band6 = Math.abs(audioSample[300]);

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

    public static float[] getFreqData(float[] audio) {


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

    public static Color getFftColor(float[] audio) {

        final FFT fft = new FFT(audio.length,  44100);
        fft.forward(audio);

        float avg  = 0;
        float highestBand = 0;
        for (int i = 0; i < fft.specSize(); i++) {
            final float band = fft.getBand(i);
            avg += band;
            if (band > highestBand) highestBand = band;
        }
        avg /= (float) fft.specSize();

        float colorMultiplier = (avg / highestBand ) * 2;
        return new Color(colorMultiplier, colorMultiplier, colorMultiplier, 0.5f);

//        final int red   = ( (int) Math.abs(avg)) % 254;
//        final int green = ( (int) Math.abs(fft.getBand(fft.specSize()))) % 254;
//        final int blue  = ( (int) Math.abs(fft.getBand(fft.specSize()/2))) % 254;
//        return new Color(red, green, blue, 128);

    }
}
