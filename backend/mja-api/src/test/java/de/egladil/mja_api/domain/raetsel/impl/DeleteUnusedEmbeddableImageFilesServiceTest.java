// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.raetsel.impl;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.InputStream;
import java.io.StringWriter;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import de.egladil.mja_api.domain.exceptions.MjaRuntimeException;
import de.egladil.mja_api.domain.generatoren.RaetselFileService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

/**
 * DeleteUnusedEmbeddableImageFilesServiceTest
 */
@QuarkusTest
public class DeleteUnusedEmbeddableImageFilesServiceTest {

	@Inject
	DeleteUnusedEmbeddableImageFilesService service;

	@InjectMock
	RaetselFileService raetselFileService;

	@Test
	void should_checkAndDeleteUnusedFiles_work() throws Exception {

		// Arrange
		String latexFrageDB = "";

		try (InputStream in = getClass().getResourceAsStream("/latex/frage-5.tex"); StringWriter sw = new StringWriter()) {

			IOUtils.copy(in, sw, "UTF-8");

			latexFrageDB = sw.toString();

		}

		String latexLoesungDB = "";

		try (InputStream in = getClass().getResourceAsStream("/latex/frage-4.tex"); StringWriter sw = new StringWriter()) {

			IOUtils.copy(in, sw, "UTF-8");

			latexLoesungDB = sw.toString();

		}

		String latexFrageNeu = "";

		try (InputStream in = getClass().getResourceAsStream("/latex/frage-5-changed.tex"); StringWriter sw = new StringWriter()) {

			IOUtils.copy(in, sw, "UTF-8");

			latexFrageNeu = sw.toString();

		}

		String latexLoesungNeu = "";

		try (InputStream in = getClass().getResourceAsStream("/latex/frage-4-changed.tex"); StringWriter sw = new StringWriter()) {

			IOUtils.copy(in, sw, "UTF-8");

			latexLoesungNeu = sw.toString();

		}

		FragenUndLoesungenVO vo = new FragenUndLoesungenVO().withFrageAlt(latexFrageDB).withFrageNeu(latexFrageNeu)
			.withLoesungAlt(latexLoesungDB).withLoesungNeu(latexLoesungNeu);

		when(raetselFileService.deleteImageFile(anyString())).thenReturn(Boolean.TRUE);

		// Act
		service.checkAndDeleteUnusedFiles(vo);

		// Assert
		verify(raetselFileService, times(2)).deleteImageFile(anyString());

	}

	@Test
	void should_checkAndDeleteUnusedFiles_work_whenFilesNotDeleted() throws Exception {

		// Arrange
		String latexFrageDB = "";

		try (InputStream in = getClass().getResourceAsStream("/latex/frage-5.tex"); StringWriter sw = new StringWriter()) {

			IOUtils.copy(in, sw, "UTF-8");

			latexFrageDB = sw.toString();

		}

		String latexLoesungDB = "";

		try (InputStream in = getClass().getResourceAsStream("/latex/frage-4.tex"); StringWriter sw = new StringWriter()) {

			IOUtils.copy(in, sw, "UTF-8");

			latexLoesungDB = sw.toString();

		}

		String latexFrageNeu = "";

		try (InputStream in = getClass().getResourceAsStream("/latex/frage-5-changed.tex"); StringWriter sw = new StringWriter()) {

			IOUtils.copy(in, sw, "UTF-8");

			latexFrageNeu = sw.toString();

		}

		String latexLoesungNeu = "";

		try (InputStream in = getClass().getResourceAsStream("/latex/frage-4-changed.tex"); StringWriter sw = new StringWriter()) {

			IOUtils.copy(in, sw, "UTF-8");

			latexLoesungNeu = sw.toString();

		}

		when(raetselFileService.deleteImageFile(anyString())).thenReturn(Boolean.FALSE);

		FragenUndLoesungenVO vo = new FragenUndLoesungenVO().withFrageAlt(latexFrageDB).withFrageNeu(latexFrageNeu)
			.withLoesungAlt(latexLoesungDB).withLoesungNeu(latexLoesungNeu);

		// Act
		service.checkAndDeleteUnusedFiles(vo);

		// Assert
		verify(raetselFileService, times(2)).deleteImageFile(anyString());

	}

	@Test
	void should_checkAndDeleteUnusedFiles_work_when_loesungNeuNull() throws Exception {

		// Arrange
		String latexFrageDB = "";

		try (InputStream in = getClass().getResourceAsStream("/latex/frage-5.tex"); StringWriter sw = new StringWriter()) {

			IOUtils.copy(in, sw, "UTF-8");

			latexFrageDB = sw.toString();

		}

		String latexLoesungDB = "";

		try (InputStream in = getClass().getResourceAsStream("/latex/frage-4.tex"); StringWriter sw = new StringWriter()) {

			IOUtils.copy(in, sw, "UTF-8");

			latexLoesungDB = sw.toString();

		}

		String latexFrageNeu = "";

		try (InputStream in = getClass().getResourceAsStream("/latex/frage-5-changed.tex"); StringWriter sw = new StringWriter()) {

			IOUtils.copy(in, sw, "UTF-8");

			latexFrageNeu = sw.toString();

		}

		FragenUndLoesungenVO vo = new FragenUndLoesungenVO().withFrageAlt(latexFrageDB).withFrageNeu(latexFrageNeu)
			.withLoesungAlt(latexLoesungDB);

		when(raetselFileService.deleteImageFile(anyString())).thenReturn(Boolean.TRUE);

		// Act
		service.checkAndDeleteUnusedFiles(vo);

		// Assert
		verify(raetselFileService, times(2)).deleteImageFile(anyString());

	}

	@Test
	void should_checkAndDeleteUnusedFiles_work_when_loesungAltBlank() throws Exception {

		// Arrange
		String latexFrageDB = "";

		try (InputStream in = getClass().getResourceAsStream("/latex/frage-5.tex"); StringWriter sw = new StringWriter()) {

			IOUtils.copy(in, sw, "UTF-8");

			latexFrageDB = sw.toString();

		}

		String latexLoesungDB = "";

		String latexFrageNeu = "";

		try (InputStream in = getClass().getResourceAsStream("/latex/frage-5-changed.tex"); StringWriter sw = new StringWriter()) {

			IOUtils.copy(in, sw, "UTF-8");

			latexFrageNeu = sw.toString();

		}

		String latexLoesungNeu = "";

		try (InputStream in = getClass().getResourceAsStream("/latex/frage-4-changed.tex"); StringWriter sw = new StringWriter()) {

			IOUtils.copy(in, sw, "UTF-8");

			latexLoesungNeu = sw.toString();

		}

		when(raetselFileService.deleteImageFile(anyString())).thenReturn(Boolean.TRUE);

		FragenUndLoesungenVO vo = new FragenUndLoesungenVO().withFrageAlt(latexFrageDB).withFrageNeu(latexFrageNeu)
			.withLoesungAlt(latexLoesungDB).withLoesungNeu(latexLoesungNeu);

		// Act
		service.checkAndDeleteUnusedFiles(vo);

		// Assert
		verify(raetselFileService, times(1)).deleteImageFile(anyString());

	}

	@Test
	void should_checkAndDeleteUnusedFiles_onlyLogMjaRuntimeException() throws Exception {

		// Arrange
		String latexFrageDB = "";

		try (InputStream in = getClass().getResourceAsStream("/latex/frage-5.tex"); StringWriter sw = new StringWriter()) {

			IOUtils.copy(in, sw, "UTF-8");

			latexFrageDB = sw.toString();

		}

		String latexLoesungDB = "";

		try (InputStream in = getClass().getResourceAsStream("/latex/frage-4.tex"); StringWriter sw = new StringWriter()) {

			IOUtils.copy(in, sw, "UTF-8");

			latexLoesungDB = sw.toString();

		}

		String latexFrageNeu = "";

		try (InputStream in = getClass().getResourceAsStream("/latex/frage-5-changed.tex"); StringWriter sw = new StringWriter()) {

			IOUtils.copy(in, sw, "UTF-8");

			latexFrageNeu = sw.toString();

		}

		String latexLoesungNeu = "";

		try (InputStream in = getClass().getResourceAsStream("/latex/frage-4-changed.tex"); StringWriter sw = new StringWriter()) {

			IOUtils.copy(in, sw, "UTF-8");

			latexLoesungNeu = sw.toString();

		}

		FragenUndLoesungenVO vo = new FragenUndLoesungenVO().withFrageAlt(latexFrageDB).withFrageNeu(latexFrageNeu)
			.withLoesungAlt(latexLoesungDB).withLoesungNeu(latexLoesungNeu);

		when(raetselFileService.deleteImageFile(anyString())).thenThrow(new MjaRuntimeException("IOException beim Löschen"));

		// Act
		service.checkAndDeleteUnusedFiles(vo);

		// Assert
		verify(raetselFileService, times(2)).deleteImageFile(anyString());

	}

}
