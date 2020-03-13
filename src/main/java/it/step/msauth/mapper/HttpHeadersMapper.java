package it.step.msauth.mapper;

import it.step.msauth.model.TokensSaver;
import org.springframework.http.HttpHeaders;

public class HttpHeadersMapper {
    public static HttpHeaders tokenSaverToHttpsHeaders(TokensSaver tokensSaver){
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Auth-Token", tokensSaver.getToken());
        responseHeaders.set("Refresh-Token", tokensSaver.getRefreshToken());
        responseHeaders.set("User-Id", tokensSaver.getUserId().toString());
        return responseHeaders;
    }
}
