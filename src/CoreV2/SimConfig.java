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
    private long duracionCicloMs;

    private int defaultInstrucciones;
    private String defaultTipoProceso; 
    private int defaultCiclosGenExcepcion;
    private int defaultCiclosSatExcepcion;

    public SimConfig() {
        this.duracionCicloMs = 500;

        this.defaultInstrucciones = 10; 
        this.defaultTipoProceso = "CPU bound"; 
        this.defaultCiclosGenExcepcion = 5;
        this.defaultCiclosSatExcepcion = 10;
    }


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
