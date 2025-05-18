package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Empleado;

import java.util.List;

public class EmpleadosController {

    @FXML private TableView<Empleado> tablaEmpleados;
    @FXML private TableColumn<Empleado, Integer> idColumna;
    @FXML private TableColumn<Empleado, String> nombreColumna;
    @FXML private TableColumn<Empleado, String> cargoColumna;

    @FXML
    public void initialize() {
        // Configurar columnas (los nombres deben coincidir con los getters del modelo)
        idColumna.setCellValueFactory(new PropertyValueFactory<>("id"));
        nombreColumna.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        cargoColumna.setCellValueFactory(new PropertyValueFactory<>("cargo"));

        // Cargar datos desde Firebase
        List<Empleado> lista = util.FirebaseService.leerEmpleados();
        ObservableList<Empleado> empleados = FXCollections.observableArrayList(lista);
        tablaEmpleados.setItems(empleados);
    }

    @FXML
    private void abrirFormularioNuevo() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FormEmpleado.fxml"));
            Parent root = loader.load();

            FormEmpleadoController controller = loader.getController();
            controller.setListaEmpleados(tablaEmpleados.getItems());

            Stage stage = new Stage();
            stage.setTitle("Nuevo Empleado");
            stage.setScene(new Scene(root));
            stage.initOwner(tablaEmpleados.getScene().getWindow());
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
