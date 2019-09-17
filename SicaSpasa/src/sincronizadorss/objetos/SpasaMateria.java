
package sincronizadorss.objetos;

/**
 *
 * @author degmi
 */

public class SpasaMateria {
    private String materiaRuta;
    private String nombreMat;
    private String nombreDepto;

    public SpasaMateria(String materiaRuta, String nombreMat, String nombreDepto) {
        this.materiaRuta = materiaRuta;
        this.nombreMat = nombreMat;
        this.nombreDepto = nombreDepto;
    }

    public SpasaMateria() {
    }
    
    
    

    public String getMateriaRuta() {
        return materiaRuta;
    }

    public void setMateriaRuta(String materiaRuta) {
        this.materiaRuta = materiaRuta;
    }

    public String getNombreMat() {
        return nombreMat;
    }

    public void setNombreMat(String nombreMat) {
        this.nombreMat = nombreMat;
    }

    public String getNombreDepto() {
        return nombreDepto;
    }

    public void setNombreDepto(String nombreDepto) {
        this.nombreDepto = nombreDepto;
    }
    
    
}
