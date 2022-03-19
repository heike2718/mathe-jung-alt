// =====================================================
// Project: latex-cli
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.latex_cli;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

/**
 * LaTeXPpmCommand
 */
@Command(
	name = "jppm2png", mixinStandardHelpOptions = true, version = "jppm2png 0.0.1",
	description = "crops a ppm file to png")
public class Ppm2PngCommand implements Callable<Integer> {

	@Parameters(index = "0", description = "The tex-file that is going to be compiled and transformed")
	private File file;

	/**
	 * @param args
	 */
	public static void main(final String[] args) {

		int exitCode = new CommandLine(new Ppm2PngCommand()).execute(args);
		System.exit(exitCode);
	}

	@Override
	public Integer call() throws Exception {

		int result = ps2Ppm();

		return result;
	}

	private int ps2Ppm() {

		List<String> cmdList = new ArrayList<String>();
		cmdList.add("sh");
		cmdList.add(file.getParent() + File.separator + "ppm2png.sh");
		cmdList.add(getFileNameWithoutExtension());

		ProcessBuilder processBuilder = new ProcessBuilder();
		processBuilder.command(cmdList);
		processBuilder.directory(file.getParentFile());

		try {

			Process process = processBuilder.start();

			BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));

			String line;

			while ((line = br.readLine()) != null) {

				System.out.println(line);
			}

			int exitCode = process.waitFor();

			System.out.println("\nExited with code : " + exitCode);

			return 0;

		} catch (IOException e) {

			e.printStackTrace();
			return -1;
		} catch (InterruptedException e) {

			e.printStackTrace();
			return -2;
		}

	}

	private String getFileNameWithoutExtension() {

		String name = file.getName();
		String fileNameWithoutExtension = name.substring(0, name.length() - 4);
		System.out.println(fileNameWithoutExtension);
		return fileNameWithoutExtension;

	}

}
