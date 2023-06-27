package ru.practicum.huuudrich.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exception.BadRequestException;
import ru.practicum.huuudrich.model.ServiceRequest;
import ru.practicum.huuudrich.model.ShortStat;
import ru.practicum.huuudrich.service.StatsService;
import ru.practicum.model.ApiError;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping
public class StatsController {
    private final StatsService statsService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @PostMapping("/hit")
    public ResponseEntity<ServiceRequest> saveRequest(@RequestBody ServiceRequest serviceRequest) {
        return new ResponseEntity<>(statsService.createRequest(serviceRequest), HttpStatus.CREATED);
    }

    @GetMapping("/stats")
    public ResponseEntity<List<ShortStat>> getStatistic(@RequestParam(value = "start") String start,
                                                        @RequestParam(value = "end") String end,
                                                        @RequestParam(value = "uris", required = false) List<String> uris,
                                                        @RequestParam(value = "unique", defaultValue = "false", required = false) Boolean unique) throws BadRequestException {
        String decodedStart = URLDecoder.decode(start, StandardCharsets.UTF_8);
        String decodedEnd = URLDecoder.decode(end, StandardCharsets.UTF_8);

        LocalDateTime startTime = LocalDateTime.parse(decodedStart, formatter);
        LocalDateTime endTime = LocalDateTime.parse(decodedEnd, formatter);
        return new ResponseEntity<>(statsService.getStatistic(startTime, endTime, uris, unique), HttpStatus.OK);
    }

    @GetMapping("/check")
    public Boolean checkIp(@RequestParam(value = "ip") String ip, @RequestParam(value = "uri") String uri) {
        return statsService.checkIpAndUri(ip, uri);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiError> handleBadRequestException(BadRequestException e) {
        log.warn("BadRequestException: " + e.getMessage());
        ApiError apiError = new ApiError();
        apiError.setMessage(e.getMessage());
        apiError.setTimestamp(LocalDateTime.now());
        apiError.setReason("Incorrectly made request.");
        apiError.setStatus(HttpStatus.BAD_REQUEST.toString());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }
}
