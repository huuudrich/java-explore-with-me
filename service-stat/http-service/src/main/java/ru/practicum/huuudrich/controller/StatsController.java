package ru.practicum.huuudrich.controller;

import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.huuudrich.model.ServiceRequest;
import ru.practicum.huuudrich.model.ShortStat;
import ru.practicum.huuudrich.service.StatsService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/stats")
public class StatsController {
    private final StatsService statsService;

    @PostMapping("/hit")
    public ResponseEntity<ServiceRequest> saveRequest(ServiceRequest serviceRequest) {
        return new ResponseEntity<>(statsService.createRequest(serviceRequest), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ShortStat>> getStatistic(@RequestParam(value = "start") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                                        @RequestParam(value = "start") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                                        @RequestParam(value = "uris", required = false) List<String> uris,
                                                        @RequestParam(value = "unique", defaultValue = "false", required = false) Boolean unique) {
        return new ResponseEntity<>(statsService.getStatistic(start, end, uris, unique), HttpStatus.OK);
    }

    @GetMapping("/check")
    public Boolean checkIp(@RequestParam(value = "ip") String ip) {
        return statsService.checkUri(ip);
    }
}
