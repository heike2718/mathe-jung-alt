// =====================================================
// Project: mathe-jung-alt-ws
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mathe_jung_alt_ws.domain.deskriptoren.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mathe_jung_alt_ws.domain.deskriptoren.DeskriptorenService;
import de.egladil.mathe_jung_alt_ws.domain.semantik.DomainService;
import de.egladil.mathe_jung_alt_ws.infrastructure.persistence.entities.Deskriptor;

/**
 * DeskriptorenServiceImpl
 */
@DomainService
@ApplicationScoped
public class DeskriptorenServiceImpl implements DeskriptorenService {

	private static final Logger LOGGER = LoggerFactory.getLogger(DeskriptorenServiceImpl.class);

	@Inject
	DeskriptorenRepository deskriptorenRepository;

	@Override
	public List<Deskriptor> mapToDeskriptoren(final String deskriptorenIds) {

		if (StringUtils.isBlank(deskriptorenIds)) {

			LOGGER.warn("deskriptorenIds blank => return an empty list");
			return new ArrayList<>();
		}

		String[] tokens = StringUtils.split(deskriptorenIds, ',');
		List<Long> ids = Arrays.stream(tokens).map(t -> Long.valueOf(t)).collect(Collectors.toList());

		List<Deskriptor> alleDeskriptoren = deskriptorenRepository.listAll();

		List<Deskriptor> result = alleDeskriptoren.stream().filter(d -> ids.contains(d.id)).collect(Collectors.toList());

		return result;
	}

}
