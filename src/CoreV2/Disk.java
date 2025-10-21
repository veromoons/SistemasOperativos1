/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CoreV2;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author verol
 */
public class Disk {
    private final Semaphore mutex = new Semaphore(1); // protege acceso concurrente

    // 🔹 Guardar un proceso en disco (long-term)
    public void guardarProceso(Proceso p) {
        try {
            mutex.acquire();
//            colaLargoPlazo.add(p);
            System.out.println("Disco: " + p.getNombre() + " guardado en disco");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            mutex.release();
        }
    }
    
    public void sacarProcesoDisco(Proceso p){
        try {
            mutex.acquire();
            System.out.println("Disco: " + p.getNombre() + " sacado de disco");
        } catch (InterruptedException ex) {
        }finally{
        mutex.release();}
    }
    

    // 🔹 Cargar un proceso desde disco (para pasar a memoria principal)
//    public Proceso cargarProceso() {
//        try {
//            mutex.acquire();
//            Proceso p = colaLargoPlazo.poll();
//            if (p != null) {
//                System.out.println("Disk: Proceso " + p.getId() + " cargado desde disco");
//            }
//            return p;
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//            return null;
//        } finally {
//            mutex.release();
//        }
//    }


    // 🔹 Consultar si hay procesos en disco
//    public boolean hayProcesosLongTerm() {
//        try {
//            mutex.acquire();
//            return !colaLargoPlazo.isEmpty();
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//            return false;
//        } finally {
//            mutex.release();
//        }
//    }
}

