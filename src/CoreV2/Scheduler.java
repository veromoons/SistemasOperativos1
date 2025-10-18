/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CoreV2;
import CoreV2.AlgorithmsStrategies.ISchedulingAlgorithm;
import java.util.List;
import java.util.Queue;
import java.util.LinkedList;

/**
 *
 * @author verol
 */
// 🔹 Interfaz genérica para algoritmos de planificació


// 🔹 Scheduler que utiliza un algoritmo dinámico
public class Scheduler {
    private ISchedulingAlgorithm algoritmo;

    public Scheduler(ISchedulingAlgorithm algoritmoInicial) {
        this.algoritmo = algoritmoInicial;
    }

    public void setAlgoritmo(ISchedulingAlgorithm nuevoAlgoritmo) {
        this.algoritmo = nuevoAlgoritmo;
    }

    public void agregarProceso(Proceso p) {
        algoritmo.agregarProceso(p);
    }

    public Proceso obtenerSiguienteProceso() {
        return algoritmo.obtenerSiguienteProceso();
    }

    public boolean hayProcesos() {
        return algoritmo.hayProcesos();
    }
}

