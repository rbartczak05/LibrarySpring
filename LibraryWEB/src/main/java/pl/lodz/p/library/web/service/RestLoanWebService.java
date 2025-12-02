package pl.lodz.p.library.web.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pl.lodz.p.library.web.dto.LoanDTO;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class RestLoanWebService {

    private final RestTemplate restTemplate;
    private final String baseApiUrl;

    public RestLoanWebService(RestTemplateBuilder restTemplateBuilder,
                              @Value("${library.rest.url:http://localhost:8082}") String restUrl) {
        this.restTemplate = restTemplateBuilder.build();
        if (!restUrl.startsWith("http://") && !restUrl.startsWith("https://")) {
            restUrl = "http://" + restUrl;
        }
        this.baseApiUrl = restUrl;
    }

    public List<LoanDTO> getAllLoans() throws RestClientException {
        String url = baseApiUrl + "/loans";
        LoanDTO[] response = restTemplate.getForObject(url, LoanDTO[].class);
        return response == null ? List.of() : Arrays.asList(response);
    }

    public List<LoanDTO> getLoansByReader(String readerId) throws RestClientException {
        String url = baseApiUrl + "/loans/reader_id/" + readerId;
        LoanDTO[] response = restTemplate.getForObject(url, LoanDTO[].class);
        return response == null ? List.of() : Arrays.asList(response);
    }

    public List<LoanDTO> getLoansByActive(boolean active) throws RestClientException {
        String url = baseApiUrl + "/loans/active/" + active;
        LoanDTO[] response = restTemplate.getForObject(url, LoanDTO[].class);
        return response == null ? List.of() : Arrays.asList(response);
    }

    public List<LoanDTO> getLoansByReaderAndActive(String readerId, boolean active) throws RestClientException {
        String path = active ? "/loans/reader_id/" + readerId + "/active" : "/loans/reader_id/" + readerId + "/inactive";
        String url = baseApiUrl + path;
        LoanDTO[] response = restTemplate.getForObject(url, LoanDTO[].class);
        return response == null ? List.of() : Arrays.asList(response);
    }

    public LoanDTO createLoan(String readerId, String bookSetId, LocalDateTime startTime) throws RestClientException {
        String url = baseApiUrl + "/loans";
        UriComponentsBuilder builder = UriComponentsBuilder.fromUri(URI.create(url))
                .queryParam("readerId", readerId)
                .queryParam("bookSetId", bookSetId);
        if (startTime != null) {
            builder.queryParam("loanStartTime", startTime);
        }
        String finalUrl = builder.build().toUriString();
        return restTemplate.postForObject(finalUrl, null, LoanDTO.class);
    }

    public LoanDTO endLoan(String loanId) throws RestClientException {
        String url = baseApiUrl + "/loans/" + loanId + "/end";
        return restTemplate.postForObject(url, null, LoanDTO.class);
    }
}
