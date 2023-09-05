package org.wtech.concatgrouper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wtech.concatgrouper.appender.LineWriterController;
import org.wtech.concatgrouper.model.SC100;
import org.wtech.concatgrouper.repository.DataFetcher;

public class Grouper {

	private final Map<String, long[]> groupedLines = new HashMap<>();
	private static Logger logger = LoggerFactory.getLogger(Grouper.class);
	private int lineCount = 1;  

	private boolean isAppendable;
	private String appendableAccount;

	private DataFetcher dbService= new DataFetcher();

	
	public String processLine(String line) {

		String identifier = line.substring(0, 5);
		switch (identifier) {

		case "DC100":
			handleDC100(line);
			break;
		case "DC200":
			handleD200(line);
			break;
		default:
			break;
		}

		if (line.startsWith("DI100") && !line.startsWith("DI100SUBTOTAL")) {

			if (lineCount <= 3) {  
				lineCount++;
				return line;
			}

			String key = line.substring(5, 58); 
			long[] values = {
					Long.parseLong(line.substring(59, 70).trim()),
					Long.parseLong(line.substring(83, 94).trim())
			};

			// Update the grouped values
			long[] existingValues = groupedLines.getOrDefault(key, new long[]{0L, 0L});
			existingValues[0] += values[0];
			existingValues[1] += values[1];
			groupedLines.put(key, existingValues);

			// Return null to indicate that the line doesn't need to be written immediately.
			return null;

		} else if (line.startsWith("DI100SUBTOTAL")) {
			StringBuilder output = new StringBuilder(); 

			for (Map.Entry<String, long[]> entry : groupedLines.entrySet()) {
				long[] groupedValues = entry.getValue();
				String formattedValue1 = String.format("%012d", groupedValues[0]);
				String formattedValue2 = String.format("%011d", groupedValues[1]);

				// Construct the output line using the lastKeyLine and just replacing the values
				String newLine = "DI100" 
						+ entry.getKey() 
						+ line.substring(59, 70).replace(line.substring(59, 70).trim(), formattedValue1)
						+ line.substring(70, 83)
						+ line.substring(83, 94).replace(line.substring(83, 94).trim(), formattedValue2)
						+ line.substring(94);
				while (newLine.length() < 260) {
					newLine += " ";
				}
				output.append(newLine).append(System.lineSeparator());
			}

			// Clear the map for the next set
			groupedLines.clear();

			// Reset lineWriteControl
			lineCount = 1;

			return output.toString()+line;

		} 
		else if (line.startsWith("DS100")) {
			String outputLine = adjustLineLength(line,260);
			return outputLine;

		} else if (line.startsWith("SC150") &&isAppendable) {
			String outputLineSC100 =adjustLineLength(addSC100Records().toString(),261) ;
			String outputLineSC150 =adjustLineLength(addSC150Records().toString(),261) ;
			String outputLine = outputLineSC100.trim();
			if(null != outputLine && !outputLine.isEmpty()) {
				String outputLine2 = outputLineSC150.trim();
				outputLine=adjustLineLength(outputLine,260);
				logger.debug("outputLine: "+outputLine);
				logger.debug("outputline2: "+outputLine2);
				return outputLine+="\n"+adjustLineLength(outputLine2,260);
			} 
			return line;
		}
		else {
			return line;
		}
	}
	//

	private void handleDC100(String line) {
		appendableAccount = line.substring(7, 15); 	
	}

	private void handleD200(String line) {
		String valueSegment = line.substring(135, 147); 

		int value;
		try {
			value = Integer.parseInt(valueSegment.trim());
		} catch (NumberFormatException e) {
			value = 0;
		}

		if (value > 0) {
			this.isAppendable = true;
		}

	}

	private String addSC100Records() {
		List<SC100> sc100Records = retrieveSC100RecordsFromDatabase(appendableAccount);
		LineWriterController lineWriterSC100 = new LineWriterController();

		for (SC100 record : sc100Records) {
			lineWriterSC100.writeLine(record.toString());
		}
		return lineWriterSC100.getContent();
	}

	private List<SC100> retrieveSC100RecordsFromDatabase(String account) {
		List<SC100> sc100rec = new ArrayList<>();
		try {
			sc100rec = dbService.fetchSC100(account);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sc100rec;
	}


	private String addSC150Records() {

		String sc150Record = retrieveSC150RecordsFromDatabase(appendableAccount);
		LineWriterController lineWriterSC150 = new LineWriterController();

		lineWriterSC150.writeLine(sc150Record);

		return lineWriterSC150.getContent();

	}

	private String retrieveSC150RecordsFromDatabase(String appendableAccount) {

		try {
			return dbService.fetchSC150(appendableAccount).toString();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}



	private static String adjustLineLength(String line, int desiredLength) {
		if (line.length() > desiredLength) {
			// Truncate if it exceeds the desired length.
			return line.substring(0, desiredLength);
		} else {
			// Pad with spaces if it's shorter.
			while (line.length() < desiredLength) {
				line += " ";
			}
			return line+"";
		}
	}
}
