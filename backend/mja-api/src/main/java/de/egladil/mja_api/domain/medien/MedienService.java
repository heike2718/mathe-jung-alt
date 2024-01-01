// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.medien;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mja_api.domain.auth.dto.MessagePayload;
import de.egladil.mja_api.domain.medien.dto.MediensucheResult;
import de.egladil.mja_api.domain.medien.dto.MediensucheTrefferItem;
import de.egladil.mja_api.domain.medien.dto.MediumDto;
import de.egladil.mja_api.domain.medien.dto.MediumQuelleDto;
import de.egladil.mja_api.domain.medien.impl.MedienPermissionDelegate;
import de.egladil.mja_api.infrastructure.cdi.AuthenticationContext;
import de.egladil.mja_api.infrastructure.persistence.dao.MediumDao;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistentesMedium;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * MedienService
 */
@ApplicationScoped
public class MedienService {

	private static final Logger LOGGER = LoggerFactory.getLogger(MedienService.class);

	@Inject
	AuthenticationContext authCtx;

	@Inject
	MedienPermissionDelegate permissionDelegate;

	@Inject
	MediumDao mediumDao;

	/**
	 * Holt das Medium mit dieser id.
	 *
	 * @param  id
	 * @return    Optional
	 */
	public Optional<MediumDto> getMediumWithId(final String id) {

		PersistentesMedium ausDB = mediumDao.findMediumById(id);

		permissionDelegate.checkReadPermission(ausDB);

		return ausDB == null ? Optional.empty() : Optional.of(mapFullFromDB(ausDB));

	}

	MediumDto mapFullFromDB(final PersistentesMedium ausDB) {

		MediumDto medium = new MediumDto()
			.withAutor(ausDB.autor)
			.withId(ausDB.uuid)
			.withKommentar(ausDB.kommentar)
			.withMedienart(ausDB.medienart)
			.withTitel(ausDB.titel)
			.withUrl(ausDB.url);

		try {

			permissionDelegate.checkWritePermission(ausDB);

			// yes!
			medium.markiereAlsAenderbar();
		} catch (WebApplicationException e) {

			// in diesem Fall keine Schreibberechtigung
		}

		if (authCtx.getUser().getName().equals(ausDB.owner)) {

			medium.setOwnMedium(true);
		}

		return medium;
	}

	MediumQuelleDto mapToMediumForQuelleFromDB(final PersistentesMedium ausDB) {

		MediumQuelleDto medium = new MediumQuelleDto()
			.withId(ausDB.uuid)
			.withMedienart(ausDB.medienart)
			.withTitel(ausDB.titel);

		return medium;
	}

	/**
	 * Legt ein neues Medium an.
	 *
	 * @param  medium
	 *                MediumDto - die Daten
	 * @return
	 */
	@Transactional
	public MediumDto mediumAnlegen(final MediumDto medium) {

		long anzahlDubletten = mediumDao.countMedienWithSameTitel(medium.getTitel(), medium.getId());

		if (anzahlDubletten > 0) {

			throw new WebApplicationException(Response.status(409).entity(MessagePayload.error("Der Titel ist bereits vergeben."))
				.build());
		}

		int maxSortnr = mediumDao.getMaximumOfAllSortNumbers();

		String userId = authCtx.getUser().getUuid();

		String uuid = UUID.randomUUID().toString();

		PersistentesMedium persistentesMedium = new PersistentesMedium();
		persistentesMedium.autor = medium.getAutor();
		persistentesMedium.geaendertDurch = userId;
		persistentesMedium.kommentar = medium.getKommentar();
		persistentesMedium.medienart = medium.getMedienart();
		persistentesMedium.owner = userId;
		persistentesMedium.sortNumber = maxSortnr + 1;
		persistentesMedium.titel = medium.getTitel();
		persistentesMedium.url = medium.getUrl();
		persistentesMedium.setImportierteUuid(uuid);

		mediumDao.saveMedium(persistentesMedium);

		medium.withId(uuid);

		return medium;
	}

	@Transactional
	public MediumDto mediumAendern(final MediumDto medium) {

		PersistentesMedium persistentesMedium = mediumDao.findMediumById(medium.getId());

		if (persistentesMedium == null) {

			throw new WebApplicationException(
				Response.status(404).entity(MessagePayload.error("Das Medium existiert nicht.")).build());
		}

		permissionDelegate.checkWritePermission(persistentesMedium);

		long anzahlDubletten = mediumDao.countMedienWithSameTitel(medium.getTitel(), medium.getId());

		if (anzahlDubletten > 0) {

			throw new WebApplicationException(Response.status(409).entity(MessagePayload.error("Der Titel ist bereits vergeben."))
				.build());
		}

		String userId = authCtx.getUser().getUuid();

		// owner darf nicht geändert werden!
		persistentesMedium.autor = medium.getAutor();
		persistentesMedium.geaendertDurch = userId;
		persistentesMedium.kommentar = medium.getKommentar();
		persistentesMedium.medienart = medium.getMedienart();
		persistentesMedium.titel = medium.getTitel();
		persistentesMedium.url = medium.getUrl();

		mediumDao.saveMedium(persistentesMedium);

		return medium;
	}

	/**
	 * Läd die durch limit und offset eingegrenzte Teilmenge aller Medien. Sortierung nach titel.
	 *
	 * @param  limit
	 * @param  offset
	 * @return        MediensucheResult
	 */
	public MediensucheResult loadMedien(final int limit, final int offset) {

		long gesamtzahl = mediumDao.countAllMedien();
		List<PersistentesMedium> treffermenge = mediumDao.loadAllMedien(limit, offset);

		List<MediensucheTrefferItem> trefferItems = treffermenge.stream().map(this::mapToTrefferitemFromDB)
			.collect(Collectors.toList());

		MediensucheResult result = new MediensucheResult();
		result.setTreffer(trefferItems);
		result.setTrefferGesamt(gesamtzahl);

		return result;
	}

	/**
	 * Innerhalb aller Medien wird nach allen Einträgen gesucht, deren Titel oder Kommentare unabhängig von Groß- und
	 * Kleinschreibung den suchstring einthält. Sortiert wird nach titel. Admins bekommen alle Treffer, Autoren nur die eigenen.
	 *
	 * @param  suchstring
	 * @param  limit
	 * @param  offset
	 * @return            MediensucheResult
	 */
	public MediensucheResult findMedien(final String suchstring, final int limit, final int offset) {

		if (StringUtils.isBlank(suchstring)) {

			throw new WebApplicationException(
				Response.status(Status.BAD_REQUEST).entity(MessagePayload.error("suchstring darf nicht leer sein")).build());
		}

		long gesamtzahl = mediumDao.countAllMedienWithSuchstring(suchstring);
		List<PersistentesMedium> treffermenge = mediumDao.findAllMedienWithSuchstring(suchstring, limit, offset);

		List<MediensucheTrefferItem> trefferItems = treffermenge.stream().map(this::mapToTrefferitemFromDB).toList();

		MediensucheResult result = new MediensucheResult();
		result.setTreffer(trefferItems);
		result.setTrefferGesamt(gesamtzahl);

		return result;
	}

	/**
	 * Innerhalb aller Medien der gegebenen Medienart wird nach allen Einträgen gesucht, deren Titel unabhängig von Groß- und
	 * Kleinschreibung den suchstring einthält. Sortiert wird nach titel.
	 *
	 * @param  medienart
	 *                   Medienart
	 * @return           List
	 */
	public List<MediumQuelleDto> findMedienForUseInQuelle(final Medienart medienart) {

		List<PersistentesMedium> trefferliste = mediumDao.findWithMedienart(medienart);
		LOGGER.debug("medienart={}, anzahl Treffer: {}", medienart, trefferliste.size());
		return trefferliste.stream().map(this::mapToMediumForQuelleFromDB).toList();
	}

	MediensucheTrefferItem mapToTrefferitemFromDB(final PersistentesMedium ausDB) {

		return new MediensucheTrefferItem()
			.withId(ausDB.uuid)
			.withKommentar(ausDB.kommentar)
			.withMedienart(ausDB.medienart)
			.withTitel(ausDB.titel);
	}
}
