import com.google.common.collect.*;
import java.nio.file.Path;
public class RecordMerger {

	public static final String FILENAME_COMBINED = "combined.csv";

	/**
	 * Entry point of this test.
	 *
	 * @param args command line arguments: first.html and second.csv.
	 * @throws Exception bad things had happened.
	 */
	public static void main(final String[] args) throws Exception {
	
	//test method
	/*
	public static void main(final String[] targs) throws Exception {
		String [] args = {"file1", "[", "file2", "]"};
	*/	
		if (args.length == 0) {
			System.err.println("Usage: java RecordMerger file1 [ file2 [...] ]");
			System.exit(1);
		}
		// your code starts here.

		Tables[] tables = new Tables[args.length];

		//structure of input is assumed equivalent to error message therefore files will only appear every other argument
		for (int i = 0; i<args.length; i+=2) {
			if(args[i].equals("]")) {
				System.out.println("Last file detected beginning conversion\n");
				break;
			}
			System.out.println("Converting File: " + args[i]);
			
			Path path = new Path(args[i]);
			FileConverter convertFile = new FileConverter (args[i]);
			
		}
	}

	//removing the brackets from the arguments
	//Files are assumed to have normal filenames meaning 
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
}
