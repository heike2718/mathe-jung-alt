// =====================================================
// Project: mja-admin-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_admin_api.domain.raetsel.impl;

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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.egladil.mja_admin_api.domain.deskriptoren.DeskriptorenService;
import de.egladil.mja_admin_api.domain.dto.SortDirection;
import de.egladil.mja_admin_api.domain.dto.Suchfilter;
import de.egladil.mja_admin_api.domain.dto.SuchfilterVariante;
import de.egladil.mja_admin_api.domain.generatoren.RaetselFileService;
import de.egladil.mja_admin_api.domain.raetsel.Antwortvorschlag;
import de.egladil.mja_admin_api.domain.raetsel.Raetsel;
import de.egladil.mja_admin_api.domain.raetsel.RaetselDao;
import de.egladil.mja_admin_api.domain.raetsel.RaetselService;
import de.egladil.mja_admin_api.domain.raetsel.dto.EditRaetselPayload;
import de.egladil.mja_admin_api.domain.raetsel.dto.RaetselsucheTreffer;
import de.egladil.mja_admin_api.domain.raetsel.dto.RaetselsucheTrefferItem;
import de.egladil.mja_admin_api.infrastructure.persistence.entities.PersistentesRaetsel;
import de.egladil.mja_admin_api.infrastructure.persistence.entities.PersistentesRaetselHistorieItem;
import de.egladil.web.mja_auth.dto.MessagePayload;
import de.egladil.web.mja_shared.exceptions.MjaRuntimeException;

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

	// @Override
	// public long zaehleRaetselMitSuchfilter(final Suchfilter suchfilter) {
	//
	// SuchfilterVariante suchfilterVariante = suchfilter.suchfilterVariante();
	//
	// long result = 0;
	//
	// switch (suchfilterVariante) {
	//
	// case COMPLETE -> result = raetselDao.zaehleRaetselComplete(suchfilter.getSuchstring(), suchfilter.getDeskriptorenIds());
	// case DESKRIPTOREN -> result = raetselDao.zaehleMitDeskriptoren(suchfilter.getDeskriptorenIds());
	// case VOLLTEXT -> result = raetselDao.zaehleRaetselVolltext(suchfilter.getSuchstring());
	// default -> throw new IllegalArgumentException("unerwartete SuchfilterVariante " + suchfilterVariante);
	// }
	//
	// return result;
	//
	// }

	@Override
	public RaetselsucheTreffer sucheRaetsel(final Suchfilter suchfilter, final int limit, final int offset, final SortDirection sortDirection) {

		SuchfilterVariante suchfilterVariante = suchfilter.suchfilterVariante();

		List<PersistentesRaetsel> trefferliste = new ArrayList<>();

		List<RaetselsucheTrefferItem> treffer = new ArrayList<>();
		long anzahlGesamt = 0L;

		switch (suchfilterVariante) {

		case COMPLETE -> anzahlGesamt = raetselDao.zaehleRaetselComplete(suchfilter.getSuchstring(),
			suchfilter.getDeskriptorenIds());
		case DESKRIPTOREN -> anzahlGesamt = raetselDao.zaehleMitDeskriptoren(suchfilter.getDeskriptorenIds());
		case VOLLTEXT -> anzahlGesamt = raetselDao.zaehleRaetselVolltext(suchfilter.getSuchstring());
		default -> throw new IllegalArgumentException("unerwartete SuchfilterVariante " + suchfilterVariante);
		}

		if (anzahlGesamt == 0) {

			return new RaetselsucheTreffer();
		}

		switch (suchfilterVariante) {

		case COMPLETE -> trefferliste = raetselDao.sucheRaetselComplete(suchfilter.getSuchstring(), suchfilter.getDeskriptorenIds(),
			limit,
			offset, sortDirection);
		case DESKRIPTOREN -> trefferliste = raetselDao.sucheMitDeskriptoren(suchfilter.getDeskriptorenIds(), limit, offset,
			sortDirection);
		case VOLLTEXT -> trefferliste = raetselDao.sucheRaetselVolltext(suchfilter.getSuchstring(), limit, offset, sortDirection);

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

		byte[] imageFrage = raetselFileService.findImageFrage(result.getSchluessel());
		result.setImageFrage(imageFrage);

		byte[] imageLoesung = raetselFileService.findImageLoesung(result.getSchluessel());
		result.setImageLoesung(imageLoesung);

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
			.withAntwortvorschlaege(deserializeAntwortvorschlaege(raetselDB.antwortvorschlaege))
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

	Antwortvorschlag[] deserializeAntwortvorschlaege(final String json) {

		if (json == null) {

			return new Antwortvorschlag[0];
		}

		try {

			return new ObjectMapper().readValue(json, Antwortvorschlag[].class);
		} catch (JsonProcessingException e) {

			throw new MjaRuntimeException("konnte antwortvorschlaege nicht deserialisieren: " + e.getMessage(), e);
		}
	}
}
