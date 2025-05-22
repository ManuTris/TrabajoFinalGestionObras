package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.beans.property.SimpleStringProperty;
import model.Fichaje;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class InformesController {

    private Map<String, String> empleadosMap = new HashMap<>();
    private Map<String, String> obrasMap = new HashMap<>();

    // Filtros
    @FXML private TextField filtroEmpleado;
    @FXML private DatePicker fechaInicio;
    @FXML private DatePicker fechaFin;
    @FXML private HBox contenedorFiltros;
    @FXML private TabPane tabPane; // asegúrate de darle fx:id en el FXML también


    // Fichajes
    @FXML private TableView<Fichaje> tablaFichajes;
    @FXML private TableColumn<Fichaje, String> colFecha;
    @FXML private TableColumn<Fichaje, String> colNombre;
    @FXML private TableColumn<Fichaje, String> colObra;
    @FXML private TableColumn<Fichaje, String> colEntrada;
    @FXML private TableColumn<Fichaje, String> colSalida;
    @FXML private TableColumn<Fichaje, Boolean> colTarde;

    // Errores (pendiente)
    @FXML private TableView<?> tablaErrores;
    @FXML private TableColumn<?, ?> colErrorObra;
    @FXML private TableColumn<?, ?> colDescripcion;
    @FXML private TableColumn<?, ?> colFechaError;

    private List<Fichaje> todosFichajes = new ArrayList<>();

    @FXML
    public void initialize() {
    	tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
    	    if (newTab != null && "Fichajes".equals(newTab.getText())) {
    	        contenedorFiltros.setVisible(true);
    	        contenedorFiltros.setManaged(true);
    	    } else {
    	        contenedorFiltros.setVisible(false);
    	        contenedorFiltros.setManaged(false);
    	    }
    	});

        cargarNombres();

        // Configurar columnas
        colFecha.setCellValueFactory(cellData -> {
            String rawFecha = cellData.getValue().getFecha();
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
        colTarde.setCellFactory(column -> new TableCell<Fichaje, Boolean>() {
            @Override
            protected void updateItem(Boolean value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || value == null) {
                    setText(null);
                    setStyle("");
                } else if (value) {
                    setText("🔴 Tarde");
                    setStyle("-fx-background-color: #ffcccc; -fx-text-fill: red; -fx-font-weight: bold;");
                } else {
                    setText("🟢 Puntual");
                    setStyle("-fx-background-color: #ccffcc; -fx-text-fill: green; -fx-font-weight: bold;");
                }
            }
        });

        // Cargar y mostrar datos
        todosFichajes = util.FirebaseService.leerFichajes();
        tablaFichajes.setItems(FXCollections.observableArrayList(todosFichajes));
    }

    @FXML
    private void filtrarFichajes() {
        String texto = filtroEmpleado.getText().toLowerCase();
        LocalDate desde = fechaInicio.getValue();
        LocalDate hasta = fechaFin.getValue();

        List<Fichaje> filtrados = new ArrayList<>();

        for (Fichaje f : todosFichajes) {
            String nombre = empleadosMap.getOrDefault(f.getEmpleadoId(), "").toLowerCase();
            LocalDate fecha;
            try {
                fecha = LocalDate.parse(f.getFecha());
            } catch (Exception e) {
                continue;
            }

            boolean coincideEmpleado = texto.isEmpty() || nombre.contains(texto) || f.getEmpleadoId().toLowerCase().contains(texto);
            boolean dentroRango = (desde == null || !fecha.isBefore(desde)) && (hasta == null || !fecha.isAfter(hasta));

            if (coincideEmpleado && dentroRango) {
                filtrados.add(f);
            }
        }

        tablaFichajes.setItems(FXCollections.observableArrayList(filtrados));
    }

    @FXML
    private void resetFiltros() {
        filtroEmpleado.clear();
        fechaInicio.setValue(null);
        fechaFin.setValue(null);
        tablaFichajes.setItems(FXCollections.observableArrayList(todosFichajes));
    }

    private void cargarNombres() {
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

