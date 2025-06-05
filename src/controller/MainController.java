package controller;

import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;

public class MainController {

    @FXML
    private StackPane contentArea;

    @FXML
    private void loadEmpleados() {
        cargarVista("/view/EmpleadosView.fxml");
    }

    @FXML
    private void loadObras() {
        cargarVista("/view/ObrasView.fxml");
    }

    @FXML
    private void loadPerfil() {
        cargarVista("/view/PerfilView.fxml");
    }

    @FXML
    private void handleLogout() {
        System.exit(0); // En el futuro, volver al login
    }

    private void cargarVista(String rutaFXML) {
        try {
            Parent pane = FXMLLoader.load(getClass().getResource(rutaFXML));
            contentArea.getChildren().setAll(pane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void loadInformes() {
        cargarVista("/view/InformesView.fxml");
    }
    @FXML
    private void loadHorarios() {
        cargarVista("/view/HorariosView.fxml");
    }

}
