package Planificadores;

import java.util.List;

import Interfaces.Planificador;
import Modelo.PCB;

public class PlanificadorPrioridades implements Planificador {
    @Override
    public PCB seleccionarProceso(List<PCB> listaProcesos) {
        PCB mejor = null;
        for (PCB proceso : listaProcesos) {
            if (proceso.getEstado() == PCB.Estado.LISTO) {
                if (mejor == null || proceso.getPrioridad() < mejor.getPrioridad()) {
                    mejor = proceso;
                }
            }
        }
        return mejor;
    }

}
