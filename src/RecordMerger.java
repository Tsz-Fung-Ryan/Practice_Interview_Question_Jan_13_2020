import java.util.ArrayList;
import com.google.common.collect.*;
public class RecordMerger {

	public static final String FILENAME_COMBINED = "combined.csv";
	public static final String UNIQUEIDENTIFIER = "ID";

	/**
	 * Entry point of this test.
	 *
	 * @param args command line arguments: first.html and second.csv.
	 * @throws Exception bad things had happened.
	 */
	public static void main(final String[] args) throws Exception {
		if (args.length == 0) {
			System.err.println("Usage: java RecordMerger file1 [ file2 [...] ]");
			System.exit(1);
		}
		// your code starts here.
		final int formula = (args.length+2)/3;

		ArrayList<Table<Integer, String, String>> tables = new ArrayList<Table<Integer, String, String>>();		
		System.out.println("Number of Files: " + formula);

		//Structure of input is assumed equivalent to error message therefore files will only appear every other argument
		for (int i = 0; i<args.length; i+=2) {
			if(args[i].equals("]")) {
				System.out.println("Last file detected proceeding to testing\n");
				break;
			}
			System.out.println("\nConverting File: " + args[i]);

			//converts the file into a table and adds it to tables
			FileConverter convertFile = new FileConverter (args[i]);
			if(convertFile.fileReadable()==false)
				continue;
			tables.add(convertFile.toTable());
		}
		
		System.out.println("Beginning table tests");
		
		//Outputs tables in array to commandline 
		for(int i = 0; i<tables.size();i++) {
			System.out.println("Testing table: "+ i);
			TableTools.testTable(tables.get(i));
		}
		
		//Merges all tables in ArrayList and outputs resultant table in commandline
		Table<Integer, String, String> mergedTable = TableTools.mergeAndSortTable(tables);
		System.out.println("Testing merged table");
		TableTools.testTable(mergedTable);
		
		System.out.println("Writing to csv file");
		
		//Writes the table to a csv file under provided FILENAME_COMBINED constant
		TableTools.writeToCsv(mergedTable, FILENAME_COMBINED);
		
		System.out.println("File Written");
		
		System.exit(0);
	}
}
