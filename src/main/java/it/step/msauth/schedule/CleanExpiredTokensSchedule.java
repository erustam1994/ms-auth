package it.step.msauth.schedule;

import it.step.msauth.service.TokenService;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CleanExpiredTokensSchedule {

    private final TokenService tokenService;

    public CleanExpiredTokensSchedule(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Scheduled(fixedDelay = 10*60_000L)
    @SchedulerLock(name = "cleanTokens")
    public void cleanLogout() {
        tokenService.deleteInvalidTokens();
    }

}
