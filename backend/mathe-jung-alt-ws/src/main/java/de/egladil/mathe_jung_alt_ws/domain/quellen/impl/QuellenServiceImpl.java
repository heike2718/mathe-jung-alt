// =====================================================
// Project: mathe-jung-alt-ws
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mathe_jung_alt_ws.domain.quellen.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mathe_jung_alt_ws.domain.deskriptoren.DeskriptorenService;
import de.egladil.mathe_jung_alt_ws.domain.dto.Suchfilter;
import de.egladil.mathe_jung_alt_ws.domain.quellen.QuelleReadonly;
import de.egladil.mathe_jung_alt_ws.domain.quellen.QuellenRepository;
import de.egladil.mathe_jung_alt_ws.domain.quellen.QuellenService;
import de.egladil.mathe_jung_alt_ws.domain.semantik.DomainService;
import de.egladil.mathe_jung_alt_ws.domain.utils.SetOperationUtils;
import de.egladil.mathe_jung_alt_ws.infrastructure.persistence.entities.Deskriptor;
import de.egladil.mathe_jung_alt_ws.infrastructure.persistence.entities.PersistenteQuelleReadonly;

/**
 * QuellenServiceImpl
 */
@DomainService
@ApplicationScoped
public class QuellenServiceImpl implements QuellenService {

	private static final Logger LOGGER = LoggerFactory.getLogger(QuellenServiceImpl.class);

	@Inject
	QuellenRepository quellenRepository;

	@Inject
	DeskriptorenService deskriptorenService;

	@Override
	public List<QuelleReadonly> sucheQuellen(final Suchfilter suchfilter) {

		if (StringUtils.isBlank(suchfilter.getSuchstring())) {

			LOGGER.warn("suchstring blank => return an empty list");
			return new ArrayList<>();
		}

		List<PersistenteQuelleReadonly> trefferliste = quellenRepository.findQuellenLikeMediumOrPerson(suchfilter.getSuchstring());

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

		List<QuelleReadonly> result = trefferlisteMitDeskriptoren.stream().map(pq -> mapFromDB(pq)).collect(Collectors.toList());

		return result;
	}

	QuelleReadonly mapFromDB(final PersistenteQuelleReadonly persistenteQuelle) {

		List<Deskriptor> deskriptoren = deskriptorenService.mapToDeskriptoren(persistenteQuelle.getDeskriptoren());

		QuelleNameStrategie nameStrategie = QuelleNameStrategie.getStrategie(persistenteQuelle.getQuellenart());

		return new QuelleReadonly(persistenteQuelle.getUuid()).withDeskriptoren(deskriptoren)
			.withSortNumber(persistenteQuelle.getSortNumber()).withQuellenart(persistenteQuelle.getQuellenart())
			.withName(nameStrategie.getName(persistenteQuelle)).withMediumIdentifier(persistenteQuelle.getMediumUuid());
	}
}
