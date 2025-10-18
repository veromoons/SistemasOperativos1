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

/**
 *
 * @author verol
 */

/**
 * Main: punto de entrada de la simulación.
 * Crea módulos, procesos (CPU-bound / IO-bound) y arranca el sistema.
 */

public class Main {
     public static void main(String[] args) throws InterruptedException {
        long unidadTiempoMs = 3000; // duración de un tick (0.5s)
        int memoriaTotal = 100;    // tamaño de memoria

        // 🔹 Instanciamos los componentes
        MainMemory memory = new MainMemory(memoriaTotal);
        Disk disk = new Disk();
        DMA dma = new DMA(unidadTiempoMs);
        CPU cpu = new CPU(); // SO se setea después
        Scheduler scheduler = new Scheduler(new FIFOScheduling()); // FIFO inicial
        Clock clock = new Clock(unidadTiempoMs);
        OperatingSystem so = new OperatingSystem(cpu, memory, disk, dma, scheduler, clock);
        clock.setSO(so);

        // 🔹 Seteamos el SO en la CPU
        cpu = new CPU();
        clock.startClock();

        // 🔹 Creamos procesos
        Proceso p1 = new Proceso(1, Proceso.Tipo.NORMAL, 10L, 20L, 2L, 8);      // tamaño 20, 2 ticks de E/S
        Proceso p2 = new Proceso(2, Proceso.Tipo.IO_BOUND, 20L, 10L, 10, 6);    // tamaño 30, 5 ticks de E/S
        Proceso p3 = new Proceso(3,Proceso.Tipo.CPU_BOUND,15L, 30L, 5L, 7);   // tamaño 10, sin E/S

        // 🔹 Agregamos los procesos al SO
        so.agregarProceso(p1);
        so.agregarProceso(p2);
        so.agregarProceso(p3);

        // 🔹 Asignamos procesos a CPU según disponibilidad
        so.asignarProcesoACPU();

        // 🔹 Simulamos un tic global (ej. 20 ticks)
        for (int i = 0; i < 20; i++) {
            Thread.sleep(unidadTiempoMs);
            // Revisar si CPU está libre y asignar siguiente
            so.asignarProcesoACPU();

            // Bloquear aleatoriamente procesos por E/S
            if (i == 3) {
                so.bloquearProcesoES(p2); // p2 solicita E/S
            }
        }

        // 🔹 Detener CPU al final
        clock.stopClock();

        System.out.println("Simulación completada");
    }
}

