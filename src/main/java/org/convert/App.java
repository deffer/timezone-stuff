package org.convert;

import java.io.*;


/**
 *
 * Expecting two arguments:
 *   - input file name. should have read access
 *   - [OPTIONAL] csv column separator. default is space
 *
 * Output:
 *   file with name <inputfile>.out. Should have write access.
 *
 *
 * Using System.out to report warnings and errors, but could use slf4j easily.
 */
public class App {
	public static String CSV_COLUMN_SEPARATOR = " ";

    public static void main( String[] args ){
	    if (args.length == 0 || args[0].trim().length()==0 ){
		    System.err.println("Expected one argument: file name");
		    System.exit(0);
	    }

	    if (args.length>1){
		    if (args[1].length() == 1)
		        CSV_COLUMN_SEPARATOR = args[1];
		    else
			    System.out.println("Second argument was ignored. Expected single character - CSV column separator. Using default - SPACE");
	    }

	    String filename = args[0];
	    String newFileName = filename+".out";
	    try {
		    BufferedReader inputFile = new BufferedReader(new FileReader(filename));
		    BufferedWriter bw = new BufferedWriter(new FileWriter(newFileName, false));
		    FileProcessor fp = new FileProcessor(CSV_COLUMN_SEPARATOR);

		    int linesRead = fp.doJob(inputFile, bw); // <------------------

		    // cleanup
		    try{
			    bw.close();
		        inputFile.close();
			    if (linesRead < 1){
				    new File(newFileName).delete();
			    }
		    }catch (Exception e) {
			    // dont really care
		    }

		    System.out.println("FINISH. Successfully processed "+linesRead+" line(s)");

	    } catch (FileNotFoundException e) {
		    System.err.println("Unable to open file: " + filename);
		    System.exit(0);
	    } catch (IOException e){
		    System.err.println("Unexpected error while reading/writing " + filename+"/"+newFileName);
		    System.exit(0);
	    }
	}

}
