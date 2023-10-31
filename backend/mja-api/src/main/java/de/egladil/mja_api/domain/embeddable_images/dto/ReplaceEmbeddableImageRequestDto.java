// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.embeddable_images.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.egladil.mja_api.domain.upload.UploadedFile;
import de.egladil.mja_api.domain.validation.MjaRegexps;
import jakarta.validation.constraints.Pattern;

/**
 * ReplaceEmbeddableImageRequestDto
 */
@Schema(description = "Transportiert die Daten, die eine gegebene eps-Datei im Filesystem ersetzen sollen.")
public class ReplaceEmbeddableImageRequestDto {

	@JsonProperty
	@Schema(description = "uuid des Rätsels, für das die eps-Datei ausgetauscht werden soll.")
	@Pattern(
		regexp = MjaRegexps.VALID_DOMAIN_OBJECT_ID,
		message = "die raetselId enthält ungültige Zeichen")
	private String raetselId;

	@JsonProperty
	@Schema(description = "Pfad einer vorhandenen eps-Datei relativ zu latex.base.dir")
	@Pattern(
		regexp = MjaRegexps.VAILD_RELATIVE_PATH_EPS,
		message = "pfad ist nicht akzeptabel")
	private String relativerPfad;

	@JsonProperty
	@Schema(description = "Daten des hochgeladenen Files: name und die Daten als Base64-encodeter String")
	private UploadedFile file;

	public String getRaetselId() {

		return raetselId;
	}

	public ReplaceEmbeddableImageRequestDto withRaetselId(final String raetselId) {

		this.raetselId = raetselId;
		return this;
	}

	public UploadedFile getFile() {

		return file;
	}

	public ReplaceEmbeddableImageRequestDto withUpload(final UploadedFile uploadData) {

		this.file = uploadData;
		return this;
	}

	public String getRelativerPfad() {

		return relativerPfad;
	}

	public ReplaceEmbeddableImageRequestDto withRelativerPfad(final String relativerPfad) {

		this.relativerPfad = relativerPfad;
		return this;
	}

}
