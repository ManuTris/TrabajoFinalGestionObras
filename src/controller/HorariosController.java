package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.*;
import javafx.beans.property.SimpleStringProperty;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class HorariosController {

    @FXML private ComboBox<String> comboEmpleados;
    @FXML private TableView<Map.Entry<String, String>> tablaHorarios;
    @FXML private TableColumn<Map.Entry<String, String>, String> colDia;
    @FXML private TableColumn<Map.Entry<String, String>, String> colHorario;

    private Map<String, String> empleadosMap = new HashMap<>(); // UID → nombre

    @FXML
    public void initialize() {
        System.out.println("Inicializando vista de horarios...");
        cargarEmpleados();

        colDia.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getKey()));
        colHorario.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getValue()));
    }

    private void cargarEmpleados() {
        try {
            URL url = new URL("https://gestorobras-db4ac-default-rtdb.europe-west1.firebasedatabase.app/empleados.json");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            JsonObject data = JsonParser.parseReader(reader).getAsJsonObject();
            reader.close();

            for (String uid : data.keySet()) {
                JsonObject obj = data.getAsJsonObject(uid);
                String nombre = obj.get("nombre").getAsString();
                empleadosMap.put(uid, nombre);
                comboEmpleados.getItems().add(nombre + " (" + uid + ")");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void cargarHorario() {
        String seleccionado = comboEmpleados.getValue();
        if (seleccionado == null) return;

        String uid = seleccionado.substring(seleccionado.indexOf("(") + 1, seleccionado.indexOf(")"));

        try {
            URL url = new URL("https://gestorobras-db4ac-default-rtdb.europe-west1.firebasedatabase.app/horarios/" + uid + ".json");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            JsonObject data = JsonParser.parseReader(reader).getAsJsonObject();
            reader.close();

            ObservableList<Map.Entry<String, String>> listaHorarios = FXCollections.observableArrayList();
            for (Map.Entry<String, com.google.gson.JsonElement> entrada : data.entrySet()) {
                String dia = entrada.getKey();
                String horario = entrada.getValue().getAsString();
                listaHorarios.add(new AbstractMap.SimpleEntry<>(dia, horario));
            }

            tablaHorarios.setItems(listaHorarios);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
