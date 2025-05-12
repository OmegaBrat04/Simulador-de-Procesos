package Controladores;
public class ManejadorRecursos {
    private boolean cpuOcupada;
    private int memoriaDisponible;

    public ManejadorRecursos() {
        this.cpuOcupada = false;
        this.memoriaDisponible = 4096; // Son 4GB de memoria
    }

    public boolean solicitarCPU(String pid) {
        if (!cpuOcupada) {
            cpuOcupada = true;
            return true;
        }
        System.out.println("CPU no disponible. Proceso " + pid + " debe esperar.");
        return false;
    }

    public boolean liberarCPU(String pid) {
        if (cpuOcupada) {
            cpuOcupada = false;
            return true;
        }
        return false;
    }

    public boolean solicitarMemoria(String pid, int cantidad) {
        if (cantidad <= memoriaDisponible) {
            memoriaDisponible -= cantidad;
            return true;
        }
        System.out.println("Memoria insuficiente para proceso " + pid + ". Disponible: " + memoriaDisponible + "MB");
        return false;
    }

    public void liberarMemoria(int cantidad) {
        if (cantidad < 0) {
            System.out.println("No se puede liberar memoria negativa.");
            return;
        }
        memoriaDisponible += cantidad;
        if (memoriaDisponible > 4096) {
            memoriaDisponible = 4096;
        }
    }

    public int getMemoriaDisponible() {
        return memoriaDisponible;
    }

    public boolean isCpuOcupada() {
        return cpuOcupada;
    }
}
