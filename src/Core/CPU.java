/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Core;

/**
 *
 * @author verol
 */
public class CPU {
    private int baseQuantum; // quantum estandar que asigna a  c/proceso

    public CPU(int baseQuantum) {
        this.baseQuantum = baseQuantum;
    }

    // ejecuta  proceso usando un hilo
    public void executeProcess(Process process) {
        if (process.isFinished()) return;

        process.setQuantum(baseQuantum); // Asigna quantum
        Thread t = new Thread(process);
        t.start();

        try {
            t.join(); // espera a que termine su turno
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
