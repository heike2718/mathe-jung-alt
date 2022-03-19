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
	name = "jlatexppm", mixinStandardHelpOptions = true, version = "jlatexppm 0.0.1",
	description = "compiles a given tex-File and produces ps and then creates ppm")
public class LaTeXPpmCommand implements Callable<Integer> {

	/*
	 * printf ("gs -sDEVICE=ppmraw -r300 -q -dNOPAUSE -dBATCH -sOutputFile=./ppmdir/%s.ppm ./psdir/%s.ps \n",a[1],a[1]) > outdatei
	 * #printf
	 * ("pnmcrop ./ppmdir/%s.ppm | pnmscale 0.3 | pnmgamma 0.4 | pnmtopng -interlace -transparent white > ./pngdir/%s.png \n",a[1],a
	 * [1]) > outdatei
	 * printf ("pnmcrop ./ppmdir/%s.ppm | pnmscale 0.3 | pnmgamma 0.8 | pnmtopng > ./pngdir/%s.png \n",a[1],a[1]) > outdatei
	 * printf ("rm ./ppmdir/%s.ppm \n",a[1]) > outdatei
	 */

	@Parameters(index = "0", description = "The tex-file that is going to be compiled and transformed")
	private File file;

	/**
	 * @param args
	 */
	public static void main(final String[] args) {

		int exitCode = new CommandLine(new LaTeXPpmCommand()).execute(args);
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
		cmdList.add(file.getParent() + File.separator + "ps2ppm.sh");
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

	private String getFileNameWithoutExtension() {

		String name = file.getName();
		String fileNameWithoutExtension = name.substring(0, name.length() - 3);
		return fileNameWithoutExtension;

	}

}
