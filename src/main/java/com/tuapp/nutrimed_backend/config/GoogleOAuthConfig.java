package com.tuapp.nutrimed_backend.config;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Configuration
public class GoogleOAuthConfig {

    @Value("${google.oauth.client-id}")
    private String clientId;

    @Bean
    public GoogleIdTokenVerifier googleVerifier() {
        return new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(), new GsonFactory())
                .setAudience(List.of(clientId))
                .build();
    }
}