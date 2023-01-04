// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.deskriptoren.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mja_api.domain.deskriptoren.DeskriptorSuchkontext;
import de.egladil.mja_api.domain.deskriptoren.DeskriptorUI;
import de.egladil.mja_api.domain.deskriptoren.DeskriptorenService;
import de.egladil.mja_api.domain.semantik.DomainService;
import de.egladil.mja_api.domain.utils.PermissionUtils;
import de.egladil.mja_api.infrastructure.persistence.entities.Deskriptor;
import de.egladil.web.mja_auth.session.AuthenticatedUser;

/**
 * DeskriptorenServiceImpl
 */
@DomainService
@ApplicationScoped
public class DeskriptorenServiceImpl implements DeskriptorenService {

	/**
	 *
	 */
	private static final DeskriptorenNameComparator DESKRIPTOREN_COMPARATOR = new DeskriptorenNameComparator();

	private static final Logger LOGGER = LoggerFactory.getLogger(DeskriptorenServiceImpl.class);

	@Inject
	DeskriptorenRepository deskriptorenRepository;

	@Override
	public List<Deskriptor> mapToDeskriptoren(final String deskriptorenIds, final AuthenticatedUser user) {

		if (StringUtils.isBlank(deskriptorenIds)) {

			LOGGER.warn("deskriptorenIds blank => return an empty list");
			return new ArrayList<>();
		}

		String[] tokens = StringUtils.split(deskriptorenIds, ',');
		List<Long> ids = Arrays.stream(tokens).map(t -> Long.valueOf(t)).toList();

		List<Deskriptor> alleDeskriptoren = deskriptorenRepository.listAll();

		List<Deskriptor> result = alleDeskriptoren.stream().filter(d -> ids.contains(d.id)).toList();

		if (PermissionUtils.isUserAdmin(user) || PermissionUtils.isUserAutor(user)) {

			return result;
		}

		return result.stream().filter(d -> !d.admin).toList();
	}

	@Override
	public String transformToDeskriptorenOrdinal(final String deskriptorenNames) {

		List<Deskriptor> alleDeskriptoren = deskriptorenRepository.listAll();

		List<String> namen = Arrays.asList(StringUtils.split(deskriptorenNames, ','));

		// schmeißen die Duplikate raus
		Set<String> namenAlsSet = namen.stream().collect(Collectors.toSet());
		namen = new ArrayList<>(namenAlsSet);

		List<Long> filteredIDs = new ArrayList<>();

		namen.forEach(name -> {

			Optional<Deskriptor> optDeskriptor = alleDeskriptoren.stream().filter(d -> name.equalsIgnoreCase(d.name.toLowerCase()))
				.findFirst();

			if (optDeskriptor.isPresent()) {

				filteredIDs.add(optDeskriptor.get().id);
			}
		});

		Collections.sort(filteredIDs);

		return StringUtils.join(filteredIDs, ',');
	}

	@Override
	public List<Deskriptor> filterByKontext(final DeskriptorSuchkontext kontext, final List<Deskriptor> deskriptoren) {

		if (DeskriptorSuchkontext.NOOP == kontext) {

			return deskriptoren;
		}

		List<Deskriptor> result = deskriptoren.stream().filter(d -> d.kontext.contains(kontext.toString())).toList();
		return result;
	}

	@Override
	public List<DeskriptorUI> loadDeskriptorenRaetsel(final boolean admin) {

		List<Deskriptor> alle = deskriptorenRepository.listAll();
		Collections.sort(alle, DESKRIPTOREN_COMPARATOR);

		List<Deskriptor> nurRaetsel = filterByKontext(DeskriptorSuchkontext.RAETSEL, alle);

		if (admin) {

			return nurRaetsel.stream().map(d -> new DeskriptorUI(d.id, d.name)).toList();
		}

		return nurRaetsel.stream().filter(d -> !d.admin).map(d -> new DeskriptorUI(d.id, d.name)).toList();

	}

	@Override
	public Optional<Deskriptor> findByName(final String name) {

		List<Deskriptor> alleDeskriptoren = deskriptorenRepository.listAll();

		return alleDeskriptoren.stream().filter(d -> d.name.equalsIgnoreCase(name)).findFirst();
	}

	@Override
	public String sortAndStringifyIdsDeskriptoren(final List<Deskriptor> deskriptoren) {

		if (deskriptoren == null || deskriptoren.isEmpty()) {

			return null;
		}

		List<Long> ids = getSortedIds(deskriptoren);

		return "," + StringUtils.join(ids, ",,") + ",";
	}

	private List<Long> removeDuplicates(final List<Long> ids) {

		Set<Long> set = new HashSet<>();
		set.addAll(ids);

		return set.stream().collect(Collectors.toList());

	}

	private List<Long> getSortedIds(final List<Deskriptor> deskriptoren) {

		List<Long> ids = deskriptoren.stream().map(d -> d.id).collect(Collectors.toList());
		ids = removeDuplicates(ids);
		Collections.sort(ids);

		return ids;
	}

}
