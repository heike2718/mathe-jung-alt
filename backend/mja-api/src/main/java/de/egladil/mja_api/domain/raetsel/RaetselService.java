// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.raetsel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.mja_api.domain.auth.dto.MessagePayload;
import de.egladil.mja_api.domain.auth.session.Benutzerart;
import de.egladil.mja_api.domain.deskriptoren.DeskriptorenService;
import de.egladil.mja_api.domain.dto.AnzahlabfrageResponseDto;
import de.egladil.mja_api.domain.dto.SortDirection;
import de.egladil.mja_api.domain.dto.Suchfilter;
import de.egladil.mja_api.domain.dto.SuchfilterVariante;
import de.egladil.mja_api.domain.embeddable_images.dto.Textart;
import de.egladil.mja_api.domain.exceptions.MjaRuntimeException;
import de.egladil.mja_api.domain.generatoren.RaetselFileService;
import de.egladil.mja_api.domain.quellen.Quelle;
import de.egladil.mja_api.domain.quellen.QuellenService;
import de.egladil.mja_api.domain.quellen.Quellenart;
import de.egladil.mja_api.domain.quellen.dto.QuelleDto;
import de.egladil.mja_api.domain.quellen.impl.QuelleNameStrategie;
import de.egladil.mja_api.domain.raetsel.dto.EditRaetselPayload;
import de.egladil.mja_api.domain.raetsel.dto.EmbeddableImageInfo;
import de.egladil.mja_api.domain.raetsel.dto.Images;
import de.egladil.mja_api.domain.raetsel.dto.RaetselLaTeXDto;
import de.egladil.mja_api.domain.raetsel.dto.RaetselsucheTreffer;
import de.egladil.mja_api.domain.raetsel.dto.RaetselsucheTrefferItem;
import de.egladil.mja_api.domain.raetsel.impl.DeleteUnusedEmbeddableImageFilesService;
import de.egladil.mja_api.domain.raetsel.impl.FindPathsGrafikParser;
import de.egladil.mja_api.domain.raetsel.impl.FragenUndLoesungenVO;
import de.egladil.mja_api.domain.raetsel.impl.RaetselPermissionDelegate;
import de.egladil.mja_api.infrastructure.cdi.AuthenticationContext;
import de.egladil.mja_api.infrastructure.persistence.dao.QuellenRepository;
import de.egladil.mja_api.infrastructure.persistence.dao.RaetselDao;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistenteQuelle;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistenteQuelleReadonly;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistentesRaetsel;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistentesRaetselHistorieItem;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

/**
 * RaetselService
 */
@ApplicationScoped
public class RaetselService {

	private static final Logger LOGGER = LoggerFactory.getLogger(RaetselService.class);

	@Inject
	AuthenticationContext authCtx;

	@Inject
	RaetselPermissionDelegate permissionDelegate;

	@Inject
	DeskriptorenService deskriptorenService;

	@Inject
	RaetselFileService raetselFileService;

	@Inject
	QuellenService quellenService;

	@Inject
	QuellenRepository quellenRepository;

	@Inject
	RaetselDao raetselDao;

	@Inject
	DeleteUnusedEmbeddableImageFilesService deleteImagesFileService;

	private final FindPathsGrafikParser findPathsGrafikParser = new FindPathsGrafikParser();

	/**
	 * Sucht alle Rätsel, die zum Suchfilter passen und gibt sie der Permission enstprechend zurück.
	 *
	 * @param  suchfilter
	 * @param  limit
	 *                       int Anzahl Treffer in page
	 * @param  offset
	 *                       int Aufsetzpunkt für page
	 * @param  sortDirection
	 *                       SortDirection nach schluessel
	 * @return               RaetselsucheTreffer
	 */
	public RaetselsucheTreffer sucheRaetsel(final Suchfilter suchfilter, final int limit, final int offset, final SortDirection sortDirection) {

		SuchfilterVariante suchfilterVariante = suchfilter.suchfilterVariante();

		List<PersistentesRaetsel> trefferliste = new ArrayList<>();

		List<RaetselsucheTrefferItem> treffer = new ArrayList<>();
		long anzahlGesamt = 0L;

		boolean nurFreigegebene = permissionDelegate.isOnlyReadFreigegebene();

		switch (suchfilterVariante) {

		case COMPLETE -> anzahlGesamt = raetselDao.countRaetselWithFilter(suchfilter, nurFreigegebene);
		case DESKRIPTOREN -> anzahlGesamt = raetselDao.countWithDeskriptoren(suchfilter.getDeskriptorenIds(),
			suchfilter.getModusDeskriptoren(),
			nurFreigegebene);
		case VOLLTEXT -> anzahlGesamt = raetselDao.countRaetselVolltext(suchfilter.getSuchstring(), suchfilter.getModusVolltext(),
			nurFreigegebene);
		default -> throw new IllegalArgumentException("unerwartete SuchfilterVariante " + suchfilterVariante);
		}

		if (anzahlGesamt == 0) {

			return new RaetselsucheTreffer();
		}

		switch (suchfilterVariante) {

		case COMPLETE -> trefferliste = raetselDao.findRaetselWithFilter(suchfilter, limit, offset, sortDirection, nurFreigegebene);
		case DESKRIPTOREN -> trefferliste = raetselDao.findWithDeskriptoren(suchfilter.getDeskriptorenIds(),
			suchfilter.getModusDeskriptoren(), limit,
			offset, sortDirection, nurFreigegebene);
		case VOLLTEXT -> trefferliste = raetselDao.findRaetselVolltext(suchfilter.getSuchstring(), suchfilter.getModusVolltext(),
			limit,
			offset, sortDirection, nurFreigegebene);

		default -> new IllegalArgumentException("Unexpected value: " + suchfilterVariante);
		}

		treffer = trefferliste.stream().map(pr -> mapToSucheTrefferFromDB(pr)).toList();

		RaetselsucheTreffer result = new RaetselsucheTreffer();
		result.setTreffer(treffer);
		result.setTrefferGesamt(anzahlGesamt);

		return result;
	}

	/**
	 * Legt ein neues Rätsel an. Durfen nur Autoren und Admins.
	 *
	 * @param  payload
	 *                 EditRaetselPayload die Daten und Metainformationen
	 * @return         RaetselPayloadDaten mit einer generierten UUID.
	 */
	public Raetsel raetselAnlegen(final EditRaetselPayload payload) {

		String raetselId = doInsertRaetsel(payload);
		Raetsel result = this.getRaetselZuId(raetselId);

		LOGGER.info("Raetsel angelegt: [raetsel={}, admin={}]", result.getId(),
			StringUtils.abbreviate(authCtx.getUser().getName(), 11));

		return result;
	}

	@Transactional
	String doInsertRaetsel(final EditRaetselPayload payload) {

		if (schluesselGenerieren(payload)) {

			String schluessel = generiereSchluessel();
			payload.getRaetsel().setSchluessel(schluessel);
		}

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
		neuesRaetsel.filenameVorschauFrage = generateFilenameVorschau();
		neuesRaetsel.filenameVorschauLoesung = generateFilenameVorschau();

		mergeWithPayload(neuesRaetsel, payload.getRaetsel(), userId);

		QuelleDto datenQuelle = payload.getQuelle();
		String quelleId = null;

		Quelle quelle = new Quelle(datenQuelle.getId())
			.withDatenQuelle(datenQuelle)
			.withOwner(userId);

		if ("neu".equals(quelle.getId())) {

			if (Quellenart.PERSON == datenQuelle.getQuellenart() && RaetselHerkunftTyp.EIGENKREATION == neuesRaetsel.herkunft) {

				// Dann ist die userId klar. In anderen Fällen handelt es sich um eine von ein von einer anderen Person erfundenes
				// Rätsel, das der Admin für diese Person einträgt. Dann benötigt die Quelle keine userId.
				quelle.setUserId(userId);
			}

			PersistenteQuelle neueQuelle = quellenService.quelleAnlegen(quelle);
			quelleId = neueQuelle.uuid;
		} else {

			quelleId = datenQuelle.getId();
		}

		neuesRaetsel.quelle = quelleId;
		raetselDao.save(neuesRaetsel);

		return neuesRaetsel.uuid;
	}

	/**
	 * @return
	 */
	String generiereSchluessel() {

		int maxSchluessel = raetselDao.getMaximumOfAllSchluessel();
		String schluessel = StringUtils.leftPad(++maxSchluessel + "", 5, "0");
		return schluessel;
	}

	/**
	 * @param  payload
	 * @return
	 */
	boolean schluesselGenerieren(final EditRaetselPayload payload) {

		if (Benutzerart.ADMIN != authCtx.getUser().getBenutzerart()) {

			return true;
		}

		return StringUtils.isBlank(payload.getRaetsel().getSchluessel());
	}

	/**
	 * Ändert ein vorhandenes Raetsel
	 *
	 * @param  payload
	 *                 EditRaetselPayload die Daten und Metainformationen
	 * @return         RaetselPayloadDaten mit einer generierten UUID.
	 */
	public Raetsel raetselAendern(final EditRaetselPayload payload) {

		String raetselId = payload.getRaetsel().getId();
		doUpdateRaetsel(payload);

		LOGGER.info("Raetsel geaendert: [raetsel={}, admin={}]", raetselId,
			StringUtils.abbreviate(authCtx.getUser().getName(), 11));

		return getRaetselZuId(raetselId);
	}

	@Transactional
	void doUpdateRaetsel(final EditRaetselPayload payload) {

		Raetsel raetsel = payload.getRaetsel();
		String raetselId = raetsel.getId();
		PersistentesRaetsel persistentesRaetsel = raetselDao.findById(raetselId);
		String userId = authCtx.getUser().getName();

		if (persistentesRaetsel == null) {

			LOGGER.error("Aendern raetsel mit UUID {}: raetsel existiert nicht. uuidAendernderUser={}", raetselId,
				userId);

			throw new WebApplicationException(
				Response.status(404).entity(MessagePayload.error("Es gibt kein Raetsel mit dieser UUID")).build());
		}

		permissionDelegate.checkWritePermission(persistentesRaetsel);

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

			raetselDao.insert(neuesHistorieItem);
		}

		FragenUndLoesungenVO fragenLoesungenVo = new FragenUndLoesungenVO().withFrageAlt(persistentesRaetsel.frage)
			.withFrageNeu(payload.getRaetsel().getFrage()).withLoesungAlt(persistentesRaetsel.loesung)
			.withLoesungNeu(payload.getRaetsel().getLoesung());

		if (persistentesRaetsel.filenameVorschauFrage == null) {

			persistentesRaetsel.filenameVorschauFrage = generateFilenameVorschau();
		}

		if (persistentesRaetsel.filenameVorschauLoesung == null) {

			persistentesRaetsel.filenameVorschauLoesung = generateFilenameVorschau();

		}

		QuelleDto quelle = payload.getQuelle();

		String quelleId = null;

		if (!persistentesRaetsel.quelle.equals(quelle.getId())) {

			// TODO: hier alte Quelle löschen, neue Anlegen anlegen! quelleId = ....
		} else {

			quelleId = quelle.getId();

			// TODO: Hier PersistenteQuelle laden und mit neuen Daten mergen.
		}

		mergeWithPayload(persistentesRaetsel, payload.getRaetsel(), userId);
		persistentesRaetsel.quelle = quelleId;
		raetselDao.save(persistentesRaetsel);

		// Nur löschen, wenn persist klar ging!
		deleteImagesFileService.checkAndDeleteUnusedFiles(fragenLoesungenVo);
	}

	boolean schluesselExists(final EditRaetselPayload payload) {

		PersistentesRaetsel persistentesRaetsel = raetselDao.findWithSchluessel(payload.getRaetsel().getSchluessel());

		if (persistentesRaetsel == null) {

			return false;
		}

		return !persistentesRaetsel.uuid.equals(payload.getRaetsel().getId());
	}

	/**
	 * Holt die Details des Rätsels zu der gegebenen id. Falls das Rätsel existiert, wird der Schreibschutz anhand der Permissions
	 * für den User aufgehoben.
	 *
	 * @param  id
	 * @return    Raetsel oder null.
	 */
	@Transactional
	public Raetsel getRaetselZuId(final String id) {

		PersistentesRaetsel raetsel = raetselDao.findById(id);

		if (raetsel == null) {

			return null;
		}

		if (raetsel.filenameVorschauFrage == null || raetsel.filenameVorschauLoesung == null) {

			LOGGER.error(
				"{}: Datenfehler Filenames für Vorschau wurden noch nicht generiert und persistiert! Sollte auf PROD seit 2.3.2 nicht mehr vorkommen :)",
				raetsel.schluessel);
			throw new MjaRuntimeException("Filenames für Vorschau wurden noch nicht generiert und persistiert!");
		}

		Raetsel result = mapFromDB(raetsel);

		Pair<List<EmbeddableImageInfo>, List<EmbeddableImageInfo>> embedaableImageInfos = loadEmbeddableImageInfos(raetsel);
		result.addAllEmbeddableImageInfos(embedaableImageInfos.getLeft());
		result.addAllEmbeddableImageInfos(embedaableImageInfos.getRight());

		result.setImages(raetselFileService.findImages(raetsel.filenameVorschauFrage, raetsel.filenameVorschauLoesung));

		Optional<HerkunftRaetsel> optQuelle = getHerkunftRaetsel(raetsel.quelle, raetsel);

		if (optQuelle.isPresent()) {

			result.setHerkunft(optQuelle.get());
		} else {

			LOGGER.error("Datenfehler: Rätsel mit SCHLUESSEL={} - keinen Eintrag in QUELLEN mit UUID={} gefunden.",
				raetsel.schluessel, raetsel.quelle);
			throw new MjaRuntimeException("Datenfehler: es gibt keinen Eintrag in QUELLEN mit uuid=" + raetsel.quelle);
		}

		return result;
	}

	Optional<HerkunftRaetsel> getHerkunftRaetsel(final String quelleId, final PersistentesRaetsel raetsel) {

		PersistenteQuelleReadonly ausDB = this.quellenRepository.findQuelleReadonlyById(quelleId);

		if (ausDB == null) {

			return Optional.empty();
		}

		QuelleNameStrategie nameStrategie = QuelleNameStrategie.getStrategie(ausDB.quellenart);
		String text = nameStrategie.getText(ausDB);

		if (raetsel.herkunft == RaetselHerkunftTyp.ADAPTATION) {

			Optional<PersistenteQuelleReadonly> optQuelle = quellenRepository.findQuelleWithUserId(raetsel.owner);

			if (optQuelle.isPresent()) {

				PersistenteQuelleReadonly quelle = optQuelle.get();
				text = quelle.person + " (basierend auf einer Idee aus " + text + ")";
			}

		}

		// @formatter: off
		HerkunftRaetsel result = new HerkunftRaetsel()
			.withId(ausDB.uuid)
			.withQuellenart(ausDB.quellenart)
			.withText(text)
			.withHerkunftstyp(raetsel.herkunft)
			.withMediumUuid(ausDB.mediumUuid);
		// @formatter: on
		return Optional.of(result);
	}

	/**
	 * Parsed den Text von Frage und Lösung (sofern vorhanden) nach includegraphics-Statements und erzeugt daraus Listen von
	 * EmbeddableImageInfos.
	 *
	 * @param  raetsel
	 * @return         Pair left = grafikInfosFrage, right = grafikInfosLoesung. Sie sind nie null, höchstens leer.
	 */
	public Pair<List<EmbeddableImageInfo>, List<EmbeddableImageInfo>> loadEmbeddableImageInfos(final PersistentesRaetsel raetsel) {

		List<String> grafikLinksFrage = findPathsGrafikParser.findPaths(raetsel.frage);
		List<EmbeddableImageInfo> grafikInfosFrage = new ArrayList<>();
		List<EmbeddableImageInfo> grafikInfosLoesung = new ArrayList<>();

		if (!grafikLinksFrage.isEmpty()) {

			grafikInfosFrage = getGrafikInfos(grafikLinksFrage, Textart.FRAGE);
		}

		List<String> grafikLinksLoesung = findPathsGrafikParser.findPaths(raetsel.loesung);

		if (!grafikLinksLoesung.isEmpty()) {

			grafikInfosLoesung = getGrafikInfos(grafikLinksLoesung, Textart.LOESUNG);
		}

		return Pair.of(grafikInfosFrage, grafikInfosLoesung);
	}

	/**
	 * @param  schluessel
	 *                    String
	 * @return            Optional
	 */
	public Images findImagesZuSchluessel(final String schluessel) {

		PersistentesRaetsel raetselDB = raetselDao.findWithSchluessel(schluessel);

		if (raetselDB == null) {

			return new Images();
		}

		return this.raetselFileService.findImages(raetselDB.filenameVorschauFrage, raetselDB.filenameVorschauLoesung);
	}

	/**
	 * Läd das, was als Input für ein LaTeX-File erforderlich ist.
	 *
	 * @param  schluesselliste
	 *                         List eine Liste von Schlüsseln.
	 * @return                 List
	 */
	public List<RaetselLaTeXDto> findRaetselLaTeXwithSchluesselliste(final List<String> schluesselliste) {

		List<PersistentesRaetsel> trefferliste = raetselDao.findWithSchluesselListe(schluesselliste);

		return trefferliste.stream().map(pr -> RaetselLaTeXDto.mapFromDB(pr)).toList();

	}

	List<EmbeddableImageInfo> getGrafikInfos(final List<String> pfade, final Textart textart) {

		final ArrayList<EmbeddableImageInfo> result = new ArrayList<>();

		pfade.forEach(pfad -> {

			boolean exists = raetselFileService.fileExists(pfad);
			result.add(new EmbeddableImageInfo(pfad, exists, textart));
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
		persistentesRaetsel.schluessel = daten.getSchluessel();
		persistentesRaetsel.name = daten.getName();
		persistentesRaetsel.freigegeben = daten.isFreigegeben();
		persistentesRaetsel.herkunft = daten.getHerkunft().getHerkunftstyp();
		persistentesRaetsel.owner = persistentesRaetsel.isPersistent() ? persistentesRaetsel.owner : userId;
	}

	Raetsel mapFromDB(final PersistentesRaetsel raetselDB) {

		Raetsel result = new Raetsel(raetselDB.uuid)
			.withAntwortvorschlaege(AntwortvorschlaegeMapper.deserializeAntwortvorschlaege(raetselDB.antwortvorschlaege))
			.withDeskriptoren(deskriptorenService.mapToDeskriptoren(raetselDB.deskriptoren))
			.withFrage(raetselDB.frage)
			.withKommentar(raetselDB.kommentar)
			.withLoesung(raetselDB.loesung)
			.withSchluessel(raetselDB.schluessel)
			.withFreigebeben(raetselDB.freigegeben)
			.withName(raetselDB.name)
			.withFilenameVorschauFrage(raetselDB.filenameVorschauFrage)
			.withFilenameVorschauLoesung(raetselDB.filenameVorschauLoesung);

		try {

			result.markiereAlsAenderbar();
		} catch (WebApplicationException e) {

			// in diesem Fall keine Schreibberechtigung
		}

		return result;
	}

	RaetselsucheTrefferItem mapToSucheTrefferFromDB(final PersistentesRaetsel raetselDB) {

		RaetselsucheTrefferItem result = new RaetselsucheTrefferItem()
			.withDeskriptoren(deskriptorenService.mapToDeskriptoren(raetselDB.deskriptoren))
			.withId(raetselDB.uuid)
			.withName(raetselDB.name)
			.withFreigegeben(raetselDB.freigegeben)
			.withKommentar(raetselDB.kommentar)
			.withSchluessel(raetselDB.schluessel)
			.withHerkunft(raetselDB.herkunft);

		return result;
	}

	/**
	 * @param  schluessel
	 * @return            Optional
	 */
	public Optional<String> getRaetselIdWithSchluessel(final String schluessel) {

		PersistentesRaetsel raetsel = raetselDao.findWithSchluessel(schluessel);

		return raetsel == null ? Optional.empty() : Optional.of(raetsel.uuid);
	}

	/**
	 * @return AnzahlabfrageResponseDto
	 */
	public AnzahlabfrageResponseDto zaehleFreigegebeneRaetsel() {

		long anzahl = raetselDao.countRaetselWithStatus(true);
		AnzahlabfrageResponseDto result = new AnzahlabfrageResponseDto();
		result.setErgebnis(anzahl);

		return result;
	}

	/**
	 * @return
	 */
	String generateFilenameVorschau() {

		return UUID.randomUUID().toString().substring(0, 13) + Outputformat.PNG.getFilenameExtension();
	}

}
