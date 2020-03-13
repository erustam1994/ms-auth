package it.step.msauth.dao;

import it.step.msauth.dao.entity.AuthUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface AuthUserRepository extends JpaRepository<AuthUserEntity, Long> {

    Optional<AuthUserEntity> getFirstByLoginAndPassword(String login, String password);
    Optional<AuthUserEntity> getFirstByLogin(String login);
}
