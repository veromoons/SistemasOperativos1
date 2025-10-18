/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CoreV2;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Queue;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;


/**
 *
 * @author verol
 */
// Representa un bloque libre dentro de la memoria
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Queue;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;

// Representa un bloque libre dentro de la memoria
class MemoryBlock {
    public int start;  // posición inicial en la memoria
    public int size;   // tamaño del bloque

    public MemoryBlock(int start, int size) {
        this.start = start;
        this.size = size;
    }
}

// Simulación de la memoria principal
public class MainMemory {
    private final int capacidad;                      // tamaño total de memoria
    private final List<MemoryBlock> bloquesLibres;    // lista de bloques libres
    private final List<Proceso> procesosEnMemoria;   // procesos actualmente en memoria

    private final Queue<Proceso> colaCortoPlazo = new LinkedList<>();
    private final Queue<Proceso> colaMedianoPlazo = new LinkedList<>();

    private final Semaphore mutex = new Semaphore(1); // protege acceso concurrente

    public MainMemory(int capacidad) {
        this.capacidad = capacidad;
        this.bloquesLibres = new ArrayList<>();
        this.bloquesLibres.add(new MemoryBlock(0, capacidad)); // todo libre al inicio
        this.procesosEnMemoria = new ArrayList<>();
    }

    // 🔹 Intenta cargar un proceso en memoria
    public boolean cargarProceso(Proceso p) {
        try {
            mutex.acquire();
            for (MemoryBlock block : bloquesLibres) {
                if (block.size >= p.getTamano()) {
                    p.setStartAddress(block.start);
                    procesosEnMemoria.add(p);

                    if (block.size == p.getTamano()) {
                        bloquesLibres.remove(block);
                    } else {
                        block.start += p.getTamano();
                        block.size -= p.getTamano();
                    }
                    return true;
                }
            }
            return false;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        } finally {
            mutex.release();
        }
    }

    // 🔹 Libera la memoria de un proceso
    public void liberarProceso(Proceso p) {
        try {
            mutex.acquire();
            procesosEnMemoria.remove(p);
            bloquesLibres.add(new MemoryBlock(p.getStartAddress(), p.getTamano()));
            fusionarBloquesLibres();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            mutex.release();
        }
    }

    // 🔹 Fusiona bloques contiguos para evitar fragmentación
    private void fusionarBloquesLibres() {
        bloquesLibres.sort(Comparator.comparingInt(b -> b.start));
        List<MemoryBlock> fusionados = new ArrayList<>();
        MemoryBlock prev = null;

        for (MemoryBlock b : bloquesLibres) {
            if (prev == null) {
                prev = b;
            } else {
                if (prev.start + prev.size == b.start) {
                    prev.size += b.size;
                } else {
                    fusionados.add(prev);
                    prev = b;
                }
            }
        }
        if (prev != null) fusionados.add(prev);

        bloquesLibres.clear();
        bloquesLibres.addAll(fusionados);
    }

    // 🔹 Métodos para la cola de corto plazo
    public void agregarAColaCortoPlazo(Proceso p) {
        try {
            mutex.acquire();
            colaCortoPlazo.add(p);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            mutex.release();
        }
    }

    public Proceso sacarDeColaCortoPlazo() {
        try {
            mutex.acquire();
            return colaCortoPlazo.poll();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        } finally {
            mutex.release();
        }
    }

    public boolean hayProcesosCortoPlazo() {
        try {
            mutex.acquire();
            return !colaCortoPlazo.isEmpty();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        } finally {
            mutex.release();
        }
    }

    // 🔹 Métodos para la cola de mediano plazo
    public void agregarAColaMedianoPlazo(Proceso p) {
        try {
            mutex.acquire();
            colaMedianoPlazo.add(p);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            mutex.release();
        }
    }

    public Proceso sacarDeColaMedianoPlazo() {
        try {
            mutex.acquire();
            return colaMedianoPlazo.poll();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        } finally {
            mutex.release();
        }
    }

    public boolean hayProcesosMedianoPlazo() {
        try {
            mutex.acquire();
            return !colaMedianoPlazo.isEmpty();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        } finally {
            mutex.release();
        }
    }

    // 🔹 Getters públicos para SO
    public List<Proceso> getProcesosCortoPlazo() {
        return new ArrayList<>(colaCortoPlazo);
    }

    public List<Proceso> getProcesosMedianoPlazo() {
        return new ArrayList<>(colaMedianoPlazo);
    }
}


