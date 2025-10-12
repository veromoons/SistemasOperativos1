/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Core;
import java.util.concurrent.Semaphore;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author verol
 */

public class MainMemory {
    private final int totalMemory;
    private final Semaphore memoryPermits; // semáforo contador representando memoria libre en MB
    private final Queue<Process> readyQueue = new LinkedList<>();
    public MainMemory(int totalMemory) {
        this.totalMemory = totalMemory;
        this.memoryPermits = new Semaphore(totalMemory, true); // fair = true para orden FIFO de espera
    }

    /**
     * Asigna 'amount' MB de memoria. Si no hay suficientes permisos, bloquea hasta que haya.
     * @param processName nombre del proceso (solo para logging)
     * @param amount cantidad de MB a asignar
     * @throws InterruptedException si se interrumpe mientras espera
     */
    public void allocateMemory(String processName, int amount) throws InterruptedException {
        System.out.println("📦 Memory: " + processName + " requesting " + amount + " MB...");
        memoryPermits.acquire(amount); // bloquea hasta que haya 'amount' permisos disponibles
        System.out.println("✅ Memory: " + processName + " allocated " + amount + " MB (remaining " + memoryPermits.availablePermits() + "/" + totalMemory + ")");
    }

    /**
     * Libera 'amount' MB de memoria.
     * @param processName nombre del proceso (solo para logging)
     * @param amount cantidad de MB a liberar
     */
    public void freeMemory(String processName, int amount) {
        // Protección: no liberar más de lo permitido no tiene sentido con Semaphore,
        // asumimos uso coherente. Liberamos y mostramos estado.
        memoryPermits.release(amount);
        System.out.println("♻️ Memory: " + processName + " released " + amount + " MB (remaining " + memoryPermits.availablePermits() + "/" + totalMemory + ")");
    }

    /**
     * Método de inspección (opcional).
     */
    public int getAvailableMemory() {
        return memoryPermits.availablePermits();
    }
    
    public synchronized void addProcess(Process process) {
        readyQueue.add(process);
        System.out.println(process.getProcessName() + " added to ready queue.");
    }

    public synchronized Process getNextProcess() {
        return readyQueue.poll();
    }

    public boolean hasProcesses() {
        return !readyQueue.isEmpty();
    }
}
