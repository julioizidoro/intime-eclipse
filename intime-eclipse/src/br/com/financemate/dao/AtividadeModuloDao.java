package br.com.financemate.dao;

import br.com.financemate.connection.ConectionFactory;
import br.com.financemate.model.Atividademodulo;
import java.sql.SQLException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author Julio
 */
public class AtividadeModuloDao {
    
    public Atividademodulo salvar(Atividademodulo atividademodulo) throws SQLException{
        EntityManager manager = ConectionFactory.getConnection();
        manager.getTransaction().begin();
        atividademodulo = manager.merge(atividademodulo);
        manager.getTransaction().commit();
        return atividademodulo;
    }
    
    public List<Atividademodulo> listar(String sql) throws SQLException{
        EntityManager manager = ConectionFactory.getConnection();
        manager.getTransaction().begin();
        Query q = manager.createQuery(sql);
        List<Atividademodulo> lista = q.getResultList();
        manager.getTransaction().commit();
        return lista;
    }
}