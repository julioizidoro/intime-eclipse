package br.com.financemate.manageBean.relatorios;

import br.com.financemate.bean.Formatacao;
import br.com.financemate.bean.relatorios.AtividadesConcluidasBean;
import br.com.financemate.bean.relatorios.AtividadesConcluidasFactory;
import br.com.financemate.facade.AtividadeFacade;
import br.com.financemate.facade.ClienteFacade;
import br.com.financemate.facade.DepartamentoFacade;
import br.com.financemate.facade.UsuarioFacade;
import br.com.financemate.manageBean.usuairo.UsuarioLogadoMB;
import br.com.financemate.model.Atividades;
import br.com.financemate.model.Cliente;
import br.com.financemate.model.Departamento;
import br.com.financemate.model.Subdepartamento;
import br.com.financemate.model.Usuario;
import br.com.financemate.util.GerarRelatorio;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Named
@ViewScoped
public class RelatorioAtividadesConcluidasMB implements Serializable{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Inject 
    private UsuarioLogadoMB usuarioLogadoBean;
    private List<Departamento> listaDepartamento;
    private List<Subdepartamento> listaSubdepartamento;
    private List<Cliente> listaCliente;
    private List<Usuario> listaUsuario;
    private Date dataInicial;
    private Date dataFinal;
    private Usuario usuario;
    private Cliente cliente;
    private Departamento departamento;
    private Subdepartamento subdepartamento;
    
    @PostConstruct
    public void init(){
        getUsuarioLogadoBean();
        gerarListaClientes();
        gerarListaDepartamento();
        gerarListaUsuario();
        usuario = new Usuario();
        departamento = new Departamento();
        subdepartamento = new Subdepartamento();
        cliente = new Cliente();
    }

    public UsuarioLogadoMB getUsuarioLogadoBean() {
        return usuarioLogadoBean;
    }

    public void setUsuarioLogadoBean(UsuarioLogadoMB usuarioLogadoBean) {
        this.usuarioLogadoBean = usuarioLogadoBean;
    }

    public List<Departamento> getListaDepartamento() {
        return listaDepartamento;
    }

    public void setListaDepartamento(List<Departamento> listaDepartamento) {
        this.listaDepartamento = listaDepartamento;
    }

    public List<Subdepartamento> getListaSubdepartamento() {
        return listaSubdepartamento;
    }

    public void setListaSubdepartamento(List<Subdepartamento> listaSubdepartamento) {
        this.listaSubdepartamento = listaSubdepartamento;
    }

    public List<Cliente> getListaCliente() {
        return listaCliente;
    }

    public void setListaCliente(List<Cliente> listaCliente) {
        this.listaCliente = listaCliente;
    }
    
    public List<Usuario> getListaUsuario() {
        return listaUsuario;
    }

    public void setListaUsuario(List<Usuario> listaUsuario) {
        this.listaUsuario = listaUsuario;
    }
    
    public Date getDataInicial() {
        return dataInicial;
    }

    public void setDataInicial(Date dataInicial) {
        this.dataInicial = dataInicial;
    }

    public Date getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(Date dataFinal) {
        this.dataFinal = dataFinal;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public Subdepartamento getSubdepartamento() {
        return subdepartamento;
    }

    public void setSubdepartamento(Subdepartamento subdepartamento) {
        this.subdepartamento = subdepartamento;
    }
    
    
    public void gerarListaClientes() {
        ClienteFacade clienteFacade = new ClienteFacade();
        listaCliente = clienteFacade.listar("", usuarioLogadoBean.getConfig().getCliente().getIdcliente());
        if (listaCliente == null) {
            listaCliente = new ArrayList<Cliente>();
        }
    }
    
    public void gerarListaUsuario() {
        UsuarioFacade usuarioFacade = new UsuarioFacade();
        listaUsuario = usuarioFacade.listarAtivos();
        if (listaUsuario==null){
            listaUsuario = new ArrayList<Usuario>();
        }
    }
    
    public void gerarListaDepartamento() {
        DepartamentoFacade departamentoFacade = new DepartamentoFacade();
        listaDepartamento = departamentoFacade.listar("", usuarioLogadoBean.getConfig().getDepartamento().getIddepartamento());
        if (listaDepartamento==null){
            listaDepartamento = new ArrayList<Departamento>();
        }
    }
    
    private List<Atividades> gerarListaAtividadesConcluidas(){
        String sql = "Select a from Atividade a where a.prazo>='" + Formatacao.ConvercaoDataSql(dataInicial) + 
                "' and a.prazo<='" + Formatacao.ConvercaoDataSql(dataFinal) + "'  and a.situacao=1";
        if (cliente!=null){
            sql = sql + " and a.cliente.idcliente=" + cliente.getIdcliente();
        }
        if (departamento!=null){
            sql = sql + " and a.subdepartamento.departamento.iddepartamento=" + departamento.getIddepartamento();
        }
        if (subdepartamento!=null){
            sql = sql + " and a.subdepartamento.idsubdepartamento=" + subdepartamento.getIdsubdepartamento();
        }
        if (usuario!=null){
            sql = sql + " and a.usuario.idusuario=" + usuario.getIdusuario();
        }
        sql = sql + " order by a.usuario.nome, a.nome";
        AtividadeFacade atividadeFacade = new AtividadeFacade();
        List<Atividades> listaAtividade = atividadeFacade.lista(sql);
        return listaAtividade;
    }
    
    public List<AtividadesConcluidasBean>  gerarDS(){
        List<Atividades> listaAtividade = gerarListaAtividadesConcluidas();
        List<AtividadesConcluidasBean> listaAtividadesBean = null;
        if (listaAtividade!=null){
            listaAtividadesBean = new ArrayList<AtividadesConcluidasBean>();
            for(int i=0;i<listaAtividade.size();i++){
                AtividadesConcluidasBean atividadesConcluidasBean = new AtividadesConcluidasBean();
                atividadesConcluidasBean.setAtividade(listaAtividade.get(i).getNome());
                atividadesConcluidasBean.setCliente(listaAtividade.get(i).getCliente().getNomefantasia());
                atividadesConcluidasBean.setDepartamento(listaAtividade.get(i).getSubdepartamento().getDepartamento().getNome());
                atividadesConcluidasBean.setDuracao(listaAtividade.get(i).getMostratempo());
                atividadesConcluidasBean.setTempo(listaAtividade.get(i).getTempo());
                atividadesConcluidasBean.setSubDepartamento(listaAtividade.get(i).getSubdepartamento().getNome());
                atividadesConcluidasBean.setUsuario(listaAtividade.get(i).getUsuario().getNome());
                listaAtividadesBean.add(atividadesConcluidasBean);
            }
        }
        return listaAtividadesBean;
    }
    
    public void gerarRelatorioAtividadesConsluidas() throws JRException, IOException{
        String caminhoRelatorio = "resources/report/atividades/reportatividadesconcluidas.jasper";  
        Map parameters = new HashMap();
        String nomeArquivo = "AtividadesConcluidas";
        List<AtividadesConcluidasBean> lista = gerarDS();
        parameters.put("totalatividades", lista.size());
        parameters.put("totalhoras", calcularHorasTotal(lista));
        AtividadesConcluidasFactory.setListaAtividades(lista);
        JRDataSource jrds = new JRBeanCollectionDataSource(AtividadesConcluidasFactory.getListaAtividades());
        GerarRelatorio gerarRelatorio = new GerarRelatorio();
        gerarRelatorio.gerarRelatorioDSPDF(caminhoRelatorio, parameters, jrds, nomeArquivo);
    }
    
    public String calcularHorasTotal(List<AtividadesConcluidasBean> lista){
        int tempo = 0;
        for (int i = 0; i < lista.size(); i++) {
            tempo = tempo + lista.get(i).getTempo();
        }
        String sTempo = "00:00";
        if (tempo > 0) {
            int horas = tempo / 60;
            int minutos = tempo % 60;
            minutos = minutos * 60;
            if (horas > 9) {
                sTempo = String.valueOf(horas);
            } else {
                sTempo = "0" + String.valueOf(horas);
            }
            if (minutos > 9) {
                sTempo = sTempo + ":" + String.valueOf(minutos);
            } else {
                sTempo = sTempo + ":0" + String.valueOf(minutos);
            }
        }
        return sTempo;
    }
    
    public String cancelar(){
        return null;
    }
}