package br.com.financemate.util;



import static br.com.financemate.connection.ConectionFactory.getConnection;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.Map;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

/**
 *
 * @author Wolverine
 */
public class GerarRelatorio {
    
    public void gerarRelatorioDSPDF(String caminhoRelatorio, Map parameters, JRDataSource jrds, String nomeArquivo ) throws JRException, IOException{
        FacesContext facesContext = FacesContext.getCurrentInstance();  
        ServletContext servletContext = (ServletContext)facesContext.getExternalContext().getContext();
        JasperPrint arquivoPrint=null;
        HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
        response.setContentType("application/pdf");
        response.addHeader("Content-disposition", "attachment; filename=\"" + nomeArquivo + ".pdf\"");
        arquivoPrint = JasperFillManager.fillReport(caminhoRelatorio, parameters, jrds);
        JasperExportManager.exportReportToPdfStream(arquivoPrint, response.getOutputStream());
        facesContext.getApplication().getStateManager().saveView(facesContext);
        facesContext.renderResponse();
        facesContext.responseComplete();
    }
    
    public void gerarRelatorioSqlPDF(String caminhoRelatorio, Map parameters, String nomeArquivo, String subDir ) throws JRException, IOException{
        FacesContext facesContext = FacesContext.getCurrentInstance();  
        ServletContext servletContext = (ServletContext)facesContext.getExternalContext().getContext();
        caminhoRelatorio = servletContext.getRealPath(caminhoRelatorio); 
        EntityManager mg = getConnection();
        mg.getTransaction().begin();
        Connection conn = mg.unwrap(java.sql.Connection.class);
        //Connection conn = ConectionFactory.getConexao();
        if (subDir!=null){
            subDir = servletContext.getRealPath(subDir);
            subDir = subDir + File.separator + "a";
            subDir = subDir.substring(0, (subDir.length()-1));
            System.out.println(subDir);
            parameters.put("SUBREPORT_DIR", subDir);
        }
        JasperPrint arquivoPrint=null;
        HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
        response.setContentType("application/pdf");
        response.addHeader("Content-disposition", "attachment; filename=\"" + nomeArquivo + ".pdf\"");
        arquivoPrint = JasperFillManager.fillReport(caminhoRelatorio, parameters, conn);
        JasperExportManager.exportReportToPdfStream(arquivoPrint, response.getOutputStream());
        facesContext.getApplication().getStateManager().saveView(facesContext);
        facesContext.renderResponse();
        facesContext.responseComplete();
        mg.getTransaction().commit();
    }
    
}