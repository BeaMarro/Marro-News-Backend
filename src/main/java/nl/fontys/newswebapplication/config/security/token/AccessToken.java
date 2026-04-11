package nl.fontys.newswebapplication.config.security.token;

import lombok.Generated;

@Generated
public interface AccessToken {
    String getSubject();
    int getAccountId();
    String getRole();
}
