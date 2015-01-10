package ch.fhnw.meco.processor;

import ch.fhnw.meco.util.Constants;
import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.ICodec;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Setzt einen Film anhand einer Bilder von Images zusammen.
 */
public class VideoBuilder {

    private static final Logger log = Logger.getLogger(VideoBuilder.class.getName());

    private static final double FRAME_RATE = 50;

    private static final String outputFilename = Constants.VIDEO_DEFAULT_DESTINATION;
    private static final LinkedList<BufferedImage> list = new LinkedList<>();

    public static void build() {
        // let's make a IMediaWriter to write the file.
        File file = new File(outputFilename);
        final IMediaWriter writer = ToolFactory.makeWriter(file.toString());
         

        // We tell it we're going to add one video stream, with id 0,
        // at position 0, and that it will have a fixed frame rate of FRAME_RATE.
        writer.addVideoStream(0, 0, ICodec.ID.CODEC_ID_H264, list.element().getWidth(), list.element().getHeight());
 
        long startTime = System.nanoTime();
        long time = 0;

        // Iteration über die Bilder
        final Iterator<BufferedImage> iterator = list.iterator();
        while (iterator.hasNext()) {

            // nächstes Bild beziehen
            BufferedImage image = iterator.next();


            // encode the image to stream #0
            System.out.println("Div 1 Param : " + (System.nanoTime() - startTime));
            writer.encodeVideo(0, image, time, TimeUnit.NANOSECONDS);
            time += 50000000;

            /*
            // sleep for frame rate milliseconds
            try {
                Thread.sleep((long) (1000 / FRAME_RATE));
            }
            catch (InterruptedException e) {
                // ignore
            }*/
             
        }
         
        log.info("Wrote file " + outputFilename);

        // tell the writer to close and write the trailer if  needed
        writer.close();
    }
     
    public static void add(BufferedImage processedImage) {
        list.add(processedImage);
    }
}