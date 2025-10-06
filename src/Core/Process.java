/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Core;

/**
 *
 * @author verol
 */

public class Process implements Runnable {
    private int pid;
    private String name;
    private int burstTime;         // t total de CPU que necesita el proceso
    private int remainingTime;     // t restante de ejecución
    private int quantum;           // t asignado por el scheduler
    private String state;          // READY, RUNNING, BLOCKED, FINISHED
    private String processType;    // "CPU_BOUND", "IO_BOUND", o "NORMAL"

    public Process(int pid, String name, int burstTime, int quantum, String processType) {
        this.pid = pid;
        this.name = name;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
        this.quantum = quantum;
        this.state = "READY";       // edo inicial
        this.processType = processType;
    }

    @Override
    public void run() {
        try {
            state = "RUNNING";
            System.out.println(name + " (" + processType + ") is running...");

            int executionTime = Math.min(quantum, remainingTime);

            for (int i = 0; i < executionTime; i++) {
                Thread.sleep(200); // simula trabajo en CPU

                remainingTime--;

                // bloq random si es I/O-bound
                if (processType.equals("IO_BOUND") && Math.random() < 0.3) {
                    state = "BLOCKED";
                    System.out.println(name + " is performing I/O...");
                    Thread.sleep(600); // simula delay de E/S
                    state = "READY";
                    System.out.println(name + " finished I/O and is READY again.");
                    break;
                }

                // CPU-bound significa que raramente se bloquea
                if (processType.equals("CPU_BOUND") && Math.random() < 0.05) {
                    System.out.println(name + " briefly pauses for maintenance task...");
                    Thread.sleep(100); // Pequeña pausa
                }
            }

            // revisando si termino  proceso
            if (remainingTime <= 0) {
                state = "FINISHED";
                System.out.println(name + " finished execution.");
            } else if (!state.equals("BLOCKED")) {
                state = "READY"; // quantum expiro
                System.out.println(name + " quantum expired, back to READY.");
            }

        } catch (InterruptedException e) {
            System.out.println(name + " was interrupted.");
        }
    }

    // dice si el proceso terminó
    public boolean isFinished() {
        return state.equals("FINISHED");
    }

    // getter para el estado
    public String getState() {
        return state;
    }

    // getter para el nombre
    public String getName() {
        return name;
    }

    // getter para el tiempo restante
    public int getRemainingTime() {
        return remainingTime;
    }

    // getter para el tipo de proceso
    public String getProcessType() {
        return processType;
    }

    public void setQuantum(int quantum) {
        this.quantum = quantum;
    }
}



