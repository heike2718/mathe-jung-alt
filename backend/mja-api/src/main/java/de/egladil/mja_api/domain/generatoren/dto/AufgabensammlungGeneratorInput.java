// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.generatoren.dto;

import java.util.List;

import de.egladil.mja_api.domain.generatoren.FontName;
import de.egladil.mja_api.domain.generatoren.Schriftgroesse;
import de.egladil.mja_api.domain.generatoren.Verwendungszweck;
import de.egladil.mja_api.domain.quiz.dto.Quizaufgabe;
import de.egladil.mja_api.domain.raetsel.LayoutAntwortvorschlaege;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistenteAufgabensammlung;

/**
 * AufgabensammlungGeneratorInput kapselt die Parameter für den Auftrag zum Generieren einer Aufgabensammlung.
 */
public class AufgabensammlungGeneratorInput {

	/**
	 * aufgabensammlung PersistenteAufgabensammlung - die zu druckende Aufgabensammlung
	 */
	private PersistenteAufgabensammlung aufgabensammlung;

	/**
	 * aufgaben List - die Quizaufgaben, die gedruckt werden sollen.
	 */
	private List<Quizaufgabe> aufgaben;

	/**
	 * layoutAntwortvorschlaege LayoutAntwortvorschlaege - wie die Antwortvorschläge gedruckt werden sollen. Bei NOOP werden keine
	 * gedruckt.
	 */
	private LayoutAntwortvorschlaege layoutAntwortvorschlaege;

	/**
	 * font FontName - Name des Fonts, mit dem gedruckt werden soll.
	 */
	private FontName font;

	/**
	 * verwendungszweck Verwendungszweck - der Verwendungszweck
	 */
	private Verwendungszweck verwendungszweck;

	/**
	 * schriftgroesse Schriftgroesse in den LaTeX-Varianten '' (NORMAL), '\Large' (GROSS), '\LARGE' (HUGE).
	 */
	private Schriftgroesse schriftgroesse;

	public PersistenteAufgabensammlung getAufgabensammlung() {

		return aufgabensammlung;
	}

	public AufgabensammlungGeneratorInput withAufgabensammlung(final PersistenteAufgabensammlung aufgabensammlung) {

		this.aufgabensammlung = aufgabensammlung;
		return this;
	}

	public List<Quizaufgabe> getAufgaben() {

		return aufgaben;
	}

	public AufgabensammlungGeneratorInput withAufgaben(final List<Quizaufgabe> aufgaben) {

		this.aufgaben = aufgaben;
		return this;
	}

	public LayoutAntwortvorschlaege getLayoutAntwortvorschlaege() {

		return layoutAntwortvorschlaege;
	}

	public AufgabensammlungGeneratorInput withLayoutAntwortvorschlaege(final LayoutAntwortvorschlaege layoutAntwortvorschlaege) {

		this.layoutAntwortvorschlaege = layoutAntwortvorschlaege;
		return this;
	}

	public FontName getFont() {

		return font;
	}

	public AufgabensammlungGeneratorInput withFont(final FontName font) {

		this.font = font;
		return this;
	}

	public Verwendungszweck getVerwendungszweck() {

		return verwendungszweck;
	}

	public AufgabensammlungGeneratorInput withVerwendungszweck(final Verwendungszweck verwendungszweck) {

		this.verwendungszweck = verwendungszweck;
		return this;
	}

	public Schriftgroesse getSchriftgroesse() {

		return schriftgroesse;
	}

	public AufgabensammlungGeneratorInput withSchriftgroesse(final Schriftgroesse schriftgroesse) {

		this.schriftgroesse = schriftgroesse;
		return this;
	}

}
