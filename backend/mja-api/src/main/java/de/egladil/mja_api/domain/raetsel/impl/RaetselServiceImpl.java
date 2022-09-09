// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.raetsel.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mja_api.domain.deskriptoren.DeskriptorenService;
import de.egladil.mja_api.domain.dto.SortDirection;
import de.egladil.mja_api.domain.dto.Suchfilter;
import de.egladil.mja_api.domain.dto.SuchfilterVariante;
import de.egladil.mja_api.domain.generatoren.RaetselFileService;
import de.egladil.mja_api.domain.raetsel.AntwortvorschlaegeMapper;
import de.egladil.mja_api.domain.raetsel.Raetsel;
import de.egladil.mja_api.domain.raetsel.RaetselDao;
import de.egladil.mja_api.domain.raetsel.RaetselService;
import de.egladil.mja_api.domain.raetsel.dto.EditRaetselPayload;
import de.egladil.mja_api.domain.raetsel.dto.GrafikInfo;
import de.egladil.mja_api.domain.raetsel.dto.RaetselsucheTreffer;
import de.egladil.mja_api.domain.raetsel.dto.RaetselsucheTrefferItem;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistentesRaetsel;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistentesRaetselHistorieItem;
import de.egladil.web.mja_auth.dto.MessagePayload;

/**
 * RaetselServiceImpl
 */
@ApplicationScoped
public class RaetselServiceImpl implements RaetselService {

	private static final Logger LOGGER = LoggerFactory.getLogger(RaetselServiceImpl.class);

	@Inject
	DeskriptorenService deskriptorenService;

	@Inject
	RaetselFileService raetselFileService;

	@Inject
	RaetselDao raetselDao;

	private final FindPathsGrafikParser findPathsGrafikParser = new FindPathsGrafikParser();

	@Override
	public RaetselsucheTreffer sucheRaetsel(final Suchfilter suchfilter, final int limit, final int offset, final SortDirection sortDirection) {

		SuchfilterVariante suchfilterVariante = suchfilter.suchfilterVariante();

		List<PersistentesRaetsel> trefferliste = new ArrayList<>();

		List<RaetselsucheTrefferItem> treffer = new ArrayList<>();
		long anzahlGesamt = 0L;

		switch (suchfilterVariante) {

			case COMPLETE -> anzahlGesamt = raetselDao.countRaetselWithFilter(suchfilter.getSuchstring(),
				suchfilter.getDeskriptorenIds());
			case DESKRIPTOREN -> anzahlGesamt = raetselDao.countWithDeskriptoren(suchfilter.getDeskriptorenIds());
			case VOLLTEXT -> anzahlGesamt = raetselDao.countRaetselVolltext(suchfilter.getSuchstring());
			default -> throw new IllegalArgumentException("unerwartete SuchfilterVariante " + suchfilterVariante);
		}

		if (anzahlGesamt == 0) {

			return new RaetselsucheTreffer();
		}

		switch (suchfilterVariante) {

			case COMPLETE -> trefferliste = raetselDao.findRaetselWithFilter(suchfilter.getSuchstring(),
				suchfilter.getDeskriptorenIds(),
				limit,
				offset, sortDirection);
			case DESKRIPTOREN -> trefferliste = raetselDao.findWithDeskriptoren(suchfilter.getDeskriptorenIds(), limit, offset,
				sortDirection);
			case VOLLTEXT -> trefferliste = raetselDao.findRaetselVolltext(suchfilter.getSuchstring(), limit, offset,
				sortDirection);

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
	public Raetsel raetselAnlegen(final EditRaetselPayload payload, final String uuidAendernderUser) {

		PersistentesRaetsel neuesRaetsel = new PersistentesRaetsel();
		String uuid = UUID.randomUUID().toString();
		neuesRaetsel.setImportierteUuid(uuid);
		mergeWithPayload(neuesRaetsel, payload.getRaetsel(), uuidAendernderUser);

		PersistentesRaetsel.persist(neuesRaetsel);

		Raetsel result = payload.getRaetsel();
		result.setId(neuesRaetsel.uuid);

		return result;
	}

	@Override
	@Transactional
	public Raetsel raetselAendern(final EditRaetselPayload payload, final String uuidAendernderUser) {

		Raetsel raetsel = payload.getRaetsel();
		String raetselId = raetsel.getId();
		PersistentesRaetsel persistentesRaetsel = PersistentesRaetsel.findById(raetselId);

		if (persistentesRaetsel == null) {

			LOGGER.error("Aendern raetsel mit UUID {}: raetsel existiert nicht. uuidAendernderUser={}", raetselId,
				uuidAendernderUser);

			throw new WebApplicationException(
				Response.status(404).entity(MessagePayload.error("Es gibt kein Raetsel mit dieser UUID")).build());
		}

		if (payload.isLatexHistorisieren()) {

			PersistentesRaetselHistorieItem neuesHistorieItem = new PersistentesRaetselHistorieItem();
			neuesHistorieItem.frage = persistentesRaetsel.frage;
			neuesHistorieItem.loesung = persistentesRaetsel.loesung;
			neuesHistorieItem.geaendertAm = new Date();
			neuesHistorieItem.geaendertDurch = uuidAendernderUser;
			neuesHistorieItem.raetsel = persistentesRaetsel;

			PersistentesRaetselHistorieItem.persist(neuesHistorieItem);
		}

		mergeWithPayload(persistentesRaetsel, payload.getRaetsel(), uuidAendernderUser);
		PersistentesRaetsel.persist(persistentesRaetsel);

		return getRaetselZuId(raetselId);
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

		byte[] imageFrage = raetselFileService.findImageFrage(result.getSchluessel());
		result.setImageFrage(imageFrage);

		byte[] imageLoesung = raetselFileService.findImageLoesung(result.getSchluessel());
		result.setImageLoesung(imageLoesung);

		result.setGrafikInfos(grafikInfos);
		return result;
	}

	List<GrafikInfo> getGrafikInfos(final List<String> pfade) {

		final ArrayList<GrafikInfo> result = new ArrayList<>();

		pfade.forEach(pfad -> {

			boolean exists = raetselFileService.existsGrafik(pfad);
			result.add(new GrafikInfo(pfad, exists));
		});

		return result;
	}

	void mergeWithPayload(final PersistentesRaetsel persistentesRaetsel, final Raetsel daten, final String uuidAendernderUser) {

		persistentesRaetsel.antwortvorschlaege = daten.antwortvorschlaegeAsJSON();
		persistentesRaetsel.deskriptoren = deskriptorenService.sortAndStringifyIdsDeskriptoren(daten.getDeskriptoren());
		persistentesRaetsel.frage = daten.getFrage();
		persistentesRaetsel.geaendertDurch = uuidAendernderUser;
		persistentesRaetsel.kommentar = daten.getKommentar();
		persistentesRaetsel.loesung = daten.getLoesung();
		persistentesRaetsel.quelle = daten.getQuelleId();
		persistentesRaetsel.schluessel = daten.getSchluessel();
		persistentesRaetsel.name = daten.getName();
		persistentesRaetsel.status = daten.getStatus();
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

}
