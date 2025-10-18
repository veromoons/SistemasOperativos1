/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CoreV2.AlgorithmsStrategies;

import CoreV2.Proceso;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author verol
 */
public class FIFOScheduling implements ISchedulingAlgorithm {
    private final Queue<Proceso> cola = new LinkedList<>();

    @Override
    public void agregarProceso(Proceso p) {
        cola.add(p);
    }

    @Override
    public Proceso obtenerSiguienteProceso() {
        return cola.poll(); // FIFO: primero en entrar, primero en salir
    }

    @Override
    public boolean hayProcesos() {
        return !cola.isEmpty();
    }
}