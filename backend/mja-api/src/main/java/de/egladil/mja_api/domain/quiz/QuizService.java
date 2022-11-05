// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.quiz;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import de.egladil.mja_api.domain.DomainEntityStatus;
import de.egladil.mja_api.domain.deskriptoren.DeskriptorenService;
import de.egladil.mja_api.domain.generatoren.RaetselFileService;
import de.egladil.mja_api.domain.generatoren.RaetselgruppeGeneratorService;
import de.egladil.mja_api.domain.quiz.dto.Quiz;
import de.egladil.mja_api.domain.quiz.dto.Quizaufgabe;
import de.egladil.mja_api.domain.quiz.impl.QuizaufgabeComparator;
import de.egladil.mja_api.domain.raetsel.AntwortvorschlaegeMapper;
import de.egladil.mja_api.domain.raetsel.LayoutAntwortvorschlaege;
import de.egladil.mja_api.domain.raetsel.dto.GeneratedPDF;
import de.egladil.mja_api.domain.raetselgruppen.RaetselgruppenDao;
import de.egladil.mja_api.domain.raetselgruppen.Referenztyp;
import de.egladil.mja_api.domain.raetselgruppen.Schwierigkeitsgrad;
import de.egladil.mja_api.infrastructure.persistence.entities.Deskriptor;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistenteAufgabeReadonly;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistenteRaetselgruppe;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistentesRaetselgruppenelement;
import de.egladil.web.mja_auth.dto.MessagePayload;

/**
 * QuizService
 */
@ApplicationScoped
public class QuizService {

	private static final String QUELLE_HW = "Heike Winkelvoß";

	@Inject
	RaetselgruppenDao raetselgruppenDao;

	@Inject
	RaetselFileService raetselFileService;

	@Inject
	DeskriptorenService descriptorenService;

	@Inject
	RaetselgruppeGeneratorService raetselgruppeFileService;

	/**
	 * Sucht alle Aufgaben des durch die Parameter eindeutig bestimmten Quiz zur Präsentation im Browser. Es werden nur die
	 * Gruppen mit Status FREIGEGEBEN gefunden.
	 *
	 * @param  referenztyp
	 * @param  referenz
	 * @param  schwierigkeitsgrad
	 * @return                    Optional
	 */
	public Optional<Quiz> generateQuiz(final Referenztyp referenztyp, final String referenz, final Schwierigkeitsgrad schwierigkeitsgrad) {

		PersistenteRaetselgruppe dbResult = raetselgruppenDao.findByUniqueKey(referenztyp, referenz, schwierigkeitsgrad);

		if (dbResult == null || dbResult.status != DomainEntityStatus.FREIGEGEBEN) {

			return Optional.empty();
		}

		List<Quizaufgabe> aufgaben = mapRaetselgruppeItems(dbResult.uuid);

		Quiz quiz = new Quiz().withKlassenstufe(dbResult.schwierigkeitsgrad.getLabel())
			.withName(dbResult.name);
		quiz.setAufgaben(aufgaben);

		return Optional.of(quiz);
	}

	/**
	 * Generiert die Vorschau des Quiz als PDF. Dabei werden Aufgaben und Lösungen gemischt.
	 *
	 * @param  raetselgruppeID
	 * @return
	 */
	public GeneratedPDF printVorschau(final String raetselgruppeID, final LayoutAntwortvorschlaege layoutAntwortvorschlaege) {

		PersistenteRaetselgruppe dbResult = raetselgruppenDao.findByID(raetselgruppeID);

		if (dbResult == null) {

			throw new WebApplicationException(
				Response.status(Status.NOT_FOUND).entity(MessagePayload.error("Die Rätselgruppe gibt es nicht.")).build());
		}

		List<Quizaufgabe> aufgaben = mapRaetselgruppeItems(raetselgruppeID);

		return raetselgruppeFileService.generateVorschauPDFQuiz(dbResult, aufgaben, layoutAntwortvorschlaege);
	}

	List<Quizaufgabe> mapRaetselgruppeItems(final String raetselgruppeID) {

		List<PersistentesRaetselgruppenelement> elemente = raetselgruppenDao.loadElementeRaetselgruppe(raetselgruppeID);
		List<String> raetselUuids = elemente.stream().map(el -> el.raetselID).collect(Collectors.toList());
		List<PersistenteAufgabeReadonly> aufgabenReadonly = raetselgruppenDao.loadAufgabenByRaetselIds(raetselUuids);

		List<Quizaufgabe> aufgaben = new ArrayList<>();
		aufgabenReadonly.forEach(aufgabeDB -> {

			Quizaufgabe aufgabe = mapFromDB(aufgabeDB);
			aufgaben.add(aufgabe);
		});
		aufgaben.sort(new QuizaufgabeComparator());
		return aufgaben;
	}

	Quizaufgabe mapFromDB(final PersistenteAufgabeReadonly dbAufgabe) {

		Quizaufgabe aufgabe = new Quizaufgabe();
		aufgabe.setAntwortvorschlaege(AntwortvorschlaegeMapper.deserializeAntwortvorschlaege(dbAufgabe.antwortvorschlaege));
		aufgabe.setSchluessel(dbAufgabe.schluessel);
		aufgabe.setImages(raetselFileService.findImages(dbAufgabe.schluessel));
		aufgabe.setQuelle(mapQuelle(dbAufgabe));
		aufgabe.setNummer(dbAufgabe.nummer);
		aufgabe.setPunkte(dbAufgabe.punkte);
		aufgabe.setStrafpunkte(berechneStrafpunkte(aufgabe.getPunkte(), aufgabe.getAntwortvorschlaege().length));

		return aufgabe;
	}

	String mapQuelle(final PersistenteAufgabeReadonly dbAufgabe) {

		List<Deskriptor> deskriptoren = descriptorenService.mapToDeskriptoren(dbAufgabe.deskriptoren);
		Optional<Deskriptor> optAdaptiert = deskriptoren.stream().filter(d -> "adaptiert".equalsIgnoreCase(d.name)).findFirst();

		if (optAdaptiert.isPresent()) {

			return QUELLE_HW;
		}

		switch (dbAufgabe.quellenart) {

			case PERSON: {

				return dbAufgabe.person;
			}

			case BUCH: {

				return dbAufgabe.mediumTitel + ", " + dbAufgabe.seite;
			}

			case ZEITSCHRIFT: {

				return dbAufgabe.mediumTitel + " (" + dbAufgabe.ausgabe + ") " + dbAufgabe.jahrgang;
			}

			default:
				throw new IllegalArgumentException("Unexpected value: " + dbAufgabe.quellenart);
		}
	}

	int berechneStrafpunkte(final int punkte, final int anzahlAntwortvorschlaege) {

		if (anzahlAntwortvorschlaege == 0 || anzahlAntwortvorschlaege == 1) {

			return 0;
		}

		return punkte / (anzahlAntwortvorschlaege - 1);
	}

}
