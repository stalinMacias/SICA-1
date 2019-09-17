
package sincronizadorss.objetos;

/**
 * Log simple personalizado para el sincronizador.
 * Usar el nivel para visualizar: 0 desactivado :: 1 todos los mensajes :: 2 advertencias :: 3 errores 
 * @author degmi
 */
public class Logy { 
    
    
    
            
        //COlores obtenidos de http://artachone.blogspot.mx/2013/05/java-texto-de-colores-en-la-consola.html
        
        /**
        *
        */
        public static final String ANSI_RESET = "\u001B[0m";
       /**
        * Color Negro, se coloca al inicio de la cadena
        */
        public static final String ANSI_BLACK = "\u001B[30m";
       /**
        * Color Rojo, se coloca al inicio de la cadena
        */
        public static final String ANSI_RED = "\u001B[31m";
       /**
        * Color Verde, se coloca al inicio de la cadena
        */
        public static final String ANSI_GREEN = "\u001B[32m";
       /**
        * Color Amarillo, se coloca al inicio de la cadena
        */
        public static final String ANSI_YELLOW = "\u001B[33m";
       /**
        * Color Azul, se coloca al inicio de la cadena
        */
        public static final String ANSI_BLUE = "\u001B[34m";
       /**
        * Color Purpura, se coloca al inicio de la cadena
        */
        public static final String ANSI_PURPLE = "\u001B[35m";
       /**
        * Color Cyan, se coloca al inicio de la cadena
        */
        public static final String ANSI_CYAN = "\u001B[36m";
       /**
        * Color Blanco, se coloca al inicio de la cadena
        */
        public static final String ANSI_WHITE = "\u001B[37m";
    
    
    
    
    
    
    private static int nivel = 1; 
    /*
    0 -> desactivado
    1 -> todos los mensajes
    2 -> mensaes importantes
    3 -> advertencias en adelante
    4 -> errores
    */
    
    public static void mensaje(String mensaje){
        if(nivel <= 1 && nivel > 0){
            System.out.println("SSS: " + mensaje);
        }
    }
    public static void m(String mensaje){
        if(nivel <= 1 && nivel > 0){
            System.out.println("SSS: " + mensaje);
        }
    }
    
    
    public static void mensajeImportante(String mensaje){
        if(nivel <= 2 && nivel > 0){
            System.out.println("SSS: " + mensaje);
        }
    }
    public static void mi(String mensaje){
        if(nivel <= 2 && nivel > 0){
            System.out.println("SSS: " + mensaje);
        }
    }
    
    
    public static void mensajeAdvertencia(String mensaje){
        if(nivel <= 3 && nivel > 0){
            System.out.println(ANSI_YELLOW + "SSS: !!ADVERTENCIA!! " + mensaje);
        }
    }
    public static void ma(String mensaje){
        if(nivel <= 3 && nivel > 0){
            System.out.println(ANSI_YELLOW + "SSS: !!ADVERTENCIA!! " + mensaje);
        }
    }
    
    
    public static void mensajeError(String mensaje){
        if(nivel <= 4 && nivel > 0){
            System.out.println(ANSI_RED + "SSS: ERROR!!!!: " + mensaje);
        }
    }
    public static void me(String mensaje){
        if(nivel <= 4 && nivel > 0){
            System.out.println(ANSI_RED + "SSS: ERROR!!!!: " + mensaje);
        }
    }
    
    public static void lin(){
        m("-------------------------------------------------------------------------");
    }
    

    public static int getNivel() {
        return nivel;
    }

    /**
     * Segun el nivel son los mensajes mostraods.
     * @param nivel usar 0 = descativado, 1 = todos, 2 = importantes, 3 = advertencias, 4 = errores
     */
    public static void setNivel(int nivel) {
        if(nivel >= 0 && nivel <= 10){
            Logy.nivel = nivel;
        }else{
            mensajeAdvertencia("nivel para MyLog fuera de rango");
        }
    }
    
    
    
}
