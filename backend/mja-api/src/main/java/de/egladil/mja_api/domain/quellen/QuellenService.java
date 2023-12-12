// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.quellen;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

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
	 * Sucht Quellen mitdem gegebenen Suchstring im Namen.
	 *
	 * @param  suchstring
	 * @return            List
	 */
	public List<QuellenListItem> findQuellen(final String suchstring) {

		if (StringUtils.isBlank(suchstring)) {

			throw new IllegalArgumentException("suchstring erforderlich");
		}

		List<PersistenteQuelleReadonly> trefferliste = quellenRepository.findQuellenLikeMediumOrPerson(suchstring);

		if (trefferliste == null || trefferliste.isEmpty()) {

			return new ArrayList<>();
		}

		List<QuellenListItem> result = trefferliste.stream().map(pq -> mapFromDB(pq)).toList();

		return result;
	}

	/**
	 * Sucht die Quelle mit der gegebenen userId.
	 *
	 * @return Optional
	 */
	public Optional<QuelleMinimalDto> findQuelleForUser() {

		String userId = authCtx.getUser().getUuid();
		Optional<PersistenteQuelleReadonly> optAusDB = this.quellenRepository.findQuelleWithUserId(userId);

		if (optAusDB.isEmpty()) {

			return Optional.empty();
		}

		PersistenteQuelleReadonly ausDB = optAusDB.get();
		QuelleNameStrategie nameStrategie = QuelleNameStrategie.getStrategie(ausDB.quellenart);

		QuelleMinimalDto result = new QuelleMinimalDto().withId(ausDB.uuid).withName(nameStrategie.getName(ausDB));

		return Optional.of(result);
	}

	/**
	 * Sucht die Quelle mit der gegebenen id.
	 *
	 * @param  id
	 *            String
	 * @return    Optional
	 */
	public Optional<QuellenListItem> sucheQuelleMitId(final String id) {

		Optional<PersistenteQuelleReadonly> optAusDB = this.quellenRepository.findById(id);

		return optAusDB.isEmpty() ? Optional.empty() : Optional.of(mapFromDB(optAusDB.get()));
	}

	QuellenListItem mapFromDB(final PersistenteQuelleReadonly persistenteQuelle) {

		QuelleNameStrategie nameStrategie = QuelleNameStrategie.getStrategie(persistenteQuelle.quellenart);

		return new QuellenListItem(persistenteQuelle.uuid)
			.withSortNumber(persistenteQuelle.sortNumber).withQuellenart(persistenteQuelle.quellenart)
			.withName(nameStrategie.getName(persistenteQuelle)).withMediumIdentifier(persistenteQuelle.mediumUuid);
	}

	/**
	 * Läd die minimalen Attribute einer Quelle.
	 *
	 * @param  quelleId
	 * @return          QuelleMinimalDto
	 */
	public Optional<QuelleMinimalDto> loadQuelleMinimal(final String quelleId) {

		Optional<PersistenteQuelleReadonly> optAusDB = this.quellenRepository.findById(quelleId);

		if (optAusDB.isEmpty()) {

			return Optional.empty();
		}

		PersistenteQuelleReadonly quelle = optAusDB.get();
		QuelleNameStrategie nameStrategie = QuelleNameStrategie.getStrategie(quelle.quellenart);

		QuelleMinimalDto result = new QuelleMinimalDto().withId(quelle.uuid).withName(nameStrategie.getName(quelle));
		return Optional.of(result);
	}

}
