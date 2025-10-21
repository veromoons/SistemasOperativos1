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

public class RRScheduling implements ISchedulingAlgorithm { //esta clase debe cumplir con estas reglas de ISchedulling, sino empieza a arrojar errores (si no tienes los metodos adecuados(
//    private Queue<Proceso> cola = new LinkedList<>();
    private Queue<Proceso> colaNuevos = new LinkedList<>();
    private Queue<Proceso> colaListos = new LinkedList<>();
    private Queue<Proceso> colaBloqueados = new LinkedList<>();
    private Queue<Proceso> colaTerminados = new LinkedList<>();
    private Queue<Proceso> colaListoSuspendido = new LinkedList<>();
    private Queue<Proceso> colaBloqueadoSuspendido = new LinkedList<>();
    private SchedulingType type = SchedulingType.ROUNDROBIN;

    @Override
        public void agregarProcesoAListos(Proceso p) {
        colaListos.add(p);
    }

    @Override
    public Proceso obtenerSiguienteProceso() {
        return colaListos.poll(); // FIFO: primero en entrar, primero en salir
    }

    @Override
    public boolean hayProcesos() {
        return !colaListos.isEmpty();
    }
    
    @Override
    public SchedulingType getSchedulingType() {
        return this.type;
    }
    
    public void setColaNuevos(Queue<Proceso> cola){
        this.colaNuevos = cola;
    }
    public void setColaListos(Queue<Proceso> cola){
        this.colaListos = cola;
    }
    public void setColaBloqueados(Queue<Proceso> cola){
        this.colaBloqueados = cola;
    }
    public void setColaTerminados(Queue<Proceso> cola){
        this.colaTerminados = cola;
    }
    public void setColaListoSuspendido(Queue<Proceso> cola){
        this.colaListoSuspendido = cola;
    }
    public void setColaBloqueadoSuspendido(Queue<Proceso> cola){
        this.colaBloqueadoSuspendido = cola;
    }

}