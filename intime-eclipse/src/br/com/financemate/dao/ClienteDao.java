package br.com.financemate.dao;

import br.com.financemate.connection.ConectionFactory;
import br.com.financemate.model.Cliente;
import java.sql.SQLException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

public class ClienteDao {
   
    public Cliente salvar(Cliente cliente) throws SQLException{
        EntityManager manager = ConectionFactory.getConnection();
        manager.getTransaction().begin();
        cliente = manager.merge(cliente);
        manager.getTransaction().commit();
        return cliente;
    }
    public Cliente consultar(int idCliente) throws SQLException{
        EntityManager manager = ConectionFactory.getConnection();
        manager.getTransaction().begin();
        Query q = manager.createQuery("select c from Cliente c where c.idcliente=" + idCliente);
        Cliente cliente = null;
        if (q.getResultList().size()>0){
            cliente = (Cliente) q.getResultList().get(0);
        }
        manager.getTransaction().commit();
        return cliente;
    }
    
    public List<Cliente> listar(String nome, int idconfig) throws SQLException{
        EntityManager manager = ConectionFactory.getConnection();
        manager.getTransaction().begin();
        Query q = manager.createQuery("select c from Cliente c where c.nomefantasia like '%" +nome+ "%' and c.idcliente<>" 
                + idconfig + " order by c.nomefantasia");
        List<Cliente> lista = q.getResultList();
        manager.getTransaction().commit();
        return lista;
    }
    
    public List<Cliente> listar(String nome, String situacao, int idconfig) throws SQLException{
        EntityManager manager = ConectionFactory.getConnection();
        manager.getTransaction().begin();
        Query q = manager.createQuery("select c from Cliente c where c.nomefantasia like '%" +nome+ "%' and c.situacao='" + situacao + "' and c.idcliente<>"
                + idconfig + " order by c.nomefantasia");
        List<Cliente> lista = q.getResultList();
        manager.getTransaction().commit();
        return lista;
    }
    
}