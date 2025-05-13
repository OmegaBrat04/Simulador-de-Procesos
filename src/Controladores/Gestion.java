package Controladores;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import Interfaces.Planificador;
import Modelo.PCB;

public class Gestion {
    private Map<Integer, PCB> procesos;
    private ManejadorRecursos recursos;
    private Planificador planificador;

    public Gestion() {
        procesos = new HashMap<>();
        recursos = new ManejadorRecursos();
    }

    public void setPlanificador(Planificador planificador) {
        this.planificador = planificador;
    }

    // Codigo dado por la IA Generativa
    public void planificar() {
        PCB siguiente = planificador.seleccionarProceso(procesos.values().stream().toList());
        if (siguiente != null) {
            siguiente.setEstado(PCB.Estado.EJECUTANDO);
            System.out.println("Proceso en ejecución: " + siguiente.getPid());
        } else {
            System.out.println("No hay procesos listos para ejecutar.");
        }
    }

    // Creamos un nuevo proceso con una prioridad dada
    public PCB crearProceso(int prioridad) {
        PCB proceso = new PCB(prioridad);
        proceso.setEstado(PCB.Estado.LISTO);
        procesos.put(proceso.getPid(), proceso);
        System.out.println("Proceso creado: " + proceso);
        return proceso;
    }

    // Metodo para suspender un proceso
    public boolean suspenderProceso(int pid) {
        PCB proceso = procesos.get(pid);
        if (proceso != null && proceso.getEstado() != PCB.Estado.TERMINADO) {
            proceso.setEstado(PCB.Estado.ESPERANDO);
            if (proceso.isCpuAsignada()) {
                proceso.setCpuAsignada(false);
                recursos.liberarCPU(String.valueOf(pid));
                System.out.println("CPU liberada por el proceso " + pid);
            }
            System.out.println("Proceso suspendido: " + proceso.getPid());
            return true;
        }
        return false;
    }

    // Metodo para reanudar un proceso
    public boolean reanudarProceso(int pid) {
        PCB proceso = procesos.get(pid);
        if (proceso != null && proceso.getEstado() == PCB.Estado.ESPERANDO) {
            proceso.setEstado(PCB.Estado.LISTO);
            System.out.println("Proceso reanudado: " + proceso.getPid());
            return true;
        }
        return false;
    }

    public boolean solicitarRecursos(int pid, boolean necesitaCPU, int memoriaMB) {
        PCB proceso = procesos.get(pid);
        if (proceso == null || proceso.getEstado() == PCB.Estado.TERMINADO)
            return false;

        boolean cpuOk = true;
        if (necesitaCPU) {
            cpuOk = recursos.solicitarCPU(String.valueOf(pid));
            if (cpuOk)
                proceso.setCpuAsignada(true);
        }

        List<Integer> paginasAsignadas = recursos.solicitarMemoria(String.valueOf(pid), memoriaMB);
        boolean memOk = paginasAsignadas != null;

        if (memOk) {
            proceso.setMemoriaAsignada(memoriaMB);
            proceso.setPaginasAsignadas(paginasAsignadas); 
        }

        if (cpuOk && memOk) {
            System.out.println("Recursos asignados al proceso " + pid);
            return true;
        }

        // En caso de fallo, liberamos lo que se haya asignado
        if (cpuOk) {
            recursos.liberarCPU(String.valueOf(pid));
            proceso.setCpuAsignada(false);
        }
        if (memOk) {
            recursos.liberarPaginas(paginasAsignadas);
            proceso.setMemoriaAsignada(0);
            proceso.setPaginasAsignadas(null);
        }

        return false;
    }

    public void liberarRecursos(int pid) {
        PCB proceso = procesos.get(pid);
        if (proceso == null)
            return;

        if (proceso.isCpuAsignada()) {
            recursos.liberarCPU(String.valueOf(pid));
            proceso.setCpuAsignada(false);
        }

        if (proceso.getMemoriaAsignada() > 0 && proceso.getPaginasAsignadas() != null) {
            recursos.liberarPaginas(proceso.getPaginasAsignadas());
            proceso.setMemoriaAsignada(0);
            proceso.setPaginasAsignadas(null);
        }

    }

    // Metodo para terminar un proceso
    public boolean terminarProceso(int pid) {
        PCB proceso = procesos.get(pid);
        if (proceso != null && proceso.getEstado() != PCB.Estado.TERMINADO) {
            proceso.setEstado(PCB.Estado.TERMINADO);
            liberarRecursos(pid);
            proceso.setCausaTerminacion("Finalización normal");
            System.out.println("Proceso terminado: " + proceso.getPid());
            return true;
        }
        return false;
    }

    public boolean terminarProcesoForzadamente(int pid, String causa) {
        PCB proceso = procesos.get(pid);
        if (proceso == null)
            return false;
        liberarRecursos(pid);
        proceso.setEstado(PCB.Estado.TERMINADO);
        proceso.setCausaTerminacion("Terminación forzada: " + causa);
        System.out.println("Proceso " + pid + " terminado forzadamente. Causa: " + causa);
        return true;
    }

    // Obtenemos todos los procesos
    public void listarProcesos() {
        System.out.println("=== Procesos LISTOS ===");
        procesos.values().stream()
                .filter(p -> p.getEstado() == PCB.Estado.LISTO)
                .forEach(System.out::println);

        System.out.println("\n=== Procesos EN ESPERA ===");
        procesos.values().stream()
                .filter(p -> p.getEstado() == PCB.Estado.ESPERANDO)
                .forEach(System.out::println);

        System.out.println("\n=== Proceso EN EJECUCIÓN ===");
        procesos.values().stream()
                .filter(p -> p.getEstado() == PCB.Estado.EJECUTANDO)
                .forEach(System.out::println);
    }

    public Set<Integer> getPIDs() {
        return procesos.keySet();
    }

    public boolean existeProceso(int pid) {
        return procesos.containsKey(pid);
    }

    public PCB obtenerProceso(int pid) {
        return procesos.get(pid);
    }

    public void avanzarSimulacion() {
        planificar();
        mostrarEstadoSistema();
    }

    public void mostrarEstadoSistema() {
        listarProcesos(); 
        System.out.println("\n=== Procesos TERMINADOS ===");
        procesos.values().stream()
                .filter(p -> p.getEstado() == PCB.Estado.TERMINADO)
                .forEach(System.out::println);

        System.out.println("\n=== Estado del Procesador ===");
        boolean ocupado = procesos.values().stream().anyMatch(p -> p.getEstado() == PCB.Estado.EJECUTANDO);
        System.out.println(ocupado ? "En uso" : "Libre");

        System.out.println("\n=== Memoria Disponible ===");
        System.out.println(recursos.getMemoriaDisponible() + " MB");

        recursos.mostrarMarcosUsados(); 
    }

}
