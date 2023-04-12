package modele;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Attr;


/*
 *  I didn't finish the saving feature
 */
public class SaveScore implements Saveable {
	
	private static URI foldername;
	private static URI filename;
	static {
		try {
			foldername = new URI("2048");
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		try {
			filename = new URI("scores.xml");
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	private static File folder;
	private static File file;
	static{
		folder = new File(System.getenv("APPDATA")+"/"+foldername);
		file = new File(System.getenv("APPDATA")+"/"+foldername+"/"+filename);
	}
	
	
	int dimension;
	int score;
	
	public SaveScore(int dimension, int score) {
		this.dimension = dimension;
		this.score = score;
	}

	public void saveScore() {
		if(!folderExist()) createFolder();
		if(!fileExist()) createFile();
		

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder=null;
        try {
			docBuilder = dbFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
        
     // élément de racine
        Document doc = docBuilder.newDocument();
        Element racine = doc.createElement("repertoire");
        doc.appendChild(racine);
 
        // l'élément contact
        Element contact = doc.createElement("contact");
        racine.appendChild(contact);
 
        // attributs de l'élément contact
        Attr attr = doc.createAttribute("id");
        attr.setValue("1");
        contact.setAttributeNode(attr);
 
        // le nom
        Element nom = doc.createElement("nom");
        nom.appendChild(doc.createTextNode("codeur"));
        contact.appendChild(nom);
 
        // le prénom
        Element prenom = doc.createElement("prenom");
        prenom.appendChild(doc.createTextNode("java"));
        contact.appendChild(prenom);
 
        // le mobile
        Element mobile = doc.createElement("mobile");
        mobile.appendChild(doc.createTextNode("098745126"));
        contact.appendChild(mobile);
  
        // l'email
        Element email = doc.createElement("email");
        email.appendChild(doc.createTextNode("codeurjava8@gmail.com"));
        contact.appendChild(email);
 
        // write the content into xml file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = null;
		try {
			transformer = transformerFactory.newTransformer();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		}
        DOMSource source = new DOMSource(doc);
        StreamResult resultat = new StreamResult(getFile());
 
        try {
			transformer.transform(source, resultat);
		} catch (TransformerException e) {
			e.printStackTrace();
		}
 
        System.out.println("Fichier sauvegardé avec succès!");
	}
	
	
	
	public boolean folderExist() {
		return folder.exists() ? true:false;
	}
	
	@Override
	public void createFolder() {
		folder.mkdirs();
	}

	
	
	@Override
	public boolean fileExist() {
		return file.exists() ? true:false;
	}

	@Override
	public void createFile() {
		try {
			folder.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static File getFolder() {
		return folder;
	}
	
	public static File getFile() {
		return file;
	}
	
	public void createTree() {
		
	}
	
	
}
