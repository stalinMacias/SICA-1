/**
 * Es necesario primero ejecutar iniciar() para iniciar conexion y
 * despues cerrar(); para cerrar la conexion.
 */
package sincronizadorss;

import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import sincronizadorss.objetos.Departamento;
import sincronizadorss.objetos.Logy;
import sincronizadorss.objetos.SicaCRN;
import sincronizadorss.objetos.SicaHorarioCRN;
import sincronizadorss.objetos.SicaMateria;
import sincronizadorss.objetos.SpasaCRN;
import sincronizadorss.objetos.SpasaMateria;

//comparaciones
/**
 *
 * @author CuValleS
 */
public class ConexionMySQL {

    private static Connection Conexion;
    private static final String serverMySQL = "127.0.0.1";
    private static final String usuarioGlobal = "frank"; //sica
    private static final String claveGlobal = "frankvalles65"; //Sic@2711.#/
    private static final String DBnombreGlobal = "checador";
    private boolean conectado = false;

    /**
     * Para inciar la conexion con MySQL. Actualmente se encuentra por defautl
     * "localhost" como la url del servidor. Se requieren tres parametros:
     *
     * @param user : usuario de la base de datos
     * @param pass : password del mismo
     * @param db_name : nombre de la base de datos
     */
    public void iniciar(String user, String pass, String db_name) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Conexion = DriverManager.getConnection("jdbc:mysql://" + serverMySQL + ":3306/" + db_name, user, pass);
            conectado = true;
            printLog("Conexion establecida con el sevidor de MySQL en " + serverMySQL);
            //JOptionPane.showMessageDialog(null, "Bien");
        } catch (ClassNotFoundException ex) {
            Logy.me(ex.getMessage());
        } catch (SQLException ex) {
            Logy.me(ex.getMessage());
        }
    }

    /**
     * Metodo default para iniciar la comunicación con el servidor MySQL. Como
     * es por default lleva los valores "Global" por default usados en este
     * momento.
     */
    public void iniciar() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Conexion = DriverManager.getConnection("jdbc:mysql://" + serverMySQL + ":3306/" + DBnombreGlobal, usuarioGlobal, claveGlobal);
            conectado = true;
            printLog("Conexion establecida con el sevidor de MySQL en " + serverMySQL);
            //JOptionPane.showMessageDialog(null, "Bien");
        } catch (ClassNotFoundException ex) {
            Logy.me(ex.getMessage());
        } catch (SQLException ex) {
            Logy.me(ex.getMessage());
        }
    }

    /**
     * Metodo para cerrar la conexion con la base de datos. Muy recomendado
     * usarla tras terminar de usar la conexión. No lleva parametros ni
     * retornos.
     */
    public void cerrar() {
        try {
            Conexion.close();
            printLog("Conexion cerreda con MySQL en " + serverMySQL);
            conectado = false;
        } catch (SQLException ex) {
            printLog("Error al cerrar conexion con MySQL: " + ex.toString());
        }
    }

    //Lo primero que necesiamos es consular la configuracion de sincronizar
    /**
     * Consulta en 'configuraciones' la de sincronizar.
     *
     * @return retorna en byte[] el resultado (el cual se supone debe er un true
     * o false de string a binario). En caso de error retorna null.
     */
    public byte[] obtenerConfigSincronizar() {

        if (conectado) {
            try {
                String bool = "false";
                byte[] myTrue = bool.getBytes(Charset.forName("UTF-8"));

                String Query = "SELECT * FROM `configuraciones` ";
                Logy.mi("Consulta de configuraciones");
                Logy.mi("Query: " + Query);
                Statement st = Conexion.createStatement();
                ResultSet resultSet;
                resultSet = st.executeQuery(Query);
                while (resultSet.next()) {
                    if (resultSet.getString("configuracion").equals("sincronizar")) {
                        return resultSet.getBytes("valor");
                    }

                }
                Logy.mi("al parecer no se encontro la configuracion SINCRONIZAR en la DB");
                return null;

            } catch (SQLException ex) {
                Logy.me("No se pudo consultar las configuraciones de la DB: " + ex.getMessage());
                return null;
            }
        } else {
            Logy.me("No se ha establecido previamente una conexion a la DB");
            return null;
        }
    }

    /**
     * Llama la funcion CURRENT_CICLO() de la DB que retorna la letra del ciclo
     * correpondiente
     *
     * @return A o B dek ciclo acutla. Retorna NULL en caso de error.
     */
    public String obtenerCicloLetra() {

        if (conectado) {
            try {
                String letra = "";

                String Query = "SELECT `CURRENT_CICLO`() AS `CURRENT_CICLO`;  ";
                Logy.mi("Query: " + Query);
                Statement st = Conexion.createStatement();
                ResultSet resultSet;
                resultSet = st.executeQuery(Query);

                if (resultSet.next()) {
                    letra = resultSet.getString("CURRENT_CICLO");
                    Logy.m("letra obtenida ------------ : " + letra);
                } else {
                    Logy.me("no se obtuvieron resultados de CURRENT_CICLO()");
                }

                if (letra != null && (letra.equals("A") || letra.equals("B"))) {
                    return letra;
                } else {
                    Logy.me("error al obtener la letra del CICLO de DB");
                    return null;
                }

            } catch (SQLException ex) {
                Logy.me("No se pudo consultar CURRENT_CICLO`() de la DB: " + ex.getMessage());
                return null;
            }
        } else {
            Logy.me("No se ha establecido previamente una conexion a la DB");
            return null;
        }
    }

    /**
     * Obtiene los CRNs que serviran de filtro para la sincronizacion con Spasa.
     *
     * @return la lista de SpasaCRNs obtenida de la DB, sera una lista vacia si
     * no se encuentra nada en la tabla, o en caso de error retona <b>null</b>.
     */
    public List<SpasaCRN> obtenerSpasaFiltros() {

        if (conectado) {
            try {
                String bool = "false";
                List<SpasaCRN> filtrosCRNs = new ArrayList();

                String Query = "SELECT * FROM `spasa_filtros` WHERE anio = YEAR(NOW()) AND ciclo = CURRENT_CICLO() ;";
                Logy.mi("Query: " + Query);
                Statement st = Conexion.createStatement();
                ResultSet resultSet;
                resultSet = st.executeQuery(Query);
                while (resultSet.next()) {

                    SpasaCRN crn = new SpasaCRN();
                    SpasaMateria mat = new SpasaMateria();
                    crn.setCodigoProf(resultSet.getString("usuario")); //modificado ahora que se cambio in a String
                    crn.setCrnCpr(resultSet.getString("crn"));
                    String mc = resultSet.getString("materia_cod");
                    if (mc != null && !mc.isEmpty() && !mc.equals("null")) {
                        crn.setMateriaRuta(mc);
                        mat.setMateriaRuta(mc);
                    }
                    String m = resultSet.getString("materia_nom");
                    if (m != null && !m.isEmpty() && !m.equals("null")) {
                        mat.setNombreMat(m);
                    }
                    String d = resultSet.getString("departamento_nom");
                    if (d != null && !d.isEmpty() && !d.equals("null")) {
                        mat.setNombreDepto(d);
                    }
                    String dia = resultSet.getString("dia");
                    if (dia != null && !dia.isEmpty() && !dia.equals("null")) {
                        crn.setDiaHr(dia);
                    }
                    String h = resultSet.getString("hora");
                    if (h != null && !h.isEmpty() && !h.equals("null")) {
                        crn.setHiniHr(h);
                    }
                    crn.setMateria(mat);
                    filtrosCRNs.add(crn);
                }

                return filtrosCRNs;

            } catch (SQLException ex) {
                Logy.me("No se pudo consultar tabla spasa_filtros : " + ex.getMessage());
                return null;
            }
        } else {
            Logy.me("No se ha establecido previamente una conexion a la DB");
            return null;
        }
    }

    public int insertarSpasaLog(boolean ok, String detalles) {
        if (conectado) {

            Date date = new Date();
            SimpleDateFormat formateador = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String ahora = formateador.format(date);

            try {
                PreparedStatement p = Conexion.prepareStatement(
                        "INSERT INTO spasa_log (fechahora,estatus,detalles)"
                        + " VALUES (?,?,?) ");

                p.setString(1, ahora);
                p.setInt(2, (ok ? 1 : 0));
                p.setString(3, detalles);

                Logy.m("Se insertara un log");
                Logy.m(p.toString());
                return p.executeUpdate();
            } catch (SQLException e) {
                Logy.me("" + e.getLocalizedMessage() + " " + e.getMessage());
                return 0;
            }
        } else {
            Logy.me("Error!!! no ha establecido previamente una conexion a la DB");
            return 0;
        }
    }

    public int insertarSpasaEliminado(SpasaCRN eliminado) {
        if (conectado) {

            try {
                int crn;
                int usuario;
                String materiaCod;
                String materiaNom;
                String departamentoNom;
                String aula;
                String programa;
                String dia;
                String anio;
                String ciclo;
                String horaIni;
                String horaFin;

                if (eliminado.getCrnCpr() == null || eliminado.getCrnCpr().isEmpty() || eliminado.getCrnCpr().equals("null")) {
                    crn = 0;
                    //Logy.me("El CRN del SpasaCRN a insertar es null o vacio");
                } else {
                    crn = Integer.parseInt(eliminado.getCrnCpr());
                }
                //usuario = eliminado.getCodigoProf();  //modificado ahora que se cambio in a String
                if (eliminado.getCodigoProf() == null || eliminado.getCodigoProf().isEmpty() || eliminado.getCodigoProf().equals("null")) {
                    usuario = 0;
                } else {
                    usuario = Integer.parseInt(eliminado.getCodigoProf());
                }
                if (eliminado.getMateriaRuta() == null || eliminado.getMateriaRuta().isEmpty() || eliminado.getMateriaRuta().equals("null")) {
                    materiaCod = "null";
                } else {
                    materiaCod = eliminado.getMateriaRuta();
                }
                if (eliminado.getMateria().getNombreMat() == null || eliminado.getMateria().getNombreMat().isEmpty() || eliminado.getMateria().getNombreMat().equals("null")) {
                    materiaNom = "null";
                } else {
                    materiaNom = eliminado.getMateria().getNombreMat();
                }
                if (eliminado.getMateria().getNombreDepto() == null || eliminado.getMateria().getNombreDepto().isEmpty() || eliminado.getMateria().getNombreDepto().equals("null")) {
                    departamentoNom = "null";
                } else {
                    departamentoNom = eliminado.getMateria().getNombreDepto();
                }
                if (eliminado.getNombreEsp() == null || eliminado.getNombreEsp().isEmpty() || eliminado.getNombreEsp().equals("null")) {
                    aula = "null";
                } else if (eliminado.getNombreEsp().length() > 5) {
                    aula = "long";
                    Logy.ma("aula muy grande = " + eliminado.getNombreEsp());
                } else {
                    aula = eliminado.getNombreEsp();
                }
                if (eliminado.getNombrePro() == null || eliminado.getNombrePro().isEmpty() || eliminado.getNombrePro().equals("null")) {
                    programa = "null";
                } else {
                    programa = eliminado.getNombrePro();
                }
                if (eliminado.getDiaHr() == null || eliminado.getDiaHr().isEmpty() || eliminado.getDiaHr().equals("null")) {
                    dia = "DOMINGO";
                } else {
                    dia = eliminado.getDiaHr();
                }
                if (eliminado.getHiniHr() == null || eliminado.getHiniHr().isEmpty() || eliminado.getHiniHr().equals("null")) {
                    horaIni = "00:00:00";
                } else {
                    horaIni = eliminado.getHiniHr();
                }
                if (eliminado.getHfinHr() == null || eliminado.getHfinHr().isEmpty() || eliminado.getHfinHr().equals("null")) {
                    horaFin = "00:00:00";
                } else {
                    horaFin = eliminado.getHfinHr();
                }

                //anio = YEAR(NOW()) AND ciclo = CURRENT_CICLO()
                PreparedStatement p = Conexion.prepareStatement(
                        "INSERT INTO spasa_crneliminados (crn, usuario, materia_cod, materia_nom, departamento_nom, aula, programa, dia, hora_ini, hora_fin, anio, ciclo)"
                        + " VALUES (?,?,?,?,?,?,?,?,?,?,YEAR(NOW()),CURRENT_CICLO()) ");

                p.setInt(1, crn);
                p.setInt(2, usuario);
                p.setString(3, materiaCod);
                p.setString(4, materiaNom);
                p.setString(5, departamentoNom);
                p.setString(6, aula);
                p.setString(7, programa);
                p.setString(8, dia);
                p.setString(9, horaIni);
                p.setString(10, horaFin);

                //Logy.m("Se insertara un crn elimiando");
                //Logy.m("Query -> " + p.toString());
                return p.executeUpdate();
            } catch (SQLException e) {
                Logy.me("" + e.getLocalizedMessage() + " " + e.getMessage());
                return 0;
            }
        } else {
            Logy.me("Error!!! no ha establecido previamente una conexion a la DB");
            return 0;
        }
    }

    public boolean limpiarSpasaElminados() {

        if (conectado) {
            try {
                String Query = String.format(
                        "DELETE FROM spasa_crneliminados WHERE 1");
                System.out.println("QUERY -> " + Query);
                Statement st = Conexion.createStatement();
                System.out.println(st.toString());
                st.executeUpdate(Query);
                return true;
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
                System.out.println("Error!!! elimanando registros de spasa_crnelimiandos de la DB");
                return false;
            }
        } else {
            System.out.println("Error!!! no se ha establecido previamente una conexion a la DB");
            return false;
        }

    }

    public int actualizarMateria(SicaMateria m) {
        if (conectado) {

            try {
                PreparedStatement p = Conexion.prepareStatement(
                        "INSERT INTO `materias`(`codigo`, `nombre`, `departamento`) "
                        + "SELECT ?, ?, ? "
                        + "FROM dual "
                        + "WHERE NOT EXISTS (SELECT * FROM `materias` WHERE `codigo` = ? )LIMIT 1 ;  "
                );

                p.setString(1, m.getCodigo());
                p.setString(2, m.getNombre());
                p.setString(3, m.getDepartamento());

                p.setString(4, m.getCodigo());

                Logy.m("Se insertara si no existe una materia: " + m.getNombre());
                Logy.m(p.toString());

                PreparedStatement p2 = Conexion.prepareStatement(
                        "UPDATE `checador`.`materias` SET `codigo` = ?, `nombre` = ?, `departamento` = ? "
                        + "WHERE `materias`.`codigo` = ? ; "
                );

                p2.setString(1, m.getCodigo());
                p2.setString(2, m.getNombre());
                p2.setString(3, m.getDepartamento());
                p2.setString(4, m.getCodigo());

                Logy.m("Se hara update de materia: " + m.getNombre());
                Logy.m(p2.toString());

                int r = p.executeUpdate();
                int r2 = p2.executeUpdate();

                return r + r2;
            } catch (SQLException e) {
                Logy.me("" + e.getLocalizedMessage() + " " + e.getMessage());
                Sincronizador.setOk(false);
                Sincronizador.setDetalles(Sincronizador.getDetalles() + "error update materias en DB, ");
                return 0;
            }
        } else {
            Logy.me("Error!!! no ha establecido previamente una conexion a la DB");
            return 0;
        }
    }

    /**
     * ELimia todos los registros crn de la tabla cnrs en la DB correspondientes
     * al CICLO y ANIO actuales.
     *
     * @return false si sucedio algn error
     */
    public boolean limpiarCRN() {

        if (conectado) {
            try {
                String Query = String.format(
                        "DELETE FROM crn WHERE anio = YEAR(NOW()) AND ciclo = CURRENT_CICLO()");
                System.out.println("QUERY -> " + Query);
                Statement st = Conexion.createStatement();
                System.out.println(st.toString());
                st.executeUpdate(Query);
                return true;
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
                System.out.println("Error!!! elimanando registros de tabla crn de la DB");
                return false;
            }
        } else {
            System.out.println("Error!!! no se ha establecido previamente una conexion a la DB");
            return false;
        }
    }

    /**
     * Inserta un registro en la tabla CRN
     *
     * @param crn
     * @return la cantidad de filas afectadas, o algo asi
     */
    public int insertarCRN(SicaCRN crn) {
        if (conectado) {

            try {
                PreparedStatement p = Conexion.prepareStatement(
                        "INSERT INTO `crn`(`usuario`, `materia`, `crn`, `anio`, `ciclo`) "
                        + "VALUES ( ?, ?, ?, YEAR(NOW()), CURRENT_CICLO() ) "
                );

                p.setInt(1, crn.getUsuario());
                p.setString(2, crn.getMateria());
                p.setInt(3, crn.getCrn());

                Logy.m("Se insertara el CRN: " + crn.getCrn());
                Logy.m(p.toString());

                int r = p.executeUpdate();
                return r;
            } catch (SQLException e) {
                Logy.me("" + e.getLocalizedMessage() + " " + e.getMessage());
                Sincronizador.setOk(false);
                Sincronizador.setDetalles(Sincronizador.getDetalles() + "error insertar CRN en DB, ");
                return 0;
            }
        } else {
            Logy.me("Error!!! no ha establecido previamente una conexion a la DB");
            return 0;
        }
    }

    /**
     * Estos metodos son solo especificos para usarse para la etapa final de la
     * sincronizacion entre spasa y sica.
     *
     * @param crn
     * @return la cantidad de filas afectadas, o algo asi
     * @deprecated
     */
    public int actualizarCRN_update(SicaCRN crn) {
        if (conectado) {

            try {
                PreparedStatement p = Conexion.prepareStatement(
                        "INSERT INTO `crn`(`usuario`, `materia`, `crn`, `anio`, `ciclo`) "
                        + "SELECT ?, ?, ?, YEAR(NOW()), CURRENT_CICLO() "
                        + "FROM dual "
                        + "WHERE NOT EXISTS (SELECT * FROM `crn` "
                        + "WHERE `crn` = ? AND `usuario` = ? "
                        + "AND `anio` = YEAR(NOW()) AND `ciclo` = CURRENT_CICLO()) LIMIT 1 ;"
                );

                p.setInt(1, crn.getUsuario());
                p.setString(2, crn.getMateria());
                p.setInt(3, crn.getCrn());
                p.setInt(4, crn.getCrn());
                p.setInt(5, crn.getUsuario());

                Logy.m("Se insertara si no existe un CRN: " + crn.getCrn());
                Logy.m(p.toString());

                PreparedStatement p2 = Conexion.prepareStatement(
                        "UPDATE `checador`.`crn` SET `usuario` = ?, `materia` = ?, "
                        + "`crn` = ?, `anio` = YEAR(NOW()), `ciclo` = CURRENT_CICLO() "
                        + "WHERE `crn` = ? AND `usuario` = ? AND `anio` = YEAR(NOW()) AND `ciclo` = CURRENT_CICLO() ;"
                );

                p2.setInt(1, crn.getUsuario());
                p2.setString(2, crn.getMateria());
                p2.setInt(3, crn.getCrn());
                p2.setInt(4, crn.getCrn());
                p2.setInt(5, crn.getUsuario());

                Logy.m("Se hara update de crn: " + crn.getCrn());
                Logy.m(p2.toString());

                int r = p.executeUpdate();
                int r2 = p2.executeUpdate();

                return r + r2;
            } catch (SQLException e) {
                Logy.me("" + e.getLocalizedMessage() + " " + e.getMessage());
                Sincronizador.setOk(false);
                Sincronizador.setDetalles(Sincronizador.getDetalles() + "error update CRNS en DB, ");
                return 0;
            }
        } else {
            Logy.me("Error!!! no ha establecido previamente una conexion a la DB");
            return 0;
        }
    }

    /**
     * ELimia todos los registros de la tabla <b>horarioscrn</b> en la DB
     * correspondientes al CICLO y ANIO actuales.
     *
     * @return false si sucedio algn error
     */
    public boolean limpiarHorariosCRN() {

        if (conectado) {
            try {
                String Query = String.format(
                        "DELETE FROM horarioscrn WHERE anio = YEAR(NOW()) AND ciclo = CURRENT_CICLO()");
                System.out.println("QUERY -> " + Query);
                Statement st = Conexion.createStatement();
                System.out.println(st.toString());
                st.executeUpdate(Query);
                return true;
            } catch (SQLException ex) {
                Logy.me(ex.getMessage());
                Logy.me("Error!!! elimanando registros de tabla HORARIOSCNR de la DB");
                return false;
            }
        } else {
            Logy.me("Error!!! no se ha establecido previamente una conexion a la DB");
            return false;
        }
    }

    public int insertarHorariosCRN(SicaHorarioCRN h) {
        if (conectado) {

            try {
                PreparedStatement p = Conexion.prepareStatement(
                        "INSERT INTO `horarioscrn`(`crn`, `bloque`, `dia`, `hora`, `duracion`, `aula`, `anio`, `ciclo`) "
                        + "VALUES (?, ?, ?, ?, ?, ?, YEAR(NOW()), CURRENT_CICLO() ) "
                );

                p.setInt(1, h.getCrn());
                p.setInt(2, 0);
                p.setString(3, h.getDia());
                p.setString(4, h.getHora());
                p.setString(5, h.getDuracion());
                p.setString(6, h.getAula());

                Logy.m("Se insertara un horarioCRN: " + h.getCrn());
                Logy.m(p.toString());

                int r = p.executeUpdate();

                return r;
            } catch (SQLException e) {
                Logy.me("" + e.getLocalizedMessage() + " " + e.getMessage());
                Sincronizador.setOk(false);
                Sincronizador.setDetalles(Sincronizador.getDetalles() + "error update HorarioCRNs en DB, ");
                return 0;
            }
        } else {
            Logy.me("Error!!! no ha establecido previamente una conexion a la DB");
            return 0;
        }
    }

    /**
     * Estos metodos son solo especificos para usarse para la etapa final de la
     * sincronizacion entre spasa y sica. NOTA: Se esta dejando por default <b>
     * BLOQUE = 0 </b>, por lo que en caso que regresen las materias de bloques
     * 4x4 se tendrian que hacer modificaciones en el codigo.
     *
     * @param crn
     * @return la cantidad de filas afectadas, o algo asi
     * @deprecated
     */
    public int actualizarHorariosCRN_update(SicaHorarioCRN h) {
        if (conectado) {

            try {
                PreparedStatement p = Conexion.prepareStatement(
                        "INSERT INTO `horarioscrn`(`crn`, `bloque`, `dia`, `hora`, `duracion`, `aula`, `anio`, `ciclo`) "
                        + "SELECT ?, ?, ?, ?, ?, ?, YEAR(NOW()), CURRENT_CICLO()  "
                        + "FROM dual "
                        + "WHERE NOT EXISTS (SELECT * FROM `horarioscrn` "
                        + "WHERE `crn` = ? AND `bloque` = ? "
                        + "AND `dia` = ? AND `hora` = ? "
                        + "AND `anio` = YEAR(NOW()) AND `ciclo` = CURRENT_CICLO() ) LIMIT 1 ;"
                );

                p.setInt(1, h.getCrn());
                p.setInt(2, 0);
                p.setString(3, h.getDia());
                p.setString(4, h.getHora());
                p.setString(5, h.getDuracion());
                p.setString(6, h.getAula());
                p.setInt(7, h.getCrn());
                p.setInt(8, 0);
                p.setString(9, h.getDia());
                p.setString(10, h.getHora());

                Logy.m("Se insertara si no existe un horarioCRN: " + h.getCrn());
                Logy.m(p.toString());

                PreparedStatement p2 = Conexion.prepareStatement(
                        "UPDATE `checador`.`horarioscrn` "
                        + "SET `crn` = ?, `bloque` = ?, `dia` = ?, "
                        + "`hora` = ?, `duracion` = ?, "
                        + "`aula` = ?, `anio` = YEAR(NOW()), `ciclo` = CURRENT_CICLO() "
                        + "WHERE `horarioscrn`.`crn` = ? AND `horarioscrn`.`bloque` = ? "
                        + "AND `horarioscrn`.`dia` = ? AND `horarioscrn`.`hora` = ? "
                        + "AND `horarioscrn`.`anio` = YEAR(NOW()) AND `horarioscrn`.`ciclo` = CURRENT_CICLO();"
                );

                p2.setInt(1, h.getCrn());
                p2.setInt(2, 0);
                p2.setString(3, h.getDia());
                p2.setString(4, h.getHora());
                p2.setString(5, h.getDuracion());
                p2.setString(6, h.getAula());

                p2.setInt(7, h.getCrn());
                p2.setInt(8, 0);
                p2.setString(9, h.getDia());
                p2.setString(10, h.getHora());

                Logy.m("Se hara update de horarioCRN: " + h.getCrn());
                Logy.m(p2.toString());

                int r = p.executeUpdate();
                int r2 = p2.executeUpdate();

                return r + r2;
            } catch (SQLException e) {
                Logy.me("" + e.getLocalizedMessage() + " " + e.getMessage());
                Sincronizador.setOk(false);
                Sincronizador.setDetalles(Sincronizador.getDetalles() + "error update HorarioCRNs en DB, ");
                return 0;
            }
        } else {
            Logy.me("Error!!! no ha establecido previamente una conexion a la DB");
            return 0;
        }
    }

    /**
     * Metodo para consultar todos los departamentos de la base de datos
     * (tambien llamadas instancias).
     *
     * @return una List de tipo Departamento ordenada alfabeticamente por el
     * nombre de instancia.
     */
    public List<Departamento> obtenerInstancias() {
        List<Departamento> lstDepas = new ArrayList();
        if (conectado) {
            try {
                String Query = "SELECT * FROM instancias ORDER BY nombre ASC";
                Statement st = Conexion.createStatement();
                ResultSet resultSet;
                resultSet = st.executeQuery(Query);
                while (resultSet.next()) {
                    Departamento depa = new Departamento();
                    depa.setCodigo(resultSet.getString("codigo"));
                    depa.setNombre(resultSet.getString("nombre"));
                    depa.setJefe(resultSet.getString("jefe"));
                    lstDepas.add(depa);
                }
                Logy.m("Consulta de datos de las instancias: exitosa");
                return lstDepas;

            } catch (SQLException ex) {
                Logy.me("Error!! en la consulta de datos en la tabla instancias");
                return new ArrayList();
            }
        } else {
            Logy.me("Error!!! no se ha establecido previamente una conexion a la DB");
            return lstDepas;
        }
    }

    /**
     * Metodo para guardar un usuario nuevo en la tabla "usuarios". Requiere de
     * 7 parametros NOTAS: el correo se guarda en otra tabla y por ende con otro
     * metodo.
     *
     * @param cod es el código de la persona
     * @param nacimiento es la fecha de nacimiento en String con formato
     * yyyy-MM-dd
     * @param nom es el nombre completo
     * @param tipo corresponde al tipo de usuario: es un int: revisar la base de
     * datos para ello
     * @param categ es la categoria (codigo de categoria, en String) que
     * corresponde al tipo de usuario.
     * @param status corresponde al estatus del usuario, es un int, revisar la
     * base de datos para ello.
     * @param depto es el departamento al cual pertenece, es la abreviación,
     * ejemplo: CCI, CSH. Revisar la tabla instancias de la DB para mas info.
     * @param telefono el telefono del usuario
     * @param telefonoPar el telefono particular
     * @param telefonoOfi el telefono de oficina, sugerido incluir la extensión.
     * @param coment el comentario del usuario
     * @return retorna 1 en caso existoso, retorna 0 en caso de error.
     */
    public int insertarUsuario(String cod, String nom, String nacimiento, int tipo, String categ, int status, String depto, String telefono, String telefonoPar, String telefonoOfi, String coment) {
        if (conectado) {
            //seguridad contra NULL point exception
            if (categ == null) {
                categ = "";
            }
            try {
                PreparedStatement p = Conexion.prepareStatement(
                        "INSERT INTO usuarios (usuario, nombre, departamento, tipo, categoria, status,  telefono, telefono_par, telefono_ofi, nacimiento, comentario, pass) "
                        + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?) ");

                p.setString(1, cod);
                p.setString(2, nom);
                p.setString(3, depto);
                p.setInt(4, tipo);
                p.setString(5, categ);
                p.setInt(6, status);
                p.setString(7, telefono);
                p.setString(8, telefonoPar);
                p.setString(9, telefonoOfi);
                p.setString(10, nacimiento);
                p.setString(11, coment);
                p.setString(12, "");
                printLog("Se inserto correctamente un nuevo usuario:");
                printLog(p.toString());
                return p.executeUpdate();
            } catch (SQLException e) {
                printLog("" + e.getLocalizedMessage() + " " + e.getMessage());
                return 0;
            }
        } else {
            System.out.println("Error!!! no ha establecido previamente una conexion a la DB");
            return 0;
        }
    }

    /**
     * Metodo para ACTUALIZAR un usuario basado en su CÓDIGO en la tabla
     * "usuarios". Requiere de 7 parametros NOTAS: el correo se guarda en otra
     * tabla y por ende con otro metodo.
     *
     * @param cod es el codigo del usuario
     * @param nom es el nombre completo del usuario
     * @param nacimiento es la fecha de nacimiento en String con formato
     * yyyy-MM-dd
     * @param tipo corresponde al tipo de usuario: es un int: revisar la base de
     * datos para ello
     * @param categ es la categoria (codigo de categoria, en String) que
     * corresponde al tipo de usuario.
     * @param status corresponde al estatus del usuario, es un int, revisar la
     * base de datos para ello.
     * @param depto es el departamento al cual pertenece, es la abreviación,
     * ejemplo: CCI, CSH. Revisar la tabla instancias de la DB para mas info.
     * @param telefono el telefono del usuario
     * @param telefono el telefono del usuario
     * @param telefonoPar el telefono particular
     * @param coment el comentario del usuario
     * @return
     */
    public int actualizarUsuario(String cod, String nom, String nacimiento, int tipo, String categ, int status, String depto, String telefono, String telefonoPar, String telefonoOfi, String coment) {
        if (conectado) {

            //seguridad contra NULL point exception
            if (categ == null) {
                categ = "";
            }
            try {
                PreparedStatement p = Conexion.prepareStatement(
                        "UPDATE usuarios SET nombre = ?, tipo = ?, categoria = ?, status = ?, departamento = ?, nacimiento = ?, telefono = ?, telefono_par = ?, telefono_ofi = ?, comentario = ?, pass = ?"
                        + " WHERE usuario = ?");

                //p.setString(1, cod);
                p.setString(1, nom);
                p.setInt(2, tipo);
                p.setString(3, categ);
                p.setInt(4, status);
                p.setString(5, depto);
                p.setString(6, nacimiento);
                p.setString(7, telefono);
                p.setString(8, telefonoPar);
                p.setString(9, telefonoOfi);
                p.setString(10, coment);
                p.setString(11, ""); //pass
                p.setString(12, cod); //usado para el Where
                printLog("Se actualizará el siguiente usuario:");
                printLog(p.toString());
                return p.executeUpdate();
            } catch (SQLException e) {
                printLog("" + e.getLocalizedMessage() + " " + e.getMessage());
                return 0;
            }
        } else {
            System.out.println("Error!!! no se ha establecido previamente una conexion a la DB");
            return 0;
        }
    }

    public boolean eliminarUsuario(String codigo) {

        if (conectado) {
            try {
                String Query = String.format(
                        "CALL delete_usuario('%s') ",
                        codigo);
                System.out.println("QUERY -> " + Query);
                Statement st = Conexion.createStatement();
                System.out.println(st.toString());
                st.executeUpdate(Query);
                return true;
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
                System.out.println("Error!!! elimanndo el Usuario " + codigo + "de la DB");
                return false;
            }
        } else {
            System.out.println("Error!!! no se ha establecido previamente una conexion a la DB");
            return false;
        }

    }

    /*
    public List<Correo> obtenerCorreos(String codigo){
        
        if(conectado){
        try {
            List<Correo> lstCorreos = new ArrayList();
            String Query = "SELECT * FROM correosusuarios WHERE usuario="+ codigo + " ORDER BY correo ASC";
            System.out.println("Consulta correos: " + Query);
            Statement st = Conexion.createStatement();
            ResultSet resultSet;
            resultSet = st.executeQuery(Query);
            while (resultSet.next()) {
                Correo unCorreo = new Correo();
                unCorreo.setCorreo(resultSet.getString("correo"));
                unCorreo.setPrincipal( resultSet.getString("principal").equals("1") );
                lstCorreos.add(unCorreo);
            }
            printLog("Consulta de datos de las instancias: exitosa");
            return lstCorreos;
 
        } catch (SQLException ex) {
            printLog("Error!! en la consulta de datos en la tabla correousuarios", Level.SEVERE);
            return new ArrayList();
        }
        }else{
            System.out.println("Error!!! no se ha establecido previamente una conexion a la DB");
            return new ArrayList();
        }
   }
    
     */
    /**
     * Metodo para generar mensajes en el Log de java
     *
     * @param mensaje : El mensaje a mostrar, es un String
     */
    private void printLog(String mensaje) {
        Logy.m(mensaje);
    }

}
