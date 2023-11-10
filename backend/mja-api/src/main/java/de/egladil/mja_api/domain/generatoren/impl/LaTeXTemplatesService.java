// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.generatoren.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mja_api.domain.utils.MjaFileUtils;

/**
 * LaTeXTemplatesService - Singleton. Bunkert die Templates im Heap zur Verringerung der IO-Zugriffszeit.
 */

public class LaTeXTemplatesService {

	private static final Logger LOGGER = LoggerFactory.getLogger(LaTeXTemplatesService.class);

	private static LaTeXTemplatesService instance;

	private static final String TEMPLATE_PDF_RAETSEL_FRAGE = "/latex/template-pdf-frage.txt";

	private static final String TEMPLATE_PDF_RAETSEL_LOESUNG = "/latex/template-pdf-loesung.txt";

	private static final String TEMPLATE_PDF_RAETSEL_FRAGE_LOESUNG = "/latex/template-pdf-frage-loesung.txt";

	private static final String TEMPLATE_PDF_AUFGABENBLATT = "/latex/template-document-aufgabenblatt-pdf.txt";

	private static final String TEMPLATE_PDF_AUFGABENBLATT_MIT_LOESUNGEN = "/latex/template-document-aufgabenblatt-mit-loesungen-pdf.txt";

	private static final String TEMPLATE_PDF_KARTEI = "/latex/template-document-kartei-pdf.txt";

	private static final String TEMPLATE_DOCUMENT_RAETSEL_PDF = "/latex/template-document-raetsel-pdf.tex";

	private static final String TEMPLATE_DOCUMENT_RAETSEL_PNG = "/latex/template-document-raetsel-png.tex";

	private static final String LIZENZ_FONTS_FIBEL = "/latex/lizenz-fonts-fibel.txt";

	private static final String LIZENZ_FONTS_DRUCKSCHRIFT_BY_WOK = "/latex/lizenz-fonts-by-wok.txt";

	private String templatePDFRaetselFrage;

	private String templatePDFRaetselLoesung;

	private String templatePDFRaetselFrageLoesung;

	private String templateDocumentPDFAufgabenblatt;

	private String templateDocumentPDFAufgabenblattMitLoesungen;

	private String templateDocumentPDFKartei;

	private String templateDocumentRaetselPDF;

	private String templateDocumentRaetselPNG;

	private String lizenzFontsFibel;

	private String lizenzFontsDruckschrift;

	/**
	 *
	 */
	private LaTeXTemplatesService() {

		super();

	}

	public static LaTeXTemplatesService getInstance() {

		if (instance == null) {

			instance = new LaTeXTemplatesService();
		}

		return instance;
	}

	/**
	 * @return String
	 */
	public synchronized String getTemplateDocumentPDFAufgabenblatt() {

		if (templateDocumentPDFAufgabenblatt == null) {

			LOGGER.info("Lade Template {}", TEMPLATE_PDF_AUFGABENBLATT);

			templateDocumentPDFAufgabenblatt = MjaFileUtils.loadTemplate(TEMPLATE_PDF_AUFGABENBLATT);
		}

		return templateDocumentPDFAufgabenblatt;
	}

	public synchronized String getTemplateDocumentPDFAufgabenblattMitLoesungen() {

		if (templateDocumentPDFAufgabenblattMitLoesungen == null) {

			LOGGER.info("Lade Template {}", TEMPLATE_PDF_AUFGABENBLATT_MIT_LOESUNGEN);

			templateDocumentPDFAufgabenblattMitLoesungen = MjaFileUtils.loadTemplate(TEMPLATE_PDF_AUFGABENBLATT_MIT_LOESUNGEN);
		}

		return templateDocumentPDFAufgabenblattMitLoesungen;
	}

	public synchronized String getTemplateDocumentPDFKartei() {

		LOGGER.info("Lade Template {}", TEMPLATE_PDF_KARTEI);

		if (templateDocumentPDFKartei == null) {

			templateDocumentPDFKartei = MjaFileUtils.loadTemplate(TEMPLATE_PDF_KARTEI);
		}

		return templateDocumentPDFKartei;
	}

	public synchronized String getTemplatePDFRaetselFrage() {

		if (templatePDFRaetselFrage == null) {

			LOGGER.info("Lade Template {}", TEMPLATE_PDF_RAETSEL_FRAGE);

			templatePDFRaetselFrage = MjaFileUtils.loadTemplate(TEMPLATE_PDF_RAETSEL_FRAGE);
		}
		return templatePDFRaetselFrage;
	}

	public synchronized String getTemplatePDFRaetselFrageLoesung() {

		if (templatePDFRaetselFrageLoesung == null) {

			LOGGER.info("Lade Template {}", TEMPLATE_PDF_RAETSEL_FRAGE_LOESUNG);

			templatePDFRaetselFrageLoesung = MjaFileUtils.loadTemplate(TEMPLATE_PDF_RAETSEL_FRAGE_LOESUNG);
		}
		return templatePDFRaetselFrageLoesung;
	}

	public synchronized String getTemplatePDFRaetselLoesung() {

		if (templatePDFRaetselLoesung == null) {

			LOGGER.info("Lade Template {}", TEMPLATE_PDF_RAETSEL_LOESUNG);

			templatePDFRaetselLoesung = MjaFileUtils.loadTemplate(TEMPLATE_PDF_RAETSEL_LOESUNG);
		}

		return templatePDFRaetselLoesung;
	}

	public synchronized String getTemplateDocumentRaetselPDF() {

		if (templateDocumentRaetselPDF == null) {

			LOGGER.info("Lade Template {}", TEMPLATE_DOCUMENT_RAETSEL_PDF);

			templateDocumentRaetselPDF = MjaFileUtils.loadTemplate(TEMPLATE_DOCUMENT_RAETSEL_PDF);

		}

		return templateDocumentRaetselPDF;
	}

	public synchronized String getTemplateDocumentRaetselPNG() {

		if (templateDocumentRaetselPNG == null) {

			LOGGER.info("Lade Template {}", TEMPLATE_DOCUMENT_RAETSEL_PNG);

			templateDocumentRaetselPNG = MjaFileUtils.loadTemplate(TEMPLATE_DOCUMENT_RAETSEL_PNG);

		}

		return templateDocumentRaetselPNG;
	}

	public synchronized String getLizenzFontsFibel() {

		if (lizenzFontsFibel == null) {

			LOGGER.info("Lade Template {}", LIZENZ_FONTS_FIBEL);

			lizenzFontsFibel = MjaFileUtils.loadTemplate(LIZENZ_FONTS_FIBEL);

		}
		return lizenzFontsFibel;
	}

	public synchronized String getLizenzFontsDruckschrift() {

		if (lizenzFontsDruckschrift == null) {

			LOGGER.info("Lade Template {}", LIZENZ_FONTS_DRUCKSCHRIFT_BY_WOK);

			lizenzFontsDruckschrift = MjaFileUtils.loadTemplate(LIZENZ_FONTS_DRUCKSCHRIFT_BY_WOK);
		}

		return lizenzFontsDruckschrift;
	}

}
