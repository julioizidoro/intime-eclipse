package br.com.financemate.facade;

import br.com.financemate.dao.AtividadeModuloDao;
import br.com.financemate.model.Atividademodulo;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Julio
 */
public class AtividadeModuloFacade {
    
     AtividadeModuloDao atividadeModuloDao;
    
    public Atividademodulo salvar(Atividademodulo atividademodulo) {
        atividadeModuloDao = new AtividadeModuloDao();
        try {
            return atividadeModuloDao.salvar(atividademodulo);
        } catch (SQLException ex) {
            Logger.getLogger(AtividadeModuloFacade.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public List<Atividademodulo> lista(String sql) {
        atividadeModuloDao = new AtividadeModuloDao();
        try {
            return atividadeModuloDao.listar(sql);
        } catch (SQLException ex) {
            Logger.getLogger(AtividadeModuloFacade.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}