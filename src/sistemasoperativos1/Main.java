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
import Core.MainMemory;
import Core.OperatingSystem;

/**
 *
 * @author verol
 */

/**
 * Main: punto de entrada de la simulación.
 * Crea módulos, procesos (CPU-bound / IO-bound) y arranca el sistema.
 */
public class Main {
    public static void main(String[] args) {
        // Puedes cambiar este valor para hacer la simulación más lenta o más rápida
        int baseTime = 200; // ms
        MainMemory mainMemory = new MainMemory(16);
        OperatingSystem so = new OperatingSystem(baseTime, mainMemory);
        so.startSystem();
    }
}

