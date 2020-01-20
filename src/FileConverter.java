import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;

import au.com.bytecode.opencsv.CSVReader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import org.apache.commons.io.*;

import org.mozilla.universalchardet.UniversalDetector;

/**
 * Used to convert given files to tables <br>
 * Made so adding additional file type support is through creating a new method and adding a new switch case
 * @author Ryan Hoang
 *
 */
public class FileConverter {

	private boolean fileRead = false;

	File file;
	
	/**
	 * Constructor for class <br>
	 * Checks for file readability upon initial creation of object
	 * @param path The path to a file assumed files can be obtained locally
	 */
	public FileConverter (String path) {
		file = new File(path);
		if(file.canRead()) {
			fileRead = true;
			System.out.println("File read");

		}
		else {
			fileRead = false;
			System.err.println("File could not be read");
		}
	}

	/**
	 * Main method used to convert files to corresponding tables 
	 * @return The table obtained from file if unable to obtain a table returns null
	 * @throws IOException
	 */
	public Table<Integer, String, String> toTable() throws IOException{

		String fileExtension = FilenameUtils.getExtension(file.getName());

		if(fileExtension==null||fileExtension=="")
			return null;

		Table<Integer, String, String> table = TreeBasedTable.create();

		//Sends to corresponding method based on file extension
		switch (fileExtension) {
		case "html":
			System.out.println("html file detected");
			table = htmlToTable();
			break;
		case "csv":
			System.out.println("csv file detected");
			table = csvToTable();
			break;
		}

		return table;
	}

	/**
	 * Creates a table using instantiated file <br>
	 * Assumes html file will contain only one table <br>
	 * Languages are assumed supported by UTF-8
	 * @return Table corresponding to file or null if no table found
	 * @throws IOException 
	 */
	private Table<Integer, String, String> htmlToTable() throws IOException {
		String encoding = UniversalDetector.detectCharset(file);
		
		Document doc = Jsoup.parse(file, encoding); //Detects the character set used in the document
		
		System.out.println(doc);

		System.out.println("\nExtracting Table...");
		Elements tableElements = doc.select("table");
		Table<Integer, String, String> table = TreeBasedTable.create();
		Elements tableRows = tableElements.select("tr");
		Elements tableHeader = tableRows.select("th");

		//Inserts headers to table and uses them as key
		for (int i=0; i < tableHeader.size();i++) {
			table.put(-1, tableHeader.get(i).text(), tableHeader.get(i).text());
		}

		//inserts html table elements into table
		for (int row =0; row < tableRows.size(); row++) {
			Elements currentRow = tableRows.get(row).select("td");
			for(int col = 0; col < currentRow.size(); col++) {
				table.put(row-1, tableHeader.get(col).text(), currentRow.get(col).text());
			}
		}

		System.out.println();
		return table;
	}
	/**
	 * Converts a csv file to a table <br>
	 * Assumed only one table per csv file <br>
	 * Languages are assumed supported by UTF-8
	 * @return table in csv file or null if no table was found
	 * @throws CsvValidationException
	 * @throws IOException
	 */
	private Table<Integer, String, String> csvToTable() throws IOException {
		if(fileRead=false) {
			System.out.println("File could not be read");
			return null;
		}
		
		System.out.println(file.getAbsolutePath());
		
		FileReader reader = new FileReader (file.getAbsoluteFile(), StandardCharsets.UTF_8); //Set to UTF-8 to accept more languages
		CSVReader csvReader = new CSVReader (reader);
		Table<Integer, String, String> table = TreeBasedTable.create();
		String [] row;

		String [] headers = csvReader.readNext();

		//Inserting headers into table
		for (int i = 0; i<headers.length;i++){
			table.put(-1, headers[i], headers[i]);
		}

		//Inserting rest of elements into table
		for(int i=0; ((row = csvReader.readNext()) != null); i++) {
			for (int j=0; j<row.length;j++) {
				System.out.print(row [j] + "\t");
				table.put(i, headers[j], row[j]);
			}
			System.out.println();
		}	

		csvReader.close();

		return table;
	}
	/**
	 * Used to determine if file can be read
	 * @return true if file was readable else false
	 */
	public boolean fileReadable() {
		return fileRead;
	}
}
