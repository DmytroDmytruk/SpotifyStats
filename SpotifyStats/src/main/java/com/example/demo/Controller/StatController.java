package com.example.demo.Controller;

import java.net.URI;
import java.net.URL;
import java.util.Base64;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/spotify")
public class StatController {

    private static final String CLIENT_ID = "7ea1798ef6a540319a8413ce0c0c012e";
    private static final String REDIRECT_URI = "http://localhost:8080/spotify/callback";
    private static final String CLIENT_SECRET = "abaf6f915a1143018ed0009b4258e23e";
    
    
    @GetMapping("/login")
    public ResponseEntity<Void> login(HttpServletResponse httpServletResponse) {
        String state = generateRandomString(16);
        String scope = "user-read-private%20user-read-email";
        String redirectURL = "https://accounts.spotify.com/authorize?" +
                "response_type=code&" +
                "client_id=" + CLIENT_ID + "&" +
                "scope=" + scope + "&" +
                "redirect_uri=" + REDIRECT_URI + "&" +
                "state=" + state;      
        int i = 5;
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(redirectURL)).build();
       
    }
      
    
    @GetMapping("/callback")
    public ResponseEntity<String> call(@RequestParam(value = "code") String code, @RequestParam(value = "state") String state){
      
    	MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("code", code);
        requestBody.add("redirect_uri", REDIRECT_URI);
        requestBody.add("grant_type", "authorization_code");
        
        String credentials = CLIENT_ID + ":" + CLIENT_SECRET;
        String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + encodedCredentials);
        
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                "https://accounts.spotify.com/api/token", requestEntity, String.class);
        return responseEntity;
    }

    
    private String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * characters.length());
            result.append(characters.charAt(index));
        }
        return result.toString();
    }
}
