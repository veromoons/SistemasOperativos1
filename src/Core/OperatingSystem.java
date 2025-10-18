/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Core;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 *
 * @author verol
 */


import java.util.concurrent.Semaphore;

public class OperatingSystem {
    private final Scheduler scheduler;
    private final CPU cpu;
    private final Semaphore ioSemaphore;
    private final int baseTime;

    public OperatingSystem(int baseTime, MainMemory mainMemory) {
        this.baseTime = baseTime;
        this.scheduler = new Scheduler();
        this.cpu = new CPU(baseTime);
        this.ioSemaphore = new Semaphore(1); // compartido entre todos los procesos
    }

    public void startSystem() {
        System.out.println("Operating System started with base time: " + baseTime + "ms");

        // Crear procesos (usando tu baseTime input)
        Process p1 = new Process("P1", ProcessType.CPU_BOUND, baseTime, cpu, ioSemaphore, this);
        Process p2 = new Process("P2", ProcessType.IO_BOUND, baseTime, cpu, ioSemaphore, this);
        Process p3 = new Process("P3", ProcessType.NORMAL, baseTime, cpu, ioSemaphore, this);

        // Arrancar hilos y agregarlos al scheduler
        scheduler.addProcess(p1);
        scheduler.addProcess(p2);
        scheduler.addProcess(p3);

        p1.start();
        p2.start();
        p3.start();

        // El OS ya no hace join, los procesos se manejan solos
    }

    // Llamado por DMA al terminar la E/S
    public void handleIOCompletion(Process p) {
        System.out.println("[OS] I/O completed for " + p.getProcessName());
        ioSemaphore.release(); // desbloquear al hilo bloqueado
        scheduler.addProcess(p); // volver a ready
    }

    public Scheduler getScheduler() { return scheduler; }
}
