package com.music.SpotifyMusicService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
public class SpotifyService {

    @Value("${spotify.client.id}")
    private String clientId;

    @Value("${spotify.client.secret}")
    private String clientSecret;

    @Value("${spotify.api.token.url}")
    private String tokenUrl;

    private static final String API_BASE_URL = "https://api.spotify.com/v1/";

    private RestTemplate restTemplate=new RestTemplate();
    private ObjectMapper objectMapper=new ObjectMapper();
    public String getAccessToken() {
        String auth = clientId + ":" + clientSecret;
        String authBase64 = java.util.Base64.getEncoder().encodeToString(auth.getBytes());
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + authBase64);
        headers.set("Content-Type", "application/x-www-form-urlencoded");
        String body = "grant_type=client_credentials";
        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.exchange(tokenUrl, HttpMethod.POST, entity, String.class);
        String token = response.getBody();
        String accessToken = extractAccessToken(token);
        return accessToken;
    }

    private String extractAccessToken(String tokenResponse) {
        return tokenResponse.split("\"access_token\":\"")[1].split("\"")[0];
    }

    public JsonNode searchMusic(String query, String accessToken) throws JsonProcessingException {
        String url = API_BASE_URL + "search?q=" + query + "&type=track";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        return objectMapper.readTree(response.getBody());
    }
}
