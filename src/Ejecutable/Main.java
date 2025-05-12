package Ejecutable;
import java.util.Scanner;

import Controladores.Gestion;
import Planificadores.PlanificadorFCFS;
import Planificadores.PlanificadorPrioridades;
import Vista.Menu;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        System.out.println("Seleccione algoritmo de planificación: ");
        System.out.println("1 - FCFS");
        System.out.println("2 - Por prioridades");
        System.out.print("Opción: ");
        int opcion = Integer.parseInt(scanner.nextLine());

        Gestion gestor = new Gestion();
        if (opcion == 1) {
            gestor.setPlanificador(new PlanificadorFCFS());
        } else {
            gestor.setPlanificador(new PlanificadorPrioridades());
        }
        Menu interfaz = new Menu(gestor);
        interfaz.iniciar();
    }
}
