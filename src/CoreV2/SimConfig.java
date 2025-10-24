/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CoreV2;

/**
 *
 * @author juanr
 */
public class SimConfig {
// Parámetro existente
    private long duracionCicloMs;

    // Nuevos parámetros para defaults de creación de procesos
    private int defaultInstrucciones;
    private String defaultTipoProceso; // Guardaremos "CPU bound" o "I/O bound"
    private int defaultCiclosGenExcepcion;
    private int defaultCiclosSatExcepcion;

    // Constructor con valores por defecto
    public SimConfig() {
        // Valor por defecto existente
        this.duracionCicloMs = 500;

        // Valores por defecto para nuevos parámetros
        this.defaultInstrucciones = 10; // Ejemplo, pon el que prefieras
        this.defaultTipoProceso = "CPU bound"; // Ejemplo
        this.defaultCiclosGenExcepcion = 5; // Ejemplo
        this.defaultCiclosSatExcepcion = 10; // Ejemplo
    }

    // --- Getters y Setters ---

    public long getDuracionCicloMs() {
        return duracionCicloMs;
    }

    public void setDuracionCicloMs(long duracionCicloMs) {
        this.duracionCicloMs = duracionCicloMs;
    }

    public int getDefaultInstrucciones() {
        return defaultInstrucciones;
    }

    public void setDefaultInstrucciones(int defaultInstrucciones) {
        this.defaultInstrucciones = defaultInstrucciones;
    }

    public String getDefaultTipoProceso() {
        return defaultTipoProceso;
    }

    public void setDefaultTipoProceso(String defaultTipoProceso) {
        this.defaultTipoProceso = defaultTipoProceso;
    }

    public int getDefaultCiclosGenExcepcion() {
        return defaultCiclosGenExcepcion;
    }

    public void setDefaultCiclosGenExcepcion(int defaultCiclosGenExcepcion) {
        this.defaultCiclosGenExcepcion = defaultCiclosGenExcepcion;
    }

    public int getDefaultCiclosSatExcepcion() {
        return defaultCiclosSatExcepcion;
    }

    public void setDefaultCiclosSatExcepcion(int defaultCiclosSatExcepcion) {
        this.defaultCiclosSatExcepcion = defaultCiclosSatExcepcion;
    }
    
}
