/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CoreV2.AlgorithmsStrategies;

import CoreV2.Cola;
import CoreV2.Proceso;

/**
 *
 * @author verol
 */
public class FeedbackScheduling implements ISchedulingAlgorithm {
    private Cola colaNuevos = new Cola();
    private Cola colaListos = new Cola();
    private Cola colaBloqueados = new Cola();
    private Cola colaTerminados = new Cola();
    private Cola colaListoSuspendido = new Cola();
    private Cola colaBloqueadoSuspendido = new Cola();
    private SchedulingType type = SchedulingType.FEEDBACK;
    private boolean hasQuantum = true;

    @Override
    public boolean hasQuantum() {
        return hasQuantum;
    }


    // 🔹 Tres niveles fijos
    private Cola colaAlta = new Cola();
    private Cola colaMedia = new Cola();
    private Cola colaBaja = new Cola();

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
        colaAlta = new Cola();
        colaMedia = new Cola();
        colaBaja = new Cola();
        
        colaListos.forEach(p -> agregarProcesoAListos(p));
        
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
    public void setColaNuevos(Cola cola){
        this.colaNuevos = cola;
    }
    public void setColaListos(Cola cola){
        this.colaListos = cola;
    }
    public void setColaBloqueados(Cola cola){
        this.colaBloqueados = cola;
    }
    public void setColaTerminados(Cola cola){
        this.colaTerminados = cola;
    }
    public void setColaListoSuspendido(Cola cola){
        this.colaListoSuspendido = cola;
    }
    public void setColaBloqueadoSuspendido(Cola cola){
        this.colaBloqueadoSuspendido = cola;
    }

}