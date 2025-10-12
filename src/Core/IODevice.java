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

/**
 * IODevice: simula un dispositivo de E/S (ej. disco).
 * El método readData simula el tiempo de lectura según tamaño.
 *
 * Comentarios en español.
 */
public class IODevice {
    /**
     * Simula leer 'sizeMb' MB. Ajusta el tiempo por MB según necesites.
     */
    public void readData(String processName, int sizeMb) throws InterruptedException {
        System.out.println("📀 IODevice: reading " + sizeMb + " MB for " + processName + "...");
        // Cada MB tarda 10 ms (ajustable)
        Thread.sleep(sizeMb * 10L);
        System.out.println("📀 IODevice: finished reading " + sizeMb + " MB for " + processName + ".");
    }

    /**
     * Simula escritura (opcional).
     */
    public void writeData(String processName, int sizeMb) throws InterruptedException {
        System.out.println("📀 IODevice: writing " + sizeMb + " MB for " + processName + "...");
        Thread.sleep(sizeMb * 10L);
        System.out.println("📀 IODevice: finished writing " + sizeMb + " MB for " + processName + ".");
    }
}
