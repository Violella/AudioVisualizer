package ch.fhnw.meco.ui;

import ch.fhnw.meco.processor.VideoImageManipulation;
import ch.fhnw.meco.util.Constants;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
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
public class LauncherFX extends Application {

    private int WIDTH = 600;
    private int HEIGHT = 800;

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage stage) {

        // Flowpane
        VBox root = new VBox(5);
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

        String file = Constants.VIDEO_DEFAULT_SOURCE;

        // MediaPlayer
        final Label original = new Label("Original:");
        final MediaView originalMediaView = createMediaPlayer(file);

        final Label result = new Label("Resultat:");
        final MediaView resultMediaView = createMediaPlayer(file);

        // Toolbar
        HBox hbox = createPlayToolbar(originalMediaView, resultMediaView);

        // Add components
        pane.getChildren().addAll(original, originalMediaView);
        pane.getChildren().addAll(result, resultMediaView);
        pane.getChildren().add(hbox);
    }

    /**
     * Gibt eine MediaView zurück.
     *
     * @param file
     * @return
     */
    private MediaView createMediaPlayer(String file) {
        final MediaPlayer mediaPlayer = getMediaPlayer(file);
        final MediaView mediaView = new MediaView(mediaPlayer);

        mediaView.setFitWidth(WIDTH);
        return mediaView;
    }

    /**
     * Gibt einen MediaPlayer mit dem entsprechenden Media zurück.
     *
     * @param file
     * @return
     */
    private MediaPlayer getMediaPlayer(String file) {
        final Media media = new Media(new File(file).toURI().toString());
        return new MediaPlayer(media);
    }

    private HBox createPlayToolbar(final MediaView originalMediaView, final MediaView resultMediaView) {

        final Image playImage = new Image(getClass().getResourceAsStream(Constants.IMAGE_PLAY), 32, 32, true, true);
        final Button playButton = createButton(playImage, "Play"); // ▶ U+25B6 \u25B6
        playButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                originalMediaView.getMediaPlayer().play();
                resultMediaView.getMediaPlayer().play();
            }
        });
        final Image stopImage = new Image(getClass().getResourceAsStream(Constants.IMAGE_STOP), 32, 32, true, true);
        final Button stopButton = createButton(stopImage, "Stop"); // ◼ U+25FC \u25FC
        stopButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                originalMediaView.getMediaPlayer().stop();
                resultMediaView.getMediaPlayer().stop();
            }
        });
        final Image pauseImage = new Image(getClass().getResourceAsStream(Constants.IMAGE_PAUSE), 32, 32, true, true);
        final Button pauseButton = createButton(pauseImage, "Pause"); // ▐ U+2590 \u2590
        pauseButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                originalMediaView.getMediaPlayer().pause();
                resultMediaView.getMediaPlayer().pause();
            }
        });
        final Image convertImage = new Image(getClass().getResourceAsStream(Constants.IMAGE_PROCESSOR), 32, 32, true, true);
        final Button convertButton = createButton(convertImage, "Film konvertieren"); // ▐ U+2590 \u2590
        convertButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                playButton.setDisable(true);
                stopButton.setDisable(true);
                pauseButton.setDisable(true);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        VideoImageManipulation.manipulate(Constants.VIDEO_DEFAULT_SOURCE);
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                originalMediaView.getMediaPlayer().stop();
                                resultMediaView.setMediaPlayer(getMediaPlayer(Constants.VIDEO_DEFAULT_DESTINATION));
                                playButton.setDisable(false);
                                stopButton.setDisable(false);
                                pauseButton.setDisable(false);
                            }
                        });

                    }
                }).start();
            }
        });

        // HBox
        HBox hbox = new HBox();
        //hbox.setPadding(new Insets(10));
        hbox.setSpacing(10);
        hbox.getChildren().addAll(playButton, stopButton, pauseButton, convertButton);
        return hbox;
    }

    private Button createButton(Image image, String tooltip) {
        Button button = new Button();
        button.setGraphic(new ImageView(image));
        button.setTooltip(new Tooltip(tooltip));
        button.setPrefSize(32, 32);
        button.setFont(Font.font("Verdana", 32));
        return button;
    }
}
