// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.quellen.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import de.egladil.mja_api.domain.deskriptoren.DeskriptorenService;
import de.egladil.mja_api.domain.dto.Suchfilter;
import de.egladil.mja_api.domain.dto.SuchfilterVariante;
import de.egladil.mja_api.domain.quellen.QuelleMinimalDto;
import de.egladil.mja_api.domain.quellen.QuellenListItem;
import de.egladil.mja_api.domain.quellen.QuellenRepository;
import de.egladil.mja_api.domain.quellen.QuellenService;
import de.egladil.mja_api.domain.semantik.DomainService;
import de.egladil.mja_api.domain.utils.SetOperationUtils;
import de.egladil.mja_api.infrastructure.persistence.entities.Deskriptor;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistenteQuelleReadonly;

/**
 * QuellenServiceImpl
 */
@DomainService
@ApplicationScoped
public class QuellenServiceImpl implements QuellenService {

	@Inject
	QuellenRepository quellenRepository;

	@Inject
	DeskriptorenService deskriptorenService;

	@Override
	public List<QuellenListItem> sucheQuellen(final Suchfilter suchfilter) {

		SuchfilterVariante suchfilterVariante = suchfilter.suchfilterVariante();

		List<PersistenteQuelleReadonly> trefferliste = new ArrayList<>();

		if (suchfilterVariante == SuchfilterVariante.DESKRIPTOREN) {

			trefferliste = quellenRepository.findWithDeskriptoren(suchfilter.getDeskriptorenIds());
			List<QuellenListItem> result = trefferliste.stream().map(pq -> mapFromDBV1(pq)).toList();
			return result;

		}

		trefferliste = quellenRepository.findQuellenLikeMediumOrPerson(suchfilter.getSuchstring());

		if (trefferliste == null || trefferliste.isEmpty()) {

			return new ArrayList<>();
		}

		final List<PersistenteQuelleReadonly> trefferlisteMitDeskriptoren = new ArrayList<>();

		if (StringUtils.isNotBlank(suchfilter.getDeskriptorenIds())) {

			final SetOperationUtils setOperationUtils = new SetOperationUtils();

			final String idsDeskriptorenLeft = suchfilter.getDeskriptorenIds();

			Optional<PersistenteQuelleReadonly> optTrefferMitPassendenDeskriptoren = trefferliste.stream()
				.filter(pq -> setOperationUtils.isLeftSubsetOfRight(Pair.of(idsDeskriptorenLeft, pq.getDeskriptoren())))
				.findFirst();

			if (optTrefferMitPassendenDeskriptoren.isPresent()) {

				trefferlisteMitDeskriptoren.add(optTrefferMitPassendenDeskriptoren.get());
			}
		} else {

			trefferlisteMitDeskriptoren.addAll(trefferliste);
		}

		List<QuellenListItem> result = trefferlisteMitDeskriptoren.stream().map(pq -> mapFromDBV1(pq)).toList();

		return result;
	}

	@Override
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

	@Override
	public Optional<QuellenListItem> sucheQuelleMitUserID(final String userId) {

		Optional<PersistenteQuelleReadonly> optAusDB = this.quellenRepository.findQuelleWithUserId(userId);
		return optAusDB.isEmpty() ? Optional.empty() : Optional.of(mapFromDBV1(optAusDB.get()));

	}

	@Override
	public Optional<QuelleMinimalDto> findQuelleForUser(final String userId) {

		Optional<PersistenteQuelleReadonly> optAusDB = this.quellenRepository.findQuelleWithUserId(userId);

		if (optAusDB.isEmpty()) {

			return Optional.empty();
		}

		PersistenteQuelleReadonly ausDB = optAusDB.get();
		QuelleNameStrategie nameStrategie = QuelleNameStrategie.getStrategie(ausDB.getQuellenart());

		QuelleMinimalDto result = new QuelleMinimalDto().withId(ausDB.getUuid()).withName(nameStrategie.getName(ausDB));

		return Optional.of(result);
	}

	@Override
	public Optional<QuellenListItem> sucheQuelleMitId(final String id) {

		Optional<PersistenteQuelleReadonly> optAusDB = this.quellenRepository.findById(id);

		return optAusDB.isEmpty() ? Optional.empty() : Optional.of(mapFromDBV1(optAusDB.get()));
	}

	@Deprecated
	QuellenListItem mapFromDBV1(final PersistenteQuelleReadonly persistenteQuelle) {

		List<Deskriptor> deskriptoren = deskriptorenService.mapToDeskriptoren(persistenteQuelle.getDeskriptoren(), null);

		QuelleNameStrategie nameStrategie = QuelleNameStrategie.getStrategie(persistenteQuelle.getQuellenart());

		return new QuellenListItem(persistenteQuelle.getUuid()).withDeskriptoren(deskriptoren)
			.withSortNumber(persistenteQuelle.getSortNumber()).withQuellenart(persistenteQuelle.getQuellenart())
			.withName(nameStrategie.getName(persistenteQuelle)).withMediumIdentifier(persistenteQuelle.getMediumUuid());
	}

	QuellenListItem mapFromDB(final PersistenteQuelleReadonly persistenteQuelle) {

		QuelleNameStrategie nameStrategie = QuelleNameStrategie.getStrategie(persistenteQuelle.getQuellenart());

		return new QuellenListItem(persistenteQuelle.getUuid())
			.withSortNumber(persistenteQuelle.getSortNumber()).withQuellenart(persistenteQuelle.getQuellenart())
			.withName(nameStrategie.getName(persistenteQuelle)).withMediumIdentifier(persistenteQuelle.getMediumUuid());
	}

	@Override
	public Optional<QuelleMinimalDto> loadQuelleMinimal(final String quelleId) {

		Optional<PersistenteQuelleReadonly> optAusDB = this.quellenRepository.findById(quelleId);

		if (optAusDB.isEmpty()) {

			return Optional.empty();
		}

		PersistenteQuelleReadonly quelle = optAusDB.get();
		QuelleNameStrategie nameStrategie = QuelleNameStrategie.getStrategie(quelle.getQuellenart());

		QuelleMinimalDto result = new QuelleMinimalDto().withId(quelle.uuid).withName(nameStrategie.getName(quelle));
		return Optional.of(result);
	}

}
