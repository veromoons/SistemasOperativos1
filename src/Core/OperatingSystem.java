/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Core;
import java.util.concurrent.Semaphore;

/**
 *
 * @author verol
 */


public class OperatingSystem {
    private final Scheduler scheduler;
    private final CPU cpu;
    private final Semaphore ioSemaphore;
    private final int baseTime;
    private final MainMemory mainMemory;

    public OperatingSystem(int baseTime, MainMemory mainMemory) {
        this.baseTime = baseTime;
        this.scheduler = new Scheduler(mainMemory);
        this.cpu = new CPU(baseTime); // tickTime | baseTime
        this.ioSemaphore = new Semaphore(1); // semáforo para E/S
        this.mainMemory = mainMemory;
    }

    public void startSystem() {
        System.out.println("Operating System started with base time: " + baseTime + "ms");

        // Crear algunos procesos de distintos tipos
        Process p1 = new Process("P1", ProcessType.CPU_BOUND, baseTime, cpu, ioSemaphore);
        Process p2 = new Process("P2", ProcessType.IO_BOUND, baseTime, cpu, ioSemaphore);
        Process p3 = new Process("P3", ProcessType.NORMAL, baseTime, cpu, ioSemaphore);

        scheduler.addProcess(p1);
        scheduler.addProcess(p2);
        scheduler.addProcess(p3);

        // Lanzar procesos en orden FIFO
        while (scheduler.hasProcesses()) {
            Process next = scheduler.getNextProcess();
            if (next != null) next.start();
            try {
                next.join(); // esperar que termine
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("All processes have finished execution.");
    }
    public void handleIOCompletion(Process process) {
    System.out.println("🛎️ Interrupt: I/O completed for " + process.getProcessName());
    
    // Reinsertar el proceso en la cola de listos del scheduler
    scheduler.addProcess(process);
}

}
