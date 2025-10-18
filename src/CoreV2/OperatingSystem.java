/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CoreV2;
import CoreV2.AlgorithmsStrategies.ISchedulingAlgorithm;
import java.util.concurrent.Semaphore;


/**
 *
 * @author verol
 */
// 🔹 Sistema operativo que maneja colas, CPU, memoria y disco
public class OperatingSystem {
    private final Scheduler scheduler;
    private final CPU cpu;
    private final MainMemory memory;
    private final Disk disk;
    private final DMA dma;
    private final Clock clock;

    private final Semaphore mutex = new Semaphore(1); // protege acceso concurrente

    public OperatingSystem(CPU cpu, MainMemory memory, Disk disk, DMA dma, Scheduler scheduler, Clock clock) {
        this.cpu = cpu;
        this.memory = memory;
        this.disk = disk;
        this.dma = dma;
        this.scheduler = scheduler;
        this.clock = clock;
    }

    // 🔹 Agregar proceso al sistema
    public void agregarProceso(Proceso p) {
        try {
            mutex.acquire();
            // Intentamos cargar en memoria principal
            if (memory.cargarProceso(p)) {
                memory.agregarAColaCortoPlazo(p);
                scheduler.agregarProceso(p);
                System.out.println("SO: Proceso " + p.getId() + " cargado en memoria principal");
            } else {
                // Si no hay espacio, lo mandamos a disco
                disk.guardarProceso(p);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            mutex.release();
        }
    }

    // 🔹 Asignar proceso a CPU si está disponible
    public void asignarProcesoACPU() {
        if (!cpu.estaOcupado() || !scheduler.hayProcesos()) return;
        try {
            mutex.acquire();
            Proceso siguiente = scheduler.obtenerSiguienteProceso();
            if (siguiente != null) {
                cpu.asignarProceso(siguiente, clock.getTic());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            mutex.release();
        }
    }

    // 🔹 Mover proceso a cola de listos (después de interrupción)
    public void moverAColaListos(Proceso p) {
        memory.agregarAColaCortoPlazo(p);
        scheduler.agregarProceso(p);
        System.out.println("SO: Proceso " + p.getId() + " vuelto a cola de listos");
    }

    // 🔹 Interrumpir proceso (ej. por quantum)
    public void interrumpirProceso(Proceso p) {
        moverAColaListos(p);
    }

    // 🔹 Proceso finalizado
    public void procesoFinalizado(Proceso p) {
        memory.liberarProceso(p);
        System.out.println("SO: Proceso " + p.getId() + " finalizado y liberada memoria");
    }

    // 🔹 Bloquear proceso por E/S
    public void bloquearProcesoES(Proceso p) {
        System.out.println("SO: Proceso " + p.getId() + " bloqueado por E/S");
        dma.ejecutarES(p, () -> {
            System.out.println("SO: E/S completada para proceso " + p.getId());
            moverAColaListos(p);
            asignarProcesoACPU();
        });
    }

    // 🔹 Cambiar algoritmo de planificación
    public void setAlgoritmo(ISchedulingAlgorithm algoritmo) {
        scheduler.setAlgoritmo(algoritmo);
        System.out.println("SO: Algoritmo de planificación cambiado");
    }
    
    public void notifyTic() {
    try {
        mutex.acquire();

        // 🔹 Actualizamos tiempos de espera para los procesos en cola de corto plazo
        for (Proceso p : memory.getProcesosCortoPlazo()) {
            p.actualizarTiempoEsperando(clock.getTic());
        }

        // 🔹 Intentamos asignar proceso a CPU
        if (!cpu.estaOcupado() && scheduler.hayProcesos()) {
            Proceso siguiente = scheduler.obtenerSiguienteProceso();
            if (siguiente != null) {
                cpu.asignarProceso(siguiente, clock.getTic()); // <-- pasamos el tick actual
            }
        }

    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
    } finally {
        mutex.release();
    }
}

}




