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

			// Denkfehler: diese Anwendung läuft im gleichen Prozess, also kann das script innerhalb des docker-containers
			// ausgeführt werden.
			return "/home/heike/git/mathe-jung-alt/backend/latex-client/src/main/bin/call-docker-pdf.sh";
		}

	},
	PNG("/latex2png") {
		@Override
		public String getShellScript() {

			// Denkfehler: diese Anwendung läuft im gleichen Prozess, also kann das script innerhalb des docker-containers
			// ausgeführt werden.
			return "/home/heike/git/mathe-jung-alt/backend/latex-client/src/main/bin/call-docker-png.sh";
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

}
