// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.sammlungen;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import de.egladil.mja_api.domain.DomainEntityStatus;
import de.egladil.mja_api.domain.deskriptoren.DeskriptorenService;
import de.egladil.mja_api.domain.generatoren.RaetselFileService;
import de.egladil.mja_api.domain.raetsel.AntwortvorschlaegeMapper;
import de.egladil.mja_api.domain.sammlungen.dto.Aufgabe;
import de.egladil.mja_api.domain.sammlungen.dto.Aufgabensammlung;
import de.egladil.mja_api.domain.sammlungen.impl.AufgabenComparator;
import de.egladil.mja_api.infrastructure.persistence.entities.Deskriptor;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistenteAufgabeReadonly;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistenteRaetselsammlung;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistentesRaetselsammlungselement;

/**
 * AufgabensammlungService
 */
@ApplicationScoped
public class AufgabensammlungService {

	private static final String QUELLE_HW = "Heike Winkelvoß";

	@Inject
	RaetselsammlungDao raetselsammlungDao;

	@Inject
	RaetselFileService raetselFileService;

	@Inject
	DeskriptorenService descriptorenService;

	/**
	 * Sucht alle Aufgaben der durch die Parameter eindeutig bestimmten Sammlung zur Präsentation im Browser. Es werden nur die
	 * Sammlungen mit Status FREIGEGEBEN gefunden.
	 *
	 * @param  referenztyp
	 * @param  referenz
	 * @param  schwierigkeitsgrad
	 * @return                    Optional
	 */
	public Optional<Aufgabensammlung> findAufgabensammlungByUniqueKey(final Referenztyp referenztyp, final String referenz, final Schwierigkeitsgrad schwierigkeitsgrad) {

		PersistenteRaetselsammlung dbResult = raetselsammlungDao.findByUniqueKey(referenztyp, referenz, schwierigkeitsgrad);

		if (dbResult == null || dbResult.status != DomainEntityStatus.FREIGEGEBEN) {

			return Optional.empty();
		}

		List<PersistentesRaetselsammlungselement> elemente = raetselsammlungDao.loadElementeZuRaetselsammlung(dbResult.uuid);
		List<String> raetselUuids = elemente.stream().map(el -> el.raetselID).collect(Collectors.toList());
		List<PersistenteAufgabeReadonly> aufgabenReadonly = raetselsammlungDao.loadAufgabenByRaetselIds(raetselUuids);

		List<Aufgabe> aufgaben = new ArrayList<>();
		aufgabenReadonly.forEach(aufgabeDB -> {

			Aufgabe aufgabe = mapFromDB(aufgabeDB);
			Optional<PersistentesRaetselsammlungselement> optElementDB = elemente.stream()
				.filter(el -> el.raetselID.equals(aufgabeDB.uuid)).findFirst();

			if (optElementDB.isPresent()) {

				aufgabe.setNummer(optElementDB.get().nummer);
			}
			aufgaben.add(aufgabe);
		});
		aufgaben.sort(new AufgabenComparator());

		Aufgabensammlung aufgabensammlung = new Aufgabensammlung().withKlassenstufe(dbResult.schwierigkeitsgrad.getLabel())
			.withName(dbResult.name);
		aufgabensammlung.setAufgaben(aufgaben);

		return Optional.of(aufgabensammlung);
	}

	Aufgabe mapFromDB(final PersistenteAufgabeReadonly dbAufgabe) {

		Aufgabe aufgabe = new Aufgabe();
		aufgabe.setAntwortvorschlaege(AntwortvorschlaegeMapper.deserializeAntwortvorschlaege(dbAufgabe.antwortvorschlaege));
		aufgabe.setSchluessel(dbAufgabe.schluessel);
		aufgabe.setImageFrage(raetselFileService.findImageFrage(dbAufgabe.schluessel));
		aufgabe.setImageLoesung(raetselFileService.findImageLoesung(dbAufgabe.schluessel));
		aufgabe.setQuelle(mapQuelle(dbAufgabe));

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
}
