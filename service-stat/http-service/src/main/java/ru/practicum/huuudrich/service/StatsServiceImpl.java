package ru.practicum.huuudrich.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.huuudrich.model.ServiceRequest;
import ru.practicum.huuudrich.model.ShortStat;
import ru.practicum.huuudrich.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class StatsServiceImpl implements StatsService {

    private final StatsRepository statsRepository;

    @Override
    public ServiceRequest createRequest(ServiceRequest serviceRequest) {
        log.info("Save hit with IP:" + serviceRequest);
        serviceRequest.setTimestamp(LocalDateTime.now());
        return statsRepository.save(serviceRequest);
    }

    @Override
    public List<ShortStat> getStatistic(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        log.info("Getting statistic");
        if (unique) {
            return statsRepository.getUniqueUriHitCount(start, end, uris);
        }
    }

    @Override
    public Boolean checkUri(String ip) {
        return statsRepository.existsByIp(ip);
    }

    private List<ShortStat> sortHitsAsc(List<ShortStat> content) {
        return content.stream()
                .sorted(Comparator.comparing(ShortStat::getHits).reversed())
                .collect(Collectors.toList());

        return statsRepository.getUriHitCount(start, end, uris);
    }
}
