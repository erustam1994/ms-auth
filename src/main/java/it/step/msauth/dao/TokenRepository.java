package it.step.msauth.dao;

import it.step.msauth.dao.entity.TokenEntity;
import it.step.msauth.model.TokenType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface TokenRepository extends JpaRepository<TokenEntity, Long> {

    default boolean isValidToken(String uuid, LocalDateTime expiredDate, TokenType type) {
        return existsByActiveTrueAndUuidEqualsAndExpiredDateAfterAndTypeEquals(uuid, expiredDate, type);
    }

    void deleteAllByActiveFalse();
    void deleteAllByExpiredDateBefore(LocalDateTime expiredDate);

    @Modifying
    @Query("UPDATE TokenEntity te SET te.active=false WHERE te.userId=:user_id")
    void deactivateTokensById(@Param("user_id") Long id);

    boolean existsByActiveTrueAndUuidEqualsAndExpiredDateAfterAndTypeEquals(String uuid, LocalDateTime expiredDate, TokenType type);

}
