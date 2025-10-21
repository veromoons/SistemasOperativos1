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
public class FeedbackScheduling implements ISchedulingAlgorithm {
    private Queue<Proceso> colaNuevos = new LinkedList<>();
    private Queue<Proceso> colaListos = new LinkedList<>();
    private Queue<Proceso> colaBloqueados = new LinkedList<>();
    private Queue<Proceso> colaTerminados = new LinkedList<>();
    private Queue<Proceso> colaListoSuspendido = new LinkedList<>();
    private Queue<Proceso> colaBloqueadoSuspendido = new LinkedList<>();
    private SchedulingType type = SchedulingType.FEEDBACK;


    // 🔹 Tres niveles fijos
    private Queue<Proceso> colaAlta = new LinkedList<>();
    private Queue<Proceso> colaMedia = new LinkedList<>();
    private Queue<Proceso> colaBaja = new LinkedList<>();

    @Override
    public void agregarProcesoAListos(Proceso p) {
        switch (p.getQueuePriority()) {
            case 0: colaAlta.add(p); break;
            case 1: colaMedia.add(p); break;
            case 2: colaBaja.add(p); break;
            default: colaAlta.add(p); // por seguridad
        }
    }
    
    //No se contempla inanicion
    @Override
    public Proceso obtenerSiguienteProceso() { // se resetean colas cada vez que llega un nuevo proceso para simplificar el rearreglo de las 3 listas de Listos
        colaAlta = new LinkedList<>();
        colaMedia = new LinkedList<>();
        colaBaja = new LinkedList<>();
        
        for (Proceso p : colaListos) {
            agregarProcesoAListos(p);
        }
        Proceso siguiente = null;

        if (!colaAlta.isEmpty()) siguiente = colaAlta.poll();
        else if (!colaMedia.isEmpty()) siguiente = colaMedia.poll();
        else if (!colaBaja.isEmpty()) siguiente = colaBaja.poll();
        
        //comentado por ahora, es el quantum de los procesos a ejecutar
//        if (siguiente != null) {
//            // 🔹 Asigna quantum según prioridad
//            long quantum;
//            switch (siguiente.getQueuePriority()) {
//                case 0: quantum = 2; break;  // más corto
//                case 1: quantum = 4; break;
//                default: quantum = 6; break;
//            }
//            siguiente.setUltimoTicEjecucion(quantum); // o setQuantum() si lo agregas al Proceso
//        }

        return siguiente;
    }
    @Override
    public SchedulingType getSchedulingType() {
        return this.type;
    }

    @Override
    public boolean hayProcesos() {
        return !(colaAlta.isEmpty() && colaMedia.isEmpty() && colaBaja.isEmpty());
    }

    // 🔹 Métodos para setear las colas (aunque aquí no se usan tanto)
    public void setColaNuevos(Queue<Proceso> cola){ this.colaNuevos = cola; }
    public void setColaListos(Queue<Proceso> cola){ this.colaListos = cola; }
    public void setColaBloqueados(Queue<Proceso> cola){ this.colaBloqueados = cola; }
    public void setColaTerminados(Queue<Proceso> cola){ this.colaTerminados = cola; }
    public void setColaListoSuspendido(Queue<Proceso> cola){ this.colaListoSuspendido = cola; }
    public void setColaBloqueadoSuspendido(Queue<Proceso> cola){ this.colaBloqueadoSuspendido = cola; }

}