package ru.solarev.taskmanagementapi.entity.task;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.cache.annotation.EnableCaching;
import ru.solarev.taskmanagementapi.entity.user.User;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "task_comment_id")
    private Task taskComment;
    @ManyToOne
    @JoinColumn(name = "user_comment_id")
    private User userComment;
    @Column(name = "message")
    private String message;
    @CreationTimestamp
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;
    @UpdateTimestamp
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;
}
