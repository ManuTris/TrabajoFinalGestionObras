package controller;

import util.FirebaseService;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Empleado;
import javafx.collections.ObservableList;

public class FormEmpleadoController {

    @FXML private TextField nombreField;
    @FXML private TextField cargoField;

    private ObservableList<Empleado> listaEmpleados;

    public void setListaEmpleados(ObservableList<Empleado> lista) {
        this.listaEmpleados = lista;
    }

    @FXML
    private void guardarEmpleado() {
        String nombre = nombreField.getText();
        String cargo = cargoField.getText();

        if (!nombre.isEmpty() && !cargo.isEmpty()) {
            util.FirebaseService.enviarEmpleado(nombre, cargo);
            cerrarVentana();
        }
    }


    @FXML
    private void cerrarVentana() {
        Stage stage = (Stage) nombreField.getScene().getWindow();
        stage.close();
    }
}
