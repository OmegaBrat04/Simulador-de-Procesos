package Modelo;

// Se creo la clase Semaforo con ayuda de la IA Generativa
public class Semaforo {

    private int valor;

    public Semaforo(int inicial) {
        this.valor = inicial;
    }

    public synchronized void esperarSem() {
        while (valor <= 0) {
            try {
                wait();
            } catch (InterruptedException ignored) {
            }
        }
        valor--;
    }

    public synchronized void signal() {
        valor++;
        notify();
    }
}
