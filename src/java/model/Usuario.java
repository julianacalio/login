package model;

import controller.UsuarioController;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;

@Entity

public class Usuario implements Serializable{
    
    private static final long SerialVersionUID = 1L;
    
//    @Transient public static final String FIND_BY_EMAIL_SENHA = "Usuario.findByEmailSenha";

    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long ID;

    public Usuario() {
    }
    
    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }
    
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "docente",fetch = FetchType.LAZY)
//    @Fetch(FetchMode.JOIN)
//    private List<Turma> turmas;
//
//    public List<Turma> getTurmas() {
//        return turmas;
//    }
//
//    public void setTurmas(List<Turma> turmas) {
//        this.turmas = turmas;
//    }
    
//    @ManyToOne(fetch=FetchType.EAGER, targetEntity = Centro.class)
//    @JoinColumn(name = "centro_ID")
//    private Centro centro;
//
//    public Centro getCentro() {
//        return centro;
//    }
//
//    public void setCentro(Centro centro) {
//        this.centro = centro;
//    }
 
    
    private String nome;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    
    private String email;
    
    public String getEmail() { 
        return email; 
    } 
    
    public void setEmail(String email) { 
        this.email = email.trim().toLowerCase(); 
    }

    private String senha;
    
    public String getSenha() { 
        return senha; 
    } 
    
    public void setSenha(String senha) { 
        
        this.senha = UsuarioController.convertStringToMd5(senha.trim());
    }
    
    @Override
    public int hashCode(){
        int hash = 0;
        hash += (ID != null ? ID.hashCode() : 0);
        return hash;
        
    }
    
    @Override
    public boolean equals(Object object){

        if(!(object instanceof Usuario)){
            return false;
        }
        
        Usuario other = (Usuario) object;
        if((this.ID == null && other.ID != null) || (this.ID != null && !(this.ID.equals(other.ID)))){
            return false;
        }
        
        return true;
        
    }
    
    @Override
    public String toString(){
        return this.nome;
    }
    
    
    
    
}
