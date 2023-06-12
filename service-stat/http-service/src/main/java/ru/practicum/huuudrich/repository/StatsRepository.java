package ru.practicum.huuudrich.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.huuudrich.model.ServiceRequest;
import ru.practicum.huuudrich.model.ShortStat;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatsRepository extends JpaRepository<ServiceRequest, Long> {
    @Query("SELECT new ru.practicum.huuudrich.model.ShortStat(sr.app, sr.uri, COUNT(sr)) " +
            "FROM ServiceRequest sr WHERE sr.timestamp BETWEEN :start " +
            "AND :end AND sr.uri IN :uris GROUP BY sr.app, sr.uri")
    List<ShortStat> getUriHitCount(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("SELECT new ru.practicum.huuudrich.model.ShortStat(sr.app, sr.uri, COUNT(DISTINCT sr.ip)) " +
            "FROM ServiceRequest sr WHERE sr.timestamp BETWEEN :start " +
            "AND :end AND sr.uri IN :uris GROUP BY sr.app, sr.uri")
    List<ShortStat> getUniqueUriHitCount(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("SELECT new ru.practicum.huuudrich.model.ShortStat(sr.app, sr.uri, COUNT(sr)) " +
            "FROM ServiceRequest sr WHERE sr.timestamp BETWEEN :start " +
            "AND :end GROUP BY sr.app, sr.uri")
    List<ShortStat> getUriHitCountNotUris(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.huuudrich.model.ShortStat(sr.app, sr.uri, COUNT(DISTINCT sr.ip)) " +
            "FROM ServiceRequest sr WHERE sr.timestamp BETWEEN :start " +
            "AND :end GROUP BY sr.app, sr.uri")
    List<ShortStat> getUniqueUriHitCountNotUris(LocalDateTime start, LocalDateTime end);
}
