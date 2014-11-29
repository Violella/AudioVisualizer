package ch.fhnw.meco;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * @author Josiane Manera
 */
public class LauncherFX extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        StackPane root = new StackPane();

        createComponents(root.getChildren());

        Scene scene = new Scene(root, 500, 250);
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
        Button btn = new Button();
        btn.setText("Say 'Hello World'");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hello World!");
            }
        });
        componentList.add(btn);
    }

}
