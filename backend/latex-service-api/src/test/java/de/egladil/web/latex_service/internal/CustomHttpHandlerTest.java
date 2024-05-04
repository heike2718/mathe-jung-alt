// =====================================================
// Project: latex-service-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.latex_service.internal;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * CustomHttpHandlerTest
 */
public class CustomHttpHandlerTest {

	@Test
	void shouldPrintUsage() {

		// Act
		String usage = new CustomHttpHandler().getUsage();

		System.out.println(usage);

		// Assert
		assertEquals(
			"der latex-client bietet 2 commands:  /latex2pdf: transformiert ein .tex-File nach pdf. Das File und die erforderlichen Ressourcen müssen im gemounteten Verzeichnis /doc liegen.   /latex2png: transformiert ein .tex-File nach png. Das File und die erforderlichen Ressourcen müssen im gemounteten Verzeichnis /doc liegen.   Das zu transformierende File wird als Query-Parameter 'filename' ohne Suffix .tex erwartet    Beispiel: /latex2pdf?filename=02819",
			usage);

	}

}
