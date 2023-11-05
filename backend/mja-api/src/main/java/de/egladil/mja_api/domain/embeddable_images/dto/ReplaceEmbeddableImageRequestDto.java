// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.embeddable_images.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.egladil.mja_api.domain.upload.UploadedFile;
import de.egladil.mja_api.domain.validation.MjaRegexps;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * ReplaceEmbeddableImageRequestDto
 */
@Schema(description = "Transportiert die Daten, die eine gegebene eps-Datei im Filesystem ersetzen sollen.")
public class ReplaceEmbeddableImageRequestDto {

	@JsonProperty
	@Valid
	@Schema(description = "dieser Kontext ist zum Rücktransport an den Client gedacht. Er wird ungeändert in die Response gepackt.")
	EmbeddableImageContext context;

	@JsonProperty
	@Schema(description = "Pfad einer vorhandenen eps-Datei relativ zu latex.base.dir")
	@Pattern(
		regexp = MjaRegexps.VAILD_RELATIVE_PATH_EPS,
		message = "pfad ist nicht akzeptabel")
	@NotBlank
	private String relativerPfad;

	@JsonProperty
	@Schema(description = "Daten des hochgeladenen Files: name und die Daten als Base64-encodeter String")
	private UploadedFile file;

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

	public EmbeddableImageContext getContext() {

		return context;
	}

}
