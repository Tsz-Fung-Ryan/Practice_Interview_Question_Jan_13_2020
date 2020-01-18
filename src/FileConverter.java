import java.io.File;

public class FileConverter {

	boolean fileRead = false;
	
	public FileConverter (String name) {
		File file = new File(name);
		if(file.canRead()) {
			fileRead = true;
			System.out.println("File read");
		}
		else {
			fileRead = false;
			System.err.println("File could not be read");
		}
	}
	
	public String getFileExtension(String file) {
		String extension = "";
		int index = file.lastIndexOf(".");
		if(index == -1) {
			System.err.println("File Extension Not Found");
			return "";
		}
		extension = file.substring(index+1);
		return extension;
	}
}
