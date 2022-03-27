// =====================================================
// Project: latex-client
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.latex_client;

/**
 * LaTeXCommand
 */
public enum LaTeXCommand {

	PDF("/latex2pdf") {

		@Override
		public String getShellScript() {

			return "/bin/latex2pdf.sh";
		}

		@Override
		public String getDescription() {

			return "transformiert ein .tex-File nach pdf. Das File und die erforderlichen Ressourcen müssen im gemounteten Verzeichnis /doc liegen.";
		}

	},
	PNG("/latex2png") {
		@Override
		public String getShellScript() {

			return "/bin/latex2png.sh";
		}

		@Override
		public String getDescription() {

			return "transformiert ein .tex-File nach png. Das File und die erforderlichen Ressourcen müssen im gemounteten Verzeichnis /doc liegen.";
		}
	};

	private final String relativePath;

	/**
	 * @param relativePath
	 */
	private LaTeXCommand(final String relativePath) {

		this.relativePath = relativePath;
	}

	public static LaTeXCommand fromRelativePath(final String path) {

		for (LaTeXCommand cmd : LaTeXCommand.values()) {

			if (cmd.relativePath.equals(path)) {

				return cmd;
			}
		}

		return null;

	}

	public abstract String getShellScript();

	public abstract String getDescription();

	public String getRelativePath() {

		return relativePath;
	}

	// private String getEnvDoc() {
	//
	// System.getenv("PATH_DOC")
	// }

}
