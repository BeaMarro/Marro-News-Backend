package nl.fontys.newswebapplication.config.security.token;

import lombok.Generated;

@Generated
public interface AccessTokenEncoder {
    String encode(AccessToken accessToken);
}
