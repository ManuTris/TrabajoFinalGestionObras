package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import model.Obra;
import model.Empleado;
import util.FirebaseService;

import java.util.*;

public class ObrasController {

    @FXML private TableView<Obra> tablaObras;
    @FXML private TableColumn<Obra, String> nombreColumna;
    @FXML private TableColumn<Obra, String> estadoColumna;
    @FXML private TableColumn<Obra, Void> empleadosColumna;

    private Map<String, String> asignaciones;
    private Map<String, Empleado> empleadosPorId;

    @FXML
    public void initialize() {
        nombreColumna.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        estadoColumna.setCellValueFactory(new PropertyValueFactory<>("estado"));

        cargarAsignacionesYEmpleados();
        configurarColumnaEmpleados();
        cargarObras();
    }

    private void cargarAsignacionesYEmpleados() {
        asignaciones = FirebaseService.obtenerAsignaciones();

        empleadosPorId = new HashMap<>();
        for (Empleado emp : FirebaseService.leerEmpleados()) {
            empleadosPorId.put(emp.getId(), emp);
        }
    }

    private void configurarColumnaEmpleados() {
        empleadosColumna.setCellFactory(param -> new TableCell<>() {
            private final ComboBox<String> combo = new ComboBox<>();

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                    return;
                }

                Obra obra = getTableRow().getItem();
                String obraId = obra.getId();

                List<String> empleadosAsignados = new ArrayList<>();
                for (Map.Entry<String, String> entry : asignaciones.entrySet()) {
                    if (obraId.equals(entry.getValue())) {
                        Empleado emp = empleadosPorId.get(entry.getKey());
                        if (emp != null) {
                            empleadosAsignados.add(emp.getNombre());
                        }
                    }
                }

                if (empleadosAsignados.isEmpty()) empleadosAsignados.add("Sin empleados");

                combo.setItems(FXCollections.observableArrayList(empleadosAsignados));
                combo.getSelectionModel().selectFirst();
                setGraphic(combo);
            }
        });
    }

    private void cargarObras() {
        List<Obra> lista = FirebaseService.leerObras();
        tablaObras.setItems(FXCollections.observableArrayList(lista));
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
