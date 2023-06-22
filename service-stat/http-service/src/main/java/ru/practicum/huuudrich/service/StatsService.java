package ru.practicum.huuudrich.service;

import ru.practicum.huuudrich.model.ServiceRequest;
import ru.practicum.huuudrich.model.ShortStat;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    ServiceRequest createRequest(ServiceRequest serviceRequest);

    List<ShortStat> getStatistic(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);

    Boolean checkUri(String uri);
}
