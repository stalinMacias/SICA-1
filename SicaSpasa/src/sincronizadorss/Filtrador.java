
package sincronizadorss;

import java.util.ArrayList;
import java.util.List;
import sincronizadorss.objetos.Logy;
import sincronizadorss.objetos.SpasaCRN;
import sincronizadorss.objetos.SpasaMateria;

/**
 * basado en el diagrama de flujo 'Algoritmo Filtrar SpasaCRNs v0.2.dia'
 * @author degmi
 */

public class Filtrador {
    
    private static int nulles = 0;
    private static String detalles = "";
    //private static boolean ok = true;
    
    private static List<SpasaCRN> CRNBorrados;
    private static List<SpasaCRN> filtrosCRNs;
    //private static List<SpasaCRN> spasaCRNs;
    //private static List<SpasaMateria> spasaMaterias;
    
    //despues de filtrar hay que consultar las listas generadas
    
    public static boolean filtrar(List<SpasaCRN> spasaCRNs, List<SpasaMateria> spasaMaterias){
        //resetear detalles
        detalles = "";
        nulles = 0;
        
        //fase filt-01
        //agregar cada  Materia a cada SpasaCRN
        boolean flagMEncontrada = false;
        for(SpasaCRN crn : spasaCRNs){
            for(SpasaMateria materia : spasaMaterias){
                if(crn.getMateriaRuta().equals(materia.getMateriaRuta())){
                    crn.setMateria(materia);
                    flagMEncontrada = true;
                    break;
                }
            }
            if(!flagMEncontrada){
                Logy.ma("materia de crn no encontrada: " + crn.getMateriaRuta());
            }
        }
        
        
        //inicializar la lista de CRNs borrados
        CRNBorrados = new ArrayList();
        
        //eliminar los crn sin materia, pare fase filt-01
        for(int i=0; i<spasaCRNs.size(); i++){
            if(spasaCRNs.get(i).getMateria() == null){
                CRNBorrados.add(spasaCRNs.get(i));
                nulles++;
            }
        }
        //elimino los elementos hasta despues para evitar un "fuera de rango" en el for anterior
        for(SpasaCRN crn : CRNBorrados){
            if(!spasaCRNs.remove(crn)){
                Logy.ma("no se pudo eliminar el crn: " + crn.getCrnCpr() + " por materia null");
            }
        }
        Logy.m("nulles eliminados: " + nulles);
        
        //fase filt-02 + filt-03
        //revisar cada atributo de la lista spasaCRNs buscando null para eliminarlos
        Logy.m("comienza filtrado eliminando todos los crn que tenga algun null");
        List<SpasaCRN> CRNBorradosAux = new ArrayList();
        for(SpasaCRN crn : spasaCRNs){
            boolean flagBorrar = false;
            if(crn.getCodigoProf() == null || crn.getCodigoProf().isEmpty() || crn.getCodigoProf().equals("null")){
                flagBorrar = true;
            }else if(crn.getCrnCpr() == null || crn.getCrnCpr().isEmpty() || crn.getCrnCpr().equals("null")){
                flagBorrar = true;
            }else if(crn.getDiaHr()== null || crn.getDiaHr().isEmpty() || crn.getDiaHr().equals("null")){
                flagBorrar = true;
            }else if(crn.getHfinHr()== null || crn.getHfinHr().isEmpty() || crn.getHfinHr().equals("null")){
                flagBorrar = true;
            }else if(crn.getHiniHr()== null || crn.getHiniHr().isEmpty() || crn.getHiniHr().equals("null")){
                flagBorrar = true;
            }else if(crn.getMateriaRuta()== null || crn.getMateriaRuta().isEmpty() || crn.getMateriaRuta().equals("null")){
                flagBorrar = true;
            }else if(crn.getNombrePro()== null || crn.getNombrePro().isEmpty() || crn.getNombrePro().equals("null")){
                flagBorrar = true;
            }
            //NOTA: recordar que aula o nombreEsp si puede ser null, por eso no se incluyo
            
            if(flagBorrar){
                CRNBorradosAux.add(crn);
            }
        }
        for(SpasaCRN crn : CRNBorradosAux){ //ahora si a eliminarlos
            if(!spasaCRNs.remove(crn)){
                Logy.ma("no se pudo eliminar el crn: " + crn.getCrnCpr() + " por ATRIBUTO null");
            }
            CRNBorrados.add(crn); //agregarlos tambien a la lista general
            nulles++;
        }
        
        Logy.m("bien, hasta ahorita van elimiandos por null: " + nulles);
        
        //fase filt-04:
        //  filtrado por palabras clave: maestria o doctorado en 'nombrePro'
        CRNBorradosAux.clear();
        for(SpasaCRN crn : spasaCRNs){
            if(crn.getNombrePro().contains("maestria") 
                    || crn.getNombrePro().contains("Maestria")
                    || crn.getNombrePro().contains("Maestría")
                    || crn.getNombrePro().contains("MAESTRIA")
                    || crn.getNombrePro().contains("doctorado")
                    || crn.getNombrePro().contains("Doctorado")
                    || crn.getNombrePro().contains("DOCTORADO")){
                CRNBorradosAux.add(crn);
            }
        }
        //a eliminarlos
        for(SpasaCRN crn : CRNBorradosAux){
            spasaCRNs.remove(crn);
            CRNBorrados.add(crn); //agregarlos tambien a la lista general
            nulles++;
        }
        Logy.m("filtrado por doc o mast: total de nulles: " + nulles);
        
        
        //fase filt-05:
        //  usando filtros de la DB
        Sincronizador.conex.iniciar();
        filtrosCRNs = Sincronizador.conex.obtenerSpasaFiltros();
        Sincronizador.conex.cerrar();
        
        if(filtrosCRNs != null && !filtrosCRNs.isEmpty()){
            Logy.m("genial si obtuvimos la lista de filtros: "  + filtrosCRNs.size());
            
            
            //fase filt-05B

            
            CRNBorradosAux.clear();
            for(SpasaCRN crn : spasaCRNs){
                for(SpasaCRN filt : filtrosCRNs){
                    if(filt.getCodigoProf() != null && !filt.getCodigoProf().isEmpty() && !filt.getCodigoProf().equals("null") ){ 
                        if(filt.getCrnCpr() != null && !filt.getCrnCpr().isEmpty() && !filt.getCrnCpr().equals("null")){
                            if(crn.getCodigoProf() == filt.getCodigoProf() 
                                    && crn.getCrnCpr().equals(filt.getCrnCpr()) ){
                                CRNBorradosAux.add(crn);
                            }
                        }
                    }
                }
            }
            //borrar los crn
            Logy.m("usando filtros de la DB, se procedera a eliminar los crn");
            Logy.m("elimiandos hasta el momento: " + nulles);
            for(SpasaCRN crn: CRNBorradosAux){
                spasaCRNs.remove(crn);
                CRNBorrados.add(crn);
                nulles++;
            }
            Logy.m("eliminados ahora: " + nulles);
            
            //fase filt-06
            //  eliminar contenido en DB en tabla spasa-crnelimiandos
            Sincronizador.conex.iniciar();
            Sincronizador.conex.limpiarSpasaElminados();
            
            //insertar lista CRNBorrados
            Logy.mi("insertando los CRNs borrados en spasa_eliminados en la DB");
            for(SpasaCRN crn : CRNBorrados){
                Sincronizador.conex.insertarSpasaEliminado(crn);
            }
            
            Sincronizador.conex.cerrar();
            Logy.m("hasta aqui todo bien");
            

            
            
        } else {
            Logy.ma("filtros CRNs vacios");
            detalles += "filtros CRNs vacios, ";
            //saltar hasta fase filt-07
        }
        
        //fase filt-07
        //  devolver lista ya filtrada spasasCRNs
        //pues no se ocupa hacer nada, ya esta lista jeje
        
        //fase filt-08
        //  mostrar errores si hubo
        //  de igual manera no se ocupa, con que se consulte -> getDetalles es suficiente
        
        return true;
    }
    
    
    
    
    
    

    public static String getDetalles() {
        return detalles;
    }

    public static void setDetalles(String detalles) {
        Filtrador.detalles = detalles;
    }

    public static int getNulles() {
        return nulles;
    }

    public static void setNulles(int nulles) {
        Filtrador.nulles = nulles;
    }
    
    
    
    
    
}
