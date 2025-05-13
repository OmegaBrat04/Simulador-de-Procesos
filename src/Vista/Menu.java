package Vista;

import java.util.Arrays;
import java.util.Scanner;

import Controladores.Gestion;
import Controladores.ProductorConsumidor;
import Modelo.PCB;

public class Menu {
    private Gestion gestor;
    private Scanner scanner;
    private ProductorConsumidor pc;

    public Menu(Gestion gestor) {
        this.gestor = gestor;
        scanner = new Scanner(System.in);
        pc = new ProductorConsumidor(5); // Tamaño del buffer compartido
    }

    public void iniciar() {
        while (true) {
            System.out.println("Simulador de Gestión de Procesos - Línea de Comandos");
            System.out.println("Comandos disponibles:");
            System.out.println("  crear <prioridad>                - Crea un proceso con la prioridad dada");
            System.out.println("  solicitar <pid> <si/no> <memMB>  - Solicita recursos para el proceso");
            System.out.println("  suspender <pid>                  - Suspende el proceso con el ID dado");
            System.out.println("  reanudar <pid>                   - Reanuda el proceso con el ID dado");
            System.out.println("  producir <pid> <valor>           - Produce un valor en el buffer");
            System.out.println("  consumir <pid>                   - Consume un valor del buffer");
            System.out.println("  terminar <pid>                   - Termina el proceso con el ID dado");
            System.out.println("  forzar <pid> <causa>             - Termina el proceso con el ID dado forzadamente");
            System.out.println("  listar                           - Lista todos los procesos");
            System.out.println("  planificar                       - Ejecuta el planificador");
            System.out.println("  avanzar                          - Avance paso a paso");
            System.out.println("  salir                            - Finaliza el simulador");
            System.out.println();
            System.out.print("> ");
            String linea = scanner.nextLine().trim();
            // Idea de usar regex para separar los comandos y sus argumentos fue dada por la
            // IA Generativa
            if (!linea.isEmpty()) {
                System.out.println("\n----------------------------------------");
                System.out.println("Ejecutando comando: >>> " + linea);
                System.out.println("----------------------------------------\n");
            }
            String[] partes = linea.split("\\s+");

            if (partes.length == 0)
                continue;

            String comando = partes[0].toLowerCase();

            switch (comando) {
                case "crear":
                    if (partes.length != 2) {
                        System.out.println("Uso: crear <prioridad>");
                        break;
                    }
                    try {
                        int prioridad = Integer.parseInt(partes[1]);
                        gestor.crearProceso(prioridad);
                    } catch (NumberFormatException e) {
                        System.out.println("Prioridad inválida.");
                    }
                    break;

                case "suspender":
                    if (partes.length != 2) {
                        System.out.println("Uso: suspender <pid>");
                        break;
                    }
                    try {
                        int pid = Integer.parseInt(partes[1]);
                        if (!gestor.suspenderProceso(pid)) {
                            System.out.println("No se pudo suspender el proceso.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("PID inválido.");
                    }
                    break;

                case "reanudar":
                    if (partes.length != 2) {
                        System.out.println("Uso: reanudar <pid>");
                        break;
                    }
                    try {
                        int pid = Integer.parseInt(partes[1]);
                        if (!gestor.reanudarProceso(pid)) {
                            System.out.println("No se pudo reanudar el proceso.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("PID inválido.");
                    }
                    break;

                case "terminar":
                    if (partes.length != 2) {
                        System.out.println("Uso: terminar <pid>");
                        break;
                    }
                    try {
                        int pid = Integer.parseInt(partes[1]);
                        if (!gestor.terminarProceso(pid)) {
                            System.out.println("No se pudo terminar el proceso.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("PID inválido.");
                    }
                    break;

                case "solicitar":
                    if (partes.length != 4) {
                        System.out.println("Uso: solicitar <pid> <cpu:(si/no)> <memoriaMB>");
                        break;
                    }
                    try {
                        int pid = Integer.parseInt(partes[1]);
                        boolean cpu = partes[2].equalsIgnoreCase("si");
                        int mem = Integer.parseInt(partes[3]);
                        gestor.solicitarRecursos(pid, cpu, mem);
                    } catch (NumberFormatException e) {
                        System.out.println("Ingresa valores validos.");
                    }
                    break;

                case "planificar":
                    gestor.planificar();
                    break;

                case "listar":
                    gestor.listarProcesos();
                    break;

                case "producir":
                    if (partes.length != 3) {
                        System.out.println("Uso: producir <pid> <valor>");
                        break;
                    }
                    try {
                        int pid = Integer.parseInt(partes[1]);
                        int valor = Integer.parseInt(partes[2]);

                        if (!gestor.existeProceso(pid)) {
                            System.out.println("Proceso no encontrado.");
                            break;
                        }

                        PCB proceso = gestor.obtenerProceso(pid);
                        if (proceso.getEstado() == PCB.Estado.LISTO || proceso.getEstado() == PCB.Estado.EJECUTANDO) {
                            // Usamos un hilo separado para la producción
                            new Thread(() -> pc.producir(valor)).start();
                        } else {
                            System.out.println("El proceso no está en un estado válido para producir.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("PID o valor inválido.");
                    }
                    break;

                case "consumir":
                    if (partes.length != 2) {
                        System.out.println("Uso: consumir <pid>");
                        break;
                    }
                    try {
                        int pid = Integer.parseInt(partes[1]);

                        if (!gestor.existeProceso(pid)) {
                            System.out.println("Proceso no encontrado.");
                            break;
                        }

                        PCB proceso = gestor.obtenerProceso(pid);
                        if (proceso.getEstado() == PCB.Estado.LISTO || proceso.getEstado() == PCB.Estado.EJECUTANDO) {
                            new Thread(() -> pc.consumir()).start();
                        } else {
                            System.out.println("El proceso no está en un estado válido para consumir.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("PID inválido.");
                    }
                    break;

                case "forzar":
                    if (partes.length < 3) {
                        System.out.println("Uso: forzar <pid> <causa>");
                        break;
                    }
                    try {
                        int pid = Integer.parseInt(partes[1]);
                        String causa = String.join(" ", Arrays.copyOfRange(partes, 2, partes.length));
                        if (!gestor.terminarProcesoForzadamente(pid, causa)) {
                            System.out.println("No se pudo terminar forzadamente el proceso.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("PID inválido.");
                    }
                    break;
                case "avanzar":
                    gestor.avanzarSimulacion();
                    break;

                case "salir":
                    System.out.println("Finalizando...");
                    return;

                default:
                    System.out.println("No se reconoce el comando.");
            }
            System.out.println();
        }
    }
}
