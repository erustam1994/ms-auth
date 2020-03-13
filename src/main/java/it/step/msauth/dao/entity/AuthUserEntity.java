package it.step.msauth.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "auth_user")
public class AuthUserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "auth_user_seq_generator")
    @SequenceGenerator(name = "auth_user_seq_generator", sequenceName = "auth_user_seq")
    private Long id;

    private String login;
    private String password;
    private String email;
    private String phoneNumber;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updated_at;

    @PrePersist
    public void prePersist() {
        createdAt = updated_at = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updated_at = LocalDateTime.now();
    }


}
