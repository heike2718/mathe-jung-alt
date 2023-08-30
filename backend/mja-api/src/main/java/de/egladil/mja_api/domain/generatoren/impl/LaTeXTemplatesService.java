// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.generatoren.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mja_api.domain.utils.MjaFileUtils;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * LaTeXTemplatesService Bunkert die Templates im Heap zur Verringerung der IO-Zugriffszeit.
 */
@ApplicationScoped
public class LaTeXTemplatesService {

	private static final Logger LOGGER = LoggerFactory.getLogger(LaTeXTemplatesService.class);

	private static final String TEMPLATE_PDF_RAETSEL_FRAGE = "/latex/template-pdf-frage.txt";

	private static final String TEMPLATE_PDF_RAETSEL_LOESUNG = "/latex/template-pdf-loesung.txt";

	private static final String TEMPLATE_PDF_RAETSEL_FRAGE_LOESUNG = "/latex/template-pdf-frage-loesung.txt";

	private static final String TEMPLATE_PDF_AUFGABENBLATT = "/latex/template-aufgabenblatt-pdf.txt";

	private static final String TEMPLATE_PDF_AUFGABENBLATT_MIT_LOESUNGEN = "/latex/template-aufgabenblatt-mit-loesungen-pdf.txt";

	private static final String TEMPLATE_PDF_KARTEI = "/latex/template-kartei-pdf.txt";

	private String templatePDFRaetselFrage;

	private String templatePDFRaetselLoesung;

	private String templatePDFRaetselFrageLoesung;

	private String templatePDFAufgabenblatt;

	private String templatePDFAufgabenblattMitLoesungen;

	private String templatePDFKartei;

	/**
	 * @return String
	 */
	public synchronized String getTemplatePDFAufgabenblatt() {

		LOGGER.info("Lade Template {}", TEMPLATE_PDF_AUFGABENBLATT);

		if (templatePDFAufgabenblatt == null) {

			templatePDFAufgabenblatt = MjaFileUtils.loadTemplate(TEMPLATE_PDF_AUFGABENBLATT);
		}

		return templatePDFAufgabenblatt;
	}

	public String getTemplatePDFAufgabenblattMitLoesungen() {

		LOGGER.info("Lade Template {}", TEMPLATE_PDF_AUFGABENBLATT_MIT_LOESUNGEN);

		if (templatePDFAufgabenblattMitLoesungen == null) {

			templatePDFAufgabenblattMitLoesungen = MjaFileUtils.loadTemplate(TEMPLATE_PDF_AUFGABENBLATT_MIT_LOESUNGEN);
		}

		return templatePDFAufgabenblattMitLoesungen;
	}

	public String getTemplatePDFKartei() {

		LOGGER.info("Lade Template {}", TEMPLATE_PDF_KARTEI);

		if (templatePDFKartei == null) {

			templatePDFKartei = MjaFileUtils.loadTemplate(TEMPLATE_PDF_KARTEI);
		}

		return templatePDFKartei;
	}

	public synchronized String getTemplatePDFRaetselFrage() {

		LOGGER.info("Lade Template {}", TEMPLATE_PDF_RAETSEL_FRAGE);

		if (templatePDFRaetselFrage == null) {

			templatePDFRaetselFrage = MjaFileUtils.loadTemplate(TEMPLATE_PDF_RAETSEL_FRAGE);
		}
		return templatePDFRaetselFrage;
	}

	public synchronized String getTemplatePDFRaetselFrageLoesung() {

		LOGGER.info("Lade Template {}", TEMPLATE_PDF_RAETSEL_FRAGE_LOESUNG);

		if (templatePDFRaetselFrageLoesung == null) {

			templatePDFRaetselFrageLoesung = MjaFileUtils.loadTemplate(TEMPLATE_PDF_RAETSEL_FRAGE_LOESUNG);
		}
		return templatePDFRaetselFrageLoesung;
	}

	public synchronized String getTemplatePDFRaetselLoesung() {

		LOGGER.info("Lade Template {}", TEMPLATE_PDF_RAETSEL_LOESUNG);

		if (templatePDFRaetselLoesung == null) {

			templatePDFRaetselLoesung = MjaFileUtils.loadTemplate(TEMPLATE_PDF_RAETSEL_LOESUNG);
		}

		return templatePDFRaetselLoesung;
	}

}
