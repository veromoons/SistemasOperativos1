/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemasoperativos1;

import CoreV2.CPU;
import CoreV2.Scheduler;
import CoreV2.DMA;
import CoreV2.Disk;
import CoreV2.Proceso;
import CoreV2.MainMemory;
import CoreV2.OperatingSystem;
import CoreV2.*;
import CoreV2.AlgorithmsStrategies.FIFOScheduling;
import CoreV2.AlgorithmsStrategies.HRRNScheduling;
import CoreV2.AlgorithmsStrategies.RRScheduling;
import CoreV2.AlgorithmsStrategies.SPNScheduling;
import CoreV2.AlgorithmsStrategies.SRTScheduling;

/**
 *
 * @author verol
 */


public class Main {
     public static void main(String[] args) throws InterruptedException {
        long unidadTiempoMs = 500; // duración de un tick (0.5s)
        int memoriaTotal = 50;    // tamaño de memoria

        MainMemory memory = new MainMemory(memoriaTotal);
        Disk disk = new Disk();
        DMA dma = new DMA(unidadTiempoMs);
        CPU cpu = new CPU(); 
        Scheduler scheduler = new Scheduler(new FIFOScheduling()); 
        Clock clock = new Clock(unidadTiempoMs);
        OperatingSystem so = new OperatingSystem(cpu, memory, disk, dma, scheduler, clock);
        clock.setSO(so);
        scheduler.setSO(so);

        cpu = new CPU();
        int quantumDefault=0;
        if(scheduler.algoritmoTieneQuantum()){
            quantumDefault = 5;
        }
        so.setCPUQuantum(quantumDefault); 
        
       SimuladorGUI ventana = new SimuladorGUI(so, cpu, clock);
        
        so.setGUI(ventana);
        
        java.awt.EventQueue.invokeLater(() -> {
            ventana.setVisible(true);
        });
        
        
        clock.startClock();

//        so.crearProceso(1, Proceso.Tipo.CPU_BOUND, 20, 20, 2L, 8);      // tamaño 20, 2 ticks de E/S
//        so.crearProceso(2, Proceso.Tipo.IO_BOUND, 5, 5, 10, 1, 3, 10);    // tamaño 30, 5 ticks de E/S
//        so.crearProceso(3,Proceso.Tipo.NORMAL,10, 10, 5L, 7);   // tamaño 10, sin E/S
////        Thread.sleep(10*unidadTiempoMs);
//        so.crearProceso(4,Proceso.Tipo.NORMAL,8, 8, 5L, 7);   // tamaño 10, sin E/S


//        so.asignarProcesoACPU();

//        for (int i = 0; i < 20; i++) {
//            Thread.sleep(unidadTiempoMs);
//            // Revisar si CPU está libre y asignar siguiente
//            so.asignarProcesoACPU();
//
//            // Bloquear aleatoriamente procesos por E/S
//            if (i == 3) {
//                so.bloquearProcesoES(p2); // p2 solicita E/S
//            }
//        }

//        clock.stopClock();
//
//        System.out.println("Simulación completada");
    }
}

