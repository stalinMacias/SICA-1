/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sincronizadorss;

import java.util.ArrayList;
import java.util.List;
import sincronizadorss.objetos.SpasaCRN;

/**
 *
 * @author degmi
 */
public class pruebaListas {
    public static void main(String[] args) {
        
        List<SpasaCRN> lista = new ArrayList();
        
        for (int i = 0; i < 10; i++) {
            SpasaCRN crn = new SpasaCRN();
            crn.setCrnCpr(i+"");
            lista.add(crn);
        }
        
        System.out.println("hasta aqui creada la lista");
        System.out.println("tam lista: " + lista.size());
        int size = lista.size();
        for(int i = 0; i < size; i++){
            String rem = lista.remove(0).getCrnCpr();
            System.out.println("crn removido: " + rem);
        }
        System.out.println("se eliminaron los objetos, tam de lista: " + lista.size());
        
    }
}
