
package sincronizadorss;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import sincronizadorss.objetos.Convertidor;
import sincronizadorss.objetos.Departamento;
import sincronizadorss.objetos.Logy;
import sincronizadorss.objetos.SicaCRN;
import sincronizadorss.objetos.SicaHorarioCRN;
import sincronizadorss.objetos.SicaMateria;
import sincronizadorss.objetos.SpasaCRN;
import sincronizadorss.objetos.SpasaMateria;

/**
 * Clase utilizada para poder sincronizar los CRNs, incluyendo horarios y materias, de SiCA con Spasa 
 * @author sicaTeam.d
 */
public class Sincronizador {
    public static ConexionMySQL conex = new ConexionMySQL();
    private static final String urlSpasa = "http://148.202.119.30/php/sica/class.SICA.php"; ////  //"http://www.cuvalles.udg.mx/spasa/php/sica/class.SICA.php?f=getHJ&c=2017A"; "http://www.cuvalles.udg.mx/spasa/php/sica/class.SICA.php?f=getHJ&c=2018A";
    private static final String funHorJson = "getHJ";
    private static final String funMatJson = "getMJ";
    private static final String funDate = "d";
    private static final String funCiclo = "c";
    
    private static boolean ok = true;
    private static String detalles = "";
    private static String cicloLetra = "";

            
    public static void iniciar(){
        principal();
    }
    
    public static void principal(){
        System.out.println("Hola mundo, comenzemos");
        
        
        //verificar cnfig de sincronizacion en la DB
        conex.iniciar();
        byte[] bin = conex.obtenerConfigSincronizar();
        if(binaryToBool(bin)){
            Logy.mi("Configuracion de sincroniacion = true");
            cicloLetra = conex.obtenerCicloLetra();
        }else {
            Logy.mi("Configuracion de sincroniacion = false");
            Logy.mi("No se debe sincronizar, finalizando el programa");
            conex.cerrar();
            return;
        }
        conex.cerrar();
        
        
        //conectarse a http de spasa y consultar movimientos
        Logy.m(hoy());
        Boolean flagSinc = true;
        String html = getContenidoHTML(urlSpasa + "?f=mov&d=" + hoy() ); //hoy()
        Logy.m("html = " + html);
        //es true?
        if(html.length() < 8 && html.length() >0){ //por seguridad
            if(html.contains("true")){
                Logy.m("Si contiene true");
                flagSinc = true;
                
            } else { 
                Logy.m("No contiene true");
                //ok = true
                detalles += "sin movimientos, ";
                insertarLog();
                //finalizar
            }
        } else  {
            Logy.me("Error con el html obtenido de movimientos, longitud rara");
            ok = false;
            detalles += " error html movs, ";
            insertarLog();
            //finalizar
        }
        
        //mas info de JsonArray http://stackoverflow.com/questions/15609306/convert-string-to-json-array
        
        JSONArray spasaCRNJsons = new JSONArray();
        JSONArray spasaMateriaJsons = new JSONArray();
        boolean JsonsFlag = false;
        
        //procede a obtener Jsons de CRNS[ [
        if(flagSinc){
            Logy.m("procediendo a obtener Jsons de Spasa");
            String urlConsulta = urlSpasa + "?f=getHJ&c=" + hoyAnio() + cicloLetra;
            Logy.mi("se hara la consulta -> " + urlConsulta );
            String crns = getContenidoHTML(urlConsulta);
            Logy.m("pasando a Json lo obtenido de spasa crn");
            
            // Quitando caracteres extraños en CRNS
            int i = 0;
            while(crns.charAt(i) != '[') i++;
            
            crns = crns.substring(i, crns.length());            
            //JSONObject jsonObj = new JSONObject(aux); //eso para cuando no son arrays                        
                        
            spasaCRNJsons = new JSONArray(crns);             
            Logy.m("se supone que ya se paso");
            Logy.m("crn_cpr: " + spasaCRNJsons.getJSONObject(0).get("crn_cpr"));
            
            //obteniendo Jsons de Materias
            Logy.m("Obteniendo Jsons de materias");            
            String urlConsultaM = urlSpasa + "?f=getMJ&c=" + hoyAnio() + cicloLetra;
            Logy.mi("se hara la consulta materias -> " + urlConsultaM );
            String materias = getContenidoHTML(urlConsultaM);
            
            // Quitando caracteres extraños en materias
            i = 0;
            while(materias.charAt(i) != '[') i++;
            
            materias = materias.substring(i, materias.length());    
            Logy.m("pasando a Json lo obtenido de spasa materias");
            spasaMateriaJsons = new JSONArray(materias);             
            
            
            //los 2 Jsons son correcots?
            if(spasaCRNJsons != null){
                if(spasaMateriaJsons != null){
                    String r1 = "" + spasaCRNJsons.getJSONObject(0).get("crn_cpr");
                    if(r1 != null && !r1.isEmpty()){
                        Logy.m("r1 -> crn_cpr = " + r1);
                        String r2 = "" + spasaMateriaJsons.getJSONObject(0).get("materia_ruta"); //materia_ruta
                        Logy.m("r2 -> mateiria_ruta = " + r2);
                        if(r2 != null && !r2.isEmpty()){
                            JsonsFlag = true;
                        } else {Logy.me("primer objeto de spasaMateriaJsons es vacio o null");}
                    } else {Logy.me("primer objeto de spsaCRNJsons es vacio o null"); } 
                } else {Logy.me("spasaMateriaJsons es null");}
            } else {Logy.me("spasaCRNJsons es null");}
            
            
        }
        
        List<SpasaCRN> spasaCRNs = new ArrayList();
        List<SpasaMateria> spasaMaterias = new ArrayList();
        boolean listasFlag = false;
        if(JsonsFlag){
            
            
            Logy.m("genial los Jsons estan fabulosos");
            Logy.mi("procediendo a generar listas");
            
            //Pasar los arrayJsons a listas
            //crns
            for (int i = 0; i < spasaCRNJsons.length(); i++) {
                
                
                //*************************************** cambios cesar ******************************
                
                JSONObject jcrn = spasaCRNJsons.getJSONObject(i);
                SpasaCRN crn = new SpasaCRN();
                if(!"EL1".equals(jcrn.get("nombre_esp").toString())){ /* Este if se hizo para filtrar las materias
                    donde el aula es virtual que tiene el nombre_esp = a EL1*/
                crn.setCodigoProf("" + jcrn.get("codigo_prof"));
                crn.setCrnCpr(""+jcrn.get("crn_cpr"));
                crn.setDiaHr(""+jcrn.get("dia_hr"));
                crn.setHfinHr(""+jcrn.get("hfin_hr"));
                crn.setHiniHr(""+jcrn.get("hini_hr"));
                crn.setMateriaRuta(""+jcrn.get("materia_ruta"));
                crn.setNombreEsp(""+jcrn.get("nombre_esp"));
                crn.setNombrePro(""+jcrn.get("nombre_pro"));
                spasaCRNs.add(crn);
                }else{
                    System.out.println("borro este crn:"+jcrn.get("crn_cpr")+" profesor:"+jcrn.get("codigo_prof"));
                }
            }
            //materias
            for (int i = 0; i < spasaMateriaJsons.length(); i++) {
                JSONObject jmat = spasaMateriaJsons.getJSONObject(i);
                SpasaMateria mat = new SpasaMateria();
                mat.setMateriaRuta(""+jmat.get("materia_ruta"));
                mat.setNombreDepto(""+jmat.get("nombre_depto"));
                mat.setNombreMat(""+jmat.get("nombre_mat"));
                spasaMaterias.add(mat);
            }
            
            
            //comprobar que las listas no esten vacias
            Logy.m("spasaCRNs.size() = " + spasaCRNs.size());
            Logy.m("spasaMaterias.size() = " + spasaMaterias.size());
            if(spasaCRNs.size() > 0 ){
                if(spasaMaterias.size() > 0){
                    listasFlag = true;
                } else {
                    Logy.me("lista spasaMaterias vacia tras generar a partir de Jsons");
                    ok = false;
                    detalles += "lista materias vacia, ";
                    insertarLog();
                    return;
                }
            } else {
                Logy.me("lista spasaCRNs vacia tras generar a partir de Jsons"); 
                detalles += "lista crns vacia, ";
                insertarLog();
                return;
            }
            
        } else {
            ok = false;
            detalles += "error obtener Jsons, ";
            insertarLog();
            return; //se finaliza la plaicacion
        }
        
        
        //ahora si a filtrar
        boolean filtradoFlag = false;
        if(listasFlag){
            Logy.m("genial, vamos muy bien, animos animos");
            Filtrador.filtrar(spasaCRNs, spasaMaterias);
            
            //obtener resultados
            detalles += " " + Filtrador.getDetalles() + ", crn borrados: " + Filtrador.getNulles() + ", ";
            filtradoFlag = true;
        }
        
        
        //dividir lista SpasaCRNs en crn y horarioCRN
        boolean convertirFlag = false;
        List<SicaCRN> sicaCRNs = new ArrayList();
        List<SicaHorarioCRN> sicaHorarios = new ArrayList();
        List<SicaMateria> sicaMaterias = new ArrayList();
        if(filtradoFlag){

            
            for(SpasaCRN spac : spasaCRNs){
                SicaCRN sicrn = new SicaCRN();
                SicaHorarioCRN sihor = new SicaHorarioCRN();
                SicaMateria sima = new SicaMateria();
                
                Logy.m("generando sica crn: " + spac.getCrnCpr());
                sicrn.setCrn( Integer.valueOf(spac.getCrnCpr()) );
                sicrn.setUsuario( Integer.valueOf(spac.getCodigoProf()) );
                sicrn.setMateria(spac.getMateriaRuta());
                //sicrn.setAnio( Integer.valueOf(hoyAnio()) ); se setearan en la conexionMySQL
                //sicrn.setCiclo();
                sicaCRNs.add(sicrn);
                
                Logy.m("generando sica horario ");
                sihor.setCrn( Integer.valueOf(spac.getCrnCpr()) );
                sihor.setAula(spac.getNombreEsp());
                sihor.setDia(spac.getDiaHr());
                sihor.setHora(spac.getHiniHr());
                sihor.setDuracion(calcularDuracion(spac.getHiniHr(), spac.getHfinHr()));
                sicaHorarios.add(sihor);
                
                //sigue lo de materias
                Logy.m("generando sica materia");
                sima.setCodigo(spac.getMateriaRuta());
                sima.setDepartamento(spac.getMateria().getNombreDepto());
                sima.setNombre(spac.getMateria().getNombreMat());
                sicaMaterias.add(sima);
            }
            
            //Quitar repetidos y nulles en las listas
            List<SicaCRN> borradosCRNs = new ArrayList();
            List<SicaHorarioCRN> borradosHorarios = new ArrayList();
            List<SicaMateria> borradosMaterias = new ArrayList();
            for (SicaCRN crn : sicaCRNs) {
                boolean primero = false;
                if (crn.getCrn() != 0) {
                    for (SicaCRN crn2 : sicaCRNs) {
                        if (crn.getCrn() == crn2.getCrn()) {
                            if (primero == false) { //esto para no borrar el primer objeto
                                primero = true;
                            } else {
                                Logy.lin();
                                Logy.m("se encontro crn repetido:");
                                Logy.m("crn1 = " + crn.getCrn() + " y cnr2 = " + crn2.getCrn());
                                Logy.m("crn1 = " + crn.getUsuario() + " y cnr2 = " + crn2.getUsuario());
                                Logy.m("crn1 = " + crn.getMateria() + " y cnr2 = " + crn2.getMateria());
                                Logy.m("crn1 = " + crn.getCiclo() + " y cnr2 = " + crn2.getCiclo());
                                Logy.m("crn1 = " + crn.getAnio() + " y cnr2 = " + crn2.getAnio());
                                borradosCRNs.add(crn2);
                            }
                        }
                    }
                } else {
                    //en caso del crn == 0, lo eliminaremos
                    borradosCRNs.add(crn);
                }
            }
            //borrarlos
            Logy.m("se elimanaran los crns borrados");
            for(SicaCRN crn : borradosCRNs){
                sicaCRNs.remove(crn);
            }
            Logy.m("size borradosCRNs: " + borradosCRNs.size());
            
            for(SicaMateria m : sicaMaterias){
                boolean primero = false;
                if(m.getCodigo() != null && !m.getCodigo().isEmpty() && !m.getCodigo().equals("null")){
                    for(SicaMateria m2 : sicaMaterias){
                        if(m.getCodigo().equals(m2.getCodigo()) ){
                            if(primero == false){ //esto para no borrar el primer objeto
                                primero = true;
                            }else{
                                borradosMaterias.add(m2);
                            }
                        }
                    }
                }else{
                    borradosMaterias.add(m);
                }
            }
            //borrarlos 
            for(SicaMateria m : borradosMaterias){
                sicaMaterias.remove(m);
            }
            Logy.m("size borradosMaterias: " + borradosMaterias.size());

            
            for(SicaHorarioCRN h : sicaHorarios){
                boolean primero = false;
                if(h.getCrn() != 0 && h.getHora() != null && h.getDia() != null ){
                    for(SicaHorarioCRN h2 : sicaHorarios){
                        if(h.getCrn() == h2.getCrn() 
                                && h.getDia().equals(h2.getDia()) 
                                && h.getHora().equals(h2.getHora()) 
                        ){
                            if(primero == false){ //esto para no borrar el primer objeto
                                primero = true;
                            }else{
                                borradosHorarios.add(h2);
                            }
                        }
                    }
                }else{
                    borradosHorarios.add(h);
                }
                
            }
            //borrarlos 
            for(SicaHorarioCRN h : borradosHorarios){
                sicaHorarios.remove(h);
            }
            Logy.m("size borradosHorarios: " + borradosHorarios.size());

            convertirFlag = true;
        }
        
        
        
        // aun hay que convertir los nombres de departamentos a sus claves correspondientes
        boolean updateFlag = false;
        if(convertirFlag){
            
            //obtener lista de la DB
            conex.iniciar();
            List<Departamento> depas = conex.obtenerInstancias();
            conex.cerrar();
            
            //eliminar objetos que instancia.codigo sea un numero y no letras
            List<Departamento> borradosDepas= new ArrayList();
            for(Departamento d : depas){
                String c = d.getCodigo();
                if(c.contains("0") || c.contains("1") || c.contains("2")  
                       || c.contains("3") || c.contains("4") || c.contains("5")
                       || c.contains("6") || c.contains("7") || c.contains("8")
                       || c.contains("9") ){
                    borradosDepas.add(d);
                }
            }
            Logy.m("depas todas las instancias size = " + depas.size());
            for(Departamento bd : borradosDepas){
                depas.remove(bd);
            }
            Logy.m("depass puros departamentos, size = ");
            
            //quitar letras en instancia.nombre "DEPTO. " o "DEPTO. DE " 
            Logy.m("quitando palbras DEPTO: y DEPTO. DE, de lista depas");
            for(Departamento d : depas){
                if(d.getNombre().contains("DEPTO. DE")){
                    d.setNombre(d.getNombre().replace("DEPTO. DE ", ""));
                }else if(d.getNombre().contains("DEPTO. ")){
                    d.setNombre(d.getNombre().replace("DEPTO. ", ""));
                }
            }

            
            //usar el convertidor
            for(SicaMateria m : sicaMaterias ){
                m.setDepartamento(Convertidor.instanciasToCod(depas, m.getDepartamento()) );
            }
            
            //mostrar lista depas, borrame solo es para debug
            Logy.lin();
            Logy.m("lista depas codigos");
            for(SicaMateria m : sicaMaterias){
                //Logy.m("mat.dep = " + m.getDepartamento());
            }
            Logy.lin();
            
            updateFlag = true;
        }
        
        
        if(updateFlag){
            //corregir lo de las horas en horariocrns
            corregirHorarios(sicaHorarios);
            
            
            Logy.mi("se actualizaran las materias en la DB");
            conex.iniciar();
            for(SicaMateria m : sicaMaterias){
                 conex.actualizarMateria(m);
            }
            Logy.mi("se actualizaran los CRNs en la DB");
            conex.limpiarCRN();
            for(SicaCRN crn : sicaCRNs){
                 conex.insertarCRN(crn);
            }
            Logy.mi("se actualizaran los horariosCRN en la DB");
            conex.limpiarHorariosCRN();
            for(SicaHorarioCRN h : sicaHorarios){
                conex.insertarHorariosCRN(h);
            }
            
            conex.cerrar();
        }else{
            ok = false;
        }
        
        
        insertarLog();
        //fin?
        
    }
    
    
    private static void insertarLog(){

        
        
        Logy.m("detalles del log a insertar: " + detalles);
        
        if(detalles.length() >= 198){
            Logy.ma("la longitud de los detalles del Log es muy larga: " + detalles.length());
            detalles = detalles.substring(0, 194);
            detalles = detalles + "...";
        }
        
        conex.iniciar();
        conex.insertarSpasaLog(ok, detalles);
        conex.cerrar();
    }
    
    
    private static void corregirHorarios(List<SicaHorarioCRN> horarios){
        
        /*for(SicaHorarioCRN h : horarios){
            if(h.getHora().length() == 1){ //osea es de 1 digitos
                h.setHora( "0" + h.getHora() + ":00:00" );
                
            }else if(h.getHora().length() == 2){
                h.setHora(  h.getHora() + ":00:00" );
            }else{
                Logy.me("error la hora de un SicaHorarioCRN es erronea: crn=" + h.getCrn() + " hora=" +h.getHora());
            }
        }*/
        for (SicaHorarioCRN h: horarios) {
            h.setHora(formatoHora(h.getHora()));
        }
        
    }
    
    
    /**
     * Verifica si un array de bytes (byte[]) corresponde a un string 'true' 
     * @param bin un array de bytes
     * @return true si el bin byte[] equivale a un string 'true'
     */
    private static boolean binaryToBool(byte[] bin){
        
        if(bin != null){
            String bool = "true";
            byte[] myTrue = bool.getBytes(Charset.forName("UTF-8"));

            //info encontrada en http://stackoverflow.com/questions/9499560/how-to-compare-the-java-byte-array
            return Arrays.equals(myTrue, bin);
        }else{
            return false;
        }
    }

    
    //metodo obtenido de http://billyprogramador.blogspot.mx/2011/11/leer-el-contenido-html-de-una-pagina.html
    /**
     * Obtiene en un string el contenido de un pagina web. Metdo obtenio de internet.
     * @param sURL la URL en string de la pagina a consultar
     * @return un string con el resultado. Retorna string vacio en caso de error.
     */
    private static String getContenidoHTML(String sURL) {
        String contenido = "";
        try {
            URL url = new URL(sURL);
            URLConnection uc = url.openConnection();
            uc.connect();
            //Creamos el objeto con el que vamos a leer
            BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                //contenido += inputLine + "\n";
                contenido += inputLine + " "; //ligera modificacion
            }
            in.close();
            return contenido;
        } catch (IOException ex) {
            Logy.me("error tipo IOException: " + ex.getMessage());
            return contenido;
        }
    }
    
    /**
     * metodo para obtener la fechade hoy - 5 hrs en formato yyyy-MM-dd
     * @return la fecha de hoy quitando 5 horas
     */
    private static String hoy(){
        
        Date ahora = new Date();
        long secs = ahora.getTime();
        secs = secs - (5 * 60 * 60 * 1000); //restar 5 horas por si se usa despues e media noche y obtener la hora anterior
        ahora.setTime(secs);
        SimpleDateFormat formateador = new SimpleDateFormat("yyyy-MM-dd");
        String hoy = formateador.format(ahora);
        return hoy;
    }
    
    
    /**
     * metodo para obtener eñ año actual. (Incluye la resta de - 5 hrs)
     * @return el año actual
     */
    private static String hoyAnio(){
        
        Date ahora = new Date();
        long secs = ahora.getTime();
        secs = secs - (5 * 60 * 60 * 1000); //restar 5 horas por si se usa despues e media noche y obtener la hora anterior
        //secs = secs - (3600); //opcion por si se quiere realizar la sincronizacion 1 hora antes del ahora
        ahora.setTime(secs);
        SimpleDateFormat formateador = new SimpleDateFormat("yyyy");
        String hoy = formateador.format(ahora);
        return hoy;
    }
    
    public static String calcularDuracion(String horaIni, String horaSal){        
        horaIni = formatoHora(horaIni);
        horaSal = formatoHora(horaSal);
        
        String duracion = getDuracion(horaIni, horaSal);
        System.out.println("HORA INI: "+horaIni);
        System.out.println("HORA SAL: "+horaSal);                
        System.out.println("DURACION: "+duracion);        
        
        
        if(duracion.length() != 8){
            Logy.me("error extraño al calcular duracion, los digitos de la duracion constan con de mas o de menos digitos");
            Logy.me("no se pudo calcular la duracion de clase, se asigna por default 2:00 hrs");
            duracion = "02:00:00";            
        }                    
        
        return duracion;
    }
    
    public static String getDuracion(String horaIni, String horaFin) {
        String[] horaIniArr = horaIni.split(":");
        String[] horaFinArr = horaFin.split(":");
        
        int minIni = Integer.parseInt(horaIniArr[0])*60 + Integer.parseInt(horaIniArr[1]);
        int minFin = Integer.parseInt(horaFinArr[0])*60 + Integer.parseInt(horaFinArr[1]);
        
        int diferenciaMin = minFin - minIni;
        int diferenciaHora = (int)(diferenciaMin/60);
        
        diferenciaMin -= diferenciaHora*60;
        
        return formatoHora(diferenciaHora+""+diferenciaMin);        
    }
    
    public static String formatoHora(String hora) {        
        if (hora.length() == 3) hora = "0"+hora;
        if (hora.length() == 2) hora = "00"+hora;
        if (hora.length() == 1) hora = "000"+hora;
        String hr = hora.substring(0, 2);
        String min = hora.substring(2, 4);
        
        return hr+":"+min+":00";
    }

    public static boolean isOk() {
        return ok;
    }

    public static void setOk(boolean ok) {
        Sincronizador.ok = ok;
    }

    public static String getDetalles() {
        return detalles;
    }

    public static void setDetalles(String detalles) {
        Sincronizador.detalles = detalles;
    }
    
}
