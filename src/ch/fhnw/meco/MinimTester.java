package ch.fhnw.meco;

import ddf.minim.AudioPlayer;
import ddf.minim.Minim;
import ddf.minim.analysis.FFT;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.InputStream;

/**
 * @author Josiane Manera
 */
public class MinimTester extends Application {

    AudioPlayer audioPlayer;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Group root = new Group();
        HBox stPane = new HBox();
        Scene scene = new Scene(root, 500, 250);

        final String fileName = "Samplephonics - Vintage Drum Breaks Vol 1.mp3";
        Minim minim = new Minim(new MinimTester());
        audioPlayer = minim.loadFile(fileName);

        createComponents(stPane.getChildren());
        root.getChildren().add(stPane);
        primaryStage.setScene(scene);
        primaryStage.setTitle("AudioVisualizer");
        primaryStage.getIcons().add(new Image("images/icon.png"));
        primaryStage.show();
    }

    /**
     * Initialisiert alle UI Komponenten und fügt sie dem StackPane hinzu.
     *
     * @param componentList     Liste mit UI Komponenten
     */
    private void createComponents(ObservableList<Node> componentList) {
        final Label l = new Label();
        l.setText("Color");
        l.setTextFill(Color.BLUE);

        final Button btn = new Button();
        btn.setText("Start");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (audioPlayer.isPlaying()) {
                    audioPlayer.pause();
                    btn.setText("Start");
                } else {
                    FFT fourier = new FFT(audioPlayer.bufferSize(), audioPlayer.sampleRate());

                    audioPlayer.play();
                    btn.setText("Stop");
                }
            }
        });
        componentList.add(btn);
        componentList.add(l);

    }

    public InputStream createInput(final String name) {
        return MinimTester.class.getResourceAsStream("/audio/"+name);
    }

    public String sketchPath( String fileName ) {
        return "";
    }


}
