package ch.fhnw.meco.video;

import ch.fhnw.meco.util.Constants;
import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.ICodec;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Setzt Images zu einem Film zusammen.
 */
public class VideoEncoder {

    private static final Logger log = Logger.getLogger(VideoEncoder.class.getName());

    private static final String outputFilename = Constants.VIDEO_DEFAULT_DESTINATION;
    private static final LinkedList<BufferedImage> list = new LinkedList<>();
    private static final int START_TIME = 0;
    private static final int TIME_BETWEEN_NEXT_IMAGE = 50000000;

    private static final int STREAM_ZERO = 0;
    private static final int STREAM_ID = STREAM_ZERO;

    /**
     * Setzt die Bilder der internen Liste zu einem Film zusammen.
     */
    public static void encode() {
        File file = new File(outputFilename);

        // IMediaWriter schreibt das Output-Video.
        final IMediaWriter writer = ToolFactory.makeWriter(file.toString());

        // We tell it we're going to add one video stream, with id 0, at position 0, and that it will have a fixed frame rate of FRAME_RATE.
        writer.addVideoStream(STREAM_ZERO, STREAM_ID, ICodec.ID.CODEC_ID_H264, list.element().getWidth(), list.element().getHeight());

        long startTime = System.nanoTime();
        long time = START_TIME;

        // Iteration über die Bilder
        final Iterator<BufferedImage> iterator = list.iterator();
        while (iterator.hasNext()) {

            // nächstes Bild beziehen
            BufferedImage image = iterator.next();

            // encode the image to stream #0
            writer.encodeVideo(STREAM_ZERO, image, time, TimeUnit.NANOSECONDS);
            time += TIME_BETWEEN_NEXT_IMAGE;
            log.log(Level.FINE, "Past time since start: " + (System.nanoTime() - startTime));
        }
        log.log(Level.INFO, "Wrote file " + outputFilename);

        // tell the writer to close and write the trailer if  needed
        writer.close();
    }

    /**
     * Hinzugefügte Images werden vom Encoder zu einem Video zusammengesetzt.
     * Die Reihenfolge ist dabei essentiel.
     *
     * @param processedImage Image das im Video erscheinen soll.
     */
    public static void add(BufferedImage processedImage) {
        list.add(processedImage);
    }
}