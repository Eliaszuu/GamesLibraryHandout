package ch.diethelm.backend.service;

import ch.diethelm.backend.model.Game;

import java.time.Clock;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Entscheidet, ob ein Spiel als «neu» angezeigt wird: Es ist neu, wenn es
 * vor höchstens 30 Tagen erschienen ist. Die Uhr wird eingespritzt, damit
 * Tests nicht vom heutigen Datum abhängen.
 */
public class GameBadges {

    private final Clock clock;

    public GameBadges(Clock clock) {
        this.clock = clock;
    }

    public boolean isNew(Game game) {
        long days = ChronoUnit.DAYS.between(game.getReleaseDate(), LocalDate.now(clock));
        return days <= 30;
    }
}