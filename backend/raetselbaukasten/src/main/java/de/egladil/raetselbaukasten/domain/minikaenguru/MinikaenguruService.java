// =====================================================
// Project: raetselbaukasten
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.raetselbaukasten.domain.minikaenguru;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.egladil.raetselbaukasten.domain.aufgabensammlungen.AufgabensammlungenService;
import de.egladil.raetselbaukasten.domain.aufgabensammlungen.Referenztyp;
import de.egladil.raetselbaukasten.domain.aufgabensammlungen.Schwierigkeitsgrad;
import de.egladil.raetselbaukasten.domain.aufgabensammlungen.dto.AufgabensammlungSucheTrefferItem;
import de.egladil.raetselbaukasten.domain.auth.dto.MessagePayload;
import de.egladil.raetselbaukasten.domain.quiz.dto.Quizaufgabe;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

/**
 * MinikaenguruService
 */
@ApplicationScoped
public class MinikaenguruService {

	private static final Logger LOGGER = LoggerFactory.getLogger(MinikaenguruService.class);

	@Inject
	AufgabensammlungenService aufgabensammlungenervice;

	/**
	 * Gibt die Aufgaben der Klassenstufe eines noch nicht freigegebenen Wettbewerbs zurück.
	 *
	 * @param  jahr
	 * @param  schwierigkeitsgrad
	 * @return                         MinikaenguruAufgabenKlassenstufeDto
	 * @throws WebApplicationException
	 *                                 wenn der schwierigkeitsgrad nicht korrekt ist, der statusWettbewerb nicht korrekt ist oder
	 *                                 bei anderen Exceptions.
	 */
	public MinikaenguruAufgabenKlassenstufeDto getAufgabenNichtFreigegebenerWettbewerb(final String jahr, final Schwierigkeitsgrad schwierigkeitsgrad) throws WebApplicationException {

		checkSchwierigkeitsgrad(schwierigkeitsgrad);

		AufgabensammlungSucheTrefferItem aufgabensammlung = aufgabensammlungenervice
			.findAufgabensammlungByUniqueKey(Referenztyp.MINIKAENGURU, jahr, schwierigkeitsgrad);

		if (aufgabensammlung == null) {

			MessagePayload messagePayload = MessagePayload
				.error("Es gibt keine Minikänguru-Aufgaben mit jahr und schwierigkeitsgrad.");
			Response response = Response.status(404).entity(messagePayload).build();
			throw new WebApplicationException(response);
		}

		return loadAufgaben(jahr, aufgabensammlung);
	}

	MinikaenguruAufgabe mapToMinikaenguruAufgabe(final Quizaufgabe quizaufgabe) {

		return new MinikaenguruAufgabe().withImages(quizaufgabe.getImages())
			.withLoesungsbuchstabe(quizaufgabe.getLoesungsbuchstabe())
			.withNummer(quizaufgabe.getNummer())
			.withPunkte(quizaufgabe.getPunkte() / 100)
			.withQuelle(quizaufgabe.getQuelle());

	}

	/**
	 * Gibt die Aufgaben der Klassenstufe eines bereits freigegebenen Wettbewerbs zurück.
	 *
	 * @param  jahr
	 * @param  schwierigkeitsgrad
	 * @return                         MinikaenguruAufgabenKlassenstufeDto
	 * @throws WebApplicationException
	 *                                 wenn irgendwas ist (schwoerigkeitsgrad falsch, kein Wettbewerb zum angegebenen Jahr, noch
	 *                                 nicht freigegeben oder andere Dinge
	 */
	public MinikaenguruAufgabenKlassenstufeDto getAufgabenFreigegebenerWettbewerb(final String jahr, final Schwierigkeitsgrad schwierigkeitsgrad) throws WebApplicationException {

		checkSchwierigkeitsgrad(schwierigkeitsgrad);

		AufgabensammlungSucheTrefferItem aufgabensammlung = aufgabensammlungenervice
			.findAufgabensammlungByUniqueKey(Referenztyp.MINIKAENGURU, jahr, schwierigkeitsgrad);

		if (aufgabensammlung == null || !aufgabensammlung.isFreigegeben()) {

			LOGGER.warn("Zugriffsversuch auf Aufgaben eines nicht freigegebenen Minikaenguru-Wettbewerbs: {}, {}", jahr,
				schwierigkeitsgrad);

			Response response = Response.status(404).build();
			throw new WebApplicationException(response);
		}

		return loadAufgaben(jahr, aufgabensammlung);
	}

	/**
	 * @param schwierigkeitsgrad
	 */
	private void checkSchwierigkeitsgrad(final Schwierigkeitsgrad schwierigkeitsgrad) {

		if (!schwierigkeitsgrad.isValidForMinikaenguruResources()) {

			MessagePayload messagePayload = MessagePayload.error("Es gibt keine Aufgaben für den angefragten Schwierigkeitsgrad");
			Response response = Response.status(400).entity(messagePayload).build();
			throw new WebApplicationException(response);

		}
	}

	/**
	 * @param  jahr
	 * @param  aufgabensammlung
	 * @return
	 */
	private MinikaenguruAufgabenKlassenstufeDto loadAufgaben(final String jahr, final AufgabensammlungSucheTrefferItem aufgabensammlung) {

		List<Quizaufgabe> elemente = aufgabensammlungenervice.loadElementeAsQuizzaufgaben(aufgabensammlung.getId());
		List<MinikaenguruAufgabe> aufgaben = elemente.stream().map(this::mapToMinikaenguruAufgabe).toList();

		MinikaenguruAufgabenKlassenstufeDto result = new MinikaenguruAufgabenKlassenstufeDto();
		result.setAufgaben(aufgaben);
		result.setKlassenstufe(aufgabensammlung.getSchwierigkeitsgrad().getLabel());
		result.setWettbewerbsjahr(jahr);
		return result;
	}

}
