/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CoreV2;
import CoreV2.AlgorithmsStrategies.ISchedulingAlgorithm;

import java.util.concurrent.Semaphore;
import sistemasoperativos1.SimuladorGUI;
import javax.swing.SwingUtilities;
//import java.util.*;


/**
 *
 * @author verol
 */
// 🔹 Sistema operativo que maneja colas, CPU, memoria y disco
public class OperatingSystem {
    private final Scheduler scheduler;
    private final CPU cpu;
    private final MainMemory memory;
    private final Disk disk;
    private final DMA dma;
    private final Clock clock;
    public int processCounter;
    private SimuladorGUI gui;
    
    private Cola colaNuevos = new Cola();
    private Cola colaListos = new Cola();
    private Cola colaBloqueados = new Cola();
    private Cola colaTerminados = new Cola();
    private Cola colaListoSuspendido = new Cola();
    private Cola colaBloqueadoSuspendido = new Cola();
    
//    private Queue<Proceso> colaNuevos = new LinkedList<>();
//    private Queue<Proceso> colaListos = new LinkedList<>();
//    private Queue<Proceso> colaBloqueados = new LinkedList<>();
//    private Queue<Proceso> colaTerminados = new LinkedList<>();
//    private Queue<Proceso> colaListoSuspendido = new LinkedList<>();
//    private Queue<Proceso> colaBloqueadoSuspendido = new LinkedList<>();
    private Thread quantumThread;
    private boolean stopQuantumThread;
    

    private final Semaphore mutex = new Semaphore(1); // protege acceso concurrente

    private int stat_procesosTotalesTerminados = 0;
    private int stat_ioBoundTerminados = 0;
    private int stat_cpuBoundTerminados = 0;

    public OperatingSystem(CPU cpu, MainMemory memory, Disk disk, DMA dma, Scheduler scheduler, Clock clock) {
        this.cpu = cpu;
        this.memory = memory;
        this.disk = disk;
        this.dma = dma;
        this.scheduler = scheduler; 
        this.clock = clock;
        this.processCounter = 1;
        this.stopQuantumThread = false;
    }
    
    public void setGUI(SimuladorGUI gui) {
        this.gui = gui;
    }
    
    //NO IO Bound
    public void crearProceso(int id, Proceso.Tipo tipo, int instrucciones, int tamano, long tiempoES, int prioridad) {
//        System.out.println("entro A CRar");
        String nombre = "P"+processCounter;
        processCounter++;
        Proceso p = new Proceso(id, tipo, nombre, instrucciones, this.cpu.getQuantumCiclos(),tiempoES, tamano, 0, 0);
        p.setEstado(Proceso.Estado.NUEVO);
        moverANuevos(p);
        this.agregarProceso(p);
    }
    
    //IO Bound
    public void crearProceso(int id, Proceso.Tipo tipo, int instrucciones, int tamano, long tiempoES, int prioridad, int instruccionesParaES, int ciclosParaCopletarES) {
//        System.out.println("entro A CRar");
        String nombre = "P"+processCounter;
        processCounter++;

        Proceso p = new Proceso(id, tipo, nombre, instrucciones, this.cpu.getQuantumCiclos(),tiempoES, tamano, instruccionesParaES, ciclosParaCopletarES);
        p.setEstado(Proceso.Estado.NUEVO);
        
        logEvent("Proceso " + p.getNombre() + " creado.");
        // ...
        if (memory.cargarProceso(p)) {
             logEvent("Proceso " + p.getNombre() + " cargado en memoria principal (LISTO).");
             // ...
        } else {
             logEvent("Memoria llena. Intentando suspender para Proceso " + p.getNombre() + ".");
             // ...
        }
        
        moverANuevos(p);
        this.agregarProceso(p);
    }

    // 🔹 Agregar proceso al sistema
    public void agregarProceso(Proceso p) {
        try {
            
//        System.out.println(colaListos.size());
            mutex.acquire();
            // Intentamos cargar en memoria principal
            if (memory.cargarProceso(p)) {
//                memory.agregarAColaCortoPlazo(p);
                System.out.println("SO: " + p.getNombre() + " cargado en memoria principal");
                colaNuevos.remove(p);
                p.setEstado(Proceso.Estado.LISTO);
                
//        System.out.println("---"+colaListos.size());
                moverAColaListos(p);
//        System.out.println("+++"+colaListos.size());
            } else{
                verificarYSuspenderProcesos(p);
            }
//            else {
//                // Si no hay espacio, lo mandamos a disco
//                p.setEstado(Proceso.Estado.LISTOSUSPENDIDO);
//                colaNuevos.remove(p);
//                moverAListoSuspendidos(p);
//                
//                disk.guardarProceso(p);
//            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            mutex.release();
        }
    }

    // 🔹 Asignar proceso a CPU si está disponible
//    public void asignarProcesoACPU() {
//        System.out.println(!cpu.estaOcupado()+" "+ !scheduler.hayProcesos());
//        if (!cpu.estaOcupado() || !scheduler.hayProcesos()) return;
//        
//        Proceso siguiente = scheduler.obtenerSiguienteProceso();
//        if (siguiente != null) {
//            cpu.asignarProceso(siguiente, clock.getTic());
//            siguiente.setEstado(Proceso.Estado.EJECUCION);
//            System.out.println("Proceso a ejecutar según planificador (algoritmo) --> "+siguiente);
//        }
//    }

    

    // 🔹 Interrumpir proceso (ej. por quantum)
    public void interrumpirProceso(Proceso p) {
        moverAColaListos(p);
    }

    // 🔹 Proceso finalizado
    public void procesoFinalizado(Proceso p) {
        p.setEstado(Proceso.Estado.TERMINADO);
        moverATerminados(p);
        memory.liberarProceso(p);
        logEvent("Proceso " + p.getNombre() + " pasa a TERMINADO y se libera memoria."); // <-- Añadir log
        System.out.println("SO: " + p.getNombre() + " finalizado y liberado de memoria");
        
        this.stat_procesosTotalesTerminados++;
        if (p.getTipo() == Proceso.Tipo.IO_BOUND) {
            this.stat_ioBoundTerminados++;
        } else if (p.getTipo() == Proceso.Tipo.CPU_BOUND) {
            this.stat_cpuBoundTerminados++;
        }
    }

    // 🔹 Bloquear proceso por E/S
    public void bloquearProcesoES(Proceso p) {
        logEvent("Proceso " + p.getNombre() + " pasa a BLOQUEADO por E/S."); // <-- Añadir log
        System.out.println("SO: " + p.getNombre() + " bloqueado por E/S");
        p.setEstado(Proceso.Estado.BLOQUEADO);
        moverABloqueados(p);
        dma.ejecutarES(p, () -> {
            logEvent("DMA: E/S completada para Proceso " + p.getNombre() + "."); // <-- Añadir log
            System.out.println("SO: E/S completada para " + p.getNombre());
            if (p.getEstado()==Proceso.Estado.BLOQUEADO){
                this.colaBloqueados.remove(p);
                p.setEstado(Proceso.Estado.LISTO);
                logEvent("Proceso " + p.getNombre() + " pasa de BLOQUEADO a LISTO."); // <-- Añadir log
                moverAColaListos(p);
            } else if (p.getEstado()==Proceso.Estado.BLOQUEADOSUSPENDIDO){
                this.colaBloqueadoSuspendido.remove(p);
                p.setEstado(Proceso.Estado.LISTOSUSPENDIDO);
                logEvent("Proceso " + p.getNombre() + " pasa de BLOQUEADOSUSPENDIDO a LISTOSUSPENDIDO."); // <-- Añadir log
                moverAListoSuspendidos(p);
            }
//            validacionAgregarAlCPU(siguiente);
        });
    }

    // 🔹 Cambiar algoritmo de planificación
    public void setAlgoritmo(ISchedulingAlgorithm algoritmo) {
        scheduler.setAlgoritmo(algoritmo);
        logEvent("SO: Algoritmo de planificación cambiado a " + algoritmo.getSchedulingType()); // <-- Añadir log
        System.out.println("SO: Algoritmo de planificación cambiado");
    }
    
    public void startQuantumTime(){
        this.quantumThread = new Thread(()->{
            while(this.cpu.getActualQuantumCiclosCounter() > 0 && !stopQuantumThread){
                try {
                    Thread.sleep(clock.getTicTimeMs());
//                    System.out.println(this.cpu.getActualQuantumCiclosCounter());
                    this.cpu.decreaseQuantumCounter();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            if(!stopQuantumThread){
                this.cpu.throwInterruptToCPU();
                System.out.println("Interrupcion por quantum de "+ this.cpu.getQuantumCiclos()+ "ms excedido");
            }
            this.cpu.resetQuantumCounter();
            this.stopQuantumThread = false;
        });
        this.quantumThread.start();
    }
    
    public void stopQuantumTime(){
        this.stopQuantumThread = true;
        this.cpu.resetQuantumCounter();
    }
    
    public void validacionAgregarAlCPU(Proceso siguiente){
        if (siguiente != null) {
            cpu.asignarProceso(siguiente, clock.getTic());
            System.out.println("Proceso a ejecutar según planificador (algoritmo) --> "+siguiente.getNombre());
            siguiente.setEstado(Proceso.Estado.EJECUCION);
            logEvent("Proceso " + siguiente.getNombre() + " pasa a EJECUCIÓN."); // <-- Añadir log
            //System.out.println(this.cpu.getActualQuantumCiclosCounter()); //descomentar solo cuando no quiera funcionar bien el quantum!!!
            if (this.cpu.getQuantumCiclos()>0){            
                startQuantumTime();
            }
        }
    }
    public void notifyTic() {
        try {
            mutex.acquire();
            if (cpu.getProcesoActual() != null) {
                
                Proceso actual = cpu.getProcesoActual();
            // 💡 Simulación de avance de PC y MAR solo si está en ejecución
            if (actual.getEstado() == Proceso.Estado.EJECUCION) {
                actual.incrementarPCyMAR(); // <-- 👈 actualización simulada
            }
                cpu.ejecutarInstruccion( this);
            }
            // 🔹 Actualizamos tiempos de espera para los procesos en cola de corto plazo
            colaListos.forEach(p -> p.actualizarTiempoEsperando(clock.getTic()));
//            for (Proceso p : this.colaListos) {
////                System.out.println(this.colaListos.size());
//                p.actualizarTiempoEsperando(clock.getTic());
//            }

            // 🔹 Intentamos asignar proceso a CPU
            if (!cpu.estaOcupado() && scheduler.hayProcesos()) {
                Proceso siguiente = scheduler.obtenerSiguienteProceso();
                if (siguiente != null) {
//                  Proceso siguiente = scheduler.obtenerSiguienteProceso();
//                    if (siguiente != null) {
//                        cpu.asignarProceso(siguiente, clock.getTic());
//                        siguiente.setEstado(Proceso.Estado.EJECUCION);
//                        System.out.println("Proceso a ejecutar según planificador (algoritmo) --> "+siguiente);
//                    }
                    logEvent("Planificador selecciona Proceso " + siguiente.getNombre() + "."); // <-- Añadir log
                    validacionAgregarAlCPU(siguiente);
                }
            }
            
            verificarMoverListosSuspendidosAListos();
            
            if (this.gui != null) {
                // Pide al hilo de la GUI que ejecute la actualización
                SwingUtilities.invokeLater(() -> {
                    gui.actualizarGUI();
                });
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            mutex.release();
        }
    }
    
    public void verificarMoverListosSuspendidosAListos(){
        int cantidadDeListosSuspendidos = this.colaListoSuspendido.size();
        for (int i = 0; i<cantidadDeListosSuspendidos; i++){
            Proceso pActual = this.colaListoSuspendido.poll(); //aqui ya se hace remove con poll
            boolean procesoCargadoEnMainMemory = this.memory.cargarProceso(pActual);
            if (procesoCargadoEnMainMemory){
                this.colaListos.add(pActual);
                pActual.setEstado(Proceso.Estado.LISTO);
                disk.sacarProcesoDisco(pActual);
                logEvent("SO: Proceso " + pActual.getNombre() + " reactivado (LISTOSUSPENDIDO -> LISTO)."); // <-- Añadir log
            }else {
                this.colaListoSuspendido.add(pActual);
                
            }
        }
    }
    // 🔹 Verifica si hay falta de memoria y suspende procesos si es necesario
    public void verificarYSuspenderProcesos(Proceso p) {
        if (!memory.findAvailableBlock(p)) {
            Proceso candidato = null;
            
            boolean keepVerifying = true;
            
            while (keepVerifying){
                if (!colaBloqueados.isEmpty()) {
                candidato = colaBloqueados.poll(); // Saca el primero bloqueado
                } 
                // Si no hay bloqueados, pasa nuevo proceso a Listo Susp (menos ideal, pero necesario)
                else {
                    candidato = null;
                    // Si no hay espacio, lo mandamos a disco
                    colaNuevos.remove(p);
                    p.setEstado(Proceso.Estado.LISTOSUSPENDIDO);
                    moverAListoSuspendidos(p);
                    disk.guardarProceso(p);
                    keepVerifying = false;
                    break;
                }

                if (candidato != null) {
                    this.memory.liberarProceso(candidato);
                    suspenderProceso(candidato);
//                    System.out.println("🔸 Proceso " + candidato.getNombre() + " suspendido por falta de memoria.");
                    if (memory.hayEspacioDisponible() && this.memory.findAvailableBlock(p)){
                        this.memory.cargarProceso(p);
                        p.setEstado(Proceso.Estado.LISTO);
                        this.moverAColaListos(p);
                        keepVerifying = false;
                        break;
                    }
                    candidato = null; 
                }
            }
            // Primero busca un proceso bloqueado (porque suspender bloqueados es más realista)
            
        }
    }

    // 🔹 Suspende un proceso (lo pasa de memoria principal a disco)
    public void suspenderProceso(Proceso p) {
//        colaBloqueados.remove(p); //ya se hace POLL de candidato
        memory.liberarProceso(p);
        p.setEstado(Proceso.Estado.BLOQUEADOSUSPENDIDO);
        moverABloqueadoSuspendidos(p);
        disk.guardarProceso(p);
        logEvent("SO: Proceso " + p.getNombre() + " suspendido (BLOQUEADOSUSPENDIDO) por falta de memoria."); // <-- Añadir log
        System.out.println("SO: " + p.getNombre() + " suspendido (bloqueado/suspendido) para liberar memoria.");
    }
    
//    // 🔹 Reactiva un proceso suspendido si hay memoria libre en bloqueados
//    public void reactivarProcesosBloqueadoSuspendidos() {
//        if (!colaBloqueadoSuspendido.isEmpty()) {
//            Proceso p = colaBloqueadoSuspendido.peek();
//            if (memory.cargarProceso(p)) {
//                colaBloqueadoSuspendido.remove(p);
//                p.setEstado(Proceso.Estado.BLOQUEADO);
//                moverABloqueados(p);
//                System.out.println("SO: " + p.getNombre() + " reactivado y cargado en memoria en lista de Bloqueados.");
//            }
//        }
//    }

    public Cola getColaNuevos() {
        return colaNuevos;
    }

    public Cola getColaListos() {
        return colaListos;
    }

    public Cola getColaBloqueados() {
        return colaBloqueados;
    }

    public Cola getColaTerminados() {
        return colaTerminados;
    }

    public Cola getColaListoSuspendido() {
        return colaListoSuspendido;
    }

    public Cola getColaBloqueadoSuspendido() {
        return colaBloqueadoSuspendido;
    }
    
    // ----- Métodos de transición ----- No se cambian estados porque se cambian en los metodos
    public void moverANuevos(Proceso p) {
        colaNuevos.add(p);
    }
    
    // 🔹 Mover proceso a cola de listos (después de interrupción)
    public void moverAColaListos(Proceso p) {
//        scheduler.agregarProcesos(p);
        colaListos.add(p);
//        System.out.println(colaListos.size());
        logEvent("Proceso " + p.getNombre() + " movido a Cola de Listos."); // <-- Añade/Verifica este log
        System.out.println("SO: " + p.getNombre() + " pasa a la cola de listos");
    }

    public void moverABloqueados(Proceso p) {
        colaBloqueados.add(p);
    }

    public void moverAListoSuspendidos(Proceso p) {
        colaListoSuspendido.add(p);
    }

    public void moverABloqueadoSuspendidos(Proceso p) {
        colaBloqueadoSuspendido.add(p);
    }

    public void moverATerminados(Proceso p) {
        colaTerminados.add(p);
    }
    
    public void setCPUQuantum(int quantum){
        this.cpu.setQuantumCiclos(quantum);
    }

    public CPU getCpu() {
        return this.cpu;
    }

    public Clock getClock() {
        return this.clock;
    }
    
    public int getStatProcesosTotalesTerminados() {
    return this.stat_procesosTotalesTerminados;
    }

    public int getStatIoBoundTerminados() {
        return this.stat_ioBoundTerminados;
    }

    public int getStatCpuBoundTerminados() {
        return this.stat_cpuBoundTerminados;
    }
    
    public int getProcessCounter() {
        return this.processCounter;
    }

    public void setDuracionCiclo(long nuevoTiempoMs) {
        // Simplemente pasa la llamada al clock
        this.clock.setTicTimeMs(nuevoTiempoMs);
    }
    
    public void logEvent(String message) {
    // Asegurarnos de que tenemos una GUI y el área de log existe
        if (this.gui != null && this.gui.getAreaLog() != null) {
            // Formatear el mensaje con el tick actual
            String logMessage = String.format("[Tick %d] %s%n", clock.getTic(), message);

            // Usar invokeLater para actualizar la GUI desde el hilo correcto
            SwingUtilities.invokeLater(() -> {
                gui.getAreaLog().append(logMessage);
                // Opcional: Auto-scroll hacia abajo
                gui.getAreaLog().setCaretPosition(gui.getAreaLog().getDocument().getLength());
            });
        } else {
            // Si no hay GUI, imprimir en consola como fallback
            System.out.printf("[Tick %d] %s%n", clock.getTic(), message);
        }   
    }
}




