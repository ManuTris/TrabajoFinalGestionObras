package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
  
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/LoginView.fxml"));
            Scene scene = new Scene(root);

            primaryStage.setTitle("Gestión de Obras");
            primaryStage.setScene(scene);

            // Activar pantalla completa al iniciar
            primaryStage.setFullScreen(true);
            primaryStage.setFullScreenExitHint("");

            // Cuando el usuario salga del modo fullscreen con ESC...
            primaryStage.fullScreenProperty().addListener((obs, wasFull, isNowFull) -> {
                if (!isNowFull) {
                    // Fijar tamaño uniforme
                    primaryStage.setWidth(1280);
                    primaryStage.setHeight(800);
                    primaryStage.centerOnScreen();
                }
            });

            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
