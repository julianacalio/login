package controller;

import facade.UsuarioFacade;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import model.Usuario;
import util.UsuarioDataModel;

@Named(value = "usuarioController")
@SessionScoped
public class UsuarioController implements Serializable {

    public UsuarioController() {
        usuario = new Usuario();
    }

    //Guarda o usuario atual
    private Usuario usuario;

    @EJB
    private UsuarioFacade usuarioFacade;
    private UsuarioDataModel usuarioDataModel;

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    private Usuario getUsuario(Long key) {
        return this.buscar(key);

    }

    public UsuarioDataModel getUsuarioDataModel() {
        if (usuarioDataModel == null) {
            List<Usuario> usuarios = this.listarTodas();
            usuarioDataModel = new UsuarioDataModel(usuarios);
        }
        return usuarioDataModel;
    }

    public void setUsuarioDataModel(UsuarioDataModel usuarioDataModel) {
        this.usuarioDataModel = usuarioDataModel;
    }

    private List<Usuario> listarTodas() {
        return usuarioFacade.findAll();

    }

//    public List<Turma> getTurmas(){
//        return usuario.getTurmas();
//    }
    public void recriarModelo() {
        this.usuarioDataModel = null;
    }

    public SelectItem[] getItemsAvaiableSelectOne() {
        return JsfUtil.getSelectItems(usuarioFacade.findAll(), true);
    }

    public Usuario getUsuario() {
        if (usuario == null) {
            usuario = new Usuario();
        }
        return usuario;
    }

//    public String prepareCreate(int i) {
//        usuario = new Usuario();
//        if(i == 1){
//            return "/view/usuario/Create";
//        }
//        else{
//        return "Create";
//        }
//    }
    public void salvarNoBanco() {

        try {
            usuarioFacade.save(usuario);
            JsfUtil.addSuccessMessage("Usuario " + usuario.getNome() + " criado com sucesso!");
            usuario = null;
            recriarModelo();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Ocorreu um erro de persistência");

        }

    }

    public Usuario buscar(Long id) {

        return usuarioFacade.find(id);
    }

    public String index() {
        usuario = null;
        usuarioDataModel = null;
        return "/index";
    }

    public void editar() {
        try {
            usuarioFacade.edit(usuario);
            JsfUtil.addSuccessMessage("Usuario Editado com sucesso!");
            usuario = null;
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Ocorreu um erro de persistência, não foi possível editar o usuario: " + e.getMessage());

        }
    }

    public void delete() {
        usuario = (Usuario) usuarioDataModel.getRowData();
        try {
            usuarioFacade.remove(usuario);
            usuario = null;
            JsfUtil.addSuccessMessage("Usuario Deletado");
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Ocorreu um erro de persistência");
        }

        recriarModelo();
    }

    public String prepareEdit() {
        usuario = (Usuario) usuarioDataModel.getRowData();
        return "Edit";
    }

    public String prepareView() {
        usuario = (Usuario) usuarioDataModel.getRowData();
        //docente = usuarioFacade.find(usuario.getID());
        //docenteFacade.edit(usuarioFacade.find(usuario.getID()));
        //docenteFacade.edit(usuario);
        return "View";
    }
    
    public String prepareList() {
        this.usuarioDataModel = null;
        
        
            return "view/usuario/List";
        
    }
    
    public String prepareListAutorizada(){
        this.usuarioDataModel = null;
            return "view/usuario/ListAutorizada";
        
    }
    
//    public String prepareCreate(boolean podeCriar){
//        
//        if(podeCriar){
//            return "/view/usuario/Create";
//        }
//        else{
//            return "/login";
//        }
//    }
    
    public String prepareCreate(){
     
            return "/view/usuario/Create";
        
    }

    public static String convertStringToMd5(String valor) {
        MessageDigest mDigest;
        try {
            mDigest = MessageDigest.getInstance("MD5"); //Convert a String valor para um array de bytes em MD5 
            byte[] valorMD5 = mDigest.digest(valor.getBytes("UTF-8")); //Convertemos os bytes para hexadecimal, assim podemos salvar 
            //no banco para posterior comparação se senhas 
            StringBuilder sb = new StringBuilder();
            for (byte b : valorMD5) {
                sb.append(Integer.toHexString((b & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {             
            return null;
        } catch (UnsupportedEncodingException e) {             
            return null;
        }
    }
     
    



    @FacesConverter(forClass = Usuario.class)
    public static class UsuarioControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            UsuarioController controller = (UsuarioController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "usuarioController");
            return controller.getUsuario(getKey(value));
        }

        java.lang.Long getKey(String value) {
            java.lang.Long key;
            key = Long.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Long value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Usuario) {
                Usuario d = (Usuario) object;
                return getStringKey(d.getID());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Usuario.class.getName());
            }
        }
    }

}
