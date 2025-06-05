package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import util.FirebaseService;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class PerfilController {

    @FXML private Label usuarioLabel;

    @FXML
    public void initialize() {
        // Simulación de carga de datos
        usuarioLabel.setText("admin"); // Podrías pasar dinámicamente este dato con login
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
        System.exit(0); // En el futuro, podrías redirigir al login
    }
}
