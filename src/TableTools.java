import java.util.ArrayList;

import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;
import com.google.common.collect.Table.Cell;

/**
 * Object meant for all custom tools involving tables
 * Would have extended Google's Tables object but it was set to final
 * @author Ryan Hoang
 *
 */
public class TableTools {
	public static final String UNIQUEIDENTIFIER = "ID";
	
	/**
	 * Outputs contents of table into command line
	 * @param a table with Integer axis and String contents
	 */
	public static void testTable(Table<Integer, Integer, String> table) {
		if(table == null||table.isEmpty()) {
			System.out.println("Table is empty");
			return;
		}

		int currentRow=0;
		for(Cell<Integer, Integer, String> cell: table.cellSet()) {
			if(cell.getRowKey()>currentRow) {
				currentRow = cell.getRowKey();
				System.out.println();
			}
			System.out.print(cell.getValue() + "\t");
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
	public static Table<Integer, Integer, String> mergeTable(ArrayList<Table<Integer, Integer, String>> tables) {
		
		
		
		return null;
	}
}
