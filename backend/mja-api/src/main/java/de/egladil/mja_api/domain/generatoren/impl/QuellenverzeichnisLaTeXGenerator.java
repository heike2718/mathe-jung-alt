// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.generatoren.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import de.egladil.mja_api.domain.quiz.dto.Quizaufgabe;

/**
 * QuellenverzeichnisLaTeXGenerator generiert das Quellenverzeichnis.
 */
public class QuellenverzeichnisLaTeXGenerator {

	public String generiereQuellenverzeichnis(final List<Quizaufgabe> aufgaben) {

		String template = LaTeXTemplatesService.getInstance().getTemplateQuellenverzeichnis();

		String quellenangben = StringUtils.join(aufgaben.stream().map(this::mapToQuellenangabe).toList(), "");
		template = template.replace(LaTeXPlaceholder.QUELLEN.placeholder(), quellenangben);

		return template;

	}

	String mapToQuellenangabe(final Quizaufgabe aufgabe) {

		String result = LaTeXConstants.QUELLENANGABE;

		result = result.replace(LaTeXPlaceholder.NUMMER.placeholder(), aufgabe.getNummer());
		result = result.replace(LaTeXPlaceholder.QUELLENANGABE.placeholder(), aufgabe.getQuelle());

		return result;
	}

}
