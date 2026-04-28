package com.mohemeokji.mohemeokji.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DevCurrentUserProvider implements CurrentUserProvider {

    private final Long devUserId;

    public DevCurrentUserProvider(@Value("${app.auth.dev-user-id:2}") Long devUserId) {
        this.devUserId = devUserId;
    }

    @Override
    public Long getCurrentUserId() {
        return devUserId;
    }
}
