// =====================================================
// Project: mja-auth
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.infrastructure.filters;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

/**
 * CsrfTokenValidationFilterTest
 */
@QuarkusTest
public class CsrfTokenValidationFilterTest {

	@Test
	void should_identifyAsEqualsReturnTrue_when_exactlyEqual() {

		// Arrange
		String headerValue = "VJ9G6dkDmvqWbBjprDo/4mjRaXoSQuTciRw5QaKpFdplhMbszQU5J7/dFMBTcRIC";
		String cookieValue = "VJ9G6dkDmvqWbBjprDo/4mjRaXoSQuTciRw5QaKpFdplhMbszQU5J7/dFMBTcRIC";

		// Act
		boolean result = new CsrfTokenValidationFilter().identifyAsEquals(headerValue, cookieValue);

		// Assert
		assertTrue(result);

	}

	@Test
	void should_identifyAsEqualsReturnTrue_when_exactlyHeaderValueWithAdditionalHochkommas() {

		// Arrange
		String headerValue = "\"VJ9G6dkDmvqWbBjprDo/4mjRaXoSQuTciRw5QaKpFdplhMbszQU5J7/dFMBTcRIC\"";
		String cookieValue = "VJ9G6dkDmvqWbBjprDo/4mjRaXoSQuTciRw5QaKpFdplhMbszQU5J7/dFMBTcRIC";

		// Act
		boolean result = new CsrfTokenValidationFilter().identifyAsEquals(headerValue, cookieValue);

		// Assert
		assertTrue(result);
	}

}
