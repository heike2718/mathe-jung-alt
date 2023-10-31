// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.upload.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * EmbeddableImageResponseDto
 */
@Schema(description = "Ergebnis des Hochladens eines eps zum Einbetten in LaTeX.")
public class EmbeddableImageResponseDto {

	@Schema(description = "der Kontext, den der Client mitgegeben hat")
	private EmbeddableImageContext context;

	@Schema(
		description = "Dieses command kann an den text (FRAGE oder LOESUNG) angehängt werden. Es sorgt dafür, dass die Grafik beim Compilieren eingebettet wird.")
	private String includegraphicsCommand;

	@Schema(
		description = "Pfad, mit dem die Vorschau generiert werden kann.")
	private String pfad;

	public EmbeddableImageContext getContext() {

		return context;
	}

	public EmbeddableImageResponseDto with(final EmbeddableImageContext context) {

		this.context = context;
		return this;
	}

	public String getIncludegraphicsCommand() {

		return includegraphicsCommand;
	}

	public void setIncludegraphicsCommand(final String includegraphicsCommand) {

		this.includegraphicsCommand = includegraphicsCommand;
	}

	public String getPfad() {

		return pfad;
	}

	public void setPfad(final String pfad) {

		this.pfad = pfad;
	}

}
