package ch.fhnw.meco;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
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

    private int WIDTH  = 800;
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
        final Media media = new Media(new File("assets/video/big_buck_bunny.mp4").toURI().toString());
        final MediaPlayer mediaPlayer = new MediaPlayer(media);
        final MediaView mediaView = new MediaView(mediaPlayer);

        mediaView.setFitWidth(500);

        // Toolbar
        HBox hbox = createPlayToolbar(mediaPlayer);

        // Add components
        pane.getChildren().add(mediaView);
        pane.getChildren().add(hbox);
    }

    private HBox createPlayToolbar(final MediaPlayer mediaPlayer) {
        Button playButton = createButton("\u25B6", "Play"); // ▶ U+25B6 \u25B6
        playButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mediaPlayer.play();
            }
        });

        Button stopButton = createButton("\u25FC", "Stop"); // ◼ U+25FC \u25FC
        stopButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mediaPlayer.stop();
            }
        });
        Button pauseButton = createButton("||", "Pause"); // ▐ U+2590 \u2590
        pauseButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mediaPlayer.pause();
            }
        });

        // HBox
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(10));
        hbox.setSpacing(10);
        hbox.getChildren().addAll(playButton, stopButton, pauseButton);
        return hbox;
    }

    private Button createButton(String symbol, String tooltip) {
        Button button = new Button();
        button.setText(symbol);
        button.setTooltip(new Tooltip(tooltip));
        button.setPrefSize(32, 32);
        button.setFont(Font.font("Verdana", 32));
        return button;
    }
}
