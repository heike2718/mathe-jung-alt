// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.filters;

import java.io.IOException;
import java.text.Collator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mja_api.domain.auth.dto.MessagePayload;
import io.quarkus.hibernate.validator.runtime.jaxrs.ViolationReport;
import io.quarkus.hibernate.validator.runtime.jaxrs.ViolationReport.Violation;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;

/**
 * ValidationReportResponseFilter
 */
@ApplicationScoped
@Provider
public class ValidationReportResponseFilter implements ContainerResponseFilter {

	private static final Logger LOGGER = LoggerFactory.getLogger(ValidationReportResponseFilter.class);

	@Override
	public void filter(final ContainerRequestContext requestContext, final ContainerResponseContext responseContext) throws IOException {

		try {

			if (responseContext.getStatus() == 400) {

				ViolationReport entity = (ViolationReport) responseContext.getEntity();

				if (entity != null) {

					String path = requestContext.getUriInfo().getPath();
					String method = requestContext.getMethod();

					MessagePayload responsePayload = mapToMessagePayload(entity, method, path);

					responseContext.setEntity(responsePayload);

				}
			}

		} catch (ClassCastException e) {

			// nichts zu tun
		}

	}

	MessagePayload mapToMessagePayload(final ViolationReport entity, final String method, final String path) {

		List<Violation> violations = entity.getViolations();

		LOGGER.debug("{} - {}: Anzahl Violations={}", method, path, violations.size());

		// Dubletten filtern und sortieren, damit in Tests vorhersagbare Reihenfolge entsteht
		List<String> messages = violations.stream().map(v -> v.getMessage()).collect(Collectors.toSet()).stream()
			.collect(Collectors.toList());

		messages.sort(Collator.getInstance(Locale.GERMAN));

		return MessagePayload.error(StringUtils.join(messages, ','));
	}

}
