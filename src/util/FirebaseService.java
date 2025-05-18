package util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import model.Empleado;
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
            URL url = new URL(BASE_URL + "/usuarios.json");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            JsonObject data = JsonParser.parseReader(reader).getAsJsonObject();
            reader.close();

            for (String key : data.keySet()) {
                JsonObject obj = data.getAsJsonObject(key);
                String u = obj.get("usuario").getAsString();
                String p = obj.get("contraseña").getAsString();

                if (u.equals(usuario) && p.equals(contraseña)) {
                    return true;
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

}

