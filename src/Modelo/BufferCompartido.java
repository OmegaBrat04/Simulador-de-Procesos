package Modelo;
import java.util.LinkedList;
import java.util.Queue;

// Se creo el Buffer compartido con ayuda de la IA Generativa
public class BufferCompartido {
    private final Queue<Integer> buffer = new LinkedList<>();
    private final int capacidad;

    public BufferCompartido(int capacidad) {
        this.capacidad = capacidad;
    }

    public boolean estaLleno() {
        return buffer.size() == capacidad;
    }

    public boolean estaVacio() {
        return buffer.isEmpty();
    }

    public void producir(int item) {
        buffer.add(item);
    }

    public int consumir() {
        return buffer.poll();
    }

    public int tamanio() {
        return buffer.size();
    }
}
