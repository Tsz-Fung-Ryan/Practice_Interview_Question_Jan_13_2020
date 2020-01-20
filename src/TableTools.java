import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.io.FileWriter;
import java.io.IOException;

import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;

import au.com.bytecode.opencsv.CSVWriter;

import com.google.common.collect.Table.Cell;



/**
 * Object meant for all custom tools involving tables
 * Would have extended Google's Tables object but it was set to final
 * @author Ryan Hoang
 *
 */
public class TableTools {
	public static final String UNIQUEIDENTIFIER = "ID";
	public static final int HEADERROW =-1;
	/**
	 * Outputs contents of table into command line
	 * @param a table with Integer axis and String contents
	 */
	public static void testTable(Table<Integer, String, String> table) {
		if(table == null||table.isEmpty()) {
			System.out.println("Table is empty");
			return;
		}

		int currentRow=HEADERROW-2;
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
	 * Purpose of this method is to place the ID column into the first column to make sorting and merging simpler
	 * @param A table where ID was not placed in the first column
	 * @return The same table but ID moved to the first column
	 */
	public static Table<Integer, Integer, String> fixColumnOrder(Table<Integer, Integer, String> table) {
		System.out.println("Table needs to be adjusted");
		Table <Integer, Integer, String> newTable = TreeBasedTable.create();

		int idColumnLocation=0;
		for (int i=0; i<table.row(0).size();i++) {
			if(table.get(0, i).equals(UNIQUEIDENTIFIER)) {
				idColumnLocation = i;
				//System.out.println(UNIQUEIDENTIFIER + " Found at: " + i);
				break;
			}
		}

		//ID column is assumed longest as it's the one being used as the identifier
		for (int i=0; i<table.column(idColumnLocation).size();i++) {
			String data = table.get(i, idColumnLocation);
			newTable.put(i, 0, data);
			//System.out.println(newTable.get(i, 0));			
		}

		//Rest of the table is placed into the new table
		for (int col=0;col<table.row(0).size();col++) { //Header row is assumed to be longest row
			//System.out.println(table.get(0, col));
			if(col==idColumnLocation)
				continue;
			for(int row=0;row<table.column(col).size();row++) {
				String data;
				if(col>idColumnLocation) {
					System.out.println("passed ID");
					data = table.get(row, col+1);
				}
				else {
					data = table.get(row, col);
				}
				newTable.put(row, col+1, data);
			}
		}
		return newTable;
	}
	/**
	 * Does an outer join of all tables in array by ID
	 * @param tables
	 * @return
	 */
	public static Table<Integer, String, String> mergeAndSortTable(ArrayList<Table<Integer, String, String>> tables) {
		Table <Integer, String, String> newTable = TreeBasedTable.create();
		
		
		//Merges all tables and sorts them by ID assumption was made that there will be no negative ID's
		for(int i=0;i<tables.size();i++) {
			Table <Integer, String, String> table = tables.get(i);
			System.out.println("Working on Current Table: "+ i);
			newTable.row(-1).putAll(table.row(-1));
			
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
	public static Table<Integer, String, String> fillEmpties(Table<Integer, String, String> table) {
		Set<String> headers = table.columnKeySet();
		Set<Integer> records = table.rowKeySet();
		
		Iterator<Integer> recordsIterator = records.iterator();
		System.out.println("Removing Empty Slots");
		
		Table<Integer, String, String> newTable = TreeBasedTable.create();
		newTable.putAll(table);
		
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
	public static void writeToCsv(Table<Integer, String, String> table, String filename) throws IOException {
		CSVWriter writer = null;
		try {
			writer = new CSVWriter (new FileWriter(filename));
		} catch (IOException e) {
			System.out.println("Ensure File is not open");
			e.printStackTrace();
		}
		int currentRow=HEADERROW;
		String [] line = new String [table.row(HEADERROW).size()]; //Since all empty slots were filled all rows have the same size
		int counter =0;
		System.out.println();
		
		for(Cell<Integer, String, String> cell: table.cellSet()) { //writes all lines but the last one
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
