/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sincronizadorss;

import sincronizadorss.objetos.Logy;


/**
 *
 * @author degmi
 */
public class tester {
    public static void main(String[] args) {
        
        Logy.m("Este es un mensaje simple");
        Logy.ma("esto es una advertencia");
        Logy.me("este es un error chido de test");
        
        Sincronizador.iniciar();

    }
}
