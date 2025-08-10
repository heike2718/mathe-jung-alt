//=====================================================
// Projekt: raetselbaukasten
// (c) Heike Winkelvoß
//=====================================================

package de.egladil.raetselbaukasten.domain.validation;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LaTeXValidatorTest {

    static Validator validator;

    @BeforeAll
    static void setup() {
        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        validator = vf.getValidator();
    }

    @Test
    void ok_tabular_booktabs() {
        String s = """
      \\begin{tabular}{ll}
      \\toprule
      A & B \\\\
      \\midrule
      1 & 2 \\\\
      \\bottomrule
      \\end{tabular}
      """;
        var bean = new Dummy(s, true);
        assertTrue(validator.validate(bean).isEmpty());
    }

    @Test
    void ok_includegraphics_rel_path_with_options() {
        String s = "\\includegraphics[width=3cm,keepaspectratio]{images/plot.pdf}";
        var bean = new Dummy(s, true);
        assertTrue(validator.validate(bean).isEmpty());
    }

    @Test
    void fail_includegraphics_parent_dir() {
        String s = "\\includegraphics{../etc/passwd}";
        var bean = new Dummy(s, true);
        assertFalse(validator.validate(bean).isEmpty());
    }

    @Test
    void fail_forbidden_input() {
        String s = "\\input{secret.tex}";
        var bean = new Dummy(s, true);
        assertFalse(validator.validate(bean).isEmpty());
    }

    @Test
    void ok_tikz_simple_draw() {
        String s = """
      \\begin{tikzpicture}
        \\draw (0,0) -- (1,1);
      \\end{tikzpicture}
      """;
        var bean = new Dummy(s, true);
        assertTrue(validator.validate(bean).isEmpty());
    }

    @Test
    void fail_tikz_foreach_blocked() {
        String s = """
      \\begin{tikzpicture}
        \\foreach \\x in {1,2,3} { \\draw (\\x,0) -- (\\x,1); }
      \\end{tikzpicture}
      """;
        var bean = new Dummy(s, true);
        assertFalse(validator.validate(bean).isEmpty());
    }

    @Test
    void ok_math_and_formatting() {
        String s = "Die Summe ist $\\sum_{i=1}^n i$ und \\textbf{fett}.";
        var bean = new Dummy(s, false);
        assertTrue(validator.validate(bean).isEmpty());
    }

    // Hilfs-Bean
    static class Dummy {
        @ValidLaTeX(allowTikz = true)
        String content;
        @ValidLaTeX(allowTikz = false)
        String simple;

        Dummy(String content, boolean alsoSimple) {
            this.content = content;
            this.simple = alsoSimple ? content : "Text $a^2+b^2=c^2$";
        }
    }
}
