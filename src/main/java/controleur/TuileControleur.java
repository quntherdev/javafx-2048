package controleur;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import modele.Tuile;

public class TuileControleur implements Initializable{

	private static HashMap<Integer,Integer> taille_police;
	static {
		taille_police = new HashMap<>();
		taille_police.put(3, 50);
		taille_police.put(4, 40);
		taille_police.put(5, 30);
		taille_police.put(6, 25);
		taille_police.put(8, 16);
	}
	
	
	@FXML protected Pane couleurTuilePane;
	@FXML protected Label valeurTuileLabel;
	private Tuile tuile;

	JeuControleur jeuControlleur;
	
	public TuileControleur(JeuControleur jeuControlleur, Tuile tuile) {		
		this.tuile = tuile;
		this.jeuControlleur = jeuControlleur;
	
    	FXMLLoader fxml = new FXMLLoader(getClass().getResource(ControleurGlobal.resource_path+"/fxml/Tuile.fxml"));
    	fxml.setController(this);
    	
    	try {
			fxml.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		if(tuile.getValeur()==null) {
			couleurTuilePane.setVisible(false);
			valeurTuileLabel.setVisible(false);
		}
		
    	couleurTuilePane.maxWidthProperty().bind(jeuControlleur.stackWidthProperty);
    	valeurTuileLabel.maxWidthProperty().bind(jeuControlleur.stackWidthProperty);
    	
    	couleurTuilePane.maxHeightProperty().bind(jeuControlleur.stackHeightProperty);
        valeurTuileLabel.maxHeightProperty().bind(jeuControlleur.stackHeightProperty);
        
		couleurTuilePane.setStyle("-fx-background-color: "+tuile.getCouleurHexa()+";");
		
		valeurTuileLabel.setStyle("-fx-font-size: "+getTaillePolice()+"px;"
				+ "-fx-font-weight: bold;");
		
		if(tuile.getValeur()!=null) {
			if(tuile.getValeur()==2 || tuile.getValeur()==4) {
				valeurTuileLabel.setStyle("-fx-font-size: "+getTaillePolice()+"px;"
						+ "-fx-font-weight: bold;"
						+ "-fx-text-fill: #776e65;");
			}else {
				valeurTuileLabel.setStyle("-fx-font-size: "+getTaillePolice()+"px;"
						+ "-fx-font-weight: bold;"
						+ "-fx-text-fill: white;");
			}
		}
		valeurTuileLabel.setText(String.valueOf(tuile.getValeur()));
	}
	
	public Integer getTaillePolice() {
		Integer taille = taille_police.get(jeuControlleur.grille.getDimensions());
		
		if(taille==null) return 16;
		return taille;
	}
	
}
