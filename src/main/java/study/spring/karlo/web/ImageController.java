package study.spring.karlo.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import study.spring.karlo.domain.KarloResponse;

import java.util.HashMap;
import java.util.Map;
@Slf4j
@Controller
@RequestMapping("/karlo/image")
public class ImageController {
    @Value("${karlo.api.key}")
    private String RestApiKey;

    @GetMapping("/create")
    public String createForm(){

        return"karlo/form";
    }

    @PostMapping("/create")
    public String createImage(String content) throws JsonProcessingException {
        // 카카오가 제시한 요청 uri
        String url="https://api.kakaobrain.com/v2/inference/karlo/t2i";
        // HEADER에 담길 것들
        // Authorization: KakaoAK ${REST_API_KEY}
        // Content-Type: application/json
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "KakaoAK "+RestApiKey);
        headers.add("Content-Type", "application/json");

        // BODY에 담길 것들
        // prompt:사용자가 적은 내용
        Map<String, Object> body = new HashMap<>();
        body.put("prompt", content);

        // 위에서 만든 HEAD와 BODY를 HttpEntity에 하나로 합친다.
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        // 카카오로 요청을 보내야하니 RestTemplate 객체를 하나 만든다.
        // RestTemplate:요청을 보낼 수 있게 해주는 클래스, exchange()는 모든 요청 방식이 가능하다.
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        // 응답을 받은 response를 파싱해준다.
        // 방법 1. JsonNode를 이용한 방법
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode node = objectMapper.readTree(response.getBody());
        String image = node.get("images").get(0).get("image").asText();
        log.info("imageJsonNode={}",image);
        // 방법 2. ObjectMapper를 통해 객체로 역직렬화하는 방법
        ObjectMapper objectMapper1 = new ObjectMapper();
        KarloResponse response1 = objectMapper1.readValue(response.getBody(), KarloResponse.class);
        log.info("imageObjectMapper={}", response1.getImages().get(0).getImage());

        return "";
    }

}
