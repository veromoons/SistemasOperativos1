/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Core;
import java.util.concurrent.Semaphore;

/**
 *
 * @author verol
 */

public class DMAController extends Thread {
//    private static final Semaphore dmaChannels = new Semaphore(2, true);
    private final Semaphore ioSemaphore;
    private final Process process;
    private final int ioTime;
    private final OperatingSystem os;

    public DMAController(Semaphore ioSemaphore, Process process, int ioTime, OperatingSystem os) {
        this.ioSemaphore = ioSemaphore;
        this.process = process;
        this.ioTime = ioTime;
        this.os = os;
        setName("DMA-" + process.getName());
    }

    @Override
    public void run() {
        try {
//            dmaChannels.acquire();
            System.out.println("[DMA] Processing I/O for " + process.getProcessName() + " (" + ioTime + "ms)");
            Thread.sleep(ioTime);
//            process.addIoTime(ioTime);

            // Notificar al SO (simula interrupción)
            os.handleIOCompletion(process);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    
}