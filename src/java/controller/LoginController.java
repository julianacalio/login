package controller;

import static controller.HibernateUtil.getSessionFactory;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import model.Usuario;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.primefaces.context.RequestContext;

@Named(value = "loginController")
@SessionScoped
public class LoginController implements Serializable {

    private String username;

    private String password;

    private boolean loggedIn;
    
    private boolean podeCadastrar;

    private Usuario usuarioLogado;

    public boolean isPodeCadastrar() {
        return podeCadastrar;
    }

    public void setPodeCadastrar(boolean podeCadastrar) {
        this.podeCadastrar = podeCadastrar;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Verifica se usuário existe ou se pode logar 
    public Usuario isUsuarioReadyToLogin(String email, String senha) {


            try {
                Session session = getSessionFactory().openSession();
                Query query = session.createQuery("from Usuario u where u.email = :email and u.senha = :senha ");
                query.setParameter("email", email);
                query.setParameter("senha", UsuarioController.convertStringToMd5(senha.trim()));
                List resultado = query.list();

                if (resultado.size() == 1) {
                    Usuario userFound = (Usuario) resultado.get(0);
                    return userFound;
                } else {
                    return null;
                }
            } catch (HibernateException e) {
                return null;
            }

    }
    
    //Realiza o login caso de tudo certo 
    public void doLogin() {

        RequestContext context = RequestContext.getCurrentInstance();
        FacesMessage msg;
        loggedIn = false;
        
        this.username = this.username.toLowerCase().trim();
        this.password = this.password.trim();

        //Verifica se o e-mail e senha existem e se o usuario pode logar 
        Usuario usuarioFound = (Usuario) isUsuarioReadyToLogin(this.username, this.password);

        if (usuarioFound == null) {
            loggedIn = false;
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Login Error", "Invalid credentials");
        } else {

            loggedIn = true;
            usuarioLogado = usuarioFound;
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Bem-vindo(a)!", this.usuarioLogado.getNome());
            
            //Verifica se usuário também pode cadastrar outros usuários
            if(this.username.equals("juliana@ufabc")){
                this.podeCadastrar = true;
            }
        }

        FacesContext.getCurrentInstance().addMessage(null, msg);
        context.addCallbackParam("loggedIn", loggedIn);

        //return page();
    }
    
    //Realiza o login caso de tudo certo 
    public void doLogout() {

        RequestContext context = RequestContext.getCurrentInstance();
        FacesMessage msg;
        msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Logout efetuado", this.usuarioLogado.getNome());
        loggedIn = false;      
        username = "";
        password = "";
        podeCadastrar = false;
        usuarioLogado = null;

        FacesContext.getCurrentInstance().addMessage(null, msg);

    }
    
 
    public String page() {
        if (loggedIn) {
            return "/index";
        } else {
            return "/login";
        }
    }
    
    public String page2() {
        if (loggedIn) {
            return "/view/usuario/Create";
        } else {
            return "/login";
        }
    }

    public void isLogado() {

        //loggedIn = true;
        
        if (!loggedIn) {
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml?faces-redirect=true");
            

} catch (IOException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

}
