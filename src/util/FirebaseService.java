package util;



import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import model.Empleado;
import model.Fichaje;
import model.Obra;

public class FirebaseService {

    private static final String BASE_URL = "https://gestorobras-db4ac-default-rtdb.europe-west1.firebasedatabase.app";

    public static void enviarEmpleado(String nombre, String cargo) {
        try {
            URL url = new URL(BASE_URL + "/empleados.json");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setDoOutput(true);

            String json = String.format("{\"nombre\":\"%s\", \"cargo\":\"%s\"}", nombre, cargo);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = json.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            conn.getResponseCode(); // fuerza conexión

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static List<Empleado> leerEmpleados() {
        List<Empleado> empleados = new ArrayList<>();

        try {
            URL url = new URL(BASE_URL + "/empleados.json");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream())
            );

            JsonObject data = JsonParser.parseReader(reader).getAsJsonObject();
            reader.close();
            System.out.println("Cargando empleados desde Firebase...");
            System.out.println(data.toString());

            for (String key : data.keySet()) {
                JsonObject obj = data.getAsJsonObject(key);
                String nombre = obj.get("nombre").getAsString();
                String cargo = obj.get("cargo").getAsString();
                empleados.add(new Empleado(0, nombre, cargo)); // ID 0 por ahora
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return empleados;
    }
    public static boolean verificarCredenciales(String usuario, String contraseña) {
        try {
            @SuppressWarnings("deprecation")
			URL url = new URL(BASE_URL + "/admins.json");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            JsonObject data = JsonParser.parseReader(reader).getAsJsonObject();
            reader.close();

            for (Map.Entry<String, JsonElement> entry : data.entrySet()) {
                JsonObject admin = entry.getValue().getAsJsonObject();
                System.out.println("Cargando admins desde Firebase...");
                System.out.println(data.toString());

             
                if (admin.has("usuario") && admin.has("contraseña")) {
                    String nombreUsuario = admin.get("usuario").getAsString();
                    String clave = admin.get("contraseña").getAsString();

                    if (usuario.equals(nombreUsuario) && contraseña.equals(clave)) {
                        return true;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }


    public static List<Obra> leerObras() {
        List<Obra> obras = new ArrayList<>();

        try {
            URL url = new URL(BASE_URL + "/obras.json");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream())
            );

            JsonObject data = JsonParser.parseReader(reader).getAsJsonObject();
            reader.close();
           


            for (String key : data.keySet()) {
                JsonObject obj = data.getAsJsonObject(key);
                String nombre = obj.get("nombre").getAsString();
                String estado = obj.get("estado").getAsString();
                obras.add(new Obra(0, nombre, estado)); // ID 0 por ahora
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return obras;
    }
    
    
    
    public static List<Fichaje> leerFichajes() {
        List<Fichaje> lista = new ArrayList<>();

        try {
            URL url = new URL("https://gestorobras-db4ac-default-rtdb.europe-west1.firebasedatabase.app/fichajes.json");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
            reader.close();

            for (Map.Entry<String, JsonElement> empleadoEntry : root.entrySet()) {
                String empleadoId = empleadoEntry.getKey();
                JsonObject fichajesPorFecha = empleadoEntry.getValue().getAsJsonObject();

                for (Map.Entry<String, JsonElement> fichaEntry : fichajesPorFecha.entrySet()) {
                    JsonObject ficha = fichaEntry.getValue().getAsJsonObject();
                    String obraId = ficha.get("obraId").getAsString();
                    String fecha = ficha.get("fecha").getAsString();
                    String horaEntrada = ficha.get("horaEntrada").getAsString();
                    String horaSalida = ficha.has("horaSalida") ? ficha.get("horaSalida").getAsString() : "";
                    boolean fichadoTarde = ficha.get("fichadoTarde").getAsBoolean();

                    lista.add(new Fichaje(empleadoId, obraId, horaEntrada, horaSalida, fichadoTarde, fecha));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }
    
    public static void guardarObraEnFirebase(Obra obra) {
        try {
            URL url = new URL(BASE_URL + "/obras/" + obra.getId() + ".json");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setDoOutput(true);

            String json = String.format("{\"nombre\":\"%s\", \"estado\":\"%s\"}",
                                        obra.getNombre(), obra.getEstado());

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = json.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            conn.getResponseCode();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void crearEmpleadoCompleto(String uid, String nombre, String cargo) {
        try {
            // 1. empleados/
            String empleadoJson = String.format("{\"nombre\":\"%s\", \"cargo\":\"%s\"}", nombre, cargo);
            enviarPUT("/empleados/" + uid + ".json", empleadoJson);

            // 2. usuarios/uid/perfil
            String perfilJson = """
                {
                    "direccion": "",
                    "cp": "",
                    "poblacion": "",
                    "telefono": "",
                    "foto": ""
                }
            """;
            enviarPUT("/usuarios/" + uid + "/perfil.json", perfilJson);

            // 3. horarios/
            String horariosJson = """
                {
                    "Lunes": "-",
                    "Martes": "-",
                    "Miercoles": "-",
                    "Jueves": "-",
                    "Viernes": "-",
                    "Sabado": "-",
                    "Domingo": "-"
                }
            """;
            enviarPUT("/horarios/" + uid + ".json", horariosJson);

            // 4. fichajes/
            enviarPUT("/fichajes/" + uid + ".json", "{}");

            System.out.println("✅ Empleado creado correctamente en Firebase.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método auxiliar PUT genérico
    private static void enviarPUT(String path, String jsonBody) throws Exception {
        URL url = new URL(BASE_URL + path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("PUT");
        conn.setRequestProperty("Content-Type", "application/json; utf-8");
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonBody.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        conn.getResponseCode();
        conn.disconnect();
    }
}

