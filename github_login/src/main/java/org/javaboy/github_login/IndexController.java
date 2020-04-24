package org.javaboy.github_login;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @作者 江南一点雨
 * @微信公众号 江南一点雨
 * @网站 http://www.itboyhub.com
 * @国际站 http://www.javaboy.org
 * @微信 a_java_boy
 * @GitHub https://github.com/lenve
 * @Gitee https://gitee.com/lenve
 */
@Controller
public class IndexController {
    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/index.html")
    public String index() {
        return "index";
    }

    @GetMapping("/authorization_code")
    public String authorization_code(String code) throws JsonProcessingException {
        Map<String, String> map = new HashMap<>();
        map.put("client_id", "xxx");
        map.put("client_secret", "xxx");
        map.put("state", "javaboy");
        map.put("code", code);
        map.put("redirect_uri", "http://localhost:8080/authorization_code");
        Map<String,String> resp = restTemplate.postForObject("https://github.com/login/oauth/access_token", map, Map.class);
        System.out.println(resp);
        HttpHeaders httpheaders = new HttpHeaders();
        httpheaders.add("Authorization", "token " + resp.get("access_token"));
        HttpEntity<?> httpEntity = new HttpEntity<>(httpheaders);
        ResponseEntity<Map> exchange = restTemplate.exchange("https://api.github.com/user", HttpMethod.GET, httpEntity, Map.class);
        System.out.println("exchange.getBody() = " + new ObjectMapper().writeValueAsString(exchange.getBody()));
        return "forward:/index.html";
    }
}
