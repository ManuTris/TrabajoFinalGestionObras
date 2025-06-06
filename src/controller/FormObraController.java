package controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.collections.ObservableList;
import model.Obra;
import util.FirebaseService;

import java.util.UUID;

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
            String nuevoId = UUID.randomUUID().toString(); // ID único tipo String
            Obra nueva = new Obra(nuevoId, nombre, estado);

            listaObras.add(nueva); // Añadir a la lista local
            FirebaseService.guardarObraEnFirebase(nueva); // Guardar en Firebase
            cerrarVentana();
        }
    }

    @FXML
    private void cerrarVentana() {
        Stage stage = (Stage) nombreField.getScene().getWindow();
        stage.close();
    }
}
