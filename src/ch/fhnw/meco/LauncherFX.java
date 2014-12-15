package ch.fhnw.meco;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

import java.io.File;

/**
 * @author Josiane Manera
 */
public class LauncherFX extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {

        StackPane root = new StackPane();

        createComponents(root.getChildren());

        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.sizeToScene();
        stage.setTitle("AudioVisualizer");
        stage.getIcons().add(new Image("images/icon.png"));
        stage.show();
    }

    /**
     * Initialisiert alle UI Komponenten und fügt sie dem StackPane hinzu.
     *
     * @param componentList Liste mit UI Komponenten
     */
    private void createComponents(ObservableList<Node> componentList) {

        // MediaPlayer
        final Media media = new Media(new File("assets/video/big_buck_bunny.mp4").toURI().toString());
        final MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(false);
        final MediaView mediaView = new MediaView(mediaPlayer);
        mediaView.setFitWidth(600);

        // Button
        Button playButton = new Button();
        playButton.setText("\u25B6"); // ▶ U+25B6 \u25B6
        playButton.setTooltip(new Tooltip("Play"));
        playButton.setPrefSize(32, 32);
        playButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mediaPlayer.play();
            }
        });

        Button stopButton = new Button();
        stopButton.setText("\u25FC"); // ◼ U+25FC \u25FC
        stopButton.setTooltip(new Tooltip("Stop"));
        stopButton.setPrefSize(32, 32);
        stopButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mediaPlayer.stop();
            }
        });

        Button pauseButton = new Button();
        pauseButton.setText("\u2590\u2590"); // ▐ U+2590 \u2590
        pauseButton.setTooltip(new Tooltip("Pause"));
        pauseButton.setPrefSize(32, 32);
        pauseButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mediaPlayer.pause();
            }
        });


        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);

        hbox.getChildren().addAll(playButton, stopButton, pauseButton);

        // Add components
        componentList.add(mediaView);
        componentList.add(hbox);
    }
}
