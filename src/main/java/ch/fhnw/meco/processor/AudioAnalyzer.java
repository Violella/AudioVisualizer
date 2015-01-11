package ch.fhnw.meco.processor;

import ddf.minim.AudioPlayer;
import ddf.minim.AudioSource;
import ddf.minim.Minim;
import ddf.minim.analysis.FFT;

import javax.sound.sampled.AudioFormat;
import java.util.ArrayList;

/**
 * Created by Manuel on 11.01.2015.
 */
public class AudioAnalyzer {

    public static ArrayList<Float> getFreqData(byte[] audio) {

        // folgender code ist aus dem MinimTester kopiert
        // TODO: audio aus byte array in die minim analyse bringen, eventuell launcher abändern, dass ein float array mit audio übergeben wird wenn einfacher
        // analyse ohne abspielen sollte wohl möglich sein, soweit ich das gesehen habe, siehe methode im kommentar der folgenden zeile
        // genaue anwendung ist etwas schleierhaft


        Minim minim = new Minim(new AudioAnalyzer());
        // minim.createSample(audio, ???);
        // AudioSource as = new AudioSource(null); // ein audio input stream
        AudioPlayer player = minim.loadFile("", 1024);

        // FFT fourier = new FFT(as.bufferSize(), as.sampleRate());
        FFT fourier = new FFT(player.bufferSize(), player.sampleRate());

        // fourier.forward(as.mix);
        fourier.forward(player.mix);

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
