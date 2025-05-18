package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableView;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Obra;

public class ObrasController {

    @FXML private TableView<Obra> tablaObras;
    @FXML private TableColumn<Obra, Integer> idColumna;
    @FXML private TableColumn<Obra, String> nombreColumna;
    @FXML private TableColumn<Obra, String> estadoColumna;

    @FXML
    public void initialize() {
        idColumna.setCellValueFactory(new PropertyValueFactory<>("id"));
        nombreColumna.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        estadoColumna.setCellValueFactory(new PropertyValueFactory<>("estado"));

        List<Obra> lista = util.FirebaseService.leerObras();
        ObservableList<Obra> obras = FXCollections.observableArrayList(lista);
        tablaObras.setItems(obras);
    }

    
    @FXML
    private void abrirFormularioObra() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FormObra.fxml"));
            Parent root = loader.load();

            FormObraController controller = loader.getController();
            controller.setListaObras(tablaObras.getItems());

            Stage stage = new Stage();
            stage.setTitle("Nueva Obra");
            stage.setScene(new Scene(root));
            stage.initOwner(tablaObras.getScene().getWindow());
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
