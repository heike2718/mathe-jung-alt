// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.minikaenguru;

import java.util.List;

import de.egladil.mja_api.domain.aufgabensammlungen.AufgabensammlungenService;
import de.egladil.mja_api.domain.aufgabensammlungen.Referenztyp;
import de.egladil.mja_api.domain.aufgabensammlungen.Schwierigkeitsgrad;
import de.egladil.mja_api.domain.aufgabensammlungen.dto.AufgabensammlungSucheTrefferItem;
import de.egladil.mja_api.domain.auth.dto.MessagePayload;
import de.egladil.mja_api.domain.quiz.dto.Quizaufgabe;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

/**
 * MinikaenguruService
 */
@ApplicationScoped
public class MinikaenguruService {

	@Inject
	AufgabensammlungenService aufgabensammlungenervice;

	/**
	 * Gibt die Aufgaben der Klassenstufe eines noch nicht freigegebenen Wettbewerbs zurück.
	 *
	 * @param  jahr
	 * @param  schwierigkeitsgrad
	 * @param  statusWettbewerb
	 * @return                         MinikaenguruAufgabenDto
	 * @throws WebApplicationException
	 *                                 wenn der schwierigkeitsgrad nicht korrekt ist, der statusWettbewerb nicht korrekt ist oder
	 *                                 bei anderen Exceptions.
	 */
	public MinikaenguruAufgabenDto getAufgabenNichtFreigegebenerWettbewerb(final String jahr, final Schwierigkeitsgrad schwierigkeitsgrad, final StatusWettbewerb statusWettbewerb) throws WebApplicationException {

		checkSchwierigkeitsgrad(schwierigkeitsgrad);

		if (!statusWettbewerb.isAllowedForRestrictedAccessToWettbewerb()) {

			MessagePayload messagePayload = MessagePayload
				.error("Der Wettbewerb hat noch nicht den erlaubten Status, um seine Aufgaben herauszugeben.");
			Response response = Response.status(400).entity(messagePayload).build();
			throw new WebApplicationException(response);
		}

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
	 * @return                         MinikaenguruAufgabenDto
	 * @throws WebApplicationException
	 *                                 wenn irgendwas ist (schwoerigkeitsgrad falsch, kein Wettbewerb zum angegebenen Jahr, noch
	 *                                 nicht freigegeben oder andere Dinge
	 */
	public MinikaenguruAufgabenDto getAufgabenFreigegebenerWettbewerb(final String jahr, final Schwierigkeitsgrad schwierigkeitsgrad) throws WebApplicationException {

		checkSchwierigkeitsgrad(schwierigkeitsgrad);

		AufgabensammlungSucheTrefferItem aufgabensammlung = aufgabensammlungenervice
			.findAufgabensammlungByUniqueKey(Referenztyp.MINIKAENGURU, jahr, schwierigkeitsgrad);

		if (aufgabensammlung == null || !aufgabensammlung.isFreigegeben()) {

			MessagePayload messagePayload = MessagePayload
				.error("Es gibt keine Minikänguru-Aufgaben mit jahr und schwierigkeitsgrad.");
			Response response = Response.status(404).entity(messagePayload).build();
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
	private MinikaenguruAufgabenDto loadAufgaben(final String jahr, final AufgabensammlungSucheTrefferItem aufgabensammlung) {

		List<Quizaufgabe> elemente = aufgabensammlungenervice.loadElementeAsQuizzaufgaben(aufgabensammlung.getId());
		List<MinikaenguruAufgabe> aufgaben = elemente.stream().map(this::mapToMinikaenguruAufgabe).toList();

		MinikaenguruAufgabenDto result = new MinikaenguruAufgabenDto();
		result.setAufgaben(aufgaben);
		result.setKlassenstufe(aufgabensammlung.getSchwierigkeitsgrad().getLabel());
		result.setWettbewerbsjahr(jahr);
		return result;
	}

}
