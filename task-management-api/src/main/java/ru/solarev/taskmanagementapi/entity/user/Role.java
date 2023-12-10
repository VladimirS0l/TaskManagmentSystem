package ru.solarev.taskmanagementapi.entity.user;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "role")
    private String role;
    @Transient
    @ManyToMany(mappedBy = "roles")
    private List<Role> roles;

    public Role(Integer id, String role) {
        this.id = id;
        this.role = role;
    }

    @Override
    public String getAuthority() {
        return getRole();
    }
}
