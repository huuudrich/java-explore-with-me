package ru.practicum.huuudrich.model.compilations;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.huuudrich.model.event.Event;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "compilations", schema = "public")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "compilation_id")
    List<Event> events = new ArrayList<>();
    Boolean pinned;
    String title;

    @PrePersist
    public void prePersist() {
        if (this.pinned == null) {
            this.pinned = false;
        }
    }
}
