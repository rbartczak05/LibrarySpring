package pl.lodz.p.library.web.service;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.lodz.p.library.web.dto.ReaderDTO;

@Service
public class RestReaderWebService {

    private final RestTemplate restTemplate;

    private final String baseApiUrl = "http://localhost:8082";

    public RestReaderWebService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public ReaderDTO registerReader(ReaderDTO registerReaderDto) {
        ReaderDTO readerToPost = new ReaderDTO();
        readerToPost.setLogin(registerReaderDto.getLogin());
        readerToPost.setEmail(registerReaderDto.getEmail());
        readerToPost.setAge(registerReaderDto.getAge());
        readerToPost.setType("reader");

        String apiUrl = baseApiUrl + "/readers";

        return restTemplate.postForObject(apiUrl, readerToPost, ReaderDTO.class);
    }
}