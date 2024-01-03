// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.deskriptoren;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mja_api.domain.auth.dto.MessagePayload;
import de.egladil.mja_api.domain.auth.session.AuthenticatedUser;
import de.egladil.mja_api.domain.auth.session.Benutzerart;
import de.egladil.mja_api.domain.deskriptoren.impl.DeskriptorenNameComparator;
import de.egladil.mja_api.domain.deskriptoren.impl.DeskriptorenRepository;
import de.egladil.mja_api.domain.semantik.DomainService;
import de.egladil.mja_api.infrastructure.cdi.AuthenticationContext;
import de.egladil.mja_api.infrastructure.persistence.entities.Deskriptor;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * DeskriptorenService
 */
@DomainService
@ApplicationScoped
public class DeskriptorenService {

	private static final DeskriptorenNameComparator DESKRIPTOREN_COMPARATOR = new DeskriptorenNameComparator();

	private static final Logger LOGGER = LoggerFactory.getLogger(DeskriptorenService.class);

	@Inject
	AuthenticationContext authCtx;

	@Inject
	DeskriptorenRepository deskriptorenRepository;

	/**
	 * @param  deskriptorenIds
	 * @return
	 */
	public List<Deskriptor> mapToDeskriptoren(final String deskriptorenIds) {

		if (StringUtils.isBlank(deskriptorenIds)) {

			LOGGER.warn("deskriptorenIds blank => return an empty list");
			return new ArrayList<>();
		}

		String[] tokens = StringUtils.split(deskriptorenIds, ',');
		List<Long> ids = Arrays.stream(tokens).map(t -> Long.valueOf(t)).toList();

		List<Deskriptor> alleDeskriptoren = deskriptorenRepository.listAll();

		List<Deskriptor> result = alleDeskriptoren.stream().filter(d -> ids.contains(d.id)).toList();

		Benutzerart benutzerart = authCtx.getUser().getBenutzerart();

		if (Benutzerart.ADMIN == benutzerart || Benutzerart.AUTOR == benutzerart) {

			return result;
		}

		return result.stream().filter(d -> !d.admin).toList();
	}

	/**
	 * Wandelt die kommaeparierten Namen von Deskriptoren in deren kommaseparierte IDs um.<br>
	 * <br>
	 * <strong>Achtung: </strong> Nicht vorhandenen Namen werden ignoriert.
	 *
	 * @param  deskriptorenNames
	 * @return                   String kommaseparierte IDs
	 */
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

	/**
	 * Läd die Deskriptoren für RAETSEL.
	 *
	 * @return List
	 */
	public List<DeskriptorUI> loadDeskriptoren() {

		AuthenticatedUser user = authCtx.getUser();

		LOGGER.debug(" ##==>" + (user == null ? "null" : user.toString()));

		if (user == null || user.getBenutzerart() == Benutzerart.ANONYM) {

			LOGGER.warn("loadDeskriptorenV2 wurde ohne oder mit anonymer Session aufgerufen");

			throw new WebApplicationException(
				Response.status(Status.FORBIDDEN).entity(MessagePayload.error("verbotene URL aufgerufen")).build());
		}

		boolean admin = user.isAdminOrAutor();

		List<Deskriptor> alle = deskriptorenRepository.listAll();
		Collections.sort(alle, DESKRIPTOREN_COMPARATOR);

		if (admin) {

			List<DeskriptorUI> result = alle.stream().map(d -> new DeskriptorUI(d.id, d.name)).toList();
			LOGGER.debug("Deskriptoren für admin: Anzahl={}", result.size());
			return result;
		}

		List<DeskriptorUI> result = alle.stream().filter(d -> !d.admin).map(d -> new DeskriptorUI(d.id, d.name)).toList();
		LOGGER.debug("Deskriptoren für public: Anzahl={}", result.size());
		return result;

	}

	/**
	 * Sucht den Deskriptor anhand seines Namens.
	 *
	 * @param  name
	 *              String
	 * @return      Optional
	 */
	public Optional<Deskriptor> findByName(final String name) {

		List<Deskriptor> alleDeskriptoren = deskriptorenRepository.listAll();

		return alleDeskriptoren.stream().filter(d -> d.name.equalsIgnoreCase(name)).findFirst();
	}

	/**
	 * Gibt alle Ids sortiert als kommaseparierten String zurück. Dubletten werden zuvor entfernt.
	 *
	 * @param  deskriptoren
	 * @return              String oder null, wenn leere oder null-Liste
	 */
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
