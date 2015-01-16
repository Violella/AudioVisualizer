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

/**
 * Analysiert den Film und zerlegt ihn in einzelne Teile.
 */
public class VideoDecoder {

    // The video stream index, used to ensure we display frames from one and
    // only one video stream from the media container.
    private static int mVideoStreamIndex = -1;

    // Time of last frame write
    private static long mLastPtsWrite = Global.NO_PTS;

    private static final double SECONDS_BETWEEN_FRAMES = 0.05; // Abtastrate

    private static final long MICRO_SECONDS_BETWEEN_FRAMES = (long) (Global.DEFAULT_PTS_PER_SECOND * SECONDS_BETWEEN_FRAMES);
    private static SourceDataLine line;

    public static void manipulate(String inputFilename) {
        System.out.println("MICRO_SECONDS_BETWEEN_FRAMES: " + MICRO_SECONDS_BETWEEN_FRAMES);
        IMediaReader mediaReader = ToolFactory.makeReader(inputFilename);
        mediaReader.open();

        // stipulate that we want BufferedImages created in BGR 24bit color space
        mediaReader.setBufferedImageTypeToGenerate(BufferedImage.TYPE_3BYTE_BGR);
        mediaReader.addListener(new ImageSnapListener());


        openJavaSound(mediaReader);

        // read out the contents of the media file and
        // dispatch events to the attached listener
        while(mediaReader.readPacket() == null);

        mediaReader.close();
        VideoEncoder.build();
    }


    /**
     * Creates a Line for sound output via Java.
     * @param mediaReader
     */
    private static void openJavaSound(IMediaReader mediaReader) {

        int numStreams = mediaReader.getContainer().getNumStreams();

        // and iterate through the streams to find the first audio stream
        IStreamCoder audioCoder = null;
        for(int i = 0; i < numStreams; i++) {
            // Find the stream object
            IStream stream = mediaReader.getContainer().getStream(i);
            // Get the pre-configured decoder that can decode this stream;
            IStreamCoder coder = stream.getStreamCoder();

            if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_AUDIO)
            {
                audioCoder = coder;
                break;
            }
        }
        audioCoder.open();


        AudioFormat audioFormat = new AudioFormat(audioCoder.getSampleRate(), (int)IAudioSamples.findSampleBitDepth(audioCoder.getSampleFormat()),
                                                  audioCoder.getChannels(),
                                                  true, /* xuggler defaults to signed 16 bit samples */
                                                  false);
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
        try {
            line = (SourceDataLine) AudioSystem.getLine(info);
            /**
             * if that succeeded, try opening the line.
             */
            line.open(audioFormat);
            /**
             * And if that succeed, start the line.
             */
            line.start();
            System.out.println(Arrays.toString(line.getControls()));
        } catch (LineUnavailableException e) {
            throw new RuntimeException("could not open audio line");
        }
    }


    private static void playJavaSound(IAudioSamples aSamples) {

        /**
         * We're just going to dump all the samples into the line.
         */
        byte[] rawBytes = aSamples.getData().getByteArray(0, aSamples.getSize());
        line.write(rawBytes, 0, aSamples.getSize());
    }

    private static class ImageSnapListener extends MediaListenerAdapter {

        private IVideoProcessor processor = new FilterProcessor();

        @Override
        public void onAudioSamples(IAudioSamplesEvent event) {
            final IAudioSamples audioSamples = event.getAudioSamples();

            final byte[] byteBuffer = audioSamples.getData().getByteArray(0, audioSamples.getSize());

//            System.out.println("audioSamples : " + audioSamples); // audioSamples : com.xuggle.xuggler.IAudioSamples@1638941504[sample rate:22050;channels:2;format:FMT_S16;time stamp:0;complete:true;num samples:1024;size:4096;key:true;time base:1/1000000;]
//            System.out.println("byteBuffer : " + byteBuffer); // byteBuffer : java.nio.DirectByteBuffer[pos=0 lim=4096 cap=4096]

            processor.processAudio(byteBuffer);
        }

        public void onVideoPicture(IVideoPictureEvent event) {

            if (event.getStreamIndex() != mVideoStreamIndex) {
                // if the selected video stream id is not yet set, go ahead an
                // select this lucky video stream
                if (mVideoStreamIndex == -1)
                    mVideoStreamIndex = event.getStreamIndex();
                    // no need to show frames from this video stream
                else
                    return;
            }

            // if uninitialized, back date mLastPtsWrite to get the very first frame
            if (mLastPtsWrite == Global.NO_PTS)
                mLastPtsWrite = event.getTimeStamp() - MICRO_SECONDS_BETWEEN_FRAMES;

            // if it's time to write the next frame
            if (event.getTimeStamp() - mLastPtsWrite >= MICRO_SECONDS_BETWEEN_FRAMES) {

                final BufferedImage image = event.getImage();
                final BufferedImage processedImage = processor.processImage(image);
                VideoEncoder.add(processedImage);

                // update last write time
                mLastPtsWrite += MICRO_SECONDS_BETWEEN_FRAMES;
            }

        }

    }

}