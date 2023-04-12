package modele;

import java.net.URI;

public interface Saveable {
	boolean folderExist();
	void createFolder();
	
	boolean fileExist();
	void createFile();
}
