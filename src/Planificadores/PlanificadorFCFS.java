package Planificadores;

import java.util.List;

import Interfaces.Planificador;
import Modelo.PCB;

public class PlanificadorFCFS implements Planificador {
    @Override
    public PCB seleccionarProceso(List<PCB> listaProcesos) {
        for (PCB proceso : listaProcesos) {
            if (proceso.getEstado() == PCB.Estado.LISTO) {
                return proceso;
            }
        }
        return null;
    }

}
