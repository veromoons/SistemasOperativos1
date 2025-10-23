/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CoreV2.AlgorithmsStrategies;

import CoreV2.Cola;
import CoreV2.Proceso;
import CoreV2.Nodo;

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
    public Proceso obtenerSiguienteProceso() {

        // --- INICIO DE CÓDIGO AÑADIDO PARA RESINCRONIZAR ---
        // Resincronizar colas internas con la colaListos principal ANTES de seleccionar

        // 1. Vaciar colas internas
        colaAlta = new Cola();
        colaMedia = new Cola();
        colaBaja = new Cola();

        // 2. Recorrer la colaListos principal (la del SO)
        //    Iteramos usando getFrente() y getSiguiente() de tu clase Cola/Nodo
        if (colaListos != null && !colaListos.isEmpty()) { // Asegurarse que colaListos no sea null
             Nodo actual = colaListos.getFrente(); // Obtener el primer nodo
             while (actual != null) { // Mientras haya nodos
                 Proceso p = actual.getProceso(); // Obtener el proceso del nodo
                 if (p != null) { // Comprobar que el proceso no sea null
                     // 3. Recolocar en la cola interna según prioridad actual
                     switch (p.getQueuePriority()) {
                         case 0: colaAlta.add(p); break;
                         case 1: colaMedia.add(p); break;
                         case 2: colaBaja.add(p); break;
                         default: colaAlta.add(p);
                     }
                 }
                 actual = actual.getSiguiente(); // Moverse al siguiente nodo
             }
        }
        // --- FIN DE CÓDIGO AÑADIDO PARA RESINCRONIZAR ---


        // Ahora el resto del método funciona como antes, pero con las colas internas actualizadas
        Proceso siguiente = null;

        // Busca en las colas internas por prioridad (ahora están actualizadas)
        if (!colaAlta.isEmpty()) {
            siguiente = colaAlta.poll(); // poll() ya lo quita de colaAlta
        } else if (!colaMedia.isEmpty()) {
            siguiente = colaMedia.poll(); // poll() ya lo quita de colaMedia
        } else if (!colaBaja.isEmpty()) {
            siguiente = colaBaja.poll(); // poll() ya lo quita de colaBaja
        }

        // Si se encontró un proceso, quítalo TAMBIÉN de la colaListos principal
        if (siguiente != null) {
            boolean removed = colaListos.remove(siguiente);
             if (!removed) {
                 // Este warning podría aparecer si el proceso se eliminó de colaListos
                 // entre la resincronización y este punto (poco probable pero posible).
                 System.err.println("WARN: Proceso " + siguiente.getNombre() + " no encontrado en colaListos principal al intentar remover en Feedback (resync).");
             }
        }

        // Nota: La lógica para asignar quantum según prioridad estaba comentada,
        // se deja así por ahora. El quantum global lo maneja CPU/OS.

        return siguiente;
    }
    
    @Override
    public SchedulingType getSchedulingType() {
        return this.type;
    }

    @Override
    public boolean hayProcesos() {
        return !colaListos.isEmpty();
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