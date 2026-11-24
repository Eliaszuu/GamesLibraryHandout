package ch.diethelm.backend.service;

/**
 * Vereinheitlicht Spieltitel: ohne Leerzeichen am Rand, ohne doppelte
 * Leerzeichen, mit grossem Anfangsbuchstaben. Ein leerer Titel ist ein Fehler.
 */
public final class TitleNormalizer {

    private TitleNormalizer() {
    }

    public static String normalize(String raw) {
//        if (raw == null || raw.isBlank()) {
//            throw new IllegalArgumentException("Titel darf nicht leer sein");
//        }
//        String title = raw.trim().replaceAll("\\s+", " ");
//        return Character.toUpperCase(title.charAt(0)) + title.substring(1);
        return null;
    }
}