// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.raetsel.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mja_api.domain.DomainEntityStatus;
import de.egladil.mja_api.domain.auth.dto.MessagePayload;
import de.egladil.mja_api.domain.deskriptoren.DeskriptorenService;
import de.egladil.mja_api.domain.dto.AnzahlabfrageResponseDto;
import de.egladil.mja_api.domain.dto.SortDirection;
import de.egladil.mja_api.domain.dto.Suchfilter;
import de.egladil.mja_api.domain.dto.SuchfilterVariante;
import de.egladil.mja_api.domain.generatoren.RaetselFileService;
import de.egladil.mja_api.domain.quellen.QuelleMinimalDto;
import de.egladil.mja_api.domain.quellen.QuellenService;
import de.egladil.mja_api.domain.raetsel.AntwortvorschlaegeMapper;
import de.egladil.mja_api.domain.raetsel.Raetsel;
import de.egladil.mja_api.domain.raetsel.RaetselDao;
import de.egladil.mja_api.domain.raetsel.RaetselService;
import de.egladil.mja_api.domain.raetsel.dto.EditRaetselPayload;
import de.egladil.mja_api.domain.raetsel.dto.GrafikInfo;
import de.egladil.mja_api.domain.raetsel.dto.Images;
import de.egladil.mja_api.domain.raetsel.dto.RaetselLaTeXDto;
import de.egladil.mja_api.domain.raetsel.dto.RaetselsucheTreffer;
import de.egladil.mja_api.domain.raetsel.dto.RaetselsucheTrefferItem;
import de.egladil.mja_api.domain.utils.PermissionUtils;
import de.egladil.mja_api.infrastructure.cdi.AuthenticationContext;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistentesRaetsel;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistentesRaetselHistorieItem;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * RaetselServiceImpl
 */
@ApplicationScoped
public class RaetselServiceImpl implements RaetselService {

	private static final Logger LOGGER = LoggerFactory.getLogger(RaetselServiceImpl.class);

	@Inject
	AuthenticationContext authCtx;

	@Inject
	DeskriptorenService deskriptorenService;

	@Inject
	RaetselFileService raetselFileService;

	@Inject
	QuellenService quellenServive;

	@Inject
	RaetselDao raetselDao;

	private final FindPathsGrafikParser findPathsGrafikParser = new FindPathsGrafikParser();

	@Override
	public RaetselsucheTreffer sucheRaetsel(final Suchfilter suchfilter, final int limit, final int offset, final SortDirection sortDirection) {

		SuchfilterVariante suchfilterVariante = suchfilter.suchfilterVariante();

		List<PersistentesRaetsel> trefferliste = new ArrayList<>();

		List<RaetselsucheTrefferItem> treffer = new ArrayList<>();
		long anzahlGesamt = 0L;

		boolean nurFreigegebene = PermissionUtils.restrictSucheToFreigegeben(PermissionUtils.getRelevantRoles(authCtx));

		switch (suchfilterVariante) {

		case COMPLETE -> anzahlGesamt = raetselDao.countRaetselWithFilter(suchfilter.getSuchstring(),
			suchfilter.getDeskriptorenIds(), nurFreigegebene);
		case DESKRIPTOREN -> anzahlGesamt = raetselDao.countWithDeskriptoren(suchfilter.getDeskriptorenIds(), nurFreigegebene);
		case VOLLTEXT -> anzahlGesamt = raetselDao.countRaetselVolltext(suchfilter.getSuchstring(), nurFreigegebene);
		default -> throw new IllegalArgumentException("unerwartete SuchfilterVariante " + suchfilterVariante);
		}

		if (anzahlGesamt == 0) {

			return new RaetselsucheTreffer();
		}

		switch (suchfilterVariante) {

		case COMPLETE -> trefferliste = raetselDao.findRaetselWithFilter(suchfilter.getSuchstring(),
			suchfilter.getDeskriptorenIds(),
			limit,
			offset, sortDirection, nurFreigegebene);
		case DESKRIPTOREN -> trefferliste = raetselDao.findWithDeskriptoren(suchfilter.getDeskriptorenIds(), limit, offset,
			sortDirection, nurFreigegebene);
		case VOLLTEXT -> trefferliste = raetselDao.findRaetselVolltext(suchfilter.getSuchstring(), limit, offset,
			sortDirection, nurFreigegebene);

		default -> new IllegalArgumentException("Unexpected value: " + suchfilterVariante);
		}

		treffer = trefferliste.stream().map(pr -> mapToSucheTrefferFromDB(pr)).toList();

		RaetselsucheTreffer result = new RaetselsucheTreffer();
		result.setTreffer(treffer);
		result.setTrefferGesamt(anzahlGesamt);

		return result;
	}

	@Override
	@Transactional
	public Raetsel raetselAnlegen(final EditRaetselPayload payload) {

		boolean schluesselExistiert = this.schluesselExists(payload);

		if (schluesselExistiert) {

			throw new WebApplicationException(
				Response.status(409).entity(MessagePayload.error("Der Schlüssel ist bereits vergeben.")).build());
		}

		PersistentesRaetsel neuesRaetsel = new PersistentesRaetsel();
		String uuid = UUID.randomUUID().toString();
		neuesRaetsel.setImportierteUuid(uuid);
		String userId = authCtx.getUser().getName();

		neuesRaetsel.owner = userId;
		neuesRaetsel.geaendertDurch = userId;
		mergeWithPayload(neuesRaetsel, payload.getRaetsel(), userId);

		PersistentesRaetsel.persist(neuesRaetsel);

		Raetsel result = payload.getRaetsel();
		result.setId(neuesRaetsel.uuid);
		result.markiereAlsAenderbar();

		LOGGER.info("Raetsel angelegt: [raetsel={}, user={}]", result.getId(),
			StringUtils.abbreviate(authCtx.getUser().getName(), 11));

		return result;
	}

	@Override
	@Transactional
	public Raetsel raetselAendern(final EditRaetselPayload payload) {

		Raetsel raetsel = payload.getRaetsel();
		String raetselId = raetsel.getId();
		PersistentesRaetsel persistentesRaetsel = PersistentesRaetsel.findById(raetselId);
		String userId = authCtx.getUser().getName();

		if (persistentesRaetsel == null) {

			LOGGER.error("Aendern raetsel mit UUID {}: raetsel existiert nicht. uuidAendernderUser={}", raetselId,
				userId);

			throw new WebApplicationException(
				Response.status(404).entity(MessagePayload.error("Es gibt kein Raetsel mit dieser UUID")).build());
		}

		if (!PermissionUtils.hasWritePermission(userId,
			PermissionUtils.getRelevantRoles(authCtx), persistentesRaetsel.owner)) {

			LOGGER.warn("User {} hat versucht, Raetsel {} mit Owner {} zu aendern", userId, persistentesRaetsel.schluessel,
				persistentesRaetsel.owner);

			throw new WebApplicationException(Status.UNAUTHORIZED);
		}

		boolean schluesselExistiert = this.schluesselExists(payload);

		if (schluesselExistiert) {

			throw new WebApplicationException(
				Response.status(409).entity(MessagePayload.error("Der Schlüssel ist bereits vergeben.")).build());
		}

		if (payload.isLatexHistorisieren()) {

			PersistentesRaetselHistorieItem neuesHistorieItem = new PersistentesRaetselHistorieItem();
			neuesHistorieItem.frage = persistentesRaetsel.frage;
			neuesHistorieItem.loesung = persistentesRaetsel.loesung;
			neuesHistorieItem.geaendertAm = new Date();
			neuesHistorieItem.geaendertDurch = userId;
			neuesHistorieItem.raetsel = persistentesRaetsel;

			PersistentesRaetselHistorieItem.persist(neuesHistorieItem);
		}

		mergeWithPayload(persistentesRaetsel, payload.getRaetsel(), userId);
		PersistentesRaetsel.persist(persistentesRaetsel);

		LOGGER.info("Raetsel geaendert: [raetsel={}, user={}]", raetselId,
			StringUtils.abbreviate(authCtx.getUser().getName(), 11));

		return getRaetselZuId(raetselId);
	}

	boolean schluesselExists(final EditRaetselPayload payload) {

		PersistentesRaetsel persistentesRaetsel = raetselDao.findWithSchluessel(payload.getRaetsel().getSchluessel());

		if (persistentesRaetsel == null) {

			return false;
		}

		return !persistentesRaetsel.uuid.equals(payload.getRaetsel().getId());
	}

	@Override
	public Raetsel getRaetselZuId(final String id) {

		PersistentesRaetsel raetsel = PersistentesRaetsel.findById(id);

		if (raetsel == null) {

			return null;
		}

		Raetsel result = mapFromDB(raetsel);

		List<String> grafikLinks = findPathsGrafikParser.findPaths(raetsel.frage);
		grafikLinks.addAll(findPathsGrafikParser.findPaths(raetsel.loesung));

		List<GrafikInfo> grafikInfos = getGrafikInfos(grafikLinks);
		result.setImages(raetselFileService.findImages(result.getSchluessel()));

		result.setGrafikInfos(grafikInfos);
		Optional<QuelleMinimalDto> optQuelle = quellenServive.loadQuelleMinimal(raetsel.quelle);

		if (optQuelle.isPresent()) {

			result.setQuelle(optQuelle.get());
		}

		return result;
	}

	@Override
	public Images findImagesZuSchluessel(final String schluessel) {

		return this.raetselFileService.findImages(schluessel);
	}

	@Override
	public List<RaetselLaTeXDto> findRaetselLaTeXwithSchluessel(final List<String> schluessel) {

		List<PersistentesRaetsel> trefferliste = raetselDao.findWithSchluessel(schluessel);

		return trefferliste.stream().map(pr -> RaetselLaTeXDto.mapFromDB(pr)).toList();

	}

	List<GrafikInfo> getGrafikInfos(final List<String> pfade) {

		final ArrayList<GrafikInfo> result = new ArrayList<>();

		pfade.forEach(pfad -> {

			boolean exists = raetselFileService.existsGrafik(pfad);
			result.add(new GrafikInfo(pfad, exists));
		});

		return result;
	}

	void mergeWithPayload(final PersistentesRaetsel persistentesRaetsel, final Raetsel daten, final String userId) {

		persistentesRaetsel.antwortvorschlaege = daten.antwortvorschlaegeAsJSON();
		persistentesRaetsel.deskriptoren = deskriptorenService.sortAndStringifyIdsDeskriptoren(daten.getDeskriptoren());
		persistentesRaetsel.frage = daten.getFrage();
		persistentesRaetsel.geaendertDurch = userId;
		persistentesRaetsel.kommentar = daten.getKommentar();
		persistentesRaetsel.loesung = daten.getLoesung();
		persistentesRaetsel.quelle = daten.getQuelle().getId();
		persistentesRaetsel.schluessel = daten.getSchluessel();
		persistentesRaetsel.name = daten.getName();
		persistentesRaetsel.status = daten.getStatus();
		persistentesRaetsel.owner = persistentesRaetsel.isPersistent() ? persistentesRaetsel.owner : userId;
	}

	Raetsel mapFromDB(final PersistentesRaetsel raetselDB) {

		Raetsel result = new Raetsel(raetselDB.uuid)
			.withAntwortvorschlaege(AntwortvorschlaegeMapper.deserializeAntwortvorschlaege(raetselDB.antwortvorschlaege))
			.withDeskriptoren(deskriptorenService.mapToDeskriptoren(raetselDB.deskriptoren))
			.withFrage(raetselDB.frage)
			.withKommentar(raetselDB.kommentar)
			.withLoesung(raetselDB.loesung)
			.withQuelleId(raetselDB.quelle)
			.withSchluessel(raetselDB.schluessel)
			.withStatus(raetselDB.status)
			.withName(raetselDB.name);

		boolean hasWritePermission = PermissionUtils.hasWritePermission(authCtx.getUser().getName(),
			PermissionUtils.getRelevantRoles(authCtx), raetselDB.owner);

		if (hasWritePermission) {

			result.markiereAlsAenderbar();
		}

		return result;
	}

	RaetselsucheTrefferItem mapToSucheTrefferFromDB(final PersistentesRaetsel raetselDB) {

		RaetselsucheTrefferItem result = new RaetselsucheTrefferItem()
			.withDeskriptoren(deskriptorenService.mapToDeskriptoren(raetselDB.deskriptoren))
			.withId(raetselDB.uuid)
			.withName(raetselDB.name)
			.withStatus(raetselDB.status)
			.withKommentar(raetselDB.kommentar)
			.withSchluessel(raetselDB.schluessel);

		return result;
	}

	@Override
	public Optional<String> getRaetselIdWithSchluessel(final String schluessel) {

		PersistentesRaetsel raetsel = raetselDao.findWithSchluessel(schluessel);

		return raetsel == null ? Optional.empty() : Optional.of(raetsel.uuid);
	}

	@Override
	public AnzahlabfrageResponseDto zaehleFreigegebeneRaetsel() {

		long anzahl = raetselDao.countRaetselWithStatus(DomainEntityStatus.FREIGEGEBEN);
		AnzahlabfrageResponseDto result = new AnzahlabfrageResponseDto();
		result.setErgebnis(anzahl);

		return result;
	}

}
