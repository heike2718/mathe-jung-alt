// =====================================================
// Project: latex-cli
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.latex_cli;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

/**
 * LaTeXDVICommand
 */
@Command(
	name = "jlatexdvi", mixinStandardHelpOptions = true, version = "jlatex 0.0.1",
	description = "compiles a given tex-File and produces dvi")
public class LaTeXDVICommand implements Callable<Integer> {

	@Parameters(index = "0", description = "The tex-file that is coing to be compiled")
	private File file;

	/**
	 * @param args
	 */
	public static void main(final String[] args) {

		int exitCode = new CommandLine(new LaTeXDVICommand()).execute(args);
		System.exit(exitCode);
	}

	@Override
	public Integer call() throws Exception {

		ProcessBuilder processBuilder = new ProcessBuilder();
		processBuilder.command("latex", file.getAbsolutePath());
		processBuilder.directory(file.getParentFile());

		try {

			Process process = processBuilder.start();

			BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));

			String line;

			while ((line = br.readLine()) != null) {

				System.out.println(line);
			}

			int exitCode = process.waitFor();

			System.out.println("\nExited with error code : " + exitCode);

		} catch (IOException e) {

			e.printStackTrace();
			return -1;
		} catch (InterruptedException e) {

			e.printStackTrace();
			return -2;
		}

		return 0;
	}

}
