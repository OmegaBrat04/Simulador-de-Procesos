package Controladores;
import java.util.HashMap;
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

        boolean memOk = recursos.solicitarMemoria(String.valueOf(pid), memoriaMB);
        if (memOk)
            proceso.setMemoriaAsignada(memoriaMB);

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
            recursos.liberarMemoria(memoriaMB);
            proceso.setMemoriaAsignada(0);
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

        if (proceso.getMemoriaAsignada() > 0) {
            recursos.liberarMemoria(proceso.getMemoriaAsignada());
            proceso.setMemoriaAsignada(0);
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
        for (PCB pcb : procesos.values()) {
            System.out.println(pcb);
        }
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

}
