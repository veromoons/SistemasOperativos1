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
    public enum Tipo { NORMAL, IO_BOUND, CPU_BOUND };
    private final Tipo tipo;
    public enum Estado { NUEVO, LISTO, EJECUCION, BLOQUEADO, LISTOSUSPENDIDO, BLOQUEADOSUSPENDIDO, TERMINADO };
    private Estado estado;
    private final int id;
    private final String nombre;
    private int programCounter; // PC: instrucción actual
    private int memoryAddressRegister; // MAR: última dirección de memoria accedida
    private final int instrucciones;  //cant total de instrucciones
    private final long duracionTotal; // en ticks
    private final long quantum;       // 0 si no tiene quantum
    private final long tiempoES;      // en ticks, si hace E/S
    private int tamano;               // tamaño de memoria requerido
    private long ticInicioEspera = 0;        // tic en que empezó a esperar
    private long ultimoTicEjecucion = 0;     // tic de última ejecución
    private long tiempoEjecutado = 0;        // ticks ya ejecutados
    private long tiempoEsperando = 0;        // ticks total en espera
    private boolean interrumpido = false;
    private int actualInstruccion;   //por la cual va
    private int instruccionesParaES;  //cuantas instrucs tienen que pasar para que agarre ES
                                      // se usa en ejecutarInstruccion de clase CPU. ahi reviso si la instruc actual es igual a esta
    private int ciclosParaCompletarES;  //cuantas instrucs tienen que pasar para que termine ES, le dices al dma SLEEP por tanto tiempo (dma es thread)
                                        // se usa en ejecutarES de la clase DMA.
    private PCB pcb;
    
    private int queuePriority = 0;

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
    
    public int getRemainingTime() {
        return this.instrucciones - this.actualInstruccion;
    }

    // 🔹 Dirección de inicio en memoria principal
    private int startAddress = -1;
    
    public Proceso(int id, Tipo tipo, String nombre, int instrucciones, long quantum, long tiempoES, int tamano, int instruccionesParaES, int ciclosParaCompletarES) { //constructor simplificado
       
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
        this.ciclosParaCompletarES = ciclosParaCompletarES;
        this.pcb = new PCB(id, nombre, this.estado, this.programCounter, this.memoryAddressRegister);
        

    }

    public PCB getPcb() {
        return pcb;
    }

    public int getQueuePriority() {
        return queuePriority;
    }
    

    // 🔹 Getters y setters
    public int getId() { return this.pcb.getId(); }
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
//      System.out.println("[Proceso a validar "+this.id+"] "+"Validando completacion --> tiempo ejecutado: "+ tiempoEjecutado+ " duracionTotal: "+ duracionTotal);
        return tiempoEjecutado >= duracionTotal;
    }

    public Estado getEstado() { return this.pcb.getEstado(); }
    public void setEstado(Estado nuevoEstado) { 
        if (nuevoEstado == Proceso.Estado.NUEVO){
            System.out.printf(
            "📄 [%s] Agregado con éxito a la cola de largo plazo%n",
             nombre );
            return;
        }
        Estado anterior = this.pcb.getEstado();
        this.estado = nuevoEstado; 
        this.pcb.setEstado(nuevoEstado);
        // 🔹 Mostrar cambio de estado
    System.out.printf(
        "📄 [%s] Estado cambiado: %s → %s | PC=%d | MAR=%d%n",
        nombre, anterior, nuevoEstado, this.pcb.getPc(), this.pcb.getMar()
    );
    }

    public String getNombre() {
        return this.pcb.getNombre();
    }
   
    
    public int getProgramCounter() { return this.pcb.getPc(); }
    public void setProgramCounter(int programCounter) { 
        this.programCounter = programCounter;
        this.pcb.setPc(programCounter);
    }

    public int getMemoryAddressRegister() { return this.pcb.getMar(); }
    public void setMemoryAddressRegister(int mar) { 
        this.memoryAddressRegister = mar; 
        this.pcb.setMar(mar);
    }
    public void penalizar() {
        if (this.getQueuePriority() < 2) {
            this.setQueuePriority(this.getQueuePriority() + 1);
        }
    }

    public void setQueuePriority(int queuePriority) {
        this.queuePriority = queuePriority;
    }

}
