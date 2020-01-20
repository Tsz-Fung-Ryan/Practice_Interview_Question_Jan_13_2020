import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;


import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;

import au.com.bytecode.opencsv.CSVReader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import org.apache.commons.io.*;



public class FileConverter {

	private boolean fileRead = false;

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

		//testTable(table);

		return table;
	}

	/**
	 * Creates a table using instantiated file
	 * Assumes html file will contain only one table
	 * @return table corresponding to file
	 * @throws IOException 
	 */
	private Table<Integer, String, String> htmlToTable() throws IOException {
		BufferedReader reader = new BufferedReader (new FileReader(file));
		
		Document doc = Jsoup.parse(file, "UTF-8");
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
	 * Converts a csv file to a table
	 * @return
	 * @throws CsvValidationException
	 * @throws IOException
	 */
	private Table<Integer, String, String> csvToTable() throws IOException {
		if(fileRead=false) {
			System.out.println("File could not be read");
			return null;
		}
		BufferedReader reader = new BufferedReader(new FileReader(file));
		CSVReader csvReader = new CSVReader (reader);
		Table<Integer, String, String> table = TreeBasedTable.create();
		String [] row;

		String [] headers = csvReader.readNext();

		//Inserting headers into table
		for (int i = 0; i<headers.length;i++){
			table.put(-1, headers[i], headers[i]);
		}

		//inserting rest of elements into table
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
	 * Used to determine if file can be read to save time
	 * @return If the File can be read or not
	 */
	public boolean fileReadable() {
		return fileRead;
	}
}
