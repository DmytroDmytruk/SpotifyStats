package com.example.demo.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/spotify")
public class StatController {

    private static final String CLIENT_ID = "7ea1798ef6a540319a8413ce0c0c012e";
    private static final String REDIRECT_URI = "callback";

    @GetMapping("/login")
    public String login() {
        String state = generateRandomString(16);
        String scope = "user-read-private user-read-email";
        String redirectURL = "https://accounts.spotify.com/authorize?" +
                "response_type=code&" +
                "client_id=" + CLIENT_ID + "&" +
                "scope=" + scope + "&" +
                "redirect_uri=" + REDIRECT_URI + "&" +
                "state=" + state;      
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(redirectURL, String.class);
        int i = 5;
        return response;
    }
      
    @GetMapping("/callback")
    public void callback() {
    	
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
