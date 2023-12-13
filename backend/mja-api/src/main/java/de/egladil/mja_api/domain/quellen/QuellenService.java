// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.quellen;

import java.util.Optional;

import de.egladil.mja_api.domain.quellen.dto.QuelleDto;
import de.egladil.mja_api.domain.quellen.impl.QuelleNameStrategie;
import de.egladil.mja_api.domain.raetsel.HerkunftRaetsel;
import de.egladil.mja_api.domain.raetsel.RaetselHerkunftTyp;
import de.egladil.mja_api.domain.semantik.DomainService;
import de.egladil.mja_api.infrastructure.cdi.AuthenticationContext;
import de.egladil.mja_api.infrastructure.persistence.dao.QuellenRepository;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistenteQuelleReadonly;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * QuellenService
 */
@DomainService
@ApplicationScoped
public class QuellenService {

	@Inject
	AuthenticationContext authCtx;

	@Inject
	QuellenRepository quellenRepository;

	/**
	 * Sucht die Quelle mit der gegebenen userId.
	 *
	 * @return Optional
	 */
	public Optional<HerkunftRaetsel> findQuelleForUser() {

		String userId = authCtx.getUser().getUuid();
		Optional<PersistenteQuelleReadonly> optAusDB = this.quellenRepository.findQuelleWithUserId(userId);

		if (optAusDB.isEmpty()) {

			return Optional.empty();
		}

		PersistenteQuelleReadonly ausDB = optAusDB.get();
		QuelleNameStrategie nameStrategie = QuelleNameStrategie.getStrategie(ausDB.quellenart);

		HerkunftRaetsel result = new HerkunftRaetsel().withId(ausDB.uuid).withText(nameStrategie.getText(ausDB))
			.withQuellenart(ausDB.quellenart).withHerkunftstyp(RaetselHerkunftTyp.EIGENKREATION);

		return Optional.of(result);
	}

	/**
	 * Gibt die Quelle mit der gegebenen UUID zurück.
	 *
	 * @param  id
	 *            String
	 * @return    Optional
	 */
	public Optional<QuelleDto> getQuelleWithId(final String id) {

		PersistenteQuelleReadonly ausDB = this.quellenRepository.findQuelleReadonlyById(id);

		return ausDB == null ? Optional.empty() : Optional.of(mapFromDB(ausDB));
	}

	QuelleDto mapFromDB(final PersistenteQuelleReadonly persistenteQuelle) {

		QuelleDto quelle = new QuelleDto();
		quelle.setAusgabe(persistenteQuelle.ausgabe);
		quelle.setId(persistenteQuelle.uuid);
		quelle.setJahr(persistenteQuelle.jahr);
		quelle.setKlasse(persistenteQuelle.klasse);
		quelle.setMediumUuid(persistenteQuelle.mediumUuid);
		quelle.setPerson(persistenteQuelle.person);
		quelle.setQuellenart(persistenteQuelle.quellenart);
		quelle.setSeite(persistenteQuelle.seite);
		quelle.setStufe(persistenteQuelle.stufe);
		return quelle;

		// @formatter: off
		// return new Quelle(persistenteQuelle.uuid)
		// .withAusgabe(persistenteQuelle.ausgabe)
		// .withJahr(persistenteQuelle.jahr)
		// .withKlasse(persistenteQuelle.klasse)
		// .withMediumUuid(persistenteQuelle.mediumUuid)
		// .withPerson(persistenteQuelle.person)
		// .withQuellenart(persistenteQuelle.quellenart)
		// .withSeite(persistenteQuelle.seite)
		// .withStufe(persistenteQuelle.stufe)
		// .withUserId(persistenteQuelle.userId);
		// @formatter: on
	}
}
