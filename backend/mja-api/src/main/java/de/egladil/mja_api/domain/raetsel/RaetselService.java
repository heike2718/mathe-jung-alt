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
import java.util.stream.Collectors;

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
import de.egladil.mja_api.domain.quellen.QuelleInfosAdapter;
import de.egladil.mja_api.domain.quellen.QuelleNameStrategie;
import de.egladil.mja_api.domain.quellen.QuellenService;
import de.egladil.mja_api.domain.quellen.Quellenart;
import de.egladil.mja_api.domain.quellen.dto.QuelleDto;
import de.egladil.mja_api.domain.raetsel.dto.AufgabensammlungRaetselsucheTrefferItem;
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
import de.egladil.mja_api.infrastructure.persistence.entities.PersistentesAufgabensammlungRaetselsucheItemReadonly;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistentesRaetsel;
import de.egladil.mja_api.infrastructure.persistence.entities.PersistentesRaetselHistorieItem;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

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

		// TODO semantische Validierung Herkunftstyp - Quellenart!!! EIGENKREATION nur mit PERSON, ADAPTION und ZITAT brauchen
		// MEDIUM oder PERSON.

		String raetselId = doInsertRaetsel(payload);
		Raetsel result = this.getRaetselZuId(raetselId);

		LOGGER.info("Raetsel angelegt: [raetsel.schluessel={}, admin={}]", result.getSchluessel(),
			StringUtils.abbreviate(authCtx.getUser().getName(), 11));

		return result;
	}

	@Transactional
	String doInsertRaetsel(final EditRaetselPayload payload) {

		if (schluesselGenerieren(payload)) {

			String schluessel = generiereSchluessel();

			LOGGER.info("raetsel.SCHLUESSEL={}", schluessel);

			payload.setSchluessel(schluessel);

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

		mergeWithPayload(neuesRaetsel, payload, userId);

		neuesRaetsel.owner = userId;
		neuesRaetsel.geaendertDurch = userId;
		neuesRaetsel.filenameVorschauFrage = generateFilenameVorschau();
		neuesRaetsel.filenameVorschauLoesung = generateFilenameVorschau();

		if (RaetselHerkunftTyp.EIGENKREATION != payload.getHerkunftstyp()) {

			payload.getQuelle().setId("neu");
			PersistenteQuelle neueQuelle = quellenService.quelleAnlegenOderAendern(neuesRaetsel.herkunft, payload.getQuelle());
			String quelleId = neueQuelle.uuid;
			neuesRaetsel.quelle = quelleId;
		} else {

			QuelleDto quelleAutor = quellenService.findOrCreateQuelleAutor();
			neuesRaetsel.quelle = quelleAutor.getId();
		}

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

		Benutzerart benutzerart = authCtx.getUser().getBenutzerart();

		if (Benutzerart.ADMIN != benutzerart) {

			LOGGER.debug("SCHLUESSEL muss generiert werden, da Benutzerart={}", benutzerart);

			return true;
		}

		return StringUtils.isBlank(payload.getSchluessel());
	}

	/**
	 * Ändert ein vorhandenes Raetsel
	 *
	 * @param  payload
	 *                 EditRaetselPayload die Daten und Metainformationen
	 * @return         RaetselPayloadDaten mit einer generierten UUID.
	 */
	public Raetsel raetselAendern(final EditRaetselPayload payload) {

		QuelleDto quelle = payload.getQuelle();

		if (quelle.getQuellenart() != Quellenart.PERSON && quelle.getMediumUuid() == null) {

			Response response = Response.status(Status.BAD_REQUEST).entity(MessagePayload.error("mediumUuid ist erforderlich"))
				.build();
			throw new WebApplicationException(response);
		}

		String raetselId = payload.getId();
		doUpdateRaetsel(payload);

		LOGGER.info("Raetsel geaendert: [raetsel.schluessel={}, raetsel.uuid={}, admin={}]", payload.getSchluessel(), raetselId,
			StringUtils.abbreviate(authCtx.getUser().getName(), 11));

		return getRaetselZuId(raetselId);
	}

	@Transactional
	void doUpdateRaetsel(final EditRaetselPayload payload) {

		// Raetsel raetsel = payload.getRaetsel();
		String raetselId = payload.getId();
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
			.withFrageNeu(payload.getFrage()).withLoesungAlt(persistentesRaetsel.loesung)
			.withLoesungNeu(payload.getLoesung());

		if (persistentesRaetsel.filenameVorschauFrage == null) {

			persistentesRaetsel.filenameVorschauFrage = generateFilenameVorschau();
		}

		if (persistentesRaetsel.filenameVorschauLoesung == null) {

			persistentesRaetsel.filenameVorschauLoesung = generateFilenameVorschau();

		}

		String idQuelleZumLoeschen = null;

		if (persistentesRaetsel.herkunft == RaetselHerkunftTyp.EIGENKREATION
			&& payload.getHerkunftstyp() != RaetselHerkunftTyp.EIGENKREATION) {

			LOGGER.debug("muss neue Quelle anlegen");
			payload.getQuelle().setId("neu");
			PersistenteQuelle neueQuelle = quellenService.quelleAnlegenOderAendern(payload.getHerkunftstyp(), payload.getQuelle());
			persistentesRaetsel.quelle = neueQuelle.uuid;
		}

		if (persistentesRaetsel.herkunft != RaetselHerkunftTyp.EIGENKREATION
			&& payload.getHerkunftstyp() == RaetselHerkunftTyp.EIGENKREATION) {

			LOGGER.debug("muss Quelle mit ID=" + persistentesRaetsel.quelle + " löschen und Autor-Quelle zuordnen");
			idQuelleZumLoeschen = payload.getQuelle().getId();
			QuelleDto autor = quellenService.findOrCreateQuelleAutor();
			persistentesRaetsel.quelle = autor.getId();

		}

		if (persistentesRaetsel.herkunft != RaetselHerkunftTyp.EIGENKREATION
			&& payload.getHerkunftstyp() != RaetselHerkunftTyp.EIGENKREATION) {

			LOGGER.debug("muss vorhandene Quelle überschreiben - also payload.quelle.id ignorieren");
			payload.getQuelle().setId(persistentesRaetsel.quelle);
			quellenService.quelleAnlegenOderAendern(payload.getHerkunftstyp(), payload.getQuelle());
		}

		mergeWithPayload(persistentesRaetsel, payload, userId);

		raetselDao.save(persistentesRaetsel);

		if (idQuelleZumLoeschen != null) {

			this.quellenService.quelleLoeschen(idQuelleZumLoeschen);
		}

		// Nur löschen, wenn persist klar ging!
		deleteImagesFileService.checkAndDeleteUnusedFiles(fragenLoesungenVo);
	}

	boolean schluesselExists(final EditRaetselPayload payload) {

		PersistentesRaetsel persistentesRaetsel = raetselDao.findWithSchluessel(payload.getSchluessel());

		if (persistentesRaetsel == null) {

			return false;
		}

		return !persistentesRaetsel.uuid.equals(payload.getId());
	}

	/**
	 * Holt die Details des Rätsels zu der gegebenen id. Falls das Rätsel existiert, wird der Schreibschutz anhand der Permissions
	 * für den User aufgehoben.
	 *
	 * @param  id
	 * @return    Raetsel oder null.
	 */
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

		Optional<QuelleDto> optQuelle = quellenService.getQuelleWithId(raetsel.quelle);

		if (optQuelle.isEmpty()) {

			LOGGER.error("Datenfehler: Rätsel mit SCHLUESSEL={} - keinen Eintrag in QUELLEN mit UUID={} gefunden.",
				raetsel.schluessel, raetsel.quelle);
			throw new MjaRuntimeException("Datenfehler: es gibt keinen Eintrag in QUELLEN mit uuid=" + raetsel.quelle);
		}

		QuelleDto theQuelle = optQuelle.get();

		String quellenangabe = this.getQuellenangabe(theQuelle.getId(), raetsel);

		if (quellenangabe == null) {

			LOGGER.error("Datenfehler: Rätsel mit SCHLUESSEL={} - keinen Eintrag in VW_QUELLEN mit UUID={} gefunden.",
				raetsel.schluessel, raetsel.quelle);
			throw new MjaRuntimeException("Datenfehler: es gibt keinen Eintrag in QUELLEN mit uuid=" + raetsel.quelle);
		}

		result.setQuelle(theQuelle);
		result.setQuellenangabe(quellenangabe);

		try {

			permissionDelegate.checkWritePermission(raetsel);
			result.setSchreibgeschuetzt(false);

		} catch (WebApplicationException e) {

			result.setSchreibgeschuetzt(true);
		}

		return result;
	}

	String getQuellenangabe(final String quelleId, final PersistentesRaetsel raetsel) {

		PersistenteQuelleReadonly ausDB = this.quellenRepository.findQuelleReadonlyById(quelleId);

		if (ausDB == null) {

			return null;
		}

		QuelleNameStrategie nameStrategie = QuelleNameStrategie.getStrategie(ausDB.quellenart);
		String text = nameStrategie.getText(new QuelleInfosAdapter().adapt(ausDB));

		if (raetsel.herkunft == RaetselHerkunftTyp.ADAPTION) {

			Optional<PersistenteQuelleReadonly> optQuelle = quellenRepository.findQuelleWithUserId(raetsel.owner);

			if (optQuelle.isPresent()) {

				PersistenteQuelleReadonly quelle = optQuelle.get();
				text = quelle.person + " (basierend auf einer Idee aus " + text + ")";
			}

		}

		return text;
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

	void mergeWithPayload(final PersistentesRaetsel persistentesRaetsel, final EditRaetselPayload payload, final String userId) {

		persistentesRaetsel.antwortvorschlaege = AntwortvorschlaegeMapper.antwortvorschlaegeAsJSON(payload.getAntwortvorschlaege());
		persistentesRaetsel.deskriptoren = deskriptorenService.sortAndStringifyIdsDeskriptoren(payload.getDeskriptoren());
		persistentesRaetsel.frage = payload.getFrage();
		persistentesRaetsel.geaendertDurch = userId;
		persistentesRaetsel.kommentar = payload.getKommentar();
		persistentesRaetsel.loesung = payload.getLoesung();
		persistentesRaetsel.schluessel = payload.getSchluessel();
		persistentesRaetsel.name = payload.getName();
		persistentesRaetsel.freigegeben = payload.isFreigegeben();
		persistentesRaetsel.herkunft = payload.getHerkunftstyp();
		persistentesRaetsel.owner = persistentesRaetsel.isPersistent() ? persistentesRaetsel.owner : userId;
		persistentesRaetsel.antwortvorschlaegeEingebettet = payload.isAntwortvorschlaegeEingebettet();
	}

	Raetsel mapFromDB(final PersistentesRaetsel raetselDB) {

		Raetsel result = new Raetsel(raetselDB.uuid)
			.withAntwortvorschlaege(AntwortvorschlaegeMapper.deserializeAntwortvorschlaege(raetselDB.antwortvorschlaege))
			.withDeskriptoren(deskriptorenService.mapToDeskriptoren(raetselDB.deskriptoren))
			.withFrage(raetselDB.frage)
			.withKommentar(raetselDB.kommentar)
			.withLoesung(raetselDB.loesung)
			.withSchluessel(raetselDB.schluessel)
			.withFreigegeben(raetselDB.freigegeben)
			.withAntwortvorschlaegeEingebettet(raetselDB.antwortvorschlaegeEingebettet)
			.withHerkunftstyp(raetselDB.herkunft)
			.withName(raetselDB.name)
			.withFilenameVorschauFrage(raetselDB.filenameVorschauFrage)
			.withFilenameVorschauLoesung(raetselDB.filenameVorschauLoesung);

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

	/**
	 * Sucht alle Aufgabensammlungen, die das gegebene Rätsel enthalten.
	 *
	 * @param  raetselId
	 *                   String
	 * @return           List
	 */
	public List<AufgabensammlungRaetselsucheTrefferItem> findAufgabensammlungenWithRaetsel(final String raetselId) {

		List<PersistentesAufgabensammlungRaetselsucheItemReadonly> trefferliste = this.raetselDao
			.findAllAufgabensammlungenWithRaetsel(raetselId);

		return trefferliste.stream()
			.map(this::mapToAufgabensammlungRaetselsucheTrefferItem)
			.collect(Collectors.toList());
	}

	AufgabensammlungRaetselsucheTrefferItem mapToAufgabensammlungRaetselsucheTrefferItem(final PersistentesAufgabensammlungRaetselsucheItemReadonly ausDB) {

		return new AufgabensammlungRaetselsucheTrefferItem().withFreigegeben(ausDB.sammlungFreigegeben)
			.withId(ausDB.sammlungId)
			.withName(ausDB.sammlungName)
			.withNummer(ausDB.elementNummer)
			.withPrivat(ausDB.sammlungPrivat)
			.withPunkte(ausDB.elementPunkte)
			.withSchwierigkeitsgrad(ausDB.schwierigkeitsgrad)
			.withOwner(StringUtils.abbreviate(ausDB.sammlungOwner, 11));
	}

}
