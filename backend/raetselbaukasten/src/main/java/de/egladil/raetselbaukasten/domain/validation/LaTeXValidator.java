//=====================================================
// Projekt: raetselbaukasten
// (c) Heike Winkelvoß
//=====================================================

package de.egladil.raetselbaukasten.domain.validation;

import jakarta.enterprise.context.Dependent;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Dependent
public class LaTeXValidator implements ConstraintValidator<ValidLaTeX, String> {

    private boolean allowTikz;
    private Set<String> allowedImgExt;

    // --- Tokenizer / Parser-Patterns ---
    private static final Pattern CMD = Pattern.compile("\\\\([A-Za-z@]+|.)");
    private static final Pattern BEGIN = Pattern.compile("\\\\begin\\{([^}]+)\\}");
    private static final Pattern END   = Pattern.compile("\\\\end\\{([^}]+)\\}");
    private static final Pattern INCLUDEGFX = Pattern.compile(
            "\\\\includegraphics(?:\\[([^\\]]*)\\])?\\{([^}]+)\\}");

    // --- Ein-Zeichen-Escapes, die wir akzeptieren ---
    private static final Set<String> ONE_CHAR_ESC = Set.of("%","_","#","&","$","{","}","[","]");

    // --- verbotene Befehle (Sicherheitsbarriere) ---
    private static final Set<String> FORBIDDEN = Set.of(
            "input","include","openin","openout","read","write","write18",
            "usepackage","RequirePackage","includeonly","usepgflibrary","usetikzlibrary",
            "catcode","def","gdef","xdef","edef","let","futurelet","csname","expandafter",
            "everypar","everyjob","output","shipout","special","pdfobj","pdfximage",
            "luaexec","directlua","immediate","write18","foreach" // foreach speziell für TikZ
    );

    // --- erlaubte Umgebungen ---
    private static final Set<String> ALLOWED_ENVS = Set.of(
            // Text/Listen
            "itemize","enumerate","description","center","flushleft","flushright",
            // Mathe
            "equation","equation*","align","align*","gather","gather*","split",
            // Tabellen
            "tabular","tabular*","tabularx","tabulary","array","longtable",
            // TikZ (optional)
            "tikzpicture"
    );

    // --- erlaubte Kommandos (Auszug, erweiterbar) ---
    private static final Set<String> ALLOWED_CMDS = new HashSet<>(List.of(
            // Textformat
            "textbf","textit","emph","underline","textrm","texttt","textsc",
            "large","Large","LARGE","huge","Huge","small","footnotesize","normalsize",
            "color","textcolor","fcolorbox","fbox","parbox","mbox","LaTeX","TeX",
            // Mathe (amsmath/…)
            "frac","sqrt","cdot","times","sum","int","lim","log","ln",
            "le","ge","neq","in","subset","subseteq","cup","cap","ldots","cdots",
            "hat","bar","vec","overline","underline","alpha","beta","gamma","pi","mu","sigma",
            // Tabellen (booktabs/multirow)
            "toprule","midrule","bottomrule","cmidrule","multicolumn","multirow",
            // Grafiken
            "includegraphics",
            // Sonstiges
            "\\"
    ));

    // Wenn TikZ erlaubt ist, füge harmlose TikZ-Befehle hinzu:
    private static final Set<String> TIKZ_SAFE_CMDS = Set.of(
            "draw","path","node","coordinate","matrix","fill","filldraw","clip","scope","begin","end"
    );

    @Override
    public void initialize(ValidLaTeX ann) {
        this.allowTikz = ann.allowTikz();
        this.allowedImgExt = new HashSet<>();
        for (String e : ann.allowedImageExtensions()) this.allowedImgExt.add(e.toLowerCase(Locale.ROOT));
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext ctx) {
        if (s == null || s.isBlank()) return true;

        // 1) keine Control-Zeichen
        if (s.chars().anyMatch(c -> Character.getType(c) == Character.CONTROL && c != '\t' && c != '\n' && c != '\r')) {
            return fail(ctx, "Control-Zeichen sind nicht erlaubt");
        }

        // 2) Klammern grob balanciert
        if (!balanced(s,'{','}')) return fail(ctx,"Unbalancierte {}-Klammern");
        if (!balanced(s,'[',']')) return fail(ctx,"Unbalancierte []-Klammern");

        // 3) Umgebungen balanciert + erlaubt
        if (!checkEnvironments(s, ctx)) return false;

        // 4) \includegraphics speziell prüfen (Optionen + Pfad)
        Matcher gfx = INCLUDEGFX.matcher(s);
        while (gfx.find()) {
            String opts = gfx.group(1); // optional
            String path = gfx.group(2);
            if (!checkIncludeGraphics(opts, path, ctx)) return false;
        }

        // 5) Kommandos whitelisten / verbieten
        boolean insideTikz = false;
        Deque<String> envStack = new ArrayDeque<>();
        // track tikzpicture via Envs-Check: wir parsen Envs nochmal schnell für Flag
        Matcher b = BEGIN.matcher(s); Matcher e = END.matcher(s);
        List<int[]> marks = new ArrayList<>();
        while (b.find()) marks.add(new int[]{b.start(),1});
        while (e.find()) marks.add(new int[]{e.start(),2});
        marks.sort(Comparator.comparingInt(a -> a[0]));
        b.reset(); e.reset(); int bi=0, ei=0;

        for (int[] mk : marks) {
            if (mk[1]==1) { b.find(bi); bi=b.end(); envStack.push(b.group(1)); }
            else { e.find(ei); ei=e.end(); if (!envStack.isEmpty()) envStack.pop(); }
            insideTikz = !envStack.isEmpty() && "tikzpicture".equals(envStack.peek());
            // Zwischen den Marks prüfen wir Kommandos im jeweiligen Segment
            // (vereinfacht scannen wir einfach alles; das Flag steuert nur die erlaubten TikZ-Cmds)
        }

        Matcher m = CMD.matcher(s);
        while (m.find()) {
            String raw = m.group(1); // z.B. "textbf" oder ein einzelnes Zeichen
            if (raw.length() == 1) {
                if (!ONE_CHAR_ESC.contains(raw)) return fail(ctx, "Unerlaubtes Escape: \\"+raw);
                continue;
            }
            String name = raw;

            // Verbote
            if (FORBIDDEN.contains(name)) return fail(ctx, "Verbotenes Kommando: \\"+name);

            // Whitelist (inkl. TikZ-Safe-Cmds, wenn innerhalb tikzpicture)
            boolean allowed = ALLOWED_CMDS.contains(name) ||
                    (allowTikz && TIKZ_SAFE_CMDS.contains(name));
            if (!allowed) {
                return fail(ctx, "Unerlaubtes Kommando: \\"+name);
            }
        }

        return true;
    }

    private boolean checkEnvironments(String s, ConstraintValidatorContext ctx) {
        Deque<String> stack = new ArrayDeque<>();
        Matcher b = BEGIN.matcher(s); Matcher e = END.matcher(s);
        List<int[]> marks = new ArrayList<>();
        while (b.find()) marks.add(new int[]{b.start(),1});
        while (e.find()) marks.add(new int[]{e.start(),2});
        marks.sort(Comparator.comparingInt(a -> a[0]));
        b.reset(); e.reset(); int bi=0, ei=0;

        for (int[] mk : marks) {
            if (mk[1]==1) {
                b.find(bi); bi=b.end();
                String env = b.group(1);
                if (!ALLOWED_ENVS.contains(env) || (!allowTikz && "tikzpicture".equals(env))) {
                    return fail(ctx,"Unerlaubte Umgebung: "+env);
                }
                stack.push(env);
            } else {
                e.find(ei); ei=e.end();
                String env = e.group(1);
                if (stack.isEmpty() || !stack.pop().equals(env)) return fail(ctx,"Unbalancierte Umgebung: "+env);
            }
        }
        return stack.isEmpty() || fail(ctx,"Unbalancierte Umgebung(en)");
    }

    private boolean checkIncludeGraphics(String opts, String path, ConstraintValidatorContext ctx) {
        // Pfad: nur relativ, kein .., keine Backslashes
        if (path.startsWith("/") || path.contains("..") || path.contains("\\"))
            return fail(ctx, "\\includegraphics: nur relative, sichere Pfade erlaubt");

        // Endung prüfen
        int dot = path.lastIndexOf('.');
        if (dot <= 0) return fail(ctx, "\\includegraphics: fehlende Dateiendung");
        String ext = path.substring(dot+1).toLowerCase(Locale.ROOT);
        if (!allowedImgExt.contains(ext))
            return fail(ctx, "\\includegraphics: Dateiendung nicht erlaubt ("+ext+")");

        // Optionen parsen (key=val, kommasepariert)
        if (opts != null && !opts.isBlank()) {
            for (String kv : opts.split("\\s*,\\s*")) {
                if (kv.isBlank()) continue;
                String[] parts = kv.split("\\s*=\\s*", 2);
                String k = parts[0];
                String v = parts.length>1 ? parts[1] : "";
                switch (k) {
                    case "scale" -> {
                        if (!v.matches("[0-9]*\\.?[0-9]+")) return fail(ctx,"includegraphics: ungültiger scale");
                    }
                    case "width", "height" -> {
                        if (!v.matches("[0-9]*\\.?[0-9]+(cm|mm|in|pt|px)?"))
                            return fail(ctx,"includegraphics: ungültige "+k);
                    }
                    case "keepaspectratio" -> {
                        if (!v.isEmpty() && !v.equalsIgnoreCase("true") && !v.equalsIgnoreCase("false"))
                            return fail(ctx,"includegraphics: ungültiges keepaspectratio");
                    }
                    case "" -> {} // tolerieren
                    default -> { return fail(ctx,"includegraphics: Option nicht erlaubt ("+k+")"); }
                }
            }
        }
        return true;
    }

    private static boolean balanced(String s, char open, char close) {
        int d=0;
        for (int i=0; i<s.length(); i++) {
            char c = s.charAt(i);
            if (c==open) d++;
            else if (c==close) { if (d==0) return false; d--; }
        }
        return d==0;
    }

    private static boolean fail(ConstraintValidatorContext ctx, String msg) {
        ctx.disableDefaultConstraintViolation();
        ctx.buildConstraintViolationWithTemplate(msg).addConstraintViolation();
        return false;
    }
}
