/**
 * Clase que contiene metodos para convetir y desconvertir:
 * -> la descripcion de los departamentos/instancias a sus abreviaciones
 * -> el tipo de usuario a su int
 * -> el status a su int
 */
package sincronizadorss.objetos;

import java.util.List;

/**
 *
 * @author D
 */
public class Convertidor {

    public static String instanciasToCod(List<Departamento> lstInstancias, String instancia) {
        String codigo = "";
        for (Departamento ins : lstInstancias) {
            if (ins.getNombre().equals(instancia)) {
                codigo = ins.getCodigo();
                return codigo;
            }
        }
        return codigo;
    }

    /**
     * Precaución, no ha sido probada aun.
     * @param lstInstancias
     * @param cod
     * @return 
     */
    public static String codToInstancias(List<Departamento> lstInstancias, String cod) {
        String nombre = "";
        for (Departamento ins : lstInstancias) {
            if (ins.getCodigo().equals(cod)) {
                nombre = ins.getNombre();
                return nombre;
            }
        }
        return nombre;
    }


}
