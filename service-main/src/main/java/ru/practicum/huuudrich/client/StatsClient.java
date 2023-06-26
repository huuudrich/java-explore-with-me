package ru.practicum.huuudrich.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StatsClient {

    private final String uri;

    private final RestTemplate restTemplate;

    @Autowired
    public StatsClient(@Value("${stats.uri}") String uri, RestTemplate restTemplate) {
        this.uri = uri;
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<ClientRequest> saveRequest(ClientRequest clientRequest) {
        return restTemplate.postForEntity(uri + "/hit", clientRequest, ClientRequest.class);
    }

    public ResponseEntity<ClientRequest> getStatistic(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        UriComponentsBuilder params = UriComponentsBuilder.fromHttpUrl(uri + "/stats")
                .queryParam("start", start)
                .queryParam("end", end)
                .queryParam("uris", uris)
                .queryParam("unique", unique);
        return restTemplate.getForEntity(params.toUriString(), ClientRequest.class);
    }

    public Boolean checkIp(String checkIp, String checkUri) {
        UriComponentsBuilder params = UriComponentsBuilder.fromHttpUrl(uri + "/check")
                .queryParam("ip", checkIp)
                .queryParam("uri", checkUri);
        return restTemplate.getForObject(params.toUriString(), Boolean.class);
    }
}
