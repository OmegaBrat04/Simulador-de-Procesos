package Interfaces;
import java.util.List;

import Modelo.*;
// Idea de usar interfaces fue dada por la IA Generativa
public interface Planificador {
    PCB seleccionarProceso(List<PCB> listaProcesos);
}
