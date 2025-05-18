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
            Scene scene = new Scene(root); // ya no necesitas tamaño fijo

            primaryStage.setTitle("Gestión de Obras - Login");
            primaryStage.setScene(scene);

            // 👇 Modo pantalla completa
            primaryStage.setFullScreen(true);
            primaryStage.setFullScreenExitHint(""); // Oculta el mensaje "presiona ESC"

            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace(); // Ver errores en consola
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
