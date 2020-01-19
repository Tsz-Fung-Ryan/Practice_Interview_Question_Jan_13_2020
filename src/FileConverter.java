import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.BufferedReader;

import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;

import au.com.bytecode.opencsv.CSVReader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

//switched to newest version as I considered using the skip lines function but ultimately didn't use it
/*import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;*/


public class FileConverter {

	boolean fileRead = false;

	File file;
	/**
	 * 
	 * @param The path to a file assumed files can be obtained locally
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

	//converts the initialized file to a table
	public Table<Integer, Integer, String> toTable() throws IOException{

		String fileExtension = getFileExtension(file.getName());

		if(fileExtension==null)
			return null;
		
		Table<Integer, Integer, String> table = TreeBasedTable.create();
		
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

		//testTable(table);

		return table;
	}

	//Used to check the file extension of the file to ensure proper conversion is used
	/**
	 * 
	 * @param the filename/path to the file
	 * @return returns the file extension
	 */
	public String getFileExtension(String fileName) {
		String extension = "";
		int index = fileName.lastIndexOf(".");
		if(index == -1) {
			System.err.println("File Extension Not Found");
			return null;
		}
		extension = fileName.substring(index+1);
		return extension;
	}


	/**
	 * Creates a table using instantiated file
	 * Assumes html file will contain only one table
	 * @return table corresponding to file
	 * @throws IOException 
	 */
	private Table<Integer, Integer, String> htmlToTable() throws IOException {
		Document doc = Jsoup.parse(file, "UTF-8");
		System.out.println(doc);

		System.out.println("\nExtracting Table...");
		Elements tableElements = doc.select("table");
		Table<Integer, Integer, String> table = TreeBasedTable.create();
		Elements tableRows = tableElements.select("tr");

		//Inserts html table elements into table
		for (int row = 0; row < tableRows.size(); row++) {
			//System.out.println("\nCurrent row is:"+ row);
			Elements currentRow = tableRows.get(row).select("th, td");
			for(int column=0;column < currentRow.size(); column++) {
				//System.out.print(currentRow.get(column).text() + "\t");
				table.put(row, column, currentRow.get(column).text());
			}
		}
		System.out.println();
		return table;
	}
	/**
	 * Converts
	 * @return
	 * @throws CsvValidationException
	 * @throws IOException
	 */
	private Table<Integer, Integer, String> csvToTable() throws IOException {
		if(fileRead=false) {
			System.out.println("File could not be read");
			return null;
		}
		BufferedReader reader = new BufferedReader(new FileReader(file));
		CSVReader csvReader = new CSVReader (reader);
		Table<Integer, Integer, String> table = TreeBasedTable.create();
		String [] row;
		
		for (int i=0;(row = csvReader.readNext()) !=null; i++) { 
			System.out.println("Row Number: "+ i);
			for (int j=0; j<row.length;j++) {
				System.out.print(row [j] + "\t");
				table.put(i, j, row[j]);
			}
			System.out.println();
		}

		csvReader.close();
		
		return table;
	}


	private void testTable(Table<Integer, Integer, String> table) {
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
