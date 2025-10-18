/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Core;
/**
 *
 * @author verol
 */

public class Scheduler {
private SchedulingPolicy currentPolicy; // Variable para saber qué algoritmo usar

    /**
     * Constructor del Scheduler.
     * Inicia con una política por defecto, que puede ser cambiada después.
     */
    public Scheduler() {
        // Por defecto, empezamos con FCFS, el más simple.
        this.currentPolicy = SchedulingPolicy.FCFS; 
    }

    /**
     * Permite cambiar la política de planificación en tiempo de ejecución.
     * La interfaz gráfica llamará a este método.
     * @param policy La nueva política a utilizar (FCFS, SJF, etc.).
     */
    public void setPolicy(SchedulingPolicy policy) {
        this.currentPolicy = policy;
        System.out.println("🔄 Política de planificación cambiada a: " + policy);
    }

    /**
     * El método principal del planificador.
     * Recibe la cola de procesos listos y decide cuál ejecutar a continuación.
     * @param readyQueue La cola actual de procesos en estado LISTO.
     * @return El proceso seleccionado para pasar al estado de EJECUCION.
     */
    public Process selectNextProcess(CustomQueue readyQueue) {
        if (readyQueue.isEmpty()) {
            return null; // No hay procesos para planificar
        }

        Process selectedProcess = null;

        // Usamos un switch para ejecutar el algoritmo correspondiente
        switch (currentPolicy) {
            case FCFS:
                // Para FCFS, simplemente sacamos el primer proceso de la cola.
                selectedProcess = readyQueue.dequeue();
                break;
            
            case SJF:
                // TODO: Implementar la lógica para Shortest Job First
                // (Buscar en la cola el proceso con menos 'totalInstructions')
                System.out.println("Advertencia: La política SJF aún no está implementada.");
                // Como fallback, usamos FCFS por ahora.
                selectedProcess = readyQueue.dequeue();
                break;
            
            case ROUND_ROBIN:
                // TODO: Implementar la lógica para Round Robin
                System.out.println("Advertencia: La política Round Robin aún no está implementada.");
                // Round Robin también saca el primero de la fila.
                selectedProcess = readyQueue.dequeue();
                break;
                
            default:
                // Si por alguna razón la política no es reconocida, usamos FCFS.
                selectedProcess = readyQueue.dequeue();
                break;
        }

        return selectedProcess;
    }
}