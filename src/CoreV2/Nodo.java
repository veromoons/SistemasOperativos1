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
public class Nodo {
    private Proceso proceso;
    private PCB pcb;
    private Nodo siguiente;

    public Nodo(Proceso proceso) {
        this.proceso = proceso;
        this.pcb = null;
        this.siguiente = null;
    }
    
     // Constructor para PCBs
    public Nodo(PCB pcb) {
        this.pcb = pcb;
        this.proceso = null;
        this.siguiente = null;
    }


    public Proceso getProceso() { return proceso; }
    public PCB getPCB() { return pcb; }
    public Nodo getSiguiente() { return siguiente; }
    public void setSiguiente(Nodo siguiente) { this.siguiente = siguiente; }
    
    public boolean contienePCB() {
        return pcb != null;
    }
}

