package br.com.financemate.manageBean.atividades;

import br.com.financemate.facade.AtividadeFacade;
import br.com.financemate.facade.AtividadeModuloFacade;
import br.com.financemate.manageBean.usuairo.UsuarioLogadoMB;
import br.com.financemate.model.Atividademodulo;
import br.com.financemate.model.Atividades;
import br.com.financemate.model.Modulos;
import br.com.financemate.model.Projeto;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CellEditEvent;

@Named
@ViewScoped
public class AtividadeModuloMB implements Serializable{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Inject
    private UsuarioLogadoMB usuarioLogadoBean;
    @Inject
    private ListaAtividadesMB listaAtividadesMB;
    private Atividademodulo atividademodulo;
    private List<Atividademodulo> listaAtividadesModulo;
    private Modulos modulos;
    
    
    
    @PostConstruct
    public void init(){
        FacesContext fc = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) fc.getExternalContext().getSession(false);
        modulos = (Modulos) session.getAttribute("modulos");
        if (modulos!=null){
            gerarListaAtividades();
        }
    }

    public Atividademodulo getAtividademodulo() {
        return atividademodulo;
    }

    public void setAtividademodulo(Atividademodulo atividademodulo) {
        this.atividademodulo = atividademodulo;
    }

    public List<Atividademodulo> getListaAtividadesModulo() {
        return listaAtividadesModulo;
    }

    public void setListaAtividadesModulo(List<Atividademodulo> listaAtividadesModulo) {
        this.listaAtividadesModulo = listaAtividadesModulo;
    }

    public Modulos getModulos() {
        return modulos;
    }

    public void setModulos(Modulos modulos) {
        this.modulos = modulos;
    }

    public UsuarioLogadoMB getUsuarioLogadoBean() {
        return usuarioLogadoBean;
    }

    public void setUsuarioLogadoBean(UsuarioLogadoMB usuarioLogadoBean) {
        this.usuarioLogadoBean = usuarioLogadoBean;
    }

    
    public String novo(Modulos modulos){
        atividademodulo = new Atividademodulo();
        AtividadeModuloFacade atividadeModuloFacade = new AtividadeModuloFacade();
        atividademodulo.setDescricao("");
        atividademodulo.setDatafinal(new Date());
        atividademodulo.setDataInicio(new Date());
        atividademodulo.setSituacao("Ativo");
        atividademodulo.setModulos(modulos);
        Atividades atividade = new Atividades();
        atividade.setCliente(modulos.getProjeto().getCliente());
        atividade.setTempo(0);
        atividade.setEstado("Play");
        atividade.setMostratempo("00:00");
        atividade.setNome("");
        atividade.setPrioridade("3-normal");
        atividade.setSituacao(false);
        atividade.setPrazo(new Date());
        atividade.setTipo("J");
        atividade.setUsuario(usuarioLogadoBean.getUsuario());
        atividade.setSubdepartamento(usuarioLogadoBean.getUsuario().getSubdepartamento());
        AtividadeFacade atividadeFacade = new AtividadeFacade();
        atividade = atividadeFacade.salvar(atividade);
        atividademodulo.setAtividades(atividade);
        atividademodulo = atividadeModuloFacade.salvar(atividademodulo);
        atividade.setIdatividademodulo(atividademodulo.getIdatividademodulo());
        atividadeFacade.salvar(atividade);
        return "consAtividadeModulo";
    }
    
    public void gerarListaAtividades(){
        String sql = "Select a from Atividademodulo a where a.modulos.idmodulos=" + modulos.getIdmodulos();
        AtividadeModuloFacade atividadeModuloFacade = new AtividadeModuloFacade();
        listaAtividadesModulo = atividadeModuloFacade.lista(sql);
        if (listaAtividadesModulo==null){
            listaAtividadesModulo = new ArrayList<Atividademodulo>();
        }
    }
    
    
    public String raci(Atividademodulo atividadesmodulo) {
        FacesContext fc = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) fc.getExternalContext().getSession(false);
        session.setAttribute("atividademodulo", atividadesmodulo);
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("contentHeight", 430);
        RequestContext.getCurrentInstance().openDialog("RACI", options, null);
        return "";

    }
    
    public String voltar(Projeto projeto){
        projeto = modulos.getProjeto();
        FacesContext fc = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) fc.getExternalContext().getSession(false);
        session.setAttribute("projeto", projeto);
        return "consModulo";
    }
    
    public void salvarAtividade(CellEditEvent event, Atividademodulo atividademodulos){
        AtividadeFacade atividadeFacade = new AtividadeFacade();
        Atividades atividades = atividadeFacade.getAtividadeModulo(atividademodulos.getIdatividademodulo());
        if (atividades == null) {
            atividades = new Atividades();
            atividades.setCliente(atividademodulos.getModulos().getProjeto().getCliente());
            atividades.setNome(atividademodulos.getDescricao());
            atividades.setPrioridade("3-normal");
            atividades.setTipo("P");
            atividades.setEstado("Play");
            atividades.setInicio(BigInteger.valueOf(0));
            atividades.setTempo(0);
            atividades.setMostratempo("00:00");
            atividades.setPrazo(atividademodulos.getDatafinal());
            for (int i = 0; i < atividademodulos.getRaciList().size(); i++) {
                if (atividademodulos.getRaciList().get(i).getR()) {
                    atividades.setUsuario(atividademodulos.getRaciList().get(i).getMembros().getUsuario());
                    atividades.setSubdepartamento(atividademodulos.getRaciList().get(i).getMembros().getUsuario().getSubdepartamento());
                }
                if (atividademodulos.getRaciList().get(i).getR() == Boolean.FALSE) {
                    atividades.setUsuario(usuarioLogadoBean.getUsuario());
                    atividades.setSubdepartamento(usuarioLogadoBean.getUsuario().getSubdepartamento());
                }
            }
            if (atividademodulos.getRaciList().size() == 0) {
                atividades.setUsuario(usuarioLogadoBean.getUsuario());
                atividades.setSubdepartamento(usuarioLogadoBean.getUsuario().getSubdepartamento());
            }
            atividades.setIdatividademodulo(atividademodulos.getIdatividademodulo());
            atividades = atividadeFacade.salvar(atividades);
            atividademodulo.setAtividades(atividades);
            AtividadeModuloFacade atividadeModuloFacade = new AtividadeModuloFacade();
            atividadeModuloFacade.salvar(atividademodulos);
        } else {
            atividades.setNome(atividademodulos.getDescricao());
            atividades.setPrazo(atividademodulos.getDatafinal());
            atividades = atividadeFacade.salvar(atividades);
            AtividadeModuloFacade atividadeModuloFacade = new AtividadeModuloFacade();
            atividadeModuloFacade.salvar(atividademodulos);
        }
        atividademodulo = new Atividademodulo();
    }
    
}