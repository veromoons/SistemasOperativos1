/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Core;

/**
 *
 * @author juanr
 */
public class MotorSimulador implements Runnable {

    // --- Componentes del Sistema Operativo ---
    private final MainMemory mainMemory;
    private final Scheduler scheduler;
 
    // --- Colas de Procesos ---

    private final CustomQueue blockedQueue;

    private Process runningProcess;        
    private int globalClock;                
    private int cycleDuration;              
    private volatile boolean isRunning;     
    

    public MotorSimulador(MainMemory mainMemory, Scheduler scheduler) {
        this.mainMemory = mainMemory;
        this.scheduler = scheduler;
        
        this.blockedQueue = new CustomQueue();
        
        this.runningProcess = null;
        this.globalClock = 0;
        this.cycleDuration = 1000;
        this.isRunning = false; 
    }
    
    @Override
    public void run() {
        while (true) { 
            try {

                synchronized (this) {
                    while (!isRunning) {

                        wait();
                    }
                }

                // --- INICIO DE UN CICLO DE RELOJ ---
                System.out.println("------------------------------------");
                System.out.println("🕒 Ciclo de Reloj Global: " + globalClock);

                handleBlockedQueue();

                executeRunningProcess();

                if (runningProcess == null) {
                    scheduleNextProcess();
                }

                globalClock++;
                

                Thread.sleep(cycleDuration);
                // --- FIN DEL CICLO DE RELOJ ---

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Motor de simulación interrumpido.");
                break; 
            }
        }
    }
    

    public void start() {
        isRunning = true;
        new Thread(this).start(); 
    }


    public void pause() {
        isRunning = false;
    }


    public synchronized void resume() {
        isRunning = true;
        notify(); 
    }


    public void setCycleDuration(int ms) {
        this.cycleDuration = ms;
    }
    private void handleBlockedQueue() {

    }

    private void executeRunningProcess() {

        if (runningProcess != null) {
            System.out.println("  -> Ejecutando Proceso: " + runningProcess.getProcessName());

        } else {
            System.out.println("  -> CPU Ocioso");
        }
    }

    private void scheduleNextProcess() {

        CustomQueue readyQueue = mainMemory.getReadyQueue();

        // Verificamos si hay procesos esperando para ser ejecutados.
        if (!readyQueue.isEmpty()) {
            System.out.println("  -> CPU libre. Invocando al planificador...");
            
            Process nextProcess = scheduler.selectNextProcess(readyQueue);

            if (nextProcess != null) {

                this.runningProcess = nextProcess;

                this.runningProcess.getPCB().setStatus(StatusType.Running); 

                System.out.println("  ✅ Planificador seleccionó: " + runningProcess.getProcessName() + " (Política: FCFS)");
            }
        }

    }
}
