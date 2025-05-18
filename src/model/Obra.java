package model;

public class Obra {
    private int id;
    private String nombre;
    private String estado;

    public Obra(int id, String nombre, String estado) {
        this.id = id;
        this.nombre = nombre;
        this.estado = estado;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getEstado() { return estado; }
}
