/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemasoperativos1;

import Core.CPU;
import Core.Mutex;
import Core.Scheduler;
import Core.Process;

/**
 *
 * @author verol
 */
public class SISTEMASOPERATIVOS1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        CPU cpu = new CPU(3); // quantum base de 3 ciclos
        Scheduler scheduler = new Scheduler(cpu);

        Process p1 = new Process(1, "Process 1", 10, 3, "CPU_BOUND");
        Process p2 = new Process(2, "Process 2", 7, 3, "IO_BOUND");
        Process p3 = new Process(3, "Process 3", 8, 3, "NORMAL");

        // agg   procesos al scheduler o planificador
        scheduler.addProcess(p1);
        scheduler.addProcess(p2);
        scheduler.addProcess(p3);

        // corre el planificador
        scheduler.run();
    }
    
//    private static final Mutex mutex = new Mutex();
//
//    public static void main(String[] args) {
//        Runnable tarea = () -> {
//            mutex.acquire();
//            try {
//                System.out.println(Thread.currentThread().getName() + " está en la sección crítica.");
//                Thread.sleep(5000);
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//            } finally {
//                mutex.release();
//            }
//        };
//
//        Thread t1 = new Thread(tarea, "Hilo 1");
//        Thread t2 = new Thread(tarea, "Hilo 2");
//        Thread t3 = new Thread(tarea, "Hilo 3");
//
//        t1.start();
//        t2.start();
//        t3.start();
//    }
    
}

