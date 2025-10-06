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
public class Mutex {
    private final Semaphore sem = new Semaphore(1); // semforo binario (1 permiso)

    // metodo para adquirir el mutex
    public void acquire() {
        Thread current = Thread.currentThread();
        System.out.println(current.getName() + " intenta adquirir el mutex...");
        try {
            sem.acquire(); // bloq hasta que haya permiso
            System.out.println(current.getName() + " ha adquirido el mutex.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // metodo para liberar el mutex
    public void release() {
        Thread current = Thread.currentThread();
        System.out.println(current.getName() + " libera el mutex.");
        sem.release(); // libera el permiso
    }
}


