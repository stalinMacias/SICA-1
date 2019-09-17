package sica.common;

import java.io.File;

public class Configs{
    
    public static ConfigProperty<String> SERVER = new ConfigProperty<>("hostserver","http://127.0.0.1/sica");  //148.202.89.3  http://localhost/sica
    /*Se cambiara el tiempo de tolerancia de clases a 10 minutos por disposicion oficial del nuevo rector
    -Realizado por Set Martinez Jimenez*/
    public static final long TOLERANCIA_CLASE = 600000L; //millis
    public static final long TOLERANCIA_ADMINISTRATIVOS= 40*60*1000; //millis
    
    public static String SERVER_PHP(){
        return SERVER.get()+"/php/";
    }   
    public static String SERVER_JUSTIF(){
        return SERVER.get()+"/justificantes/";
    }   
    public static String SERVER_CAPTURAS(){
        return SERVER.get()+"/capturas/";
    }   
    public static String SERVER_FOTOS(){
        return SERVER.get()+"/Fotos/";
    }   
    public static String SERVER_IMGS(){
        return SERVER.get()+"/imgs/";
    }
    
    public static String PUSH_NOTIF(){
        return SERVER_PHP()+"pushnotif.html";
    }   
    public static String PHP_UPLOAD(){
        return SERVER_PHP()+"filemanager.php";
    }   
    public static String EMAIL_SENDER(){
        System.out.println("validando archivo php");
        
        String sDirectorio = "http://127.0.0.1/SiCA/php/correoincidencias2.php";
        File f = new File(sDirectorio);
       
           
            return sDirectorio;
            
       
    }   
    public static String SIIAU_LOGIN(){
        return SERVER_PHP()+"siiaulogin.php";
    }   
     
}
