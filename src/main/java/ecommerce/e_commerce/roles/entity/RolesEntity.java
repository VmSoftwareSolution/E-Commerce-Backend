package ecommerce.e_commerce.roles.entity;

import java.util.List;

import ecommerce.e_commerce.permission.entity.PermissionEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "roles")
public class RolesEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true,nullable = false)
    private String name;

    @Column(nullable = true)
    private String description;

    @ManyToMany// Type relationship
    @JoinTable(//Create table aux
        name = "roles_permissions",//name table ux
        joinColumns = @JoinColumn(name = "role_id",referencedColumnName = "id"),//relationship idRole
        inverseJoinColumns = @JoinColumn(name = "permission_id", referencedColumnName = "id")//relationship idPermission
    )
    private List<PermissionEntity> permission;

    //Methods Getters and Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<PermissionEntity> getPermission() {
        return permission;
    }

    public void setPermission(List<PermissionEntity> permission) {
        this.permission = permission;
    }    
}
