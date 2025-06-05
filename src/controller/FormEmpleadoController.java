package controller;

import util.FirebaseService;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Empleado;
import javafx.collections.ObservableList;
import java.util.UUID;

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
            String uid = UUID.randomUUID().toString(); // UID único compatible con Android
            FirebaseService.crearEmpleadoCompleto(uid, nombre, cargo);
            cerrarVentana();
        }
    }

    @FXML
    private void cerrarVentana() {
        Stage stage = (Stage) nombreField.getScene().getWindow();
        stage.close();
    }
}
