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
	name = "jlatexps", mixinStandardHelpOptions = true, version = "jlatexps 0.0.1",
	description = "compiles a given tex-File and produces ps")
public class LaTeXPSCommand implements Callable<Integer> {

	@Parameters(index = "0", description = "The tex-file that is going to be compiled and transformed")
	private File file;

	/**
	 * @param args
	 */
	public static void main(final String[] args) {

		int exitCode = new CommandLine(new LaTeXPSCommand()).execute(args);
		System.exit(exitCode);
	}

	@Override
	public Integer call() throws Exception {

		int result = latexDvi();

		if (result != 0) {

			return result;

		}

		result = dviPs();

		return result;
	}

	private int latexDvi() {

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

			return 0;

		} catch (IOException e) {

			e.printStackTrace();
			return -1;
		} catch (InterruptedException e) {

			e.printStackTrace();
			return -2;
		}

	}

	private int dviPs() {

		String pathToDviFile = getPathToDviFile();

		ProcessBuilder processBuilder = new ProcessBuilder();
		processBuilder.command("dvips", new File(pathToDviFile).getAbsolutePath());
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

			return 0;

		} catch (IOException e) {

			e.printStackTrace();
			return -1;
		} catch (InterruptedException e) {

			e.printStackTrace();
			return -2;
		}

	}

	private String getPathToDviFile() {

		File dir = file.getParentFile();

		String name = file.getName();
		String fileNameWithoutExtension = name.substring(0, name.length() - 4);

		return dir.getAbsolutePath() + File.separator + fileNameWithoutExtension + ".dvi";

	}

}
