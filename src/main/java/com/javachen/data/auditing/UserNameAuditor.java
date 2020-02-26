package com.javachen.data.auditing;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class UserNameAuditor implements AuditorAware<String> {
    @Value("${user.name}")
    private String userName;

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of(userName);
    }
}
