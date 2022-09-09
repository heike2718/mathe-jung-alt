// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.raetselgruppen;

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
import de.egladil.mja_api.domain.raetselgruppen.dto.Aufgabe;
import de.egladil.mja_api.domain.raetselgruppen.dto.Aufgabengruppe;
import de.egladil.mja_api.domain.raetselgruppen.impl.AufgabenComparator;
import de.egladil.mja_api.infrastructure.persistence.entities.Deskriptor;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistenteAufgabeReadonly;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistenteRaetselgruppe;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistentesRaetselgruppenelement;

/**
 * AufgabengruppenService
 */
@ApplicationScoped
public class AufgabengruppenService {

	private static final String QUELLE_HW = "Heike Winkelvoß";

	@Inject
	RaetselgruppenDao raetselgruppenDao;

	@Inject
	RaetselFileService raetselFileService;

	@Inject
	DeskriptorenService descriptorenService;

	/**
	 * Sucht alle Aufgaben der durch die Parameter eindeutig bestimmten Raetselgruppe zur Präsentation im Browser. Es werden nur die
	 * Gruppen mit Status FREIGEGEBEN gefunden.
	 *
	 * @param  referenztyp
	 * @param  referenz
	 * @param  schwierigkeitsgrad
	 * @return                    Optional
	 */
	public Optional<Aufgabengruppe> findAufgabengruppeByUniqueKey(final Referenztyp referenztyp, final String referenz, final Schwierigkeitsgrad schwierigkeitsgrad) {

		PersistenteRaetselgruppe dbResult = raetselgruppenDao.findByUniqueKey(referenztyp, referenz, schwierigkeitsgrad);

		if (dbResult == null || dbResult.status != DomainEntityStatus.FREIGEGEBEN) {

			return Optional.empty();
		}

		List<PersistentesRaetselgruppenelement> elemente = raetselgruppenDao.loadElementeRaetselgruppe(dbResult.uuid);
		List<String> raetselUuids = elemente.stream().map(el -> el.raetselID).collect(Collectors.toList());
		List<PersistenteAufgabeReadonly> aufgabenReadonly = raetselgruppenDao.loadAufgabenByRaetselIds(raetselUuids);

		List<Aufgabe> aufgaben = new ArrayList<>();
		aufgabenReadonly.forEach(aufgabeDB -> {

			Aufgabe aufgabe = mapFromDB(aufgabeDB);
			Optional<PersistentesRaetselgruppenelement> optElementDB = elemente.stream()
				.filter(el -> el.raetselID.equals(aufgabeDB.uuid)).findFirst();

			if (optElementDB.isPresent()) {

				aufgabe.setNummer(optElementDB.get().nummer);
			}
			aufgaben.add(aufgabe);
		});
		aufgaben.sort(new AufgabenComparator());

		Aufgabengruppe aufgabengruppe = new Aufgabengruppe().withKlassenstufe(dbResult.schwierigkeitsgrad.getLabel())
			.withName(dbResult.name);
		aufgabengruppe.setAufgaben(aufgaben);

		return Optional.of(aufgabengruppe);
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
