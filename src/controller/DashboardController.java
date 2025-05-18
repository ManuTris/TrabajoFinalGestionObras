package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DashboardController {

    @FXML private Label labelNombre;
    @FXML private Label labelCorreo;

    public void setUsuario(String nombre, String correo) {
        labelNombre.setText("Bienvenido, " + nombre);
        labelCorreo.setText("Correo: " + correo);
    }

    @FXML
    private void handleLogout() {
        System.exit(0); // En futuro, podrías volver a Login
    }
}
