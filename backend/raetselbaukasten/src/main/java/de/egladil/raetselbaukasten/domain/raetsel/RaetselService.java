// =====================================================
// Project: raetselbaukasten
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.raetselbaukasten.domain.raetsel;

import java.util.*;
import java.util.stream.Collectors;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import de.egladil.raetselbaukasten.domain.auth.dto.MessagePayload;
import de.egladil.raetselbaukasten.domain.auth.session.Benutzerart;
import de.egladil.raetselbaukasten.domain.deskriptoren.DeskriptorenService;
import de.egladil.raetselbaukasten.domain.dto.AnzahlabfrageResponseDto;
import de.egladil.raetselbaukasten.domain.dto.SortDirection;
import de.egladil.raetselbaukasten.domain.dto.Suchfilter;
import de.egladil.raetselbaukasten.domain.dto.SuchfilterVariante;
import de.egladil.raetselbaukasten.domain.embeddable_images.dto.Textart;
import de.egladil.raetselbaukasten.domain.exceptions.MjaRuntimeException;
import de.egladil.raetselbaukasten.domain.generatoren.RaetselFileService;
import de.egladil.raetselbaukasten.domain.quellen.QuelleInfosAdapter;
import de.egladil.raetselbaukasten.domain.quellen.QuelleNameStrategie;
import de.egladil.raetselbaukasten.domain.quellen.QuellenService;
import de.egladil.raetselbaukasten.domain.quellen.Quellenart;
import de.egladil.raetselbaukasten.domain.quellen.dto.QuelleDto;
import de.egladil.raetselbaukasten.domain.raetsel.dto.*;
import de.egladil.raetselbaukasten.domain.raetsel.impl.DeleteUnusedEmbeddableImageFilesService;
import de.egladil.raetselbaukasten.domain.raetsel.impl.FindPathsGrafikParser;
import de.egladil.raetselbaukasten.domain.raetsel.impl.FragenUndLoesungenVO;
import de.egladil.raetselbaukasten.domain.raetsel.impl.RaetselPermissionDelegate;
import de.egladil.raetselbaukasten.domain.utils.VorschauUtils;
import de.egladil.raetselbaukasten.infrastructure.cdi.AuthenticationContext;
import de.egladil.raetselbaukasten.infrastructure.persistence.dao.QuellenRepository;
import de.egladil.raetselbaukasten.infrastructure.persistence.dao.RaetselDao;
import de.egladil.raetselbaukasten.infrastructure.persistence.entities.*;

/**
 * RaetselService
 */
@ApplicationScoped
public class RaetselService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RaetselService.class);

    @ConfigProperty(name = "vorschautext.length")
    int lengtVorschautext;

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
     * Sucht alle Rätsel, die zum Suchfilter passen und gibt sie der Permission
     * enstprechend zurück.
     *
     * @param suchfilter    Suchfilter
     * @param limit         int Anzahl Treffer in page
     * @param offset        int Aufsetzpunkt für page
     * @param sortDirection SortDirection nach schluessel
     * @return RaetselsucheTreffer
     */
    public RaetselsucheTreffer sucheRaetsel(final Suchfilter suchfilter, final int limit, final int offset,
            final SortDirection sortDirection) {

        SuchfilterVariante suchfilterVariante = suchfilter.suchfilterVariante();

        List<PersistentesRaetsel> trefferliste = new ArrayList<>();

        List<RaetselsucheTrefferItem> treffer = new ArrayList<>();
        long anzahlGesamt = 0L;

        boolean nurFreigegebene = permissionDelegate.isOnlyReadFreigegebene();

        switch (suchfilterVariante) {

        case COMPLETE -> anzahlGesamt = raetselDao.countRaetselWithFilter(suchfilter, nurFreigegebene);
        case DESKRIPTOREN -> anzahlGesamt = raetselDao
                .countWithDeskriptoren(suchfilter.getDeskriptorenIds(), suchfilter.getModusDeskriptoren(),
                        nurFreigegebene);
        case VOLLTEXT -> anzahlGesamt = raetselDao
                .countRaetselVolltext(suchfilter.getSuchstring(), suchfilter.getModusVolltext(), nurFreigegebene);
        default -> throw new IllegalArgumentException("unerwartete SuchfilterVariante " + suchfilterVariante);
        }

        if (anzahlGesamt == 0) {

            return new RaetselsucheTreffer();
        }

        switch (suchfilterVariante) {

        case COMPLETE ->
            trefferliste = raetselDao.findRaetselWithFilter(suchfilter, limit, offset, sortDirection, nurFreigegebene);
        case DESKRIPTOREN -> trefferliste = raetselDao
                .findWithDeskriptoren(suchfilter.getDeskriptorenIds(), suchfilter.getModusDeskriptoren(), limit, offset,
                        sortDirection, nurFreigegebene);
        case VOLLTEXT -> trefferliste = raetselDao
                .findRaetselVolltext(suchfilter.getSuchstring(), suchfilter.getModusVolltext(), limit, offset,
                        sortDirection, nurFreigegebene);

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
     * @param payload EditRaetselPayload die Daten und Metainformationen
     * @return RaetselPayloadDaten mit einer generierten UUID.
     */
    public Raetsel raetselAnlegen(final EditRaetselPayload payload) {

        // TODO semantische Validierung Herkunftstyp - Quellenart!!! EIGENKREATION nur
        // mit PERSON, ADAPTION und ZITAT
        // brauchen
        // MEDIUM oder PERSON.

        String raetselId = doInsertRaetsel(payload);
        Raetsel result = this.getRaetselZuId(raetselId);

        LOGGER
                .info("Raetsel angelegt: [raetsel.schluessel={}, admin={}]", result.getSchluessel(),
                        StringUtils.abbreviate(authCtx.getUser().getName(), 11));

        return result;
    }

    @Transactional
    String doInsertRaetsel(final EditRaetselPayload payload) {

        if (schluesselGenerieren(payload)) {

            String schluessel = generiereSchluessel();

            LOGGER.debug("raetsel.SCHLUESSEL={}", schluessel);

            payload.setSchluessel(schluessel);

        }

        boolean schluesselExistiert = this.schluesselExists(payload);

        if (schluesselExistiert) {

            throw new WebApplicationException(
                    Response.status(409).entity(MessagePayload.error("Der Schlüssel ist bereits vergeben.")).build());
        }

        PersistentesRaetsel neuesRaetsel = new PersistentesRaetsel();
        String userId = authCtx.getUser().getName();

        mergeWithPayload(neuesRaetsel, payload, userId);

        neuesRaetsel.setOwner(userId);
        neuesRaetsel.setGeaendertDurch(userId);
        neuesRaetsel.setGeaendertAm(new Date());
        neuesRaetsel.setFilenameVorschauFrage(generateFilenameVorschau());
        neuesRaetsel.setFilenameVorschauLoesung(generateFilenameVorschau());

        if (RaetselHerkunftTyp.EIGENKREATION != payload.getHerkunftstyp()) {

            payload.getQuelle().setId("neu");
            PersistenteQuelle neueQuelle = quellenService
                    .quelleAnlegenOderAendern(neuesRaetsel.getHerkunft(), payload.getQuelle());
            neuesRaetsel.setQuelle(neueQuelle.getUuid());
        } else {

            QuelleDto quelleAutor = quellenService.findOrCreateQuelleAutor();
            neuesRaetsel.setQuelle(quelleAutor.getId());
        }

        raetselDao.save(neuesRaetsel);

        return neuesRaetsel.getUuid();
    }

    /**
     * @return String
     */
    String generiereSchluessel() {

        int maxSchluessel = raetselDao.getMaximumOfAllSchluessel();
        String schluessel = StringUtils.leftPad(++maxSchluessel + "", 5, "0");
        return schluessel;
    }

    /**
     * @param payload EditRaetselPayload
     * @return boolean
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
     * Ändert ein vorhandenes Raetsel.
     *
     * @param payload EditRaetselPayload die Daten und Metainformationen
     * @return RaetselPayloadDaten mit einer generierten UUID.
     */
    public Raetsel raetselAendern(final EditRaetselPayload payload) {

        QuelleDto quelle = payload.getQuelle();

        if (quelle.getQuellenart() != Quellenart.PERSON && quelle.getMediumUuid() == null) {

            Response response = Response
                    .status(Status.BAD_REQUEST)
                    .entity(MessagePayload.error("mediumUuid ist erforderlich"))
                    .build();
            throw new WebApplicationException(response);
        }

        String raetselId = payload.getId();
        doUpdateRaetsel(payload);

        LOGGER
                .info("Raetsel geaendert: [raetsel.schluessel={}, raetsel.uuid={}, admin={}]", payload.getSchluessel(),
                        raetselId, StringUtils.abbreviate(authCtx.getUser().getName(), 11));

        return getRaetselZuId(raetselId);
    }

    @Transactional
    void doUpdateRaetsel(final EditRaetselPayload payload) {

        // Raetsel raetsel = payload.getRaetsel();
        String raetselId = payload.getId();
        PersistentesRaetsel persistentesRaetsel = raetselDao.findById(raetselId);
        String userId = authCtx.getUser().getName();

        if (persistentesRaetsel == null) {

            LOGGER
                    .error("Aendern raetsel mit UUID {}: raetsel existiert nicht. uuidAendernderUser={}", raetselId,
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

            PersistentesRaetselHistorieItem neuesHistorieItem = PersistentesRaetselHistorieItem
                    .builder()
                    .frage(persistentesRaetsel.getFrage())
                    .loesung(persistentesRaetsel.getLoesung())
                    .geaendertAm(new Date())
                    .geaendertDurch(userId)
                    .raetsel(persistentesRaetsel)
                    .build();

            raetselDao.insert(neuesHistorieItem);
        }

        FragenUndLoesungenVO fragenLoesungenVo = new FragenUndLoesungenVO()
                .withFrageAlt(persistentesRaetsel.getFrage())
                .withFrageNeu(payload.getFrage())
                .withLoesungAlt(persistentesRaetsel.getLoesung())
                .withLoesungNeu(payload.getLoesung());

        if (persistentesRaetsel.getFilenameVorschauFrage() == null) {

            persistentesRaetsel.setFilenameVorschauFrage(generateFilenameVorschau());
        }

        if (persistentesRaetsel.getFilenameVorschauLoesung() == null) {

            persistentesRaetsel.setFilenameVorschauLoesung(generateFilenameVorschau());

        }

        String idQuelleZumLoeschen = null;

        if (persistentesRaetsel.getHerkunft() == RaetselHerkunftTyp.EIGENKREATION
                && payload.getHerkunftstyp() != RaetselHerkunftTyp.EIGENKREATION) {

            LOGGER.debug("muss neue Quelle anlegen");
            payload.getQuelle().setId("neu");
            PersistenteQuelle neueQuelle = quellenService
                    .quelleAnlegenOderAendern(payload.getHerkunftstyp(), payload.getQuelle());
            persistentesRaetsel.setQuelle(neueQuelle.getUuid());
        }

        if (persistentesRaetsel.getHerkunft() != RaetselHerkunftTyp.EIGENKREATION
                && payload.getHerkunftstyp() == RaetselHerkunftTyp.EIGENKREATION) {

            LOGGER
                    .debug("muss Quelle mit ID=" + persistentesRaetsel.getQuelle()
                            + " löschen und Autor-Quelle zuordnen");
            idQuelleZumLoeschen = payload.getQuelle().getId();
            QuelleDto autor = quellenService.findOrCreateQuelleAutor();
            persistentesRaetsel.setQuelle(autor.getId());

        }

        if (persistentesRaetsel.getHerkunft() != RaetselHerkunftTyp.EIGENKREATION
                && payload.getHerkunftstyp() != RaetselHerkunftTyp.EIGENKREATION) {

            LOGGER.debug("muss vorhandene Quelle überschreiben - also payload.quelle.id ignorieren");
            payload.getQuelle().setId(persistentesRaetsel.getQuelle());
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

        return !persistentesRaetsel.getUuid().equals(payload.getId());
    }

    /**
     * Holt die Details des Rätsels zu der gegebenen id. Falls das Rätsel existiert,
     * wird der Schreibschutz anhand der Permissions für den User aufgehoben.
     *
     * @param id
     * @return Raetsel oder null.
     */
    public Raetsel getRaetselZuId(final String id) {

        PersistentesRaetsel raetsel = raetselDao.findById(id);

        if (raetsel == null) {

            return null;
        }

        if (raetsel.getFilenameVorschauFrage() == null || raetsel.getFilenameVorschauLoesung() == null) {

            LOGGER
                    .error("{}: Datenfehler Filenames für Vorschau wurden noch nicht generiert und persistiert! Sollte auf PROD seit 2.3.2 nicht mehr vorkommen :)",
                            raetsel.getSchluessel());
            throw new MjaRuntimeException("Filenames für Vorschau wurden noch nicht generiert und persistiert!");
        }

        Raetsel result = mapFromDB(raetsel);

        Pair<List<EmbeddableImageInfo>, List<EmbeddableImageInfo>> embedaableImageInfos = loadEmbeddableImageInfos(
                raetsel);
        result.addAllEmbeddableImageInfos(embedaableImageInfos.getLeft());
        result.addAllEmbeddableImageInfos(embedaableImageInfos.getRight());

        result
                .setImages(raetselFileService
                        .findImages(raetsel.getFilenameVorschauFrage(), raetsel.getFilenameVorschauLoesung()));

        Optional<QuelleDto> optQuelle = quellenService.getQuelleWithId(raetsel.getQuelle());

        if (optQuelle.isEmpty()) {

            LOGGER
                    .error("Datenfehler: Rätsel mit SCHLUESSEL={} - keinen Eintrag in QUELLEN mit UUID={} gefunden.",
                            raetsel.getSchluessel(), raetsel.getQuelle());
            throw new MjaRuntimeException(
                    "Datenfehler: es gibt keinen Eintrag in QUELLEN mit uuid=" + raetsel.getQuelle());
        }

        QuelleDto theQuelle = optQuelle.get();

        String quellenangabe = this.getQuellenangabe(theQuelle.getId(), raetsel);

        if (quellenangabe == null) {

            LOGGER
                    .error("Datenfehler: Rätsel mit SCHLUESSEL={} - keinen Eintrag in VW_QUELLEN mit UUID={} gefunden.",
                            raetsel.getSchluessel(), raetsel.getQuelle());
            throw new MjaRuntimeException(
                    "Datenfehler: es gibt keinen Eintrag in QUELLEN mit uuid=" + raetsel.getQuelle());
        }

        result.setQuelle(theQuelle);
        result.setQuellenangabe(quellenangabe);
        result.setSchreibgeschuetzt(permissionDelegate.isSchreibgeschuetztForUser(raetsel));

        return result;
    }

    String getQuellenangabe(final String quelleId, final PersistentesRaetsel raetsel) {

        PersistenteQuelleReadonly ausDB = this.quellenRepository.findQuelleReadonlyById(quelleId);

        if (ausDB == null) {

            return null;
        }

        QuelleNameStrategie nameStrategie = QuelleNameStrategie.getStrategie(ausDB.getQuellenart());
        String text = nameStrategie.getText(new QuelleInfosAdapter().adapt(ausDB));

        if (raetsel.getHerkunft() == RaetselHerkunftTyp.ADAPTION) {

            Optional<PersistenteQuelleReadonly> optQuelle = quellenRepository.findQuelleWithUserId(raetsel.getOwner());

            if (optQuelle.isPresent()) {

                PersistenteQuelleReadonly quelle = optQuelle.get();
                text = quelle.getPerson() + " (basierend auf einer Idee aus " + text + ")";
            }

        }

        return text;
    }

    /**
     * Parsed den Text von Frage und Lösung (sofern vorhanden) nach
     * includegraphics-Statements und erzeugt daraus Listen von
     * EmbeddableImageInfos.
     *
     * @param raetsel PersistentesRaetsel
     * @return Pair left = grafikInfosFrage, right = grafikInfosLoesung. Sie sind
     *         nie null, höchstens leer.
     */
    public Pair<List<EmbeddableImageInfo>, List<EmbeddableImageInfo>> loadEmbeddableImageInfos(
            final PersistentesRaetsel raetsel) {

        List<String> grafikLinksFrage = findPathsGrafikParser.findPaths(raetsel.getFrage());
        List<EmbeddableImageInfo> grafikInfosFrage = new ArrayList<>();
        List<EmbeddableImageInfo> grafikInfosLoesung = new ArrayList<>();

        if (!grafikLinksFrage.isEmpty()) {

            grafikInfosFrage = getGrafikInfos(grafikLinksFrage, Textart.FRAGE);
        }

        List<String> grafikLinksLoesung = findPathsGrafikParser.findPaths(raetsel.getLoesung());

        if (!grafikLinksLoesung.isEmpty()) {

            grafikInfosLoesung = getGrafikInfos(grafikLinksLoesung, Textart.LOESUNG);
        }

        return Pair.of(grafikInfosFrage, grafikInfosLoesung);
    }

    /**
     * @param schluessel String
     * @return Optional
     */
    public Images findImagesZuSchluessel(final String schluessel) {

        PersistentesRaetsel raetselDB = raetselDao.findWithSchluessel(schluessel);

        if (raetselDB == null) {

            return new Images();
        }

        return this.raetselFileService
                .findImages(raetselDB.getFilenameVorschauFrage(), raetselDB.getFilenameVorschauLoesung());
    }

    /**
     * Läd das, was als Input für ein LaTeX-File erforderlich ist.
     *
     * @param schluesselliste List eine Liste von Schlüsseln.
     * @return List
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

    void mergeWithPayload(final PersistentesRaetsel persistentesRaetsel, final EditRaetselPayload payload,
            final String userId) {

        persistentesRaetsel
                .setAntwortvorschlaege(
                        AntwortvorschlaegeMapper.antwortvorschlaegeAsJSON(payload.getAntwortvorschlaege()));
        persistentesRaetsel
                .setDeskriptoren(deskriptorenService.sortAndStringifyIdsDeskriptoren(payload.getDeskriptoren()));
        persistentesRaetsel.setFrage(payload.getFrage());
        persistentesRaetsel.setGeaendertDurch(userId);
        persistentesRaetsel.setKommentar(payload.getKommentar());
        persistentesRaetsel.setLoesung(payload.getLoesung());
        persistentesRaetsel.setSchluessel(payload.getSchluessel());
        persistentesRaetsel.setName(payload.getName());
        persistentesRaetsel.setFreigegeben(payload.isFreigegeben());
        persistentesRaetsel.setHerkunft(payload.getHerkunftstyp());
        persistentesRaetsel.setOwner(persistentesRaetsel.isPersistent() ? persistentesRaetsel.getOwner() : userId);
        persistentesRaetsel.setAntwortvorschlaegeEingebettet(payload.isAntwortvorschlaegeEingebettet());

        if (StringUtils.isNotBlank(payload.getAutorLoesung())) {

            persistentesRaetsel.setAutorLoesung(payload.getAutorLoesung());
        }
    }

    Raetsel mapFromDB(final PersistentesRaetsel raetselDB) {

        Raetsel result = new Raetsel(raetselDB.getUuid())
                .withAntwortvorschlaege(
                        AntwortvorschlaegeMapper.deserializeAntwortvorschlaege(raetselDB.getAntwortvorschlaege()))
                .withDeskriptoren(deskriptorenService.mapToDeskriptoren(raetselDB.getDeskriptoren()))
                .withFrage(raetselDB.getFrage())
                .withKommentar(raetselDB.getKommentar())
                .withLoesung(raetselDB.getLoesung())
                .withSchluessel(raetselDB.getSchluessel())
                .withFreigegeben(raetselDB.isFreigegeben())
                .withAntwortvorschlaegeEingebettet(raetselDB.isAntwortvorschlaegeEingebettet())
                .withHerkunftstyp(raetselDB.getHerkunft())
                .withName(raetselDB.getName())
                .withFilenameVorschauFrage(raetselDB.getFilenameVorschauFrage())
                .withFilenameVorschauLoesung(raetselDB.getFilenameVorschauLoesung())
                .withAutorLoesung(raetselDB.getAutorLoesung());

        return result;
    }

    RaetselsucheTrefferItem mapToSucheTrefferFromDB(final PersistentesRaetsel raetselDB) {

        RaetselsucheTrefferItem result = new RaetselsucheTrefferItem()
                .withDeskriptoren(deskriptorenService.mapToDeskriptoren(raetselDB.getDeskriptoren()))
                .withId(raetselDB.getUuid())
                .withName(raetselDB.getName())
                .withFreigegeben(raetselDB.isFreigegeben())
                .withKommentar(raetselDB.getKommentar())
                .withSchluessel(raetselDB.getSchluessel())
                .withHerkunft(raetselDB.getHerkunft())
                .withVorschautext(VorschauUtils.getVorschautext(raetselDB.getFrage(), lengtVorschautext));

        return result;
    }

    /**
     * @param schluessel
     * @return Optional
     */
    public Optional<String> getRaetselIdWithSchluessel(final String schluessel) {

        PersistentesRaetsel raetsel = raetselDao.findWithSchluessel(schluessel);

        return raetsel == null ? Optional.empty() : Optional.of(raetsel.getUuid());
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

    String generateFilenameVorschau() {

        return UUID.randomUUID().toString().substring(0, 13) + Outputformat.PNG.getFilenameExtension();
    }

    /**
     * Sucht alle Aufgabensammlungen, die das gegebene Rätsel enthalten.
     *
     * @param raetselId String
     * @return List
     */
    public List<AufgabensammlungRaetselsucheTrefferItem> findAufgabensammlungenWithRaetsel(final String raetselId) {

        List<PersistentesAufgabensammlungRaetselsucheItemReadonly> trefferliste = this.raetselDao
                .findAllAufgabensammlungenWithRaetsel(raetselId);

        Benutzerart benutzerart = authCtx.getUser().getBenutzerart();

        boolean nurFreigegebene = false;

        switch (benutzerart) {

        case STANDARD:
            nurFreigegebene = true;
            break;

        case ADMIN:
        case AUTOR:
            break;

        default:
            throw new IllegalArgumentException("nur Benutzer mit Konto erlaub. War " + benutzerart);
        }

        List<AufgabensammlungRaetselsucheTrefferItem> items = trefferliste
                .stream()
                .map(this::mapToAufgabensammlungRaetselsucheTrefferItem)
                .collect(Collectors.toList());

        if (nurFreigegebene) {

            return items.stream().filter(i -> i.isFreigegeben()).toList();
        }

        return items;
    }

    AufgabensammlungRaetselsucheTrefferItem mapToAufgabensammlungRaetselsucheTrefferItem(
            final PersistentesAufgabensammlungRaetselsucheItemReadonly ausDB) {

        return new AufgabensammlungRaetselsucheTrefferItem()
                .withFreigegeben(ausDB.isSammlungFreigegeben())
                .withId(ausDB.getSammlungId())
                .withName(ausDB.getSammlungName())
                .withNummer(ausDB.getElementNummer())
                .withPrivat(ausDB.isSammlungPrivat())
                .withPunkte(ausDB.getElementPunkte())
                .withSchwierigkeitsgrad(ausDB.getSchwierigkeitsgrad())
                .withOwner(StringUtils.abbreviate(ausDB.getSammlungOwner(), 11));
    }

}
