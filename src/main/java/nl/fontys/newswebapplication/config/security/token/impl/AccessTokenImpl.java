package nl.fontys.newswebapplication.config.security.token.impl;

import lombok.Generated;
import nl.fontys.newswebapplication.config.security.token.AccessToken;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

@Generated
@EqualsAndHashCode
@Getter
public class AccessTokenImpl implements AccessToken {
    private final String subject;
    private final int accountId;
    private final String role;

    public AccessTokenImpl(String subject, int accountId, String role) {
        this.subject = subject;
        this.accountId = accountId;
        this.role = role;
    }
}