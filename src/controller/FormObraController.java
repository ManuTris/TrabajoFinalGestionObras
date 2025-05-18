package controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Obra;
import javafx.collections.ObservableList;

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

        if (!nombre.isEmpty() && !estado.isEmpty()) {
            int nuevoId = listaObras.size() + 100; // ejemplo: id 101, 102...
            Obra nueva = new Obra(nuevoId, nombre, estado);
            listaObras.add(nueva);
            cerrarVentana();
        }
    }

    @FXML
    private void cerrarVentana() {
        Stage stage = (Stage) nombreField.getScene().getWindow();
        stage.close();
    }
}
