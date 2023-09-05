package org.wtech.concatgrouper;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

public class ConcatGrouperMain {

	private static Logger logger = LoggerFactory.getLogger(ConcatGrouperMain.class);

	public static void main(String[] args) throws Exception {
		//  File file = new File("src/main/resources/input/test2.txt");

		if (args.length < 1) {
			System.err.println("Please provide the input file as a parameter.");
			return;
		}

		String inputFilePath = args[0];
		File inputFile = new File(inputFilePath);

		// Check if the input file exists
		if (!inputFile.exists() || inputFile.isDirectory()) {
			System.err.println("Provided input file doesn't exist or is a directory.");
			return;
		}

		// Set the output file name and path
		String outputFileName = inputFile.getName() + ".PROCESSED";
		File outputFile = new File("/BSCS_WORK/bscs01/prod/WORK/BETOUTPUT/CONCATENATED/backup", outputFileName);

		List<String> lines = FileUtils.readLines(inputFile, "ISO-8859-1");
		Grouper grouper = new Grouper();

		try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "ISO-8859-1"))) {

			for (String line : lines) {
				String processedLine = grouper.processLine(line);

				if (processedLine != null) {
					for (String outputLine : processedLine.split(System.lineSeparator())) {
						logger.debug("Writing line to output: " + outputLine);
						writer.write(outputLine);
						writer.newLine();
					}
				}
			}
		}
	}
}
