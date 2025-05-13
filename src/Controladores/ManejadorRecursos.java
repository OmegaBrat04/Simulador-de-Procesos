package Controladores;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class ManejadorRecursos {
    private boolean cpuOcupada;
    private int memoriaDisponible;
    private final int tamaniop = 256;
    private final int totalM = 4096 / tamaniop;
    private boolean[] marcos = new boolean[totalM];
    private Map<Integer, String> asignacionMarcos = new HashMap<>();

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

    // Metodo mejorado por la IA Generativa
    public List<Integer> solicitarMemoria(String pid, int cantidad) {
        int paginasNecesarias = (int) Math.ceil((double) cantidad / tamaniop);
        List<Integer> asignados = new ArrayList<>();
        for (int i = 0; i < marcos.length && asignados.size() < paginasNecesarias; i++) {
            if (!marcos[i]) {
                marcos[i] = true;
                asignados.add(i);
                asignacionMarcos.put(i, pid);
            }

        }

        if (asignados.size() == paginasNecesarias) {
            memoriaDisponible -= paginasNecesarias * tamaniop;
            return asignados;
        } else {
            for (int idx : asignados)
                marcos[idx] = false;
            System.out.println("Memoria insuficiente para proceso " + pid);
            return null;
        }
    }

    public void liberarPaginas(List<Integer> paginas) {
        for (int idx : paginas) {
            marcos[idx] = false;
            asignacionMarcos.remove(idx);
        }
        memoriaDisponible += paginas.size() * tamaniop;
    }

    public int getMemoriaDisponible() {
        return memoriaDisponible;
    }

    public boolean isCpuOcupada() {
        return cpuOcupada;
    }

    public void mostrarMarcosUsados() {
        System.out.println("=== Marcos de Memoria en uso ===");
        if (asignacionMarcos.isEmpty()) {
            System.out.println("No hay marcos asignados.");
            return;
        }
        for (Map.Entry<Integer, String> entrada : asignacionMarcos.entrySet()) {
            System.out.println("Marco #" + entrada.getKey() + " asignado al proceso PID " + entrada.getValue());
        }
    }

}
