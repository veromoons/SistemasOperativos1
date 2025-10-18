/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CoreV2;

/**
 *
 * @author verol
 */

public class Proceso {
    public enum Tipo { NORMAL, IO_BOUND, CPU_BOUND }

    private final int id;
    private final Tipo tipo;
    private final long duracionTotal; // en ticks
    private final long quantum;       // 0 si no tiene quantum
    private final long tiempoES;      // en ticks, si hace E/S
    private int tamano;               // tamaño de memoria requerido

    private long ticInicioEspera = 0;        // tic en que empezó a esperar
    private long ultimoTicEjecucion = 0;     // tic de última ejecución
    private long tiempoEjecutado = 0;        // ticks ya ejecutados
    private long tiempoEsperando = 0;        // ticks total en espera
    private boolean interrumpido = false;

    // 🔹 Dirección de inicio en memoria principal
    private int startAddress = -1;

    public Proceso(int id, Tipo tipo, long duracionTotal, long quantum, long tiempoES, int tamano) {
        this.id = id;
        this.tipo = tipo;
        this.duracionTotal = duracionTotal;
        this.quantum = quantum;
        this.tiempoES = tiempoES;
        this.tamano = tamano;
    }

    // 🔹 Getters y setters
    public int getId() { return id; }
    public Tipo getTipo() { return tipo; }
    public long getDuracionTotal() { return duracionTotal; }
    public long getQuantum() { return quantum; }
    public long getTiempoES() { return tiempoES; }
    public int getTamano() { return tamano; }
    public void setTamano(int tamano) { this.tamano = tamano; }

    public boolean isInterrumpido() { return interrumpido; }
    public void setInterrumpido(boolean interrumpido) { this.interrumpido = interrumpido; }

    public int getStartAddress() { return startAddress; }
    public void setStartAddress(int address) { this.startAddress = address; }

    // 🔹 Control de tiempos
    public void setTicInicioEspera(long tic) { this.ticInicioEspera = tic; } //tiempo en el que empezo E/S
    public long getTicInicioEspera(){ return this.ticInicioEspera; }
    public long getUltimoTicEjecucion(){ return this.ultimoTicEjecucion; } //cuando se dejo de ejecutar en que tic iba
    public void setUltimoTicEjecucion(long tic) { this.ultimoTicEjecucion = tic; }

    public void actualizarTiempoEjecutado(long ticActual) {
        tiempoEjecutado += (ticActual - ultimoTicEjecucion);
        ultimoTicEjecucion = ticActual;
    }

    public void actualizarTiempoEsperando(long ticActual) {
        tiempoEsperando += (ticActual - ticInicioEspera);
        ticInicioEspera = ticActual;
    }

    // 🔹 Método reincorporado para compatibilidad con CPU/Scheduler
    public void incrementarTiempoEjecutado() {
        tiempoEjecutado++;
    }

    // 🔹 Consultas de estado
    public long getTiempoEjecutado() { return tiempoEjecutado; }
    public long getTiempoEsperando() { return tiempoEsperando; }

    public boolean estaCompletado() {
        return tiempoEjecutado >= duracionTotal;
    }

    
}
