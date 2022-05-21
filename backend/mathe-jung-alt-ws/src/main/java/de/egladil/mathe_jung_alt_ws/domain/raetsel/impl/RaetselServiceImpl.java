// =====================================================
// Project: mathe-jung-alt-ws
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mathe_jung_alt_ws.domain.raetsel.impl;

import java.util.Arrays;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.egladil.mathe_jung_alt_ws.domain.deskriptoren.DeskriptorenService;
import de.egladil.mathe_jung_alt_ws.domain.error.MjaRuntimeException;
import de.egladil.mathe_jung_alt_ws.domain.raetsel.Antwortvorschlag;
import de.egladil.mathe_jung_alt_ws.domain.raetsel.Raetsel;
import de.egladil.mathe_jung_alt_ws.domain.raetsel.RaetselService;
import de.egladil.mathe_jung_alt_ws.domain.raetsel.dto.EditRaetselPayload;
import de.egladil.mathe_jung_alt_ws.infrastructure.persistence.entities.PersistentesRaetsel;

/**
 * RaetselServiceImpl
 */
@ApplicationScoped
public class RaetselServiceImpl implements RaetselService {

	@Inject
	DeskriptorenService deskriptorenService;

	@Override
	@Transactional
	public Raetsel raetselAnlegen(final EditRaetselPayload payload) {

		PersistentesRaetsel neuesRaetsel = new PersistentesRaetsel();
		String uuid = UUID.randomUUID().toString();
		neuesRaetsel.setImportierteUuid(uuid);
		mergeWithPayload(neuesRaetsel, payload);

		PersistentesRaetsel.persist(Arrays.asList(new PersistentesRaetsel[] { neuesRaetsel }));

		Raetsel result = payload.getRaetsel();
		result.setId(neuesRaetsel.uuid);

		return result;
	}

	@Override
	public Raetsel getRaetselZuId(final String id) {

		PersistentesRaetsel raetsel = PersistentesRaetsel.findById(id);

		if (raetsel == null) {

			return null;
		}

		return mapFromDB(raetsel);
	}

	void mergeWithPayload(final PersistentesRaetsel persistentesRaetsel, final EditRaetselPayload payload) {

		Raetsel daten = payload.getRaetsel();
		persistentesRaetsel.anzahlAntworten = daten.getAntwortvorschlaege() != null ? 0 : daten.getAntwortvorschlaege().length;
		persistentesRaetsel.antwortvorschlaege = daten.antwortvorschlaegeAsJSON();
		persistentesRaetsel.deskriptoren = deskriptorenService.serializeDeskriptoren(daten.getDeskriptoren());
		persistentesRaetsel.frage = daten.getFrage();
		persistentesRaetsel.geaendertDurch = payload.getIdAendernderUser();
		persistentesRaetsel.kommentar = daten.getKommentar();
		persistentesRaetsel.loesung = daten.getLoesung();
		persistentesRaetsel.quelle = daten.getQuelleId();
		persistentesRaetsel.schluessel = daten.getSchluessel();
		persistentesRaetsel.name = daten.getName();
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
			.withName(raetselDB.name);
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
