import java.io.File;
import java.io.IOException;

import com.google.common.collect.Table;
import com.google.common.collect.HashBasedTable;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
	public Table<Integer, Integer, String> toTable() throws IOException {
		String fileType = getFileExtension(file.getName());
		Table<Integer, Integer, String> table = HashBasedTable.create();
		switch (fileType) {
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
			System.exit(1);
			return "";
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
		Table<Integer, Integer, String> table = HashBasedTable.create();
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
	
	private Table<Integer, Integer, String> csvToTable() {
		// TODO Auto-generated method stub
		return null;
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
