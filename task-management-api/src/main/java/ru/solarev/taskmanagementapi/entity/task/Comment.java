package ru.solarev.taskmanagementapi.entity.task;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.cache.annotation.EnableCaching;
import ru.solarev.taskmanagementapi.entity.user.User;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "comments")
public class Comment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @ManyToOne
    private Task task;
    @Column(name = "username")
    private String username;
    @Column(name = "message")
    private String message;
    @CreationTimestamp
    @Column(name = "created_date")
    private LocalDateTime createdDate;
}
