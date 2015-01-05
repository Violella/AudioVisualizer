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
import java.util.ArrayList;

/**
 * @author Josiane Manera
 */
public class MinimTester extends Application {

    AudioPlayer audioPlayer;
    final String fileName = "Samplephonics - Vintage Drum Breaks Vol 1.mp3";
    final String fileName2 = "Yamaha-SY-35-Clarinet-C5.wav";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Group root = new Group();
        HBox stPane = new HBox();
        Scene scene = new Scene(root, 500, 250);


        Minim minim = new Minim(new MinimTester());
        audioPlayer = minim.loadFile(fileName, 1024);

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
        final Button btn2 = new Button();
        btn2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                audioPlayer.close();

            }
        });


        final Label l = new Label();
        l.setText("Color");
        l.setTextFill(Color.BLUE);

        final Button btn = new Button();
        btn.setText("Start");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(!audioPlayer.isPlaying()) audioPlayer.play();
                FFT fourier = new FFT(audioPlayer.bufferSize(), audioPlayer.sampleRate());

                fourier.forward(audioPlayer.mix);
                String blah = "";

                float band1 = fourier.getBand(1) + fourier.getBand(2);

                float band2 = fourier.getBand(3) + fourier.getBand(4) + fourier.getBand(5);

                float band3 = 0;
                for(int i=6; i <= 12; i++ ){
                    band3 += fourier.getBand(i);
                }

                float band4 = 0;
                for(int i=13; i <= 80; i++ ){
                    band4 += fourier.getBand(i);
                }

                float band5 = 0;
                for(int i=81; i <= 120; i++ ){
                    band5 += fourier.getBand(i);
                }

                float band6 = 0;
                for(int i=160; i <= 400; i++ ){
                    band3 += fourier.getBand(i);
                }

                float tot = band1 + band2 + band3 + band4 + band5 + band6;


                l.setText(audioPlayer.mix.level() + "");

            }
        });
        componentList.add(btn);
        componentList.add(btn2);
        componentList.add(l);

    }

    public InputStream createInput(final String name) {
        return MinimTester.class.getResourceAsStream("/audio/"+name);
    }

    public String sketchPath( String fileName ) {
        return "";
    }

    public ArrayList<Float> getFreqData(){
        Minim minim = new Minim(new MinimTester());
        AudioPlayer player = minim.loadFile(fileName, 1024);
        FFT fourier = new FFT(player.bufferSize(), audioPlayer.sampleRate());

        fourier.forward(audioPlayer.mix);

        float band1 = fourier.getBand(1) + fourier.getBand(2);

        float band2 = fourier.getBand(3) + fourier.getBand(4) + fourier.getBand(5);

        float band3 = 0;
        for(int i=6; i <= 12; i++ ){
            band3 += fourier.getBand(i);
        }

        float band4 = 0;
        for(int i=13; i <= 80; i++ ){
            band4 += fourier.getBand(i);
        }

        float band5 = 0;
        for(int i=81; i <= 120; i++ ){
            band5 += fourier.getBand(i);
        }

        float band6 = 0;
        for(int i=160; i <= 400; i++ ){
            band3 += fourier.getBand(i);
        }

        float tot = band1 + band2 + band3 + band4 + band5 + band6;

        ArrayList<Float> result = new ArrayList<Float>();
        result.add((tot / 100) * band1);
        result.add((tot / 100) * band2);
        result.add((tot / 100) * band3);
        result.add((tot / 100) * band4);
        result.add((tot / 100) * band5);
        result.add((tot / 100) * band6);

        return result;
    }
}
