package model;

public class Empleado {
    private String id;
    private String nombre;
    private String cargo;

    public Empleado(String id, String nombre, String cargo) {
        this.id = id;
        this.nombre = nombre;
        this.cargo = cargo;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCargo() {
        return cargo;
    }
}
