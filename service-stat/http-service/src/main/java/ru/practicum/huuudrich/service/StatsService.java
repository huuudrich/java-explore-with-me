package ru.practicum.huuudrich.service;

import ru.practicum.exception.BadRequestException;
import ru.practicum.huuudrich.model.*;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    ServiceRequest createRequest(ServiceRequest serviceRequest);

    List<ShortStat> getStatistic(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) throws BadRequestException;

    Boolean checkIpAndUri(String ip, String uri);
}
