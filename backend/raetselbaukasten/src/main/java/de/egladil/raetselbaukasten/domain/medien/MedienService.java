// =====================================================
// Project: raetselbaukasten
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.raetselbaukasten.domain.medien;

import de.egladil.raetselbaukasten.domain.auth.dto.MessagePayload;
import de.egladil.raetselbaukasten.domain.medien.dto.*;
import de.egladil.raetselbaukasten.domain.medien.impl.MedienPermissionDelegate;
import de.egladil.raetselbaukasten.domain.quellen.QuelleInfosAdapter;
import de.egladil.raetselbaukasten.domain.quellen.QuelleNameStrategie;
import de.egladil.raetselbaukasten.domain.raetsel.RaetselHerkunftTyp;
import de.egladil.raetselbaukasten.infrastructure.cdi.AuthenticationContext;
import de.egladil.raetselbaukasten.infrastructure.persistence.dao.MediumDao;
import de.egladil.raetselbaukasten.infrastructure.persistence.dao.QuellenRepository;
import de.egladil.raetselbaukasten.infrastructure.persistence.entities.PersistenteQuelleReadonly;
import de.egladil.raetselbaukasten.infrastructure.persistence.entities.PersistentesMedium;
import de.egladil.raetselbaukasten.infrastructure.persistence.entities.PersistentesRaetselMediensucheItemReadonly;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Inject
    QuellenRepository quellenRepository;

    /**
     * Holt das Medium mit dieser id.
     *
     * @param id
     * @return Optional
     */
    public Optional<MediumDto> getMediumWithId(final String id) {

        PersistentesMedium ausDB = mediumDao.findMediumById(id);

        permissionDelegate.checkReadPermission(ausDB);

        return ausDB == null ? Optional.empty() : Optional.of(mapFullFromDB(ausDB));

    }

    MediumDto mapFullFromDB(final PersistentesMedium ausDB) {

        MediumDto medium = new MediumDto().withAutor(ausDB.getAutor()).withId(ausDB.getUuid()).withKommentar(ausDB.getKommentar())
                .withMedienart(ausDB.getMedienart()).withTitel(ausDB.getTitel()).withUrl(ausDB.getUrl());

        try {

            permissionDelegate.checkWritePermission(ausDB);

            // yes!
            medium.markiereAlsAenderbar();
        } catch (WebApplicationException e) {

            // in diesem Fall keine Schreibberechtigung
        }

        if (authCtx.getUser().getName().equals(ausDB.getOwner())) {

            medium.setOwnMedium(true);
        }

        return medium;
    }

    MediumQuelleDto mapToMediumForQuelleFromDB(final PersistentesMedium ausDB) {

        MediumQuelleDto medium = new MediumQuelleDto().withId(ausDB.getUuid()).withMedienart(ausDB.getMedienart())
                .withTitel(ausDB.getTitel());

        return medium;
    }

    /**
     * Legt ein neues Medium an.
     *
     * @param medium MediumDto - die Daten
     * @return MediumDto
     */
    @Transactional
    public MediumDto mediumAnlegen(final MediumDto medium) {

        long anzahlDubletten = mediumDao.countMedienWithSameTitel(medium.getTitel(), medium.getId());

        if (anzahlDubletten > 0) {

            throw new WebApplicationException(
                    Response.status(409).entity(MessagePayload.error("Der Titel ist bereits vergeben.")).build());
        }

        int maxSortnr = mediumDao.getMaximumOfAllSortNumbers();

        String userId = authCtx.getUser().getUuid();

        PersistentesMedium persistentesMedium = PersistentesMedium.builder()
                .autor(medium.getAutor())
                .geaendertDurch(userId)
                .geaendertAm(new Date())
                .kommentar(medium.getKommentar())
                .medienart(medium.getMedienart())
                .owner(userId)
                .sortNumber(maxSortnr + 1)
                .titel(medium.getTitel())
                .url(medium.getUrl())
                .build();

        mediumDao.saveMedium(persistentesMedium);

        medium.withId(persistentesMedium.getUuid());

        return medium;
    }

    /**
     * Ändert ein vorhandenes Medium.
     *
     * @param medium MediumDto
     * @return MediumDto
     */
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

            throw new WebApplicationException(
                    Response.status(409).entity(MessagePayload.error("Der Titel ist bereits vergeben.")).build());
        }

        String userId = authCtx.getUser().getUuid();

        // owner darf nicht geändert werden!
        persistentesMedium.setAutor(medium.getAutor());
        persistentesMedium.setGeaendertDurch(userId);
        persistentesMedium.setGeaendertAm(new Date());
        persistentesMedium.setKommentar(medium.getKommentar());
        persistentesMedium.setMedienart(medium.getMedienart());
        persistentesMedium.setTitel(medium.getTitel());
        persistentesMedium.setUrl(medium.getUrl());

        mediumDao.saveMedium(persistentesMedium);

        return medium;
    }

    /**
     * Läd die durch limit und offset eingegrenzte Teilmenge aller Medien. Sortierung nach titel.
     *
     * @param limit
     * @param offset
     * @return MediensucheResult
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
     * Kleinschreibung den suchstring einthält. Sortiert wird nach titel. Admins bekommen alle Treffer, Autoren nur die
     * eigenen.
     *
     * @param suchstring
     * @param limit
     * @param offset
     * @return MediensucheResult
     */
    @SuppressWarnings("resource") // ist false positive, weil der Container die Response schließt.
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
     * Innerhalb aller Medien der gegebenen Medienart wird nach allen Einträgen gesucht, deren Titel unabhängig von
     * Groß- und Kleinschreibung den suchstring einthält. Sortiert wird nach titel.
     *
     * @param medienart Medienart
     * @return List
     */
    public List<MediumQuelleDto> findMedienForUseInQuelle(final Medienart medienart) {

        List<PersistentesMedium> trefferliste = mediumDao.findWithMedienart(medienart);
        LOGGER.debug("medienart={}, anzahl Treffer: {}", medienart, trefferliste.size());
        return trefferliste.stream().map(this::mapToMediumForQuelleFromDB).toList();
    }

    MediensucheTrefferItem mapToTrefferitemFromDB(final PersistentesMedium ausDB) {

        return new MediensucheTrefferItem().withId(ausDB.getUuid()).withKommentar(ausDB.getKommentar()).withMedienart(ausDB.getMedienart())
                .withTitel(ausDB.getTitel());
    }

    /**
     * Gibt alle Raetsel zurück, die das gegebene Medium als Quelle referenzieren.
     *
     * @param mediumId String
     * @return List
     */
    public List<RaetselMediensucheTrefferItem> findRaetselWithMedium(final String mediumId) {

        List<PersistentesRaetselMediensucheItemReadonly> treffermenge = this.mediumDao.findAllRaetselWithMedium(mediumId);

        List<RaetselMediensucheTrefferItem> result = treffermenge.stream().map(this::mapToRaetselMediensucheTrefferItem)
                .collect(Collectors.toList());

        return result;
    }

    RaetselMediensucheTrefferItem mapToRaetselMediensucheTrefferItem(final PersistentesRaetselMediensucheItemReadonly ausDB) {

        QuelleNameStrategie quelleNameStrategie = QuelleNameStrategie.getStrategie(ausDB.getMedienart());

        String quellenangabe = quelleNameStrategie.getText(new QuelleInfosAdapter().adapt(ausDB));

        if (RaetselHerkunftTyp.ADAPTION == ausDB.getHerkunft()) {

            Optional<PersistenteQuelleReadonly> optQuelle = quellenRepository.findQuelleWithUserId(ausDB.getRaetselOwner());

            if (optQuelle.isPresent()) {

                PersistenteQuelleReadonly quelle = optQuelle.get();
                quellenangabe = quelle.getPerson() + " (basierend auf einer Idee aus " + quellenangabe + ")";
            }
        }

        return new RaetselMediensucheTrefferItem().withFreigegeben(ausDB.isFreigegeben()).withHerkunftstyp(ausDB.getHerkunft())
                .withId(ausDB.getUuid()).withName(ausDB.getName()).withPfad(ausDB.getPfad()).withQuellenangabe(quellenangabe)
                .withSchluessel(ausDB.getSchluessel());
    }
}
