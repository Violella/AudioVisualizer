package ch.fhnw.meco.processor;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.MediaListenerAdapter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.mediatool.event.IAudioSamplesEvent;
import com.xuggle.mediatool.event.IVideoPictureEvent;
import com.xuggle.xuggler.Global;
import com.xuggle.xuggler.IAudioSamples;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

/**
 * Analysiert den Film und zerlegt ihn in einzelne Teile.
 *
 */
public class VideoDecoder {
     
    // The video stream index, used to ensure we display frames from one and
    // only one video stream from the media container.
    private static int mVideoStreamIndex = -1;

    // Time of last frame write
    private static long mLastPtsWrite = Global.NO_PTS;

    private static final double SECONDS_BETWEEN_FRAMES = 0.05; // Abtastrate

    private static final long MICRO_SECONDS_BETWEEN_FRAMES = (long)(Global.DEFAULT_PTS_PER_SECOND * SECONDS_BETWEEN_FRAMES);

    public static void manipulate(String inputFilename) {
        System.out.println("MICRO_SECONDS_BETWEEN_FRAMES: "+ MICRO_SECONDS_BETWEEN_FRAMES);
        IMediaReader mediaReader = ToolFactory.makeReader(inputFilename);
 
        // stipulate that we want BufferedImages created in BGR 24bit color space
        mediaReader.setBufferedImageTypeToGenerate(BufferedImage.TYPE_3BYTE_BGR);
         
        mediaReader.addListener(new ImageSnapListener());

        // read out the contents of the media file and
        // dispatch events to the attached listener
        while (mediaReader.readPacket() == null) ;

        mediaReader.close();
        VideoBuilder.build();
    }
 
    private static class ImageSnapListener extends MediaListenerAdapter {

        @Override
        public void onAudioSamples(IAudioSamplesEvent event) {
            final IAudioSamples audioSamples = event.getAudioSamples();
            final byte[] byteArray = audioSamples.getData().getByteArray(0, audioSamples.getSize());

            // TODO: Weiterleiten des Musikdaten zur Analyse

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
                final BufferedImage processedImage = ImageProcessing.process(image);
                VideoBuilder.add(processedImage);

                // update last write time
                mLastPtsWrite += MICRO_SECONDS_BETWEEN_FRAMES;
            }
 
        }
         
    }
 
}