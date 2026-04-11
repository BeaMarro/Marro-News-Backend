package nl.fontys.newswebapplication.config.security.token;

import lombok.Generated;

@Generated
public interface AccessTokenDecoder {
    AccessToken decode(String accessTokenEncoded);
}
