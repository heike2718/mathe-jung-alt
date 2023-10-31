// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.grafiken;

import de.egladil.mja_api.domain.auth.dto.MessagePayload;
import de.egladil.mja_api.domain.dto.UploadRequestDto;
import de.egladil.mja_api.domain.upload.Upload;
import de.egladil.mja_api.domain.upload.dto.EmbeddableImageContext;
import de.egladil.mja_api.domain.upload.dto.EmbeddableImageResponseDto;

/**
 * GrafikService
 */
public interface GrafikService {

	/**
	 * Generiert eine Vorschau der eingebetteten Grafik, falls sie existiert.
	 *
	 * @param  relativerPfad
	 * @return               Grafik oder null;
	 */
	Grafik generatePreview(String relativerPfad);

	/**
	 * Speichert die Grafik-Datei, sofern sie virenfrei ist und alle anderen Validierungen passen.
	 *
	 * @param  dto
	 * @return     MessagePayload
	 */
	MessagePayload replaceEmbeddedImage(UploadRequestDto dto);

	/**
	 * Das gegebene Image (ein eps) bekommt einen generierten Namen und wird in das passende Unterverzeichnis geschoben. Der String,
	 * mit dem das Image in LaTeX eingebettet wird, wird an den gegebenen Text angehängt. Es wird eine Grafig generiert und mit dem
	 * Response zurückgegeben, damit sie nach dem Hochladen direkt angezeigt werden kann.
	 *
	 * @param  upload
	 *                Upload
	 * @return        EmbeddableImageResponseDto
	 */
	EmbeddableImageResponseDto createAndEmbedImage(EmbeddableImageContext context, Upload upload);

}
