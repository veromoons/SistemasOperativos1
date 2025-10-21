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
public class HRRNScheduling implements ISchedulingAlgorithm {
    private Queue<Proceso> colaNuevos = new LinkedList<>();
    private Queue<Proceso> colaListos = new LinkedList<>();
    private Queue<Proceso> colaBloqueados = new LinkedList<>();
    private Queue<Proceso> colaTerminados = new LinkedList<>();
    private Queue<Proceso> colaListoSuspendido = new LinkedList<>();
    private Queue<Proceso> colaBloqueadoSuspendido = new LinkedList<>();    
    private SchedulingType type = SchedulingType.HRRN;


    @Override
    public void agregarProcesoAListos(Proceso p) {
        colaListos.add(p);
    }

    @Override
    public Proceso obtenerSiguienteProceso() {
        if (colaListos.isEmpty()) return null;

        Proceso mejor = null;
        double mejorRR = -1;

        for (Proceso p : colaListos) {
            long tiempoEsperando = p.getTiempoEsperando(); // en ticks
            long duracion = p.getDuracionTotal(); // duración total
            double rr = ((double) tiempoEsperando + duracion) / duracion;

            if (rr > mejorRR) {
                mejorRR = rr;
                mejor = p;
            }
        }

        // Quitar el proceso elegido de la cola
        colaListos.remove(mejor);
        return mejor;
    }
    
    @Override
    public SchedulingType getSchedulingType() {
        return this.type;
    }


    @Override
    public boolean hayProcesos() {
        return !colaListos.isEmpty();
    }

    @Override public void setColaNuevos(Queue<Proceso> cola){ this.colaNuevos = cola; }
    @Override public void setColaListos(Queue<Proceso> cola){ this.colaListos = cola; }
    @Override public void setColaBloqueados(Queue<Proceso> cola){ this.colaBloqueados = cola; }
    @Override public void setColaTerminados(Queue<Proceso> cola){ this.colaTerminados = cola; }
    @Override public void setColaListoSuspendido(Queue<Proceso> cola){ this.colaListoSuspendido = cola; }
    @Override public void setColaBloqueadoSuspendido(Queue<Proceso> cola){ this.colaBloqueadoSuspendido = cola; }
}