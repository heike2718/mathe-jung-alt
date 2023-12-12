// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.quellen;

import java.util.Optional;

import de.egladil.mja_api.domain.quellen.impl.QuelleNameStrategie;
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
	public Optional<QuellenangabeRaetsel> findQuelleForUser() {

		String userId = authCtx.getUser().getUuid();
		Optional<PersistenteQuelleReadonly> optAusDB = this.quellenRepository.findQuelleWithUserId(userId);

		if (optAusDB.isEmpty()) {

			return Optional.empty();
		}

		PersistenteQuelleReadonly ausDB = optAusDB.get();
		QuelleNameStrategie nameStrategie = QuelleNameStrategie.getStrategie(ausDB.quellenart);

		QuellenangabeRaetsel result = new QuellenangabeRaetsel().withId(ausDB.uuid).withName(nameStrategie.getName(ausDB))
			.withQuellenart(ausDB.quellenart);

		return Optional.of(result);
	}

	/**
	 * Gibt die Quelle mit der gegebenen UUID zurück.
	 *
	 * @param  id
	 *            String
	 * @return    Optional
	 */
	public Optional<Quelle> getQuelleWithId(final String id) {

		PersistenteQuelleReadonly ausDB = this.quellenRepository.findQuelleReadonlyById(id);

		return ausDB == null ? Optional.empty() : Optional.of(mapFromDB(ausDB));
	}

	Quelle mapFromDB(final PersistenteQuelleReadonly persistenteQuelle) {

		// @formatter: off
		return new Quelle(persistenteQuelle.uuid)
			.withAusgabe(persistenteQuelle.ausgabe)
			.withJahr(persistenteQuelle.jahr)
			.withKlasse(persistenteQuelle.klasse)
			.withMediumUuid(persistenteQuelle.mediumUuid)
			.withPerson(persistenteQuelle.person)
			.withQuellenart(persistenteQuelle.quellenart)
			.withSeite(persistenteQuelle.seite)
			.withStufe(persistenteQuelle.stufe)
			.withUserId(persistenteQuelle.userId);
		// @formatter: on
	}

	/**
	 * Läd das von der Quelle, das im Rätsel oder in zugehörigen PDFs angezeigt wird nebst der ID zum navigieren zu einem Medizm.
	 *
	 * @param  quelleId
	 * @return          QuellenangabeRaetsel
	 */
	public Optional<QuellenangabeRaetsel> getQuellenangabeRaetselWithId(final String quelleId) {

		PersistenteQuelleReadonly ausDB = this.quellenRepository.findQuelleReadonlyById(quelleId);

		if (ausDB == null) {

			return Optional.empty();
		}

		QuelleNameStrategie nameStrategie = QuelleNameStrategie.getStrategie(ausDB.quellenart);

		// @formatter: off
		QuellenangabeRaetsel result = new QuellenangabeRaetsel()
			.withId(ausDB.uuid)
			.withQuellenart(ausDB.quellenart)
			.withName(nameStrategie.getName(ausDB))
			.withMediumUuid(ausDB.mediumUuid);
		// @formatter: on
		return Optional.of(result);
	}

}
