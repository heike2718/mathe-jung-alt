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

import de.egladil.mja_api.domain.auth.dto.MessagePayload;
import de.egladil.mja_api.domain.medien.dto.MediensucheTreffer;
import de.egladil.mja_api.domain.medien.dto.MediensucheTrefferItem;
import de.egladil.mja_api.domain.medien.dto.MediumDto;
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
	Optional<MediumDto> getMediumWithId(final String id) {

		PersistentesMedium ausDB = mediumDao.findMediumById(id);

		return ausDB == null ? Optional.empty() : Optional.of(mapFromDB(ausDB));

	}

	MediumDto mapFromDB(final PersistentesMedium ausDB) {

		MediumDto medium = new MediumDto()
			.withAutor(ausDB.autor)
			.withId(ausDB.uuid)
			.withKommentar(ausDB.kommentar)
			.withMedienart(ausDB.medienart)
			.withPfad(ausDB.pfad)
			.withTitel(ausDB.titel)
			.withUrl(ausDB.url);

		try {

			permissionDelegate.checkWritePermission(ausDB);

			// yes!
			medium.markiereAlsAenderbar();
		} catch (WebApplicationException e) {

			// in diesem Fall keine Schreibberechtigung
		}

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

		int maxSortnr = mediumDao.getMaximumOfAllSortNumbers();

		String userId = authCtx.getUser().getUuid();

		String uuid = UUID.randomUUID().toString();

		PersistentesMedium persistentesMedium = new PersistentesMedium();
		persistentesMedium.autor = medium.getAutor();
		persistentesMedium.geaendertDurch = userId;
		persistentesMedium.kommentar = medium.getKommentar();
		persistentesMedium.medienart = medium.getMedienart();
		persistentesMedium.owner = userId;
		persistentesMedium.pfad = medium.getPfad();
		persistentesMedium.sortNumber = maxSortnr + 1;
		persistentesMedium.titel = medium.getTitel();
		persistentesMedium.url = medium.getUrl();
		persistentesMedium.setImportierteUuid(uuid);

		mediumDao.saveMedium(persistentesMedium);

		medium.withId(uuid);

		return medium;
	}

	/**
	 * Läd die durch limit und offset eingegrenzte Teilmenge aller Medien. Sortierung nach titel.
	 *
	 * @param  limit
	 * @param  offset
	 * @return        MediensucheTreffer
	 */
	public MediensucheTreffer loadMedien(final int limit, final int offset) {

		long gesamtzahl = mediumDao.countMedien();
		List<PersistentesMedium> treffermenge = mediumDao.loadMedien(limit, offset);

		List<MediensucheTrefferItem> trefferItems = treffermenge.stream().map(this::mapToTrefferitemFromDB)
			.collect(Collectors.toList());

		MediensucheTreffer result = new MediensucheTreffer();
		result.setTreffer(trefferItems);
		result.setTrefferGesamt(gesamtzahl);

		return result;
	}

	/**
	 * Innerhalb aller Medien wird nach allen Einträgen gesucht, deren Titel oder Kommentare unabhängig von Groß- und
	 * Kleinschreibung den suchstring einthält. Sortiert wird nach titel.
	 *
	 * @param  suchstring
	 * @param  limit
	 * @param  offset
	 * @return            MediensucheTreffer
	 */
	public MediensucheTreffer findMedien(final String suchstring, final int limit, final int offset) {

		if (StringUtils.isBlank(suchstring)) {

			throw new WebApplicationException(
				Response.status(Status.BAD_REQUEST).entity(MessagePayload.error("suchstring darf nicht leer sein")).build());
		}

		long gesamtzahl = mediumDao.countMedienWithSuchstring(suchstring);
		List<PersistentesMedium> treffermenge = mediumDao.findMedienWithSuchstring(suchstring, limit, offset);

		List<MediensucheTrefferItem> trefferItems = treffermenge.stream().map(this::mapToTrefferitemFromDB).toList();

		MediensucheTreffer result = new MediensucheTreffer();
		result.setTreffer(trefferItems);
		result.setTrefferGesamt(gesamtzahl);

		return result;
	}

	/**
	 * Innerhalb aller Medien wird nach allen Einträgen gesucht, deren Titel unabhängig von Groß- und
	 * Kleinschreibung den suchstring einthält. Sortiert wird nach titel.
	 *
	 * @param  suchstring
	 * @return            List
	 */
	public List<MediumDto> findMedienWithTitel(final String suchstring) {

		List<PersistentesMedium> trefferliste = mediumDao.findMedienWithTitelLikeSuchstring(suchstring);

		return trefferliste.stream().map(this::mapFromDB).toList();

	}

	MediensucheTrefferItem mapToTrefferitemFromDB(final PersistentesMedium ausDB) {

		return new MediensucheTrefferItem()
			.withId(ausDB.uuid)
			.withKommentar(ausDB.kommentar)
			.withMedienart(ausDB.medienart)
			.withTitel(ausDB.titel);
	}
}
