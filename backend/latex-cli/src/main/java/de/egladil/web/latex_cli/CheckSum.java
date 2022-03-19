// =====================================================
// Project: latex-cli
// (c) Heike Winkelvoß
// =====================================================
package de.egladil.web.latex_cli;

import java.io.File;
import java.math.BigInteger;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.util.concurrent.Callable;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

/**
 * CheckSum
 */
@Command(
	name = "checksum", mixinStandardHelpOptions = true, version = "checksum 4.0",
	description = "Prints the checksum (SHA-256 by default) of a file to STDOUT.")
public class CheckSum implements Callable<Integer> {

	@Parameters(index = "0", description = "The file whose checksum to calculate.")
	private File file;

	@Option(names = { "-a", "--algorithm" }, description = "MD5, SHA-1, SHA-256, ...")
	private String algorithm = "SHA-256";

	/**
	 * @param args
	 */
	public static void main(final String[] args) {

		int exitCode = new CommandLine(new CheckSum()).execute(args);
		System.exit(exitCode);
	}

	@Override
	public Integer call() throws Exception {

		byte[] fileContents = Files.readAllBytes(file.toPath());
		byte[] digest = MessageDigest.getInstance(algorithm).digest(fileContents);
		System.out.printf("%0" + (digest.length * 2) + "x%n", new BigInteger(1, digest));
		return 0;
	}
}
