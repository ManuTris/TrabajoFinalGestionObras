package controller;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
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
    @FXML private TableColumn<Fichaje, String> colFechaFichaje;
    @FXML private TableColumn<Fichaje, String> colNombre;
    @FXML private TableColumn<Fichaje, String> colObra;
    @FXML private TableColumn<Fichaje, String> colEntrada;
    @FXML private TableColumn<Fichaje, String> colSalida;
    @FXML private TableColumn<Fichaje, Boolean> colTarde;

    // Errores de obra (todavía no implementado)
    @FXML private TableView<?> tablaErrores;
    @FXML private TableColumn<?, ?> colErrorObra;
    @FXML private TableColumn<?, ?> colDescripcion;
    @FXML private TableColumn<?, ?> colFechaError;

    @FXML
    public void initialize() {
        cargarNombres(); 

        // Mostrar solo la fecha sin la hora
        colFechaFichaje.setCellValueFactory(cellData -> {
            String rawFecha = cellData.getValue().getFecha();
            String soloFecha = rawFecha.split("T")[0];
            String[] partes = soloFecha.split("-");
            String formatoCorto = partes[2] + "/" + partes[1] + "/" + partes[0];
            return new SimpleStringProperty(formatoCorto);
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

        // Leer fichajes desde Firebase y cargarlos en la tabla
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
