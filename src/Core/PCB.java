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
enum StatusType {
    New,
    Running,
    Blocked,
    Ready,
    Done,
    ReadySuspended,
    BlockedSuspended,
}

public class PCB {
    private final String id;
    private StatusType status;
    private final String name;
    private String PCstatus; //no se para que
    private String MARstatus; //no se para que
    
    public PCB (String id, String name){
        this.id = id;
        this.status = StatusType.New;
        this.name = name;
        this.PCstatus = null;
        this.MARstatus = null;
        
    }
    
    public String getName(){
        return this.name;
    }
    
    public StatusType getStatus(){
        return this.status;
    }
    
    public void setStatus(StatusType newStatus){
        this.status = newStatus;
    }
}
