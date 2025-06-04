package controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Obra;
import javafx.collections.ObservableList;
import util.FirebaseService;

public class FormObraController {

    @FXML private TextField nombreField;
    @FXML private TextField estadoField;

    private ObservableList<Obra> listaObras;

    public void setListaObras(ObservableList<Obra> lista) {
        this.listaObras = lista;
    }

    @FXML
    private void guardarObra() {
        String nombre = nombreField.getText();
        String estado = estadoField.getText();

        if (nombre != null && !nombre.isEmpty() && estado != null && !estado.isEmpty()) {
            int nuevoId = (int) (Math.random() * 1000000); // generar ID único
            Obra nueva = new Obra(nuevoId, nombre, estado);
            listaObras.add(nueva);
            FirebaseService.guardarObraEnFirebase(nueva);
            cerrarVentana();
        }
    }

    @FXML
    private void cerrarVentana() {
        Stage stage = (Stage) nombreField.getScene().getWindow();
        stage.close();
    }
}
