package Modelo;

import java.util.ArrayList;
import java.util.List;

public class PCB {
    private String causaTerminacion;

    public enum Estado {
        NUEVO, LISTO, EJECUTANDO, ESPERANDO, TERMINADO
    }

    private static int contador = 1;
    private final int pid;
    private Estado estado;
    private int prioridad;
    private boolean cpuAsignada;
    private int memoriaAsignada;

    public PCB(int prioridad) {
        this.pid = contador++; // Para generar un PID unico
        this.estado = Estado.NUEVO;
        this.prioridad = prioridad;
        this.cpuAsignada = false;
        this.memoriaAsignada = 0;
    }

    // Getters y setters
    public int getPid() {
        return pid;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public int getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(int prioridad) {
        this.prioridad = prioridad;
    }

    public boolean isCpuAsignada() {
        return cpuAsignada;
    }

    public void setCpuAsignada(boolean cpuAsignada) {
        this.cpuAsignada = cpuAsignada;
    }

    public int getMemoriaAsignada() {
        return memoriaAsignada;
    }

    public void setMemoriaAsignada(int memoriaAsignada) {
        this.memoriaAsignada = memoriaAsignada;
    }

    public String getCausaTerminacion() {
        return causaTerminacion;
    }

    public void setCausaTerminacion(String causa) {
        this.causaTerminacion = causa;
    }

    private List<Integer> paginasAsignadas = new ArrayList<>();

    public List<Integer> getPaginasAsignadas() {
        return paginasAsignadas;
    }

    public void setPaginasAsignadas(List<Integer> paginas) {
        this.paginasAsignadas = paginas;
    }

    @Override
    public String toString() {
        return "PCB{" +
                "PID=" + pid +
                ", Estado=" + estado +
                ", Prioridad=" + prioridad +
                ", CPU=" + (cpuAsignada ? "Asignada" : "Libre") +
                ", Memoria=" + memoriaAsignada + "MB" +
                ", Marcos/Páginas=" + (paginasAsignadas != null ? paginasAsignadas.toString() : "[]") +
                ", CausaTerminación=" + (causaTerminacion != null ? causaTerminacion : "N/A") +
                '}';
    }
}
