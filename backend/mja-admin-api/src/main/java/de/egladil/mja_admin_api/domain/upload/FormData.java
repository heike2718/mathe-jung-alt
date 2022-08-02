// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_admin_api.domain.upload;

import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.annotations.providers.multipart.PartType;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

/**
 * FormData
 */
public class FormData {

	@RestForm
	@PartType(MediaType.TEXT_PLAIN)
	public String description;

	@RestForm("uploadedFile")
	public FileUpload file;

}
