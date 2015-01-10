package ch.fhnw.meco.ui;

import ch.fhnw.meco.processor.VideoDecoder;
import ch.fhnw.meco.util.Constants;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.File;

/**
 * @author Josiane Manera
 */
public class Launcher extends Application {

    private static final int BUTTON_SIZE = 32;
    private static final int SPACING_VERTICAL = 5;
    private static final int SPACING_HORIZONTAL = 10;
    private static int WIDTH = 600;
    private static int HEIGHT = 810;
    private static int FOOTAGE_HEIGHT = 340;
    private ProgressBar progress;

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage stage) {

        VBox root = new VBox(SPACING_VERTICAL);
        createComponents(root);

        Scene scene = new Scene(root, WIDTH, HEIGHT);
        stage.setScene(scene);
        stage.sizeToScene();
        stage.setTitle(Constants.APPLICATION_NAME);
        stage.getIcons().add(new Image(Constants.APPLICATION_ICON));
        stage.show();
    }


    /**
     * Initialisiert alle UI Komponenten und fügt sie dem StackPane hinzu.
     *
     * @param pane Pane mit den UI Komponenten
     */
    private void createComponents(Pane pane) {

        String sourceFilm = Constants.VIDEO_DEFAULT_SOURCE;

        // MediaPlayer
        final Label original = new Label("Original:");
        final MediaView originalMediaView = createMediaPlayer(sourceFilm);

        final Label result = new Label("Resultat:");
        final MediaView resultMediaView = createMediaPlayer(sourceFilm);

        // Toolbar
        HBox hbox = createPlayToolbar(originalMediaView, resultMediaView);

        // Progressbar
        createProgressBar();

        // Add components
        pane.getChildren().addAll(original, originalMediaView);
        pane.getChildren().addAll(result, resultMediaView);
        pane.getChildren().add(hbox);
        pane.getChildren().add(progress);
    }

    /**
     * Gibt eine MediaView zurück.
     *
     * @param file  Filmpfad
     * @return      MediaView
     */
    private MediaView createMediaPlayer(String file) {
        final MediaPlayer mediaPlayer = getMediaPlayer(file);
        final MediaView mediaView = new MediaView(mediaPlayer);

        mediaView.setFitHeight(FOOTAGE_HEIGHT);
        return mediaView;
    }

    /**
     * Gibt einen MediaPlayer mit dem entsprechenden Media zurück.
     *
     * @param file  Filmpfad
     * @return      MediaPlayer
     */
    private MediaPlayer getMediaPlayer(String file) {
        final Media media = new Media(new File(file).toURI().toString());
        return new MediaPlayer(media);
    }

    /**
     * Erzeugt eine Toolbar mit Buttons zum manipulieren des Filmes.
     *
     * @param originalMediaView     MediaView mit originalem Video
     * @param resultMediaView       MediaVierw mir resultierendem Video
     * @return                      Pane mit den Toolbar Komponenten
     */
    private HBox createPlayToolbar(final MediaView originalMediaView, final MediaView resultMediaView) {

        final Button playButton = createButton(Constants.IMAGE_PLAY, "Play");
        playButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                originalMediaView.getMediaPlayer().play();
                resultMediaView.getMediaPlayer().play();
            }
        });
        final Button stopButton = createButton(Constants.IMAGE_STOP, "Stop");
        stopButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                originalMediaView.getMediaPlayer().stop();
                resultMediaView.getMediaPlayer().stop();
            }
        });
        final Button pauseButton = createButton(Constants.IMAGE_PAUSE, "Pause");
        pauseButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                originalMediaView.getMediaPlayer().pause();
                resultMediaView.getMediaPlayer().pause();
            }
        });
        final Button configButton = createButton(Constants.IMAGE_CONFIG, "Einstellungen");
        configButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // TODO: Implement
            }
        });
        final Button convertButton = createButton(Constants.IMAGE_PROCESSOR, "Film konvertieren");
        convertButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                convertButton.setDisable(true);
                progress.setVisible(true);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        VideoDecoder.manipulate(Constants.VIDEO_DEFAULT_SOURCE);
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                resultMediaView.setMediaPlayer(getMediaPlayer(Constants.VIDEO_DEFAULT_DESTINATION));
                                originalMediaView.getMediaPlayer().pause();
                                originalMediaView.getMediaPlayer().stop();
                                progress.setVisible(false);
                                convertButton.setDisable(false);
                            }
                        });

                    }
                }).start();
            }
        });

        HBox hbox = new HBox();
        hbox.setSpacing(SPACING_HORIZONTAL);
        hbox.getChildren().addAll(playButton, stopButton, pauseButton, configButton, convertButton);
        return hbox;
    }

    /**
     * Erstellt ein Button mit einem Anzeigebild und Tooltip.
     *
     * @param imagePath Anzeigebild
     * @param tooltip   Tooltip
     * @return          Button
     */
    private Button createButton(String imagePath, String tooltip) {
        final Image image = new Image(getClass().getResourceAsStream(imagePath), BUTTON_SIZE, BUTTON_SIZE, true, true);
        Button button = new Button();
        button.setGraphic(new ImageView(image));
        button.setTooltip(new Tooltip(tooltip));
        button.setPrefSize(BUTTON_SIZE, BUTTON_SIZE);
        button.setFont(Font.font("Verdana", BUTTON_SIZE));
        return button;
    }

    private void createProgressBar() {
        progress = new ProgressBar();
        progress.setTooltip(new Tooltip("Video wird bearbeitet. Bitte warten..."));
        progress.setMaxWidth(WIDTH);
        progress.setVisible(false);
    }
}
