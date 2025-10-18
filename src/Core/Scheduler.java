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

import java.util.concurrent.ConcurrentLinkedQueue;

public class Scheduler {
    private final ConcurrentLinkedQueue<Process> readyQueue = new ConcurrentLinkedQueue<>();

    public void addProcess(Process process) {
        readyQueue.add(process);
    }

    public Process getNextProcess() {
        return readyQueue.poll();
    }

    public boolean hasProcesses() {
        return !readyQueue.isEmpty();
    }
}