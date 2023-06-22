package ru.practicum.huuudrich.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.huuudrich.model.event.Event.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {
}
