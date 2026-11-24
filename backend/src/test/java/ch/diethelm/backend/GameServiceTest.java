package ch.diethelm.backend;

import ch.diethelm.backend.model.Game;
import ch.diethelm.backend.repository.GameRepository;
import ch.diethelm.backend.service.GameService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private GameService gameService;

    private Game zelda() {
        return Game.builder()
                .id(1L)
                .title("Zelda")
                .description("Abenteuer")
                .imageUrl("http://bild/zelda.png")
                .releaseDate(LocalDate.of(2017, 3, 3))
                .build();
    }

    @Test
    void getAllGames_returnsAllFromRepository() {
        List<Game> games = List.of(zelda());
        when(gameRepository.findAll()).thenReturn(games);

        List<Game> result = gameService.getAllGames();

        assertEquals(games, result);
        verify(gameRepository).findAll();
    }

    @Test
    void getGameById_existingId_returnsGame() {
        Game game = zelda();
        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));

        Game result = gameService.getGameById(1L);

        assertSame(game, result);
        verify(gameRepository).findById(1L);
    }

    @Test
    void getGameById_unknownId_throwsNoSuchElement() {
        when(gameRepository.findById(99L)).thenReturn(Optional.empty());

        NoSuchElementException ex = assertThrows(NoSuchElementException.class,
                () -> gameService.getGameById(99L));

        assertEquals("Spiel mit ID 99 nicht gefunden", ex.getMessage());
        verify(gameRepository).findById(99L);
    }

    @Test
    void searchByTitle_delegatesToRepository() {
        List<Game> games = List.of(zelda());
        when(gameRepository.findByTitleContainingIgnoreCase("zel")).thenReturn(games);

        List<Game> result = gameService.searchByTitle("zel");

        assertEquals(games, result);
        verify(gameRepository).findByTitleContainingIgnoreCase("zel");
    }

    @Test
    void createGame_savesGameAndReturnsSavedEntity() {
        Game input = zelda();
        input.setId(null);
        Game saved = zelda();
        when(gameRepository.save(input)).thenReturn(saved);

        Game result = gameService.createGame(input);

        assertSame(saved, result);
        verify(gameRepository).save(input);
    }

    @Test
    void updateGame_existingId_overwritesAllFieldsAndSaves() {
        Game existing = zelda();
        Game update = Game.builder()
                .title("Mario")
                .description("Jump and Run")
                .imageUrl("http://bild/mario.png")
                .releaseDate(LocalDate.of(1985, 9, 13))
                .build();
        when(gameRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(gameRepository.save(any(Game.class))).thenAnswer(inv -> inv.getArgument(0));

        Game result = gameService.updateGame(1L, update);

        ArgumentCaptor<Game> captor = ArgumentCaptor.forClass(Game.class);
        verify(gameRepository).save(captor.capture());
        Game saved = captor.getValue();
        assertEquals(1L, saved.getId());
        assertEquals("Mario", saved.getTitle());
        assertEquals("Jump and Run", saved.getDescription());
        assertEquals("http://bild/mario.png", saved.getImageUrl());
        assertEquals(LocalDate.of(1985, 9, 13), saved.getReleaseDate());
        assertSame(saved, result);
    }

    @Test
    void updateGame_unknownId_throwsAndDoesNotSave() {
        when(gameRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class,
                () -> gameService.updateGame(99L, zelda()));

        verify(gameRepository, never()).save(any(Game.class));
    }

    @Test
    void deleteGame_existingId_deletesById() {
        when(gameRepository.existsById(1L)).thenReturn(true);

        gameService.deleteGame(1L);

        verify(gameRepository).deleteById(1L);
    }

    @Test
    void deleteGame_unknownId_throwsAndDoesNotDelete() {
        when(gameRepository.existsById(99L)).thenReturn(false);

        assertThrows(NoSuchElementException.class, () -> gameService.deleteGame(99L));

        verify(gameRepository, never()).deleteById(any());
    }
}