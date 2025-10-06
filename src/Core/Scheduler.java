/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Core;
import java.util.LinkedList;
import java.util.Queue;
/**
 *
 * @author verol
 */

public class Scheduler {
    private Queue<Process> readyQueue = new LinkedList<>();
    private CPU cpu;

    public Scheduler(CPU cpu) {
        this.cpu = cpu;
    }

    public void addProcess(Process process) {
        readyQueue.add(process);
        System.out.println(process.getName() + " added to READY queue.");
    }

    public void run() {
        while (!readyQueue.isEmpty()) {
            Process process = readyQueue.poll();

            if (!process.isFinished()) {
                cpu.executeProcess(process);

                // si todavia no termino lo volvemos a poner al final de la cola
                if (!process.isFinished()) {
                    readyQueue.add(process);
                }
            }
        }
        System.out.println("All processes finished execution.");
    }
}

