package ch.fhnw.meco.util;

/**
 * Ablage von globalen Konstanten wie Pfandangaben.
 */
public class Constants {

    // Path from working directory
    public static final String PATH_IMAGE_RESOURCES = "src/main/resources/images/";
    public static final String PATH_VIDEO_RESOURCES = "src/main/resources/video/";

    // Formats
    public static final String MP4_MANIPULATED = "_m1.mp4";
    public static final String MP4 = ".mp4";

    // Video
    private static final String VIDEO_NAME             = "catAccident";
//    private static final String VIDEO_NAME               = "big_buck_bunny";
    public static final String VIDEO_DEFAULT_SOURCE      = PATH_VIDEO_RESOURCES + VIDEO_NAME + MP4;
    public static final String VIDEO_DEFAULT_DESTINATION = PATH_VIDEO_RESOURCES + VIDEO_NAME + MP4_MANIPULATED;

    // Images
    public static final String IMAGE_PLAY = "/images/play.png";
    public static final String IMAGE_STOP = "/images/stop.png";
    public static final String IMAGE_PAUSE = "/images/pause.png";
    public static final String IMAGE_CONFIG = "/images/config.png";
    public static final String IMAGE_PROCESSOR = "/images/film.png";
    public static final String APPLICATION_ICON = "/images/audiVis.png";

    // Strings
    public static final String APPLICATION_NAME = "AudioVisualizer";
}
