package util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

import com.google.gson.*;
import model.Empleado;
import model.Fichaje;
import model.Obra;

public class FirebaseService {

    private static final String BASE_URL = "https://gestorobras-db4ac-default-rtdb.europe-west1.firebasedatabase.app";

    // ============================================================================
    // AUTENTICACIÓN
    // ============================================================================

    public static boolean verificarCredenciales(String usuario, String contraseña) {
        try {
            URL url = new URL(BASE_URL + "/admins.json");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            JsonObject data = JsonParser.parseReader(reader).getAsJsonObject();
            reader.close();

            for (Map.Entry<String, JsonElement> entry : data.entrySet()) {
                JsonObject admin = entry.getValue().getAsJsonObject();
                if (admin.has("usuario") && admin.has("contraseña")) {
                    String nombreUsuario = admin.get("usuario").getAsString();
                    String clave = admin.get("contraseña").getAsString();

                    if (usuario.equals(nombreUsuario) && contraseña.equals(clave)) {
                        util.Sesion.adminKey = entry.getKey();   // Guardar sesión activa
                        util.Sesion.usuario = nombreUsuario;
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean cambiarContrasenaAdmin(String nuevaContrasena) {
        try {
            if (util.Sesion.adminKey == null) return false;

            String path = "/admins/" + util.Sesion.adminKey + "/contraseña.json";
            URL url = new URL(BASE_URL + path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(("\"" + nuevaContrasena + "\"").getBytes("utf-8"));
            }

            return conn.getResponseCode() == 200;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // ============================================================================
    // EMPLEADOS
    // ============================================================================

    public static void enviarEmpleado(String nombre, String cargo) {
        try {
            URL url = new URL(BASE_URL + "/empleados.json");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setDoOutput(true);

            String json = String.format("{\"nombre\":\"%s\", \"cargo\":\"%s\"}", nombre, cargo);
            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes("utf-8"));
            }

            conn.getResponseCode();
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

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            JsonObject data = JsonParser.parseReader(reader).getAsJsonObject();
            reader.close();

            for (String uid : data.keySet()) {
                JsonObject obj = data.getAsJsonObject(uid);
                empleados.add(new Empleado(uid, obj.get("nombre").getAsString(), obj.get("cargo").getAsString()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return empleados;
    }

    public static Map<String, String> obtenerEmpleados() {
        Map<String, String> empleados = new HashMap<>();
        try {
            URL url = new URL(BASE_URL + "/empleados.json");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            JsonObject data = JsonParser.parseReader(reader).getAsJsonObject();
            reader.close();

            for (String uid : data.keySet()) {
                JsonObject obj = data.getAsJsonObject(uid);
                empleados.put(uid, obj.get("nombre").getAsString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return empleados;
    }

    public static void crearEmpleadoCompleto(String uid, String nombre, String cargo) {
        try {
            enviarPUT("/empleados/" + uid + ".json",
                    String.format("{\"nombre\":\"%s\", \"cargo\":\"%s\"}", nombre, cargo));

            enviarPUT("/usuarios/" + uid + "/perfil.json", """
                {
                    "direccion": "",
                    "cp": "",
                    "poblacion": "",
                    "telefono": "",
                    "foto": ""
                }
            """);

            enviarPUT("/horarios/" + uid + ".json", """
                {
                    "Lunes": "-", "Martes": "-", "Miercoles": "-",
                    "Jueves": "-", "Viernes": "-", "Sabado": "-", "Domingo": "-"
                }
            """);

            enviarPUT("/fichajes/" + uid + ".json", "{}");

            System.out.println("✅ Empleado creado correctamente en Firebase.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ============================================================================
    // OBRAS
    // ============================================================================

    public static List<Obra> leerObras() {
        List<Obra> obras = new ArrayList<>();
        try {
            URL url = new URL(BASE_URL + "/obras.json");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            JsonObject data = JsonParser.parseReader(reader).getAsJsonObject();
            reader.close();

            for (String id : data.keySet()) {
                JsonObject obj = data.getAsJsonObject(id);
                obras.add(new Obra(id, obj.get("nombre").getAsString(), obj.get("estado").getAsString()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obras;
    }

    public static Map<String, String> obtenerObras() {
        Map<String, String> obras = new HashMap<>();
        try {
            URL url = new URL(BASE_URL + "/obras.json");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            JsonObject data = JsonParser.parseReader(reader).getAsJsonObject();
            reader.close();

            for (String id : data.keySet()) {
                JsonObject obj = data.getAsJsonObject(id);
                obras.put(id, obj.get("nombre").getAsString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obras;
    }

    public static void guardarObraEnFirebase(Obra obra) {
        try {
            URL url = new URL(BASE_URL + "/obras/" + obra.getId() + ".json");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setDoOutput(true);

            String json = String.format("{\"nombre\":\"%s\", \"estado\":\"%s\"}", obra.getNombre(), obra.getEstado());
            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes("utf-8"));
            }

            conn.getResponseCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ============================================================================
    // FICHAJES
    // ============================================================================

    public static List<Fichaje> leerFichajes() {
        List<Fichaje> lista = new ArrayList<>();
        try {
            URL url = new URL(BASE_URL + "/fichajes.json");
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
                    lista.add(new Fichaje(
                        empleadoId,
                        ficha.get("obraId").getAsString(),
                        ficha.get("horaEntrada").getAsString(),
                        ficha.has("horaSalida") ? ficha.get("horaSalida").getAsString() : "",
                        ficha.get("fichadoTarde").getAsBoolean(),
                        ficha.get("fecha").getAsString()
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    // ============================================================================
    // HORARIOS
    // ============================================================================

    public static Map<String, String> obtenerHorariosPorUID(String uid) {
        Map<String, String> horarios = new HashMap<>();
        try {
            URL url = new URL(BASE_URL + "/horarios/" + uid + ".json");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            JsonObject data = JsonParser.parseReader(reader).getAsJsonObject();
            reader.close();

            for (Map.Entry<String, JsonElement> entry : data.entrySet()) {
                horarios.put(entry.getKey(), entry.getValue().getAsString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return horarios;
    }

    // ============================================================================
    // ASIGNACIONES
    // ============================================================================

    public static Map<String, String> obtenerAsignaciones() {
        Map<String, String> asignaciones = new HashMap<>();
        try {
            URL url = new URL(BASE_URL + "/asignaciones.json");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            JsonObject data = JsonParser.parseReader(reader).getAsJsonObject();
            reader.close();

            for (String uid : data.keySet()) {
                asignaciones.put(uid, data.get(uid).getAsString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return asignaciones;
    }

    public static void asignarObraAEmpleado(String uid, String obraId) {
        try {
            URL url = new URL(BASE_URL + "/asignaciones/" + uid + ".json");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setDoOutput(true);

            String json = "\"" + obraId + "\"";
            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes("utf-8"));
            }

            conn.getResponseCode();
            conn.disconnect();

            System.out.println("✅ Obra asignada a empleado: " + uid + " → " + obraId);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ============================================================================
    // UTILIDAD
    // ============================================================================

    private static void enviarPUT(String path, String jsonBody) throws Exception {
        URL url = new URL(BASE_URL + path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("PUT");
        conn.setRequestProperty("Content-Type", "application/json; utf-8");
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(jsonBody.getBytes("utf-8"));
        }

        conn.getResponseCode();
        conn.disconnect();
    }
}
