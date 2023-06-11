package ru.practicum.huuudrich.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.practicum.huuudrich.model.ClientRequest;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class StatsClient {
    @Value("${stats.uri}")
    private String uri;

    private RestTemplate restTemplate;

    public ResponseEntity<ClientRequest> saveRequest(ClientRequest clientRequest) {
        return restTemplate.postForEntity(uri + "/hit", clientRequest, ClientRequest.class);
    }

    public ResponseEntity<ClientRequest> saveStatistic(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        UriComponentsBuilder params = UriComponentsBuilder.fromHttpUrl(uri)
                .queryParam("start", start)
                .queryParam("end", end)
                .queryParam("uris", uris)
                .queryParam("unique", unique);
        return restTemplate.getForEntity(params.toUriString(), ClientRequest.class);
    }
}
