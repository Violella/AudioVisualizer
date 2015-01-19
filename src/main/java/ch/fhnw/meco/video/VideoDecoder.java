package ch.fhnw.meco.video;

import ch.fhnw.meco.processor.FilterProcessor;
import ch.fhnw.meco.processor.IVideoProcessor;
import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.MediaListenerAdapter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.mediatool.event.IAudioSamplesEvent;
import com.xuggle.mediatool.event.IVideoPictureEvent;
import com.xuggle.xuggler.*;

import javax.sound.sampled.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Analysiert den Film und zerlegt ihn in einzelne Teile.
 */
public class VideoDecoder extends VideoEncoder {

    private static final Logger log = Logger.getLogger(VideoDecoder.class.getName());

    // Abtastrate
    private static final double SECONDS_BETWEEN_FRAMES = 0.05;

    private static final long MICRO_SECONDS_BETWEEN_FRAMES = (long) (Global.DEFAULT_PTS_PER_SECOND * SECONDS_BETWEEN_FRAMES);
    private static SourceDataLine line;

    public static void decode(String inputFilename) {
        log.log(Level.FINE, "Micro seconds between frames " + MICRO_SECONDS_BETWEEN_FRAMES);
        IMediaReader mediaReader = ToolFactory.makeReader(inputFilename);
        mediaReader.open();

        // Der Reader soll BufferedImage in einem BGR 24bit Farbraum erstellen
        mediaReader.setBufferedImageTypeToGenerate(BufferedImage.TYPE_3BYTE_BGR);
        mediaReader.addListener(new ImageAudioSnapListener());

        openJavaSound(mediaReader);

        // Liest den Inhalt aus dem Mediafile und benachrichtigt den Event an Listeners
        while (mediaReader.readPacket() == null) ;

        mediaReader.close();
        VideoEncoder.encode();
    }

    /**
     * Creates a Line for sound output via Java.
     *
     * @param mediaReader
     */
    private static void openJavaSound(IMediaReader mediaReader) {

        int numStreams = mediaReader.getContainer().getNumStreams();

        // Sucht den ersten Audiostream
        IStreamCoder audioCoder = null;
        for (int i = 0; i < numStreams; ++i) {

            IStream stream = mediaReader.getContainer().getStream(i);
            IStreamCoder coder = stream.getStreamCoder();

            if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_AUDIO) {
                audioCoder = coder;
                break;
            }
        }
        audioCoder.open();

        AudioFormat audioFormat = new AudioFormat(audioCoder.getSampleRate(), (int) IAudioSamples.findSampleBitDepth(audioCoder.getSampleFormat()),
                audioCoder.getChannels(),
                true, /* xuggler defaults to signed 16 bit samples */
                false);
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
        try {
            line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(audioFormat); // Öffnet line
            line.start(); // Startet line
            log.log(Level.FINE, Arrays.toString(line.getControls()));
        } catch (LineUnavailableException e) {
            throw new RuntimeException("Could not open audio line");
        }
    }

    /**
     * Output eines Xuggler AudioSamples über Java
     *
     * @param audioSamples Xuggler AudioSamples
     */
    private static void playJavaSound(IAudioSamples audioSamples) {
        // Dumpt alle samples in die Line
        byte[] rawBytes = audioSamples.getData().getByteArray(0, audioSamples.getSize());
        line.write(rawBytes, 0, audioSamples.getSize());
    }

    /**
     * Listener der beim Readvorgang der MediaReaders aktiv wird.
     */
    private static class ImageAudioSnapListener extends MediaListenerAdapter {

        private static final int INTIAL_STREAM_INDEX = -1;

        // Über diesen Vidostream Index des Media Containers werden die Frames angezeigt
        private static int videoStreamIndex = INTIAL_STREAM_INDEX;

        // Zeit seit letztem schreiben eines Frames
        private static long lastPtsWrite = Global.NO_PTS;

        private IVideoProcessor processor = new FilterProcessor();

        @Override
        public void onAudioSamples(IAudioSamplesEvent event) {
            final IAudioSamples audioSamples = event.getAudioSamples();

            final byte[] byteBuffer = audioSamples.getData().getByteArray(0, audioSamples.getSize());

            processor.processAudio(byteBuffer);
        }

        public void onVideoPicture(IVideoPictureEvent event) {

            if (event.getStreamIndex() != videoStreamIndex) {
                if (videoStreamIndex != INTIAL_STREAM_INDEX) return;

                // Wenn der videoStreamIndex noch keine valide Id besitzt, erhält er die ID des aktuellen Stream Indexes
                videoStreamIndex = event.getStreamIndex();
            }

            // wenn lastPtsWrite noch unitialisiert ist, wird der Timestamp des allerersten Frames gesetzt
            if (lastPtsWrite == Global.NO_PTS) lastPtsWrite = event.getTimeStamp() - MICRO_SECONDS_BETWEEN_FRAMES;

            // Schreibt das nächste Frame
            if (event.getTimeStamp() - lastPtsWrite >= MICRO_SECONDS_BETWEEN_FRAMES) {

                final BufferedImage image = event.getImage();
                final BufferedImage processedImage = processor.processImage(image);
                VideoEncoder.add(processedImage);

                // Update die letzte Zeit in der ein Frame geschrieben wurde
                lastPtsWrite += MICRO_SECONDS_BETWEEN_FRAMES;
            }
        }
    }
}