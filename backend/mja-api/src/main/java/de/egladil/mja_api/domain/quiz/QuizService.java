// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.quiz;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mja_api.domain.aufgabensammlungen.Referenztyp;
import de.egladil.mja_api.domain.aufgabensammlungen.Schwierigkeitsgrad;
import de.egladil.mja_api.domain.deskriptoren.DeskriptorenService;
import de.egladil.mja_api.domain.generatoren.RaetselFileService;
import de.egladil.mja_api.domain.quiz.dto.Quiz;
import de.egladil.mja_api.domain.quiz.dto.Quizaufgabe;
import de.egladil.mja_api.domain.quiz.impl.QuizaufgabeComparator;
import de.egladil.mja_api.domain.raetsel.AntwortvorschlaegeMapper;
import de.egladil.mja_api.infrastructure.persistence.dao.AufgabensammlungDao;
import de.egladil.mja_api.infrastructure.persistence.entities.Deskriptor;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistenteAufgabeReadonly;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistenteAufgabensammlung;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * QuizService
 */
@ApplicationScoped
public class QuizService {

	private static final String QUELLE_HW = "Heike Winkelvoß";

	private static final Logger LOGGER = LoggerFactory.getLogger(QuizService.class);

	@Inject
	AufgabensammlungDao aufgabensammlungDao;

	@Inject
	RaetselFileService raetselFileService;

	@Inject
	DeskriptorenService descriptorenService;

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

		LOGGER.debug(" ==> (1)");
		PersistenteAufgabensammlung dbResult = aufgabensammlungDao.findByUniqueKey(referenztyp, referenz, schwierigkeitsgrad);

		LOGGER.debug(" ==> (4)");

		// nur öffentliche und freigegebene Aufgabensammlungen.
		if (dbResult == null || !dbResult.freigegeben || dbResult.privat) {

			return Optional.empty();
		}

		List<Quizaufgabe> aufgaben = getItemsAsQuizaufgaben(dbResult.uuid);

		Quiz quiz = new Quiz().withKlassenstufe(dbResult.schwierigkeitsgrad.getLabel())
			.withName(dbResult.name);
		quiz.setAufgaben(aufgaben);

		return Optional.of(quiz);
	}

	public List<Quizaufgabe> getItemsAsQuizaufgaben(final String aufgabensammlungID) {

		List<PersistenteAufgabeReadonly> aufgabenReadonly = aufgabensammlungDao.loadAufgabenByAufgabensammlung(aufgabensammlungID);
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
		aufgabe.setImages(raetselFileService.findImages(dbAufgabe.filenameVorschauFrage, dbAufgabe.filenameVorschauLoesung));
		aufgabe.setFreigegeben(dbAufgabe.freigegeben);
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
