// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.quellen;

import java.util.Optional;
import java.util.UUID;

import de.egladil.mja_api.domain.quellen.dto.QuelleDto;
import de.egladil.mja_api.domain.quellen.impl.QuelleNameStrategie;
import de.egladil.mja_api.domain.raetsel.HerkunftRaetsel;
import de.egladil.mja_api.domain.raetsel.RaetselHerkunftTyp;
import de.egladil.mja_api.domain.semantik.DomainService;
import de.egladil.mja_api.infrastructure.cdi.AuthenticationContext;
import de.egladil.mja_api.infrastructure.persistence.dao.QuellenRepository;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistenteQuelle;
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
	}

	/**
	 * Legt eine neue Quelle an.
	 *
	 * @param  quelle
	 * @return        PersistenteQuelle
	 */
	public PersistenteQuelle quelleAnlegen(final Quelle quelle) {

		int maxSornr = quellenRepository.getMaximumOfAllSortNumbers();

		QuelleDto datenQuelle = quelle.getDatenQuelle();
		String userId = authCtx.getUser().getUuid();

		String uuid = UUID.randomUUID().toString();

		PersistenteQuelle neueQuelle = new PersistenteQuelle();
		neueQuelle.setImportierteUuid(uuid);
		neueQuelle.ausgabe = datenQuelle.getAusgabe();
		neueQuelle.geaendertDurch = userId;
		neueQuelle.jahr = datenQuelle.getJahr();
		neueQuelle.klasse = datenQuelle.getKlasse();
		neueQuelle.mediumID = datenQuelle.getMediumUuid();
		neueQuelle.owner = userId;
		neueQuelle.person = datenQuelle.getPerson();
		neueQuelle.quellenart = datenQuelle.getQuellenart();
		neueQuelle.seite = datenQuelle.getSeite();
		neueQuelle.pfad = datenQuelle.getPfad();
		neueQuelle.userId = quelle.getUserId();
		neueQuelle.sortNumber = maxSornr + 1;

		PersistenteQuelle persisted = quellenRepository.save(neueQuelle);

		return persisted;
	}

}
