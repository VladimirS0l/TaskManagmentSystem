package ru.solarev.taskmanagementapi.entity.user;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "role")
    private String role;
    @Transient
    @ManyToMany(mappedBy = "roles")
    private List<User> users;
}
