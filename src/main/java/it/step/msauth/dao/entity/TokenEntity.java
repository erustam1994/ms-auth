package it.step.msauth.dao.entity;

import it.step.msauth.model.TokenType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "token")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "token_seq_generator")
    @SequenceGenerator(name = "token_seq_generator", sequenceName = "token_seq")
    private Long id;

    private TokenType type;

    @Column(name = "user_id")
    private Long userId;

    @Builder.Default
    private Boolean active = true;

    private String uuid;

    @Column(name = "expired_date")
    private LocalDateTime expiredDate;

}
