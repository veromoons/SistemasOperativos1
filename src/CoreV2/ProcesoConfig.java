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

public class ProcesoConfig {

    private int instrucciones;
    private String tipo; 
    private int instruccionesParaES = 0; 
    private int ciclosParaCompletarES = 0; 

    public ProcesoConfig() {}


    public int getInstrucciones() { return instrucciones; }
    public String getTipo() { return tipo; }
    public int getTamano() { return this.instrucciones; }
    public int getInstruccionesParaES() { return instruccionesParaES; }
    public int getCiclosParaCompletarES() { return ciclosParaCompletarES; }


    public void setInstruccionesManual(int instrucciones) { this.instrucciones = instrucciones; }
    public void setTipoManual(String tipo) { this.tipo = tipo; }
    public void setInstruccionesParaESManual(int instruccionesParaES) { this.instruccionesParaES = instruccionesParaES; }
    public void setCiclosParaCompletarESManual(int ciclosParaCompletarES) { this.ciclosParaCompletarES = ciclosParaCompletarES; }
}