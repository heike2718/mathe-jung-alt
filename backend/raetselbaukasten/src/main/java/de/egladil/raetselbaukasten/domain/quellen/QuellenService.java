// =====================================================
// Project: raetselbaukasten
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.raetselbaukasten.domain.quellen;

import java.util.Optional;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.commons.lang3.StringUtils;

import de.egladil.raetselbaukasten.domain.auth.session.AuthenticatedUser;
import de.egladil.raetselbaukasten.domain.exceptions.MjaRuntimeException;
import de.egladil.raetselbaukasten.domain.quellen.dto.QuelleDto;
import de.egladil.raetselbaukasten.domain.raetsel.RaetselHerkunftTyp;
import de.egladil.raetselbaukasten.domain.semantik.DomainService;
import de.egladil.raetselbaukasten.infrastructure.cdi.AuthenticationContext;
import de.egladil.raetselbaukasten.infrastructure.persistence.dao.QuellenRepository;
import de.egladil.raetselbaukasten.infrastructure.persistence.entities.PersistenteQuelle;
import de.egladil.raetselbaukasten.infrastructure.persistence.entities.PersistenteQuelleReadonly;

/**
 * QuellenService
 */
@DomainService
@ApplicationScoped
public class QuellenService {

    private static final Logger LOGGER = LoggerFactory.getLogger(QuellenService.class);

    @Inject
    AuthenticationContext authCtx;

    @Inject
    QuellenRepository quellenRepository;

    public QuelleDto findOrCreateQuelleAutor() {

        AuthenticatedUser user = authCtx.getUser();
        String userId = user.getUuid();

        Optional<QuelleDto> optQuelle = this.findQuelleAutor();

        if (optQuelle.isPresent()) {

            return optQuelle.get();
        }
        QuelleDto datenQuelle = new QuelleDto();
        datenQuelle.setPerson(user.getFullName());
        datenQuelle.setQuellenart(Quellenart.PERSON);
        datenQuelle.setId("neu");

        PersistenteQuelle neueQuelleDB = this.quelleAnlegenOderAendern(RaetselHerkunftTyp.EIGENKREATION, datenQuelle);

        LOGGER.info("Quelle für AUTOR {} angelegt: UUID={}", user.getFullName(), neueQuelleDB);

        optQuelle = this.getQuelleWithId(neueQuelleDB.getUuid());

        if (optQuelle.isEmpty()) {

            String message = "Da ist etwas fürchterlich schiefgegangen beim Anlegen einer Quelle für den gegebenen Autor "
                    + StringUtils.abbreviate(userId, 11);
            LOGGER.error(message);
            throw new MjaRuntimeException(message);
        }

        return optQuelle.get();
    }

    Optional<QuelleDto> findQuelleAutor() {

        AuthenticatedUser user = authCtx.getUser();
        String userId = user.getUuid();
        Optional<PersistenteQuelleReadonly> optAusDB = this.quellenRepository.findQuelleWithUserId(userId);

        if (optAusDB.isEmpty()) {

            return Optional.empty();
        }

        return Optional.of(mapFromDB(optAusDB.get()));

    }

    /**
     * Gibt die Quelle mit der gegebenen UUID zurück.
     *
     * @param id String
     * @return Optional
     */
    public Optional<QuelleDto> getQuelleWithId(final String id) {

        PersistenteQuelleReadonly ausDB = this.quellenRepository.findQuelleReadonlyById(id);

        return ausDB == null ? Optional.empty() : Optional.of(mapFromDB(ausDB));
    }

    QuelleDto mapFromDB(final PersistenteQuelleReadonly persistenteQuelle) {

        QuelleDto quelle = new QuelleDto();
        quelle.setAusgabe(persistenteQuelle.getAusgabe());
        quelle.setId(persistenteQuelle.getUuid());
        quelle.setJahr(persistenteQuelle.getJahr());
        quelle.setKlasse(persistenteQuelle.getKlasse());
        quelle.setMediumUuid(persistenteQuelle.getMediumUuid());
        quelle.setPerson(persistenteQuelle.getPerson());
        quelle.setQuellenart(persistenteQuelle.getQuellenart());
        quelle.setSeite(persistenteQuelle.getSeite());
        quelle.setStufe(persistenteQuelle.getStufe());
        quelle.setPfad(persistenteQuelle.getPfad());
        return quelle;
    }

    public PersistenteQuelle quelleAnlegenOderAendern(final RaetselHerkunftTyp herkunftTyp,
            final QuelleDto datenQuelle) {

        Quelle quelle = createQuelle(herkunftTyp, datenQuelle);

        if ("neu".equals(quelle.getId())) {

            return quelleAnlegen(quelle);

        }

        return quelleAendern(quelle);
    }

    @Transactional
    public void quelleLoeschen(final String quelleId) {

        PersistenteQuelle quelle = this.quellenRepository.findQuelleEntityWithId(quelleId);

        if (quelle != null) {

            this.quellenRepository.deleteQuelle(quelle);

            LOGGER.debug("quelle gelöscht: UUID={}", quelleId);
        }
    }

    /**
     * @param quelle
     * @return
     */
    @Transactional
    PersistenteQuelle quelleAnlegen(final Quelle quelle) {

        int maxSornr = quellenRepository.getMaximumOfAllSortNumbers();

        String userId = authCtx.getUser().getUuid();
        QuelleDto datenQuelle = quelle.getDatenQuelle();

        PersistenteQuelle quelleEntity = PersistenteQuelle
                .builder()
                .sortNumber(maxSornr + 1)
                .owner(userId)
                .ausgabe(datenQuelle.getAusgabe())
                .geaendertDurch(userId)
                .jahr(datenQuelle.getJahr())
                .klasse(datenQuelle.getKlasse())
                .mediumID(datenQuelle.getMediumUuid())
                .person(datenQuelle.getPerson())
                .quellenart(datenQuelle.getQuellenart())
                .seite(datenQuelle.getSeite())
                .pfad(datenQuelle.getPfad())
                .userId(quelle.getUserId())
                .build();

        PersistenteQuelle persisted = quellenRepository.save(quelleEntity);

        LOGGER.debug("quelle angelegt: {}", quelle.getDatenQuelle().toString());

        return persisted;
    }

    /**
     * @param quelle
     * @return
     */
    @Transactional
    PersistenteQuelle quelleAendern(final Quelle quelle) {

        PersistenteQuelle quelleEntity = quellenRepository.findQuelleEntityWithId(quelle.getId());

        if (quelleEntity == null) {

            LOGGER
                    .error("keine QUELLE mit uuid={} vorhanden. Das darf nur bei neuen Rätseln (id='neu') der Fall sein. Da stimmt beim Laden der Details eines Rätsels etwas nicht oder beim Mappen der Herkunft auf die Quelle im Frontend!");
            throw new MjaRuntimeException("Inonsistente Daten Rätsel-Quelle");
        }

        String userId = authCtx.getUser().getUuid();
        QuelleDto datenQuelle = quelle.getDatenQuelle();

        quelleEntity.setAusgabe(datenQuelle.getAusgabe());
        quelleEntity.setGeaendertDurch(userId);
        quelleEntity.setJahr(datenQuelle.getJahr());
        quelleEntity.setKlasse(datenQuelle.getKlasse());
        quelleEntity.setMediumID(datenQuelle.getMediumUuid());
        quelleEntity.setPerson(datenQuelle.getPerson());
        quelleEntity.setQuellenart(datenQuelle.getQuellenart());
        quelleEntity.setSeite(datenQuelle.getSeite());
        quelleEntity.setPfad(datenQuelle.getPfad());

        PersistenteQuelle persisted = quellenRepository.save(quelleEntity);

        LOGGER.debug("quelle geandert: {}", quelle.getDatenQuelle().toString());

        return persisted;
    }

    private Quelle createQuelle(final RaetselHerkunftTyp herkunftstyp, final QuelleDto datenQuelle) {

        String theUserId = authCtx.getUser().getUuid();

        Quelle quelle = new Quelle(datenQuelle.getId()).withDatenQuelle(datenQuelle);

        if ("neu".equals(quelle.getId()) && Quellenart.PERSON == datenQuelle.getQuellenart()
                && RaetselHerkunftTyp.EIGENKREATION == herkunftstyp) {

            // Dann ist die userId klar. In anderen Fällen handelt es sich um eine von ein
            // von einer anderen Person erfundenes
            // Rätsel, das der Admin für diese Person einträgt. Dann benötigt die Quelle
            // keine userId.
            quelle.setUserId(theUserId);
        }

        return quelle;
    }
}
