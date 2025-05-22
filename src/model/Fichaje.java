package model;

public class Fichaje {
    private String fecha;
    private String empleadoId;
    private String obraId;
    private String horaEntrada;
    private String horaSalida;
    private boolean fichadoTarde;

    public Fichaje(String empleadoId, String obraId, String horaEntrada, String horaSalida, boolean fichadoTarde, String fecha) {
        this.empleadoId = empleadoId;
        this.obraId = obraId;
        this.horaEntrada = horaEntrada;
        this.horaSalida = horaSalida;
        this.fichadoTarde = fichadoTarde;
        this.fecha = fecha;
    }

    public String getFecha() {
        return fecha;
    }

    public String getEmpleadoId() {
        return empleadoId;
    }

    public String getObraId() {
        return obraId;
    }

    public String getHoraEntrada() {
        return horaEntrada;
    }

    public String getHoraSalida() {
        return horaSalida;
    }

    public boolean isFichadoTarde() {
        return fichadoTarde;
    }
}
