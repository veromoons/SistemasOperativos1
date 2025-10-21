/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CoreV2.AlgorithmsStrategies;

import CoreV2.Proceso;
import java.util.Queue;

/**
 *
 * @author verol
 */

public interface ISchedulingAlgorithm {
    public enum SchedulingType { FCFS, ROUNDROBIN, SPN, SRT, HRRN, FEEDBACK};
    
    void agregarProcesoAListos(Proceso p);
    Proceso obtenerSiguienteProceso();
    SchedulingType getSchedulingType();
    boolean hayProcesos();
    void setColaNuevos(Queue<Proceso> cola);
    void setColaListos(Queue<Proceso> cola);
    void setColaBloqueados(Queue<Proceso> cola);
    void setColaTerminados(Queue<Proceso> cola);
    void setColaListoSuspendido(Queue<Proceso> cola);
    void setColaBloqueadoSuspendido(Queue<Proceso> cola);
}
