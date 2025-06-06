package controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import util.FirebaseService;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class PerfilController {
	@FXML private Circle circleFoto;

    @FXML private Label usuarioLabel;

    @FXML
    public void initialize() {
        // Simulación de carga de datos
        usuarioLabel.setText("admin"); // Podrías pasar dinámicamente este dato con login
        circleFoto.setFill(javafx.scene.paint.Color.LIGHTGRAY); // Color de fondo simulado

    }

    @FXML
    private void cambiarContrasena() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Cambio de Contraseña");
        dialog.setHeaderText("Introduce la nueva contraseña:");
        dialog.setContentText("Contraseña:");

        dialog.showAndWait().ifPresent(nueva -> {
        	boolean exito = FirebaseService.cambiarContrasenaAdmin(nueva);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Resultado");

            if (exito) {
                alert.setContentText("Contraseña actualizada correctamente.");
            } else {
                alert.setAlertType(Alert.AlertType.ERROR);
                alert.setContentText("No se pudo actualizar la contraseña.");
            }

            alert.showAndWait();
        });
    }


    @FXML
    private void cerrarSesion() {
        try {
           

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LoginView.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) circleFoto.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
