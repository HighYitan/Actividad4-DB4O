/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package activitat;

import com.db4o.ext.Db4oException;
import com.db4o.*;
import com.db4o.Db4oVersion;
import java.util.List;
        
/**
 *
 * @author windeveloper
 */
public class Activitat {
    public static void instanciar(){
        GestorProductes gestor=null;
        try {
            gestor = new GestorProductes("productos.db4o");

            gestor.obtenirObjecte(new UnitatDeMesura("kg", "kilogramo"));
            gestor.obtenirObjecte(new UnitatDeMesura("g", "gramo"));
            gestor.obtenirObjecte(new UnitatDeMesura("l", "litro"));

            Magatzem m = gestor.obtenirObjecte(new Magatzem("01", "El primero"));
            gestor.obtenirObjecte(new Magatzem("01", "El primero"));



            Article art = gestor.obtenirObjecte(new Article("bolígrafo"));
            gestor.obtenirObjecte(new Producte(art, "DeColores", 2.22));
            gestor.obtenirObjecte(new Producte(art, "Rios de Tinta", 1.29));
            gestor.obtenirObjecte(new Producte(art, "De Regalo", 8.99));

            art = gestor.obtenirObjecte(new Article("fideos cabello de ángel"));
            UnitatDeMesura u = gestor.obtenirObjecte(new UnitatDeMesura("g"));
            Envas env = gestor.obtenirObjecte(new Envas("paquete", 500, u));
            gestor.obtenirObjecte(new ProducteEnvasat(art,
            "Pastarada",
            1.78,
            env));
            gestor.obtenirObjecte(new ProducteEnvasat(art,
            "Pasta Gansa",
            2.15,
            env));

            art = gestor.obtenirObjecte(new Article("fideos gruesos"));
            gestor.obtenirObjecte(new ProducteEnvasat(art,
            "Pasta Gansa",
            2.25,
            env));
            env = gestor.obtenirObjecte(new Envas("paquete", 250, u));
            gestor.obtenirObjecte(new ProducteEnvasat(art,
            "Pasta Gansa",
            2.25,
            env));
            art = gestor.obtenirObjecte(
            new activitat.Article("agua mineral"));
            u = gestor.obtenirObjecte(new UnitatDeMesura("l"));
            env = gestor.obtenirObjecte(new Envas("garrafa", 5, u));
            gestor.obtenirObjecte(new ProducteEnvasat(art,
            "Fuente Divina",
            2.10,
            env));

            art = gestor.obtenirObjecte(
            new activitat.Article("pera"));
            u = gestor.obtenirObjecte(new UnitatDeMesura("kg"));
            gestor.obtenirObjecte(new ProducteAGranel(art, 3.21, u));
            art = gestor.obtenirObjecte(
            new activitat.Article("mongolia otoñal"));
            gestor.obtenirObjecte(new ProducteAGranel(art, 4.39, u));

            gestor.actualitzaCanvis();

            List<Producte> llistaProd = gestor.obtenirProductes();

            double[] quant = new double[]{5.0, 10.0, 15.0, 25.0, 8.0,
            2.0, 17.0, 45.0, 12.5, 1.25};

            for(int i=0; i<llistaProd.size(); i++){
                Estoc stc;
                Producte p = llistaProd.get(i);
                stc = new Estoc(p, quant[i]);
                m.assignarEstoc(stc);
            }

            gestor.actualitzaCanvis(m);

        }
        catch (Db4oException ex) {
            System.err.println("Error:" + ex.getMessage());
        }
        finally{
            if(gestor!=null){
                gestor.tancar();
            }
        }
    }

    public void modificar(){
        GestorProductes gestor=null;
        try {
            gestor = new GestorProductes("productos.db4o");
            List<Producte> llista = gestor.obtenirProductePerArticle(
            new Article("bolígrafo"));
            for(Producte p: llista){
                p.setPreu(p.getPreu()*1.05);
            }

            gestor.actualitzaCanvis();

            Magatzem m = gestor.obtenirObjecte(new Magatzem("01"));
            llista = gestor.obtenirProductePerArticle(
            new Article("bolígrafo"));
            for(Producte p: llista){
                m.incrementarEstocProducte(p, 10.0);
            }

            gestor.actualitzaCanvis();

            m = gestor.obtenirObjecte(new Magatzem("01"));
            llista = gestor.obtenirProductePerArticle(
            new Article("bolígrafo"));
            for(Producte p: llista){
                m.decrementarEstocProducte(p, 12.0);
            }
            gestor.actualitzaCanvis();
        }
        catch (Db4oException ex) {
            System.err.println("Error:" + ex.getMessage());
        }
        finally{
            if(gestor!=null){
                gestor.tancar();
            }
        }
    }

    public static void mostrar(){
        GestorProductes gestor=null;
        try {
            gestor = new GestorProductes("productos.db4o");
            List<Producte> llista = gestor.obtenirEstocZero("01", 10.0);
            System.out.println("Productos con stock igual o menor a 10");
            for(Producte p: llista){
                System.out.println(p.toString());
            }
        }
        finally{
            if(gestor!=null){
                gestor.tancar();
            }
        }
    }
    public static void main(String[] args) {
       instanciar();
       
       //modificar();
       
       mostrar(); 
    }
}