package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.SimpleStringProperty;
import model.Fichaje;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class InformesController {

    private Map<String, String> empleadosMap = new HashMap<>();
    private Map<String, String> obrasMap = new HashMap<>();

    // Fichajes
    @FXML private TableView<Fichaje> tablaFichajes;
    @FXML private TableColumn<Fichaje, String> colFecha;
    @FXML private TableColumn<Fichaje, String> colNombre;
    @FXML private TableColumn<Fichaje, String> colObra;
    @FXML private TableColumn<Fichaje, String> colEntrada;
    @FXML private TableColumn<Fichaje, String> colSalida;
    @FXML private TableColumn<Fichaje, Boolean> colTarde;

    // Errores de obra (a implementar)
    @FXML private TableView<?> tablaErrores;
    @FXML private TableColumn<?, ?> colErrorObra;
    @FXML private TableColumn<?, ?> colDescripcion;
    @FXML private TableColumn<?, ?> colFechaError;

    @FXML
    public void initialize() {
        cargarNombres(); // Cargar empleados y obras antes de mostrar

        // Formatear fecha a dd/MM/yyyy
        colFecha.setCellValueFactory(cellData -> {
            String rawFecha = cellData.getValue().getFecha(); // yyyy-MM-dd
            String[] partes = rawFecha.split("-");
            if (partes.length == 3) {
                return new SimpleStringProperty(partes[2] + "/" + partes[1] + "/" + partes[0]);
            } else {
                return new SimpleStringProperty(rawFecha);
            }
        });

        colNombre.setCellValueFactory(cellData -> {
            String id = cellData.getValue().getEmpleadoId();
            return new SimpleStringProperty(empleadosMap.getOrDefault(id, id));
        });

        colObra.setCellValueFactory(cellData -> {
            String id = cellData.getValue().getObraId();
            return new SimpleStringProperty(obrasMap.getOrDefault(id, id));
        });

        colEntrada.setCellValueFactory(new PropertyValueFactory<>("horaEntrada"));
        colSalida.setCellValueFactory(new PropertyValueFactory<>("horaSalida"));
        colTarde.setCellValueFactory(new PropertyValueFactory<>("fichadoTarde"));


        // Celda personalizada para ¿Tarde?
        colTarde.setCellFactory(column -> new TableCell<Fichaje, Boolean>() {
            @Override
            protected void updateItem(Boolean value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || value == null) {
                    setText(null);
                    setStyle("");
                } else if (value) {
                    setText("Tarde");
                    setStyle("-fx-background-color: #ffcccc; -fx-text-fill: red; -fx-font-weight: bold;");
                } else {
                    setText("Puntual");
                    setStyle("-fx-background-color: #ccffcc; -fx-text-fill: green; -fx-font-weight: bold;");
                }
            }
        });

        // Cargar datos desde Firebase
        List<Fichaje> lista = util.FirebaseService.leerFichajes();
        ObservableList<Fichaje> datos = FXCollections.observableArrayList(lista);
        tablaFichajes.setItems(datos);
    }

  

    private void cargarNombres() {
        // Leer empleados
        try {
            URL url = new URL("https://gestorobras-db4ac-default-rtdb.europe-west1.firebasedatabase.app/empleados.json");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            JsonObject data = JsonParser.parseReader(reader).getAsJsonObject();
            reader.close();

            for (String key : data.keySet()) {
                JsonObject obj = data.getAsJsonObject(key);
                empleadosMap.put(key, obj.get("nombre").getAsString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Leer obras
        try {
            URL url = new URL("https://gestorobras-db4ac-default-rtdb.europe-west1.firebasedatabase.app/obras.json");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            JsonObject data = JsonParser.parseReader(reader).getAsJsonObject();
            reader.close();

            for (String key : data.keySet()) {
                JsonObject obj = data.getAsJsonObject(key);
                obrasMap.put(key, obj.get("nombre").getAsString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
