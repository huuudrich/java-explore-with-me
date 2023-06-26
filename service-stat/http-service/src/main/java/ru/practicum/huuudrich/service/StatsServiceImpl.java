package ru.practicum.huuudrich.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.exception.BadRequestException;
import ru.practicum.huuudrich.model.ServiceRequest;
import ru.practicum.huuudrich.model.ShortStat;
import ru.practicum.huuudrich.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
@Slf4j
public class StatsServiceImpl implements StatsService {

    private final StatsRepository statsRepository;

    @Override
    public ServiceRequest createRequest(ServiceRequest serviceRequest) {
        log.info("Save hit with IP:" + serviceRequest.getIp());
        return statsRepository.save(serviceRequest);
    }

    @Override
    public List<ShortStat> getStatistic(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) throws BadRequestException {
        log.info("Getting statistic");

        if (start.isAfter(end)) {
            throw new BadRequestException("Start is after end");
        }

        if (uris == null) {
            if (unique) {
                return statsRepository.getUniqueUriHitCountNotUris(start, end);
            } else {
                return sortHitsAsc(statsRepository.getUriHitCountNotUris(start, end));
            }
        } else {
            if (unique) {
                return statsRepository.getUniqueUriHitCount(start, end, uris);
            } else {
                return sortHitsAsc(statsRepository.getUriHitCount(start, end, uris));
            }
        }
    }

    @Override
    public Boolean checkIpAndUri(String ip, String uri) {
        return statsRepository.existsByIpAndUri(ip, uri);
    }

    private List<ShortStat> sortHitsAsc(List<ShortStat> content) {
        return content.stream()
                .sorted(Comparator.comparing(ShortStat::getHits).reversed())
                .collect(Collectors.toList());
    }
}
