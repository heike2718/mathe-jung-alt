//=====================================================
// Project: mja-api
// (c) Heike Winkelvoß
//=====================================================
package de.egladil.mja_api.domain.exceptions;

import jakarta.ws.rs.core.Response.Status;

/**
 * MjaWebApplicationException
 */
public class MjaWebApplicationException extends RuntimeException {

	private static final long serialVersionUID = 7043045784473537621L;

	private final Status status;

	public MjaWebApplicationException(final String message, final Status status) {
		super(message);
		this.status = status;

	}

	public Status getStatus() {
		return status;
	}

}
