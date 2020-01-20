import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;

import au.com.bytecode.opencsv.CSVWriter;

import com.google.common.collect.Table.Cell;



/**
 * Object meant for all custom tools involving tables <br>
 * Would have extended Google's Tables object but it's set to final
 * @author Ryan Hoang
 *
 */
public class TableTools {
	public static final String UNIQUEIDENTIFIER = "ID";
	public static final int HEADERROW =-1;
	
	/**
	 * Outputs contents of table into command line <br>
	 * If there was no table provided method will indicate {@literal "}Table is empty{@literal "}
	 * @param table a table with {@literal <}Integer, String, String{@literal >} tuples the table used for this assignment
	 */
	public static void testTable(Table<Integer, String, String> table) {
		
		//checks to see if table is empty or null
		if(table == null||table.isEmpty()) {
			System.out.println("Table is empty");
			return;
		}

		int currentRow=HEADERROW-1; //Used to properly space out the table in command line

		 //Outputs the table in command line
		for(Cell<Integer, String, String> cell: table.cellSet()) {
			if(cell.getRowKey()>currentRow) {
				currentRow = cell.getRowKey();
				System.out.println();
				System.out.print(currentRow + "\t");
			}
			System.out.print(cell.getValue() + "\t\t");
		}
		System.out.println();

	}
	

	/**
	 * Does an outer join of all tables in array by ID <br>
	 * Fills empty cells with an empty character to simplify printing
	 * @param tables an ArrayList of tables with {@literal <}Integer, String, String{@literal >} tuples
	 * @return a single table of the outer joins of all tables and empties filled with empty characters
	 */
	public static Table<Integer, String, String> mergeAndSortTable(ArrayList<Table<Integer, String, String>> tables) {
		Table <Integer, String, String> newTable = TreeBasedTable.create();
		
		
		//Merges all tables and sorts them by ID assumption was made that there will be no negative ID's
		for(int i=0;i<tables.size();i++) {
			Table <Integer, String, String> table = tables.get(i);
			System.out.println("Working on Current Table: "+ i);
			newTable.row(HEADERROW).putAll(table.row(HEADERROW)); //Maps all headers to header's row of merged table
			
			//Merges non-header rows into resultant table
			for(int j=0; !table.row(j).isEmpty();j++) {
				System.out.println("ID: "+table.get(j, UNIQUEIDENTIFIER));
				Integer identifier = Integer.parseInt(table.get(j, UNIQUEIDENTIFIER));
				newTable.row(identifier).putAll(table.row(j));
				System.out.println("old Table:" + table.row(j).toString());
				System.out.println("Merged Table: " + newTable.row(identifier));
			}
		}
		
		newTable =  fillEmpties(newTable);
		return newTable;
	}
	
	/**
	 * Used to fill null cells<br>
	 * Method was kept public as there are multiple use cases for filling not just due to merges
	 * @param table a table where null cells need to be filled with {@literal <}Integer, String, String{@literal >} tuples
	 * @return a table with all cells filled
	 */
	public static Table<Integer, String, String> fillEmpties(Table<Integer, String, String> table) {
		Set<String> headers = table.columnKeySet();
		Set<Integer> records = table.rowKeySet();
		
		Iterator<Integer> recordsIterator = records.iterator();
		System.out.println("Removing Empty Slots");
		
		Table<Integer, String, String> newTable = TreeBasedTable.create();
		newTable.putAll(table);
		
		//checks each possible entry in table and fills in any null values
		while(recordsIterator.hasNext() == true) {
			int record = recordsIterator.next();
			Iterator <String> headerIterator = headers.iterator();
			System.out.println();
			while(headerIterator.hasNext() == true) {
				String column = headerIterator.next();
				System.out.print(newTable.get(record, column));
				if(newTable.get(record, column)==null) {
					System.out.println("Empty Detected");
					newTable.put(record, column, "");
				}
			}	
		}
		
		return newTable;
	}
	/**
	 * Writes provided table into a csv file <br>
	 * Writer is mapped to UTF-8 to allow for support of multiple languages
	 * @param table to be written into csv with {@literal <}Integer, String, String{@literal >} tuples
	 * @param filename designated filename to be written in
	 * @throws IOException
	 */
	public static void writeToCsv(Table<Integer, String, String> table, String filename) throws IOException {
		CSVWriter writer = null;
		try {
			
			Writer fw = new OutputStreamWriter (new FileOutputStream(filename), StandardCharsets.UTF_8);
			writer = new CSVWriter (fw);
		} catch (IOException e) {
			System.out.println("Ensure File is not open");
			e.printStackTrace();
		}
		int currentRow=HEADERROW;
		String [] line = new String [table.row(HEADERROW).size()]; //Since all empty slots were filled all rows have the same size
		int counter =0;
		System.out.println();
		
		//writes all lines into csv but last one
		for(Cell<Integer, String, String> cell: table.cellSet()) { 
			if(currentRow<cell.getRowKey()) {
				System.out.println("New Line");
				currentRow=cell.getRowKey();
				writer.writeNext(line);
				counter =0;
			}
			line [counter] = cell.getValue();
			System.out.print(line[counter] + "\t");
			counter++;
		}
		
		System.out.println("New Line"); //writes the last line
		writer.writeNext(line);
		
		writer.close();
	}
}
