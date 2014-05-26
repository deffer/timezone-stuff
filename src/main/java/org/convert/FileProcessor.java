package org.convert;

import org.convert.services.GeoLocationException;
import org.convert.services.GeoLocationInfo;
import org.convert.services.GeoLocationService;
import org.convert.services.GeoNamesWeb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Writer;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * author: Irina Benediktovich - http://plus.google.com/+IrinaBenediktovich
 */
public class FileProcessor {
	public static final String SYS_SEPARATOR = System.getProperty("line.separator");
	public static final int ERRORS_THRESHOLD  = 6;

	static DateFormat DF_FROM = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	static DateFormat DF_OUT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	String csvSeparator;
	GeoLocationService service = new GeoNamesWeb(); // could use spring injection here, but we are asked for small app.

	public FileProcessor(String csvSeparator){
		this.csvSeparator = csvSeparator;
		DF_FROM.setTimeZone(TimeZone.getTimeZone("UTC"));
	}


	/**
	 * Iterate over lines, append location and date and write into provided writer.
	 * Interrupt after hitting 6 errors.
	 *
	 * @param input
	 * @param output
	 * @return number of lines successfully converted. returning -1 if we had to stop due to many (6) errors.
	 * @throws IOException
	 */
	public int doJob(BufferedReader input, Writer output ) throws IOException{

		// read/write sequentially since input file could be too big to fit in the memory.
		// if we are sure file is small, then we could use apache IOUtils.readLines().
		String currentLine = input.readLine();
		int lineNumber = 0;
		int errors = 0;
		while(currentLine != null && errors < ERRORS_THRESHOLD){

			String writeLine = convertLine(currentLine, lineNumber);
			if (writeLine == null){
				errors++;
			}else{
				output.write(writeLine);
			}

			currentLine = input.readLine();
			lineNumber ++;
		}

		if (currentLine != null && errors >= ERRORS_THRESHOLD){
			System.out.println("Too many errors. Exiting....");
		}
		return lineNumber - errors;
	}

	/**
	 *
	 * @param currentLine
	 * @param currentLineNumber
	 * @return null in case of error (error gets logged immediately)
	 */
	String convertLine(String currentLine, int currentLineNumber){
		String result = null;

		String[] columns = currentLine.split(csvSeparator);
		if (columns.length != 3){
			String message = "Error: expected to find 3 columns on line " + currentLineNumber + " but found " + columns.length;
			if (columns.length == 1)
				message += ".\nTry setting different separator (second argument). Current separator is '"+csvSeparator+"'";
			System.out.println(message);
		}else{

			try {
				// TODO make sure columns are of expected type (date and doubles)
				GeoLocationInfo info = service.getGeoLocationInfo(columns[1], columns[2]);  // <--------------------
				DF_OUT.setTimeZone(TimeZone.getTimeZone(info.timeZoneId));
				String writeline = currentLine + csvSeparator +
						info.timeZoneId + csvSeparator +
						DF_OUT.format(DF_FROM.parse(columns[0]));
				result = writeline + SYS_SEPARATOR;
			} catch (GeoLocationException e) {
				String message = "Error on line "+currentLineNumber+":";
				if (e.getCause() == null){
					message += e.getMessage();
				}else{
					message += "Unable to retrieve timezone information: " + e.getMessage()+": "+e.getCause().getMessage();
				}
				System.out.println(message);
			} catch (ParseException e) {
				System.out.println("Error on line "+currentLineNumber+": Unable to parse date");
			}
		}
		return result;
	}
}
