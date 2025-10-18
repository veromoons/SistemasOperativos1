/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Core;

/**
 *
 * @author juanr
 */
public class CustomQueue {
   
    private class Node {
        public Process process; 
        public Node next;      
        
        public Node(Process process) {
            this.process = process;
            this.next = null; 
        }
    }
    
    private Node head; 
    private Node tail; 


    public CustomQueue() {
        this.head = null; 
        this.tail = null;
    }
    
    public void enqueue(Process process) {
        Node newNode = new Node(process); 

        if (isEmpty()) {

            head = newNode;
            tail = newNode;
        } else {

            tail.next = newNode;

            tail = newNode;
        }
    }
    
    public Process dequeue() {
        if (isEmpty()) {
 
            return null;
        }

        Process processToReturn = head.process;

        head = head.next;

        if (head == null) {
            tail = null;
        }

        return processToReturn;
    }
    
    public boolean isEmpty() {
        return head == null;
    }

// Método para ver el primer proceso sin sacarlo de la cola
    public Process peek() {
        if (isEmpty()) {
            return null;
        }
        return head.process;
    }
}
