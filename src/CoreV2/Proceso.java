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
    private final Tipo tipo;
    public enum Estado { NUEVO, LISTO, EJECUCION, BLOQUEADO, LISTOSUSPENDIDO, BLOQUEADOSUSPENDIDO, TERMINADO };
    private Estado estado;
    private final int id;
    private final String nombre;
    private final int instrucciones;
    private final long duracionTotal; // en ticks
    private final long quantum;       // 0 si no tiene quantum
    private final long tiempoES;      // en ticks, si hace E/S
    private int tamano;               // tamaño de memoria requerido
    private long ticInicioEspera = 0;        // tic en que empezó a esperar
    private long ultimoTicEjecucion = 0;     // tic de última ejecución
    private long tiempoEjecutado = 0;        // ticks ya ejecutados
    private long tiempoEsperando = 0;        // ticks total en espera
    private boolean interrumpido = false;
    private int programCounter; // PC: instrucción actual
    private int memoryAddressRegister; // MAR: última dirección de memoria accedida
    private int actualInstruccion;
    private int instruccionesParaES;
    private int ciclosParaCompletarES;

    public int getActualInstruccion() {
        return actualInstruccion;
    }
    
    public int nextInstruccion() {
        return actualInstruccion++;
    }
    
    public boolean isLastInstruccion() {
        return actualInstruccion >= instrucciones;
    }

    public int getInstruccionesParaES() {
        return instruccionesParaES;
    }

    public int getCiclosParaCompletarES() {
        return ciclosParaCompletarES;
    }

    // 🔹 Dirección de inicio en memoria principal
    private int startAddress = -1;
    
    public Proceso(int id, Tipo tipo, String nombre, int instrucciones, long quantum, long tiempoES, int tamano, int instruccionesParaES, int ciclosParaCopletarES) { //constructor simplificado
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.instrucciones = instrucciones;
        this.actualInstruccion = 0;
        this.duracionTotal = instrucciones;  //Todas las instrucciones se ejecutan en un único ciclo de instrucción
        this.quantum = quantum;
        this.tiempoES = tiempoES;
        this.tamano = tamano;
        this.estado = Estado.NUEVO;
        this.programCounter = 0;
        this.memoryAddressRegister = -1;
        this.instruccionesParaES = instruccionesParaES;
        this.ciclosParaCompletarES = ciclosParaCopletarES;
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
//        System.out.println("[Proceso a validar "+this.id+"] "+"Validando completacion --> tiempo ejecutado: "+ tiempoEjecutado+ " duracionTotal: "+ duracionTotal);
        return tiempoEjecutado >= duracionTotal;
    }

    public Estado getEstado() { return estado; }
    public void setEstado(Estado nuevoEstado) { 
        if (nuevoEstado == Proceso.Estado.NUEVO){
            System.out.printf(
            "📄 [%s] Agregado con éxito a la cola de largo plazo%n",
             nombre );
            return;
        }
        Estado anterior = this.estado;
        this.estado = nuevoEstado; 
        // 🔹 Mostrar cambio de estado
    System.out.printf(
        "📄 [%s] Estado cambiado: %s → %s | PC=%d | MAR=%d%n",
        nombre, anterior, nuevoEstado, programCounter, memoryAddressRegister
    );
    }

    public String getNombre() {
        return nombre;
    }
   
    
    public int getProgramCounter() { return programCounter; }
    public void setProgramCounter(int programCounter) { this.programCounter = programCounter; }

    public int getMemoryAddressRegister() { return memoryAddressRegister; }
    public void setMemoryAddressRegister(int mar) { this.memoryAddressRegister = mar; }

}
