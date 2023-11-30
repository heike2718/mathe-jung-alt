// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.embeddable_images.dto;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.egladil.mja_api.domain.upload.UploadedFile;
import jakarta.validation.Valid;

/**
 * CreateEmbeddableImageRequestDto
 */
@Schema(description = "Dto zum Hochladen einer neuen eps-Datei.")
public class CreateEmbeddableImageRequestDto {

	@JsonProperty
	@Valid
	@Schema(description = "dieser Kontext ist zum Rücktransport an den Client gedacht. Er wird ungeändert in die Response gepackt.")
	private EmbeddableImageContext context;

	@JsonProperty
	@Valid
	@Schema(description = "die hochgeladene Datei")
	private UploadedFile file;

	public EmbeddableImageContext getContext() {

		return context;
	}

	public void setContext(final EmbeddableImageContext context) {

		this.context = context;
	}

	public UploadedFile getFile() {

		return file;
	}

	public void setFile(final UploadedFile file) {

		this.file = file;
	}
}
