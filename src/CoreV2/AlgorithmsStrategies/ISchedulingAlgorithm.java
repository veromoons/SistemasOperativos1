/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CoreV2.AlgorithmsStrategies;

import CoreV2.Proceso;

/**
 *
 * @author verol
 */
public interface ISchedulingAlgorithm {
    void agregarProceso(Proceso p);
    Proceso obtenerSiguienteProceso();
    boolean hayProcesos();
}