package Controladores;
import Modelo.BufferCompartido;
import Modelo.Semaforo;

public class ProductorConsumidor {

    private final BufferCompartido buffer;
    private final Semaforo mutex = new Semaforo(1);
    private final Semaforo lleno;
    private final Semaforo vacio;

    public ProductorConsumidor(int capacidadBuffer) {
        this.buffer = new BufferCompartido(capacidadBuffer);
        this.lleno = new Semaforo(0);
        this.vacio = new Semaforo(capacidadBuffer);
    }

    public void producir(int item) {
        vacio.esperarSem();
        mutex.esperarSem();

        buffer.producir(item);
        System.out.println("Productor produjo: " + item);

        mutex.signal();
        lleno.signal();
    }

    public void consumir() {
        lleno.esperarSem();
        mutex.esperarSem();

        int item = buffer.consumir();
        System.out.println("Consumidor consumió: " + item);

        mutex.signal();
        vacio.signal();
    }

    public int getBufferSize() {
        return buffer.tamanio();
    }
}
