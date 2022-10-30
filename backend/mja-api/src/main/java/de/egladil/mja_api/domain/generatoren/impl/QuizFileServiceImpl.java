// =====================================================
// Project: mja-api
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.mja_api.domain.generatoren.impl;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * QuizFileServiceImpl
 */
@ApplicationScoped
public class QuizFileServiceImpl {

	@Inject
	QuizitemLaTeXGenerator quizitemGenerator;

}
