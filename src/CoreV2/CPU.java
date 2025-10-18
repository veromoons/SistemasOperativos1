/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CoreV2;
import java.util.concurrent.Semaphore;

/**
 *
 * @author verol
 */
public class CPU {

    private Proceso procesoActual;
    private int tiempoOcupado; // total ticks que la CPU ha estado ocupada
    private boolean ocupado;

    public CPU() {
        this.procesoActual = null;
        this.tiempoOcupado = 0;
        this.ocupado = false;
    }

    // ✅ Asigna un nuevo proceso a la CPU
    public void asignarProceso(Proceso proceso, long tiempoActual) {
        this.procesoActual = proceso;
        this.ocupado = true;
        proceso.setUltimoTicEjecucion(tiempoActual);
    }

    // ✅ Libera la CPU
    public Proceso liberarCPU(long tiempoActual) {
        if (procesoActual != null) {
            // Actualiza el tiempo de ejecución total antes de salir
            long tiempoEjecutadoEnEstaRacha = tiempoActual - procesoActual.getUltimoTicEjecucion();
            procesoActual.actualizarTiempoEjecutado(tiempoEjecutadoEnEstaRacha);
        }

        Proceso terminado = this.procesoActual;
        this.procesoActual = null;
        this.ocupado = false;
        return terminado;
    }

    // ✅ Avanza un tic del reloj
    public void tick(int tiempoActual) {
        if (procesoActual != null) {
            procesoActual.incrementarTiempoEjecutado(); // +1 tick
            tiempoOcupado++;

            // Si el proceso ya terminó su ráfaga o todo su tiempo total
            if (procesoActual.estaCompletado()) {
                liberarCPU(tiempoActual);
            }
        }
    }

    public boolean estaOcupado() {
        return ocupado;
    }

    public Proceso getProcesoActual() {
        return procesoActual;
    }

    public int getTiempoOcupado() {
        return tiempoOcupado;
    }
}
