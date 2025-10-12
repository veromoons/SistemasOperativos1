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
    private final MainMemory mainMemory;
    public Scheduler(MainMemory mainMemory){
        this.mainMemory = mainMemory;
    }
    // Por ahora FIFO
    public synchronized void addProcess(Process process) {
        this.mainMemory.addProcess(process);
    }

    public synchronized Process getNextProcess() {
        return this.mainMemory.getNextProcess();
    }

    public boolean hasProcesses() {
        return this.mainMemory.hasProcesses();
    }
}