import java.util.ArrayList;

import com.google.common.collect.*;
public class RecordMerger {

	public static final String FILENAME_COMBINED = "combined.csv";

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
		//Table<Integer, Integer, String>[] tables = new Table[formula];

		ArrayList <Table<Integer, Integer, String>> tables = new ArrayList<Table<Integer,Integer, String>>();		
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
			tables.add(convertFile.toTable());
			//testTable(tables.get(i/2)); 
		}
		System.out.println("Beginning table tests");
		for(int i = 0; i<tables.size();i++) {
			System.out.println("Testing table: "+ i);
			testTable(tables.get(i));
		}
	}

	//removing the brackets from the arguments
	//Files are assumed to have normal filenames
	public static String[] removeBrackets(String[] args) {
		System.out.println("Removing Brackets");
		System.out.println("Length of Args: "+ args.length);

		for(int i =0; i<args.length;i++) {
			System.out.println(args[i]);
			if (args[i].equals("[") || args[i].equals("]")) {
				System.out.println("Bracket Detected");
				args=removeBracket(args, i);
			}
		}

		return args;
	}
	private static String[] removeBracket(String [] arg, int index) {
		String[] newArg = new String[arg.length-1];
		for (int i = 0; i<newArg.length; i++)
		{
			if(i>=index)
				newArg[i]=arg[i+1];
			else
				newArg[i]=arg[i];

		}
		System.out.println("new array:");
		for (int i=0; i<newArg.length; i++) {
			System.out.print(" "+ newArg[i]);
		}
		return newArg;
	}
	
	/**
	 * Outputs contents of table into command line
	 * @param a table with Integer axis and String contents
	 */
	private static void testTable(Table<Integer, Integer, String> table) {
		if(table == null) {
			System.out.println("Table is empty");
			return;
		}
		for(int row=0;row<4;row++) {
			if(table.row(row).isEmpty())
				break;
			System.out.println("Row Number: "+ row + "\t" + table.row(row));
		}
	}
}
