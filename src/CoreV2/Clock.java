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
public class Clock {
    private volatile long ticGlobal = 0;
    private final long ticTimeMs;
    private boolean running = false;
    private Thread thread;
    public OperatingSystem so;
    public CPU cpu;

    public Clock(long ticTimeMs) {
        this.ticTimeMs = ticTimeMs;

    }

    public long getTicTimeMs() {
        return ticTimeMs;
    }

    public long getTic() {
        return ticGlobal;
    }

    public void notifySO() {
        this.so.notifyTic();
    }

    public void startClock() {
        running = true;
        thread = new Thread(() -> {
            while (running) {
                try {
                    Thread.sleep(ticTimeMs);
                    ticGlobal++;
//                    cpu.tick(ticGlobal);
                    notifySO();
                    System.out.println("Tic Global: " + ticGlobal);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
       thread.start();
    }

    public void stopClock() {
        running = false;
    }
    
    public void setSO(OperatingSystem so){
        this.so = so;
    }
    
    public void setCPU(CPU cpu){
        this.cpu = cpu;
    }
}

