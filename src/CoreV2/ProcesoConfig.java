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

// Clase simple para representar un proceso en el archivo JSON/CSV
public class ProcesoConfig {
    // Campos que corresponden a las columnas/claves
//    private String nombre;
    private int instrucciones;
    private String tipo; // "CPU_BOUND", "IO_BOUND" o "NORMAL"
    private int instruccionesParaES = 0; // Valor por defecto
    private int ciclosParaCompletarES = 0; // Valor por defecto

    // Constructor vacío (necesario para Gson y útil para CSV)
    public ProcesoConfig() {}

    // --- Getters ---
//    public String getNombre() { return nombre; }
    public int getInstrucciones() { return instrucciones; }
    public String getTipo() { return tipo; }
    public int getTamano() { return this.instrucciones; }
    public int getInstruccionesParaES() { return instruccionesParaES; }
    public int getCiclosParaCompletarES() { return ciclosParaCompletarES; }

    // --- Setters (necesarios para la lectura del CSV) ---
//    public void setNombreManual(String nombre) { this.nombre = nombre; }
    public void setInstruccionesManual(int instrucciones) { this.instrucciones = instrucciones; }
    public void setTipoManual(String tipo) { this.tipo = tipo; }
    public void setInstruccionesParaESManual(int instruccionesParaES) { this.instruccionesParaES = instruccionesParaES; }
    public void setCiclosParaCompletarESManual(int ciclosParaCompletarES) { this.ciclosParaCompletarES = ciclosParaCompletarES; }
}