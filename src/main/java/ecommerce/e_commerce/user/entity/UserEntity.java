package ecommerce.e_commerce.user.entity;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import ecommerce.e_commerce.roles.entity.RolesEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class UserEntity implements UserDetails{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(unique =  true,nullable = false)
    private String email;

    
    @Column(nullable = false)
    private String password;
        
    
    @ManyToOne //Type relationship
    @JoinColumn(name = "role")//name to column in the table
    private RolesEntity role;
    
    
    //Method Get and Set
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RolesEntity getRole() {
        return role;
    }

    public void setRole(RolesEntity role) {
        this.role = role;
    }

     //UserDetail Methods


    /**
    * Retrieves the authorities (permissions) associated with the user's role.
    * 
    * This method processes the user's role and converts its associated permissions
    * into a collection of GrantedAuthority objects. These authorities are used by
    * Spring Security to determine what actions the user is allowed to perform.
    * 
    * @return a collection of GrantedAuthority objects representing the user's permissions
    */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getPermission().stream()
            .map(permission -> new SimpleGrantedAuthority(permission.getName()))
            .collect(Collectors.toList());
    }


    @Override
    public String getUsername() {
        return email;
    }
    
}
