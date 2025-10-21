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
public class DMA {
    private final Semaphore mutex = new Semaphore(1); // protege acceso concurrente
    private boolean running = false;
    private final long unidadTiempoMs; // duración de un tick
    private Runnable onESComplete; // callback al completar E-S

    public DMA(long unidadTiempoMs) {
        this.unidadTiempoMs = unidadTiempoMs;
    }

    // 🔹 Ejecutar operación de E-S para un proceso
    public void ejecutarES(Proceso p, Runnable callback) {
        new Thread(() -> {
            try {
                mutex.acquire();
                running = true;
                this.onESComplete = callback;

                long tiempoES = p.getCiclosParaCompletarES(); // en ticks
//                System.out.println(unidadTiempoMs+"---"+tiempoES);
                Thread.sleep(unidadTiempoMs*tiempoES);
//                for (long i = 0; i < tiempoES; i++) {
//                    Thread.sleep(unidadTiempoMs); // simula tiempo de E-S
//                }

                System.out.println("DMA: Operación E/S completada → genera interrupción al SO (" + p.getNombre() + ")");

                if (onESComplete != null) {
                    onESComplete.run(); // notificar al SO
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                running = false;
                mutex.release();
            }
        }).start();
    }

    public boolean estaOcupado() {
        return running;
    }
}

