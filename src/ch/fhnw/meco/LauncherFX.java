package ch.fhnw.meco;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
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

    private int WIDTH  = 600;
    private int HEIGHT = 800;

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage stage) {

        // Flowpane
        VBox root = new VBox();
        createComponents(root);

        Scene scene = new Scene(root, WIDTH, HEIGHT);
        stage.setScene(scene);
        stage.sizeToScene();
        stage.setTitle("AudioVisualizer");
        stage.getIcons().add(new Image("images/icon.png"));
        stage.show();
    }


    /**
     * Initialisiert alle UI Komponenten und fügt sie dem StackPane hinzu.
     *
     * @param pane Pane mit den UI Komponenten
     */
    private void createComponents(Pane pane) {

        // MediaPlayer
        final Label original = new Label("Original");
        final MediaView mediaViewOriginal = createMediaPlayer();

        final Label result = new Label("Resultat");
        final MediaView mediaViewResult = createMediaPlayer();

        // Toolbar
        HBox hbox = createPlayToolbar(mediaViewResult.getMediaPlayer(), mediaViewOriginal.getMediaPlayer());

        // Add components
        pane.getChildren().addAll(original, mediaViewOriginal);
        pane.getChildren().addAll(result, mediaViewResult);
        pane.getChildren().add(hbox);
    }

    private MediaView createMediaPlayer() {
        final Media media = new Media(new File("assets/video/big_buck_bunny.mp4").toURI().toString());
        final MediaPlayer mediaPlayer = new MediaPlayer(media);
        final MediaView mediaView = new MediaView(mediaPlayer);
        mediaView.setFitWidth(WIDTH);
        return mediaView;
    }

    private HBox createPlayToolbar(final MediaPlayer originalMediaPlayer, final MediaPlayer resultMediaPlayer) {
        Image playImage = new Image(getClass().getResourceAsStream("/images/play.png"), 32, 32, true, true);
        Button playButton = createButton(playImage, "Play"); // ▶ U+25B6 \u25B6
        playButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                originalMediaPlayer.play();
                resultMediaPlayer.play();
            }
        });
        Image stopImage = new Image(getClass().getResourceAsStream("/images/stop.png"), 32, 32, true, true);
        Button stopButton = createButton(stopImage, "Stop"); // ◼ U+25FC \u25FC
        stopButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                originalMediaPlayer.stop();
                resultMediaPlayer.stop();
            }
        });
        Image pauseImage = new Image(getClass().getResourceAsStream("/images/pause.png"), 32, 32, true, true);
        Button pauseButton = createButton(pauseImage, "Pause"); // ▐ U+2590 \u2590
        pauseButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                originalMediaPlayer.pause();
                resultMediaPlayer.pause();
            }
        });

        // HBox
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(10));
        hbox.setSpacing(10);
        hbox.getChildren().addAll(playButton, stopButton, pauseButton);
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
