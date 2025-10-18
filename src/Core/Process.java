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
    private final PCB PCB;
    private final OperatingSystem os;
    private final Semaphore ioSemaphore;
    private final Random random = new Random();

    public Process(String name, ProcessType type, int baseTime, CPU cpu, Semaphore ioSemaphore, OperatingSystem os) {
        super(name);
        this.type = type;
        this.baseTime = baseTime;
        this.cpu = cpu;
        this.ioSemaphore = ioSemaphore;
        this.PCB = new PCB("1", name);
        this.os = os;
    }

    @Override
    public void run() {
        try {
            while (!finished) {
                // CPU burst
                int cpuBurst = getCpuBurstTime();
                System.out.println(PCB.getName() + " using CPU for " + cpuBurst + "ms (" + type + ")");
                cpu.execute(this, cpuBurst);

                // Posible operación de E/S
                if (random.nextDouble() < getIoProbability()) {
                    int ioTime = getIoTime();
                    this.PCB.setStatus(StatusType.Blocked);
                    System.out.println(PCB.getName() + " requesting I/O for " + ioTime + "ms...");

                    // El DMA liberará el semáforo cuando termine
                    DMAController dma = new DMAController(ioSemaphore, this, ioTime, os);

                    // El hilo se bloquea hasta que el DMA complete
                    ioSemaphore.acquire();
                    dma.start();
                    ioSemaphore.release();

                    this.PCB.setStatus(StatusType.Ready);
                    System.out.println(PCB.getName() + " resumes after I/O.");
                } else {
                    finished = true;
                    this.PCB.setStatus(StatusType.Done);
                    System.out.println(PCB.getName() + " finished execution.");
                }
            }
        } catch (InterruptedException e) {
            System.out.println(PCB.getName() + " was interrupted.");
            Thread.currentThread().interrupt();
        }
    }

    private int getCpuBurstTime() {
        switch (type) {
            case CPU_BOUND: return randomBetween((int)(1.0*baseTime),(int)(2.0*baseTime));
            case IO_BOUND: return randomBetween((int)(0.2*baseTime),(int)(0.6*baseTime));
            case NORMAL:
            default: return randomBetween((int)(0.5*baseTime),(int)(1.0*baseTime));
        }
    }

    private int getIoTime() {
        switch (type) {
            case CPU_BOUND: return randomBetween((int)(0.2*baseTime),(int)(0.5*baseTime));
            case IO_BOUND: return randomBetween((int)(1.0*baseTime),(int)(2.0*baseTime));
            case NORMAL:
            default: return randomBetween((int)(0.5*baseTime),(int)(1.0*baseTime));
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
        return random.nextInt(max-min+1) + min;
    }

    public String getProcessName() { return PCB.getName(); }
    public PCB getPCB() { return PCB; }
    public ProcessType getType() { return type; }
}