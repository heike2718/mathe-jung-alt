// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

/**
 * SuchfilterTest
 */
@QuarkusTest
public class SuchfilterTest {

	@Test
	void should_suchfilterVarianteReturnCOMPLETE_when_bothAttributesNotBlank() {

		// Arrange
		String suchstring = "jah";
		String deskriptorenIds = "5,16,32";

		// Act
		SuchfilterVariante result = new Suchfilter(suchstring, deskriptorenIds).suchfilterVariante();

		// Assert
		assertEquals(SuchfilterVariante.COMPLETE, result);
	}

	@Test
	void should_suchfilterVarianteReturnDESKRIPTOREN_when_suchstringBlankDeskriptorenIdsNotBlank() {

		// Arrange
		String suchstring = " ";
		String deskriptorenIds = "5,16,32";

		// Act
		SuchfilterVariante result = new Suchfilter(suchstring, deskriptorenIds).suchfilterVariante();

		// Assert
		assertEquals(SuchfilterVariante.DESKRIPTOREN, result);
	}

	@Test
	void should_suchfilterVarianteReturnDESKRIPTOREN_when_suchstringNullDeskriptorenIdsNotBlank() {

		// Arrange
		String suchstring = " ";
		String deskriptorenIds = "5,16,32";

		// Act
		SuchfilterVariante result = new Suchfilter(suchstring, deskriptorenIds).suchfilterVariante();

		// Assert
		assertEquals(SuchfilterVariante.DESKRIPTOREN, result);
	}

	@Test
	void should_suchfilterVarianteReturnVOLLTEXT_when_suchstringNotBlankDeskriptorenIdsBlank() {

		// Arrange
		String suchstring = "hui";
		String deskriptorenIds = "  ";

		// Act
		SuchfilterVariante result = new Suchfilter(suchstring, deskriptorenIds).suchfilterVariante();

		// Assert
		assertEquals(SuchfilterVariante.VOLLTEXT, result);
	}

	@Test
	void should_suchfilterVarianteReturnVOLLTEXT_when_suchstringNotBlankDeskriptorenIdsNull() {

		// Arrange
		String suchstring = "joa";
		String deskriptorenIds = null;

		// Act
		SuchfilterVariante result = new Suchfilter(suchstring, deskriptorenIds).suchfilterVariante();

		// Assert
		assertEquals(SuchfilterVariante.VOLLTEXT, result);
	}

	@Test
	void should_suchfilterVarianteThrowIllegalArgumentException_when_bothSuchstringAndDeskriptorenIdsBlank() {

		// Arrange
		String suchstring = "   ";
		String deskriptorenIds = null;

		try {

			new Suchfilter(suchstring, deskriptorenIds).suchfilterVariante();
			fail("keine IllegalArgumentException");
		} catch (IllegalArgumentException e) {

			assertEquals("suchstring oder deskriptorenIds erforderlich", e.getMessage());
		}

	}

}
