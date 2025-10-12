/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Core;
import java.util.Random;
import java.util.concurrent.Semaphore;

/**
 *
 * @author verol
 */


/**
 * Process: representa un proceso real (extiende Thread).
 *
 * - Usa semáforos para bloqueo por I/O (ioWait) y soporte de suspensión (suspendSemaphore).
 * - isCpuBound determina la tendencia del proceso (CPU-bound o IO-bound).
 * - ioProbability determina la probabilidad de iniciar I/O en cada ciclo.
 */

enum ProcessType {
    CPU_BOUND,
    IO_BOUND,
    NORMAL
}

public class Process extends Thread {
    private final ProcessType type;
    private final int baseTime;
    private final CPU cpu;
    private boolean finished = false;
    private int totalCpuTime = 0;
    private int totalIoTime = 0;
    private final Random random = new Random();
    private PCB PCB;

    // Semáforo compartido para operaciones de E/S (DMA o dispositivo simulado)
    private final Semaphore ioSemaphore;

    public Process(String name, ProcessType type, int baseTime, CPU cpu, Semaphore ioSemaphore) {
        this.type = type;
        this.baseTime = baseTime;
        this.cpu = cpu;
        this.ioSemaphore = ioSemaphore;
        this.PCB = new PCB("1", name);
    }

    @Override
    public void run() {
        try {
            while (!finished) {
                // Simular ráfaga de CPU
                int cpuBurst = getCpuBurstTime();
                System.out.println(this.PCB.getName() + " is using CPU for " + cpuBurst + "ms (" + type + ")");
                cpu.execute(this, cpuBurst);

                // Simular posible operación de E/S
                if (random.nextDouble() < getIoProbability()) {
                    int ioTime = getIoTime();
                    performIO(ioTime);
                } else {
                    finished = true;
                    System.out.println(this.PCB.getName() + " finished execution.");
                }
            }
        } catch (InterruptedException e) {
            System.out.println(this.PCB.getName() + " was interrupted.");
        }
    }

    private void performIO(int ioTime) throws InterruptedException {
        System.out.println(this.PCB.getName() + " waiting for I/O for " + ioTime + "ms...");
        ioSemaphore.acquire(); // acceso exclusivo al dispositivo (simula DMA)
        Thread.sleep(ioTime);
        totalIoTime += ioTime;
        ioSemaphore.release();
        System.out.println(this.PCB.getName() + " completed I/O and returns to ready queue.");
    }

    private int getCpuBurstTime() {
        switch (type) {
            case CPU_BOUND:
                return randomBetween((int)(1.0 * baseTime), (int)(2.0 * baseTime));
            case IO_BOUND:
                return randomBetween((int)(0.2 * baseTime), (int)(0.6 * baseTime));
            case NORMAL:
            default:
                return randomBetween((int)(0.5 * baseTime), (int)(1.0 * baseTime));
        }
    }

    private int getIoTime() {
        switch (type) {
            case CPU_BOUND:
                return randomBetween((int)(0.2 * baseTime), (int)(0.5 * baseTime));
            case IO_BOUND:
                return randomBetween((int)(1.0 * baseTime), (int)(2.0 * baseTime));
            case NORMAL:
            default:
                return randomBetween((int)(0.5 * baseTime), (int)(1.0 * baseTime));
        }
    }

    private double getIoProbability() {
        switch (type) {
            case CPU_BOUND: return 0.3;
            case IO_BOUND: return 0.8;
            default: return 0.5;
        }
    }

    private int randomBetween(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }

    public String getProcessName() { return this.PCB.getName(); }
}

