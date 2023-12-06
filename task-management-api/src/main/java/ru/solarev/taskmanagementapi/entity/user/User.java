package ru.solarev.taskmanagementapi.entity.user;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import ru.solarev.taskmanagementapi.entity.task.Comment;
import ru.solarev.taskmanagementapi.entity.task.Task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "username")
    private String username;
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;
    @Transient
    private String confirmedPassword;
    @OneToMany(mappedBy = "author")
    private List<Task> taskAuthors;
    @OneToMany(mappedBy = "performer")
    private List<Task> taskPerformers;
    @OneToMany(mappedBy = "userComment")
    private List<Comment> comments;
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;
    @CreationTimestamp
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;
    @UpdateTimestamp
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;
}
