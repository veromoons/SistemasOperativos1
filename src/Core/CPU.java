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


/**
 * CPU: simula la CPU. Usa un Semaphore(1) para exclusión (una sola CPU disponible).
 * El método executeProcess() adquiere el semáforo, duerme el tiempo simulado y libera.
 *
 * Comentarios en español para mayor claridad.
 */
public class CPU {
    private final Semaphore cpuSemaphore = new Semaphore(1); // semáforo para exclusión mutua
    private final int tickTime; // duración de un tick del reloj (en ms)
    private int systemClock = 0; // reloj del sistema

    public CPU(int tickTime) {
        this.tickTime = tickTime;
    }

    public void execute(Process process, int cpuBurst) throws InterruptedException {
        cpuSemaphore.acquire();
        int elapsed = 0;

        while (elapsed < cpuBurst) {
            Thread.sleep(tickTime); // simula 1 tick de CPU
            systemClock += tickTime;
            elapsed += tickTime;
            System.out.println("[Clock " + systemClock + "ms] " + process.getProcessName() + " running...");
        }

        cpuSemaphore.release();
    }

    public int getClock() { return systemClock; }
}
