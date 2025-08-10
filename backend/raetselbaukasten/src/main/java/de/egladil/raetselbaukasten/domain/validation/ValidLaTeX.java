//=====================================================
// Projekt: raetselbaukasten
// (c) Heike Winkelvoß
//=====================================================

package de.egladil.raetselbaukasten.domain.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = LaTeXValidator.class)
public @interface ValidLaTeX {
    String message() default "Ungültiger LaTeX-Inhalt";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    // Erlaubt TikZ (ohne foreach/Library-Loads)?
    boolean allowTikz() default true;

    // Erlaubte Bild-Endungen (ohne Punkt, klein)
    String[] allowedImageExtensions() default { "png", "jpg", "jpeg", "pdf" };

    // Optionaler Basis-Pfad-Präfix (nur relative Pfade erlaubt; ".." verboten)
    String imageBaseHint() default ""; // rein informativ für Fehlermeldungen
}
