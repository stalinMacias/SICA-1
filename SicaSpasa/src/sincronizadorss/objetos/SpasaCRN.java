/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sincronizadorss.objetos;

/**
 *
 * @author degmi
 */
public class SpasaCRN {
    
    private String codigoProf;
    private String materiaRuta;
    private String crnCpr;
    private String diaHr;
    private String hiniHr;
    private String hfinHr;
    private String nombreEsp;
    private String nombrePro; //nombre programa
    private SpasaMateria materia;

    /**
     * Constructor para instanciar con atributos de una vez. No incluye la SpasMateria.
     * @param codigoProf codigo del profesor
     * @param materiaRuta clave de la materia
     * @param crnCpr CRN de la materia correspondiente al ciclo actual
     * @param diaHr dia de la clase (LUNES,MARTES,MIERCOLES,JUEVES,VIERNES,SABADO,DOMINGO)
     * @param hiniHr hora de inicio de la clase
     * @param hfinHr hora de fin de la clase
     * @param nombreEsp nombre del espacio
     * @param nombrePro nombre del programa
     */
    public SpasaCRN(String codigoProf, String materiaRuta, String crnCpr, String diaHr, String hiniHr, String hfinHr, String nombreEsp, String nombrePro) {
        this.codigoProf = codigoProf;
        this.materiaRuta = materiaRuta;
        this.crnCpr = crnCpr;
        this.diaHr = diaHr;
        this.hiniHr = hiniHr;
        this.hfinHr = hfinHr;
        this.nombreEsp = nombreEsp;
        this.nombrePro = nombrePro;
    }

    /**
     * COnstructor por default. Los atributos seran todos nullos, se dederan setear manualmente.
     */
    public SpasaCRN() {
    }
    
    
    
    

    public String getCodigoProf() {
        return codigoProf;
    }

    public void setCodigoProf(String codigoProf) {
        this.codigoProf = codigoProf;
    }

    public String getMateriaRuta() {
        return materiaRuta;
    }

    public void setMateriaRuta(String materiaRuta) {
        this.materiaRuta = materiaRuta;
    }

    public String getCrnCpr() {
        return crnCpr;
    }

    public void setCrnCpr(String crnCpr) {
        this.crnCpr = crnCpr;
    }

    public String getDiaHr() {
        return diaHr;
    }

    public void setDiaHr(String diaHr) {
        this.diaHr = diaHr;
    }

    public String getHiniHr() {
        return hiniHr;
    }

    public void setHiniHr(String hiniHr) {
        this.hiniHr = hiniHr;
    }

    public String getHfinHr() {
        return hfinHr;
    }

    public void setHfinHr(String hfinHr) {
        this.hfinHr = hfinHr;
    }

    public String getNombreEsp() {
        return nombreEsp;
    }

    public void setNombreEsp(String nombreEsp) {
        this.nombreEsp = nombreEsp;
    }

    /**
     * Nombre del programa
     * @return 
     */
    public String getNombrePro() {
        return nombrePro;
    }

    /**
     * Nombre del programa
     * @param nombrePro 
     */
    public void setNombrePro(String nombrePro) {
        this.nombrePro = nombrePro;
    }

    public SpasaMateria getMateria() {
        return materia;
    }

    public void setMateria(SpasaMateria materia) {
        this.materia = materia;
    }
    
    
    
}
