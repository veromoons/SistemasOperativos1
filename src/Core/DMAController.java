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

/**
 * DMAController: simula un controlador DMA. Puede modelarse con un semáforo
 * si decides limitar la cantidad de canales DMA paralelos.
 *
 * Al completar la transferencia notifica al OperatingSystem (simulando una interrupción).
 */
public class DMAController extends Thread {
    private static final Semaphore dmaChannels = new Semaphore(2, true); // ejemplo: 2 canales DMA paralelos
    private final IODevice device;
    private final MainMemory memory;
    private final Process target;
    private final int sizeMb;
    private final OperatingSystem os;

    public DMAController(IODevice device, MainMemory memory, Process target, int sizeMb, OperatingSystem os) {
        this.device = device;
        this.memory = memory;
        this.target = target;
        this.sizeMb = sizeMb;
        this.os = os;
        setName("DMA-" + target.getName());
    }

    @Override
    public void run() {
        try {
            System.out.println("⚙️ DMA: attempting to acquire channel for " + target.getName());
            dmaChannels.acquire();
            System.out.println("⚙️ DMA: channel acquired for " + target.getName() + ", starting transfer (size=" + sizeMb + "MB).");

            // Simular lectura desde el dispositivo
            device.readData(target.getName(), sizeMb);

            // Simular breve escritura a memoria (no necesitamos reservar memoria adicional aquí,
            // porque la memoria ya suele ser asignada al proceso; si quieres modelar DMA que escribe
            // en buffer extra, puedes usar memory.allocateMemory(...) aquí).
            Thread.sleep(50);

            System.out.println("⚡ DMA: transfer complete for " + target.getName() + ". Releasing channel and notifying OS.");
            os.handleIOCompletion(target); // notificar al OS (simulación de interrupción)
        } catch (InterruptedException e) {
            System.out.println("❌ DMA: interrupted for " + target.getName());
            Thread.currentThread().interrupt();
        } finally {
            dmaChannels.release();
        }
    }

    /**
     * Permite configurar el número de canales DMA si lo deseas (opcional).
     */
    public static void setChannels(int num) {
        // No se puede reasignar fácilmente el Semaphore static sin reiniciar; si necesitas dinámica,
        // crea instancia de controlador con su propio semáforo. De momento dejamos static por simplicidad.
    }
}
