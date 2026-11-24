package ch.diethelm.backend;

import ch.diethelm.backend.model.Game;
import ch.diethelm.backend.service.GameBadges;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameBadgesTest {

    // Feste Uhr: Der Test rechnet an jedem Tag gleich.
    private final Clock fixed = Clock.fixed(Instant.parse("2026-10-27T10:00:00Z"),
            ZoneId.of("Europe/Zurich"));
    private final GameBadges badges = new GameBadges(fixed);

    private Game releasedOn(LocalDate date) {
        return Game.builder().title("X").releaseDate(date).build();
    }

    @Test
    void releasedLastWeek_isNew() {
        assertTrue(badges.isNew(releasedOn(LocalDate.of(2026, 10, 20))));
    }

    @Test
    void releasedOneYearAgo_isNotNew() {
        assertFalse(badges.isNew(releasedOn(LocalDate.of(2025, 10, 27))));
    }

    // Grenzwerte: 30 Tage sind noch neu, 31 Tage nicht mehr.
    @Test
    void releasedExactly30DaysAgo_isNew() {
        assertTrue(badges.isNew(releasedOn(LocalDate.of(2026, 9, 27))));
    }

    @Test
    void released31DaysAgo_isNotNew() {
        assertFalse(badges.isNew(releasedOn(LocalDate.of(2026, 9, 26))));
    }
}