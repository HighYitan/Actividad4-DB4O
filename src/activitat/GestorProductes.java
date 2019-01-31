/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package activitat;

import com.db4o.*;
import com.db4o.ext.Db4oException;
import com.db4o.ext.Db4oIOException;
import com.db4o.config.*;
import com.db4o.query.*;
import com.db4o.ta.*;
import com.db4o.query.Constraint;
import com.db4o.query.Predicate;
import com.db4o.query.Query;
import java.util.ArrayList;
import java.util.List;

public class GestorProductes {
    ObjectContainer db;

    public GestorProductes(String dbStr) {
        Configuration conf = Db4o.newConfiguration();
        conf.objectClass(Magatzem.class).cascadeOnDelete(true);
        conf.activationDepth(8);
        conf.updateDepth(8);
        this.db = Db4o.openFile(conf, dbStr);
    }

    public GestorProductes(Configuration conf, String dbStr) {
        this.db = Db4o.openFile(conf, dbStr);
    }

    public Article obtenirObjecte(Article obj) throws Db4oException{
        return (Article) obtenirUnObjecte(obj);
    }

    public Envas obtenirObjecte(Envas obj) throws Db4oException{
        return (Envas) obtenirUnObjecte(obj);
    }

    public Magatzem obtenirObjecte(Magatzem obj) throws Db4oException{
        return (Magatzem) obtenirUnObjecte(obj);
    }

    public Producte obtenirObjecte(Producte obj) throws Db4oException{
        return (Producte) obtenirUnObjecte(obj);
    }

    public UnitatDeMesura obtenirObjecte(UnitatDeMesura obj)
        throws Db4oException{
            return (UnitatDeMesura) obtenirUnObjecte(obj);
        }

    public List<Article> obtenirArticles(){
        return db.queryByExample(new Article());
    }

    public List<Envas> obtenirEnvasos(){
        return db.queryByExample(new Envas());
    }

    public List<Magatzem> obtenirMagatzems(){
        return db.queryByExample(new Magatzem());
    }

    public List<Producte> obtenirProductes(){
        return db.queryByExample(new Producte());
    }

    public List<UnitatDeMesura> obtenirUnitats(){
        return db.queryByExample(new UnitatDeMesura());
    }

    public List<Producte> obtenirProductePerArticle(Article article) {
        Query query = db.query();
        query.constrain(Producte.class);
        query.descend("article").descend("id").constrain(article.getId());
        ObjectSet<Producte> set = query.execute();
        return set;
    }

    public List<Producte> obtenirProductesDunArticlePerPreu(
        final String iniciNom,
        final double minim,
        final double maxim){
        Query q1 = db.query();
        q1.constrain(Producte.class);
        Constraint starsWith = q1.descend("article").descend("id")
        .constrain(iniciNom).startsWith(true);
        q1.descend("preu").constrain(minim).equal().greater();
        q1.descend("preu").constrain(maxim).equal().smaller();
        return q1.execute();
    }

    public List<Producte> obtenirEstocZero(final String magId,
        final double quantitat){
            final Magatzem m;
            ObjectSet<Producte> ret = null;
            ObjectSet<Magatzem> subQuery = db.query(new Predicate<Magatzem>() {
                @Override
                public boolean match(Magatzem et) {
                    return et.getId().equalsIgnoreCase(magId);
                }
            });

            if(subQuery.hasNext()){
                m=subQuery.next();
            }
            else{
                return new ArrayList();
            }

            ret = db.query(new Predicate<Producte>(){

                @Override
                public boolean match(Producte producte) {
                    return m.getEstoc(producte).getQuantitat()<= quantitat;
                }
            });
            return ret;
        }


    public void eliminar(Object obj){
        try{
            db.delete(obj);
        }
        catch(Db4oIOException ex){
            db.rollback();
        }
        db.commit();
    }


    public void tancar() {
        if(!db.ext().isClosed()){
            db.commit();
            db.close();
        }
    }

    private Object obtenirUnObjecte(Object obj) throws Db4oException{
        ObjectSet llista = null;

        llista = db.queryByExample(obj);
        if(llista.hasNext()){
            obj = llista.next();
            if(llista.hasNext()){
                throw new Db4oException("Patrón poco identificativo");
            }
        }
        else{
            db.store(obj);
            db.commit();
        }
        return obj;
    }

    public void actualitzaCanvis() {
        db.commit();
    }

    public void actualitzaCanvis(Object obj) {
        db.store(obj);
        db.commit();
    }
}