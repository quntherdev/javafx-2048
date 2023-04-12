package controleur;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class ControleurGlobal extends Application implements Initializable{

	//public static String resource_path = "/resources";
	public static String resource_path = "";

	@FXML protected BorderPane mainBorderPane;

	@FXML protected HBox changerGrilleTailleHBox;

	
	int grille_taille;
	@FXML protected Label grilleTailleLabel ;
	@FXML protected Button grilleTailleUpButton;
	@FXML protected Button grilleTailleDownButton;

	
	@FXML protected Button commencerJeuButton;
	@FXML protected Button meilleursScoresButton;

	@FXML protected VBox buttonsVBox;
	
	protected Scene start_scene;
	protected JeuControleur jeu;
	private static Stage stage;// = new Stage();
		
	public static void main(String[] args) {
		launch(args);
	}	
	
	@Override
	public void start(Stage arg0) throws Exception {
		ControleurGlobal controleur = new ControleurGlobal();
		controleur.switchToControleurGlobal();
	}
	
	
	public ControleurGlobal() {
		grille_taille = 4;
	}

	
	public void switchToControleurGlobal() throws IOException {
		//Font font = Font.loadFont(ControleurGlobal.class.getClassLoader().getResourceAsStream("fonts/Roboto.ttf"),10);
		if(stage==null) {
			stage = new Stage();
		}
		grille_taille=4;
		jeu=null;
		
		FXMLLoader fxml = new FXMLLoader();
		fxml.setLocation(getClass().getResource(resource_path+"/fxml/Global.fxml"));
		fxml.setController(this);

		Parent root_node = fxml.load();
		start_scene = new Scene(root_node);
		
		changeStyle(resource_path+"/style/start_scene.css");

		stage.getIcons().add(new Image(resource_path+"/images/image_principale.png"));
		stage.setScene(start_scene);

		
		Timeline t = new Timeline();
		t.setCycleCount(8);
		
			
		stage.show();
	}
	

	public void initialize(URL arg0, ResourceBundle arg1) {
		commencerJeuButton.setEffect(new DropShadow());
		meilleursScoresButton.setEffect(new DropShadow());
		
		commencerJeuButton.setOnAction((e)->{			
			
			jeu = new JeuControleur(this,grille_taille);
			try {
				jeu.prepareGrid();
			} catch (Exception e2) {
				e2.printStackTrace();
			}

			mainBorderPane.setCenter(jeu.mainBorderPane);
			changeStyle("/style/jeu.css");
			jeu.mettre_a_jourGrille();
		});
		
		grilleTailleDownButton.setOnAction((e) -> {
			switch(grille_taille) {
			case 3:
				grille_taille = 8;
				grilleTailleLabel.setText("8x8");
				break;
			case 4:
				grille_taille = 3;
				grilleTailleLabel.setText("3x3");
				break;
			case 5:
				grille_taille = 4;
				grilleTailleLabel.setText("4x4");
				break;
			case 6:
				grille_taille = 5;
				grilleTailleLabel.setText("5x5");
				break;
			case 8:
				grille_taille = 6;
				grilleTailleLabel.setText("6x6");
				break;
			}
		});
		
		
		grilleTailleUpButton.setOnAction((e) -> {
			switch(grille_taille) {
			case 3:
				grille_taille = 4;
				grilleTailleLabel.setText("4x4");
				break;
			case 4:
				grille_taille = 5;
				grilleTailleLabel.setText("5x5");
				break;
			case 5:
				grille_taille = 6;
				grilleTailleLabel.setText("6x6");
				break;
			case 6:
				grille_taille = 8;
				grilleTailleLabel.setText("8x8");
				break;
			case 8:
				grille_taille = 3;
				grilleTailleLabel.setText("3x3");
				break;
			}
		});
		
	}
	
	public static Stage getStage() { // à appeler seulement après qu'un stage lui soit assigné dans start()
		return stage;
	}

	
	public void changeStyle(String style_filename) {
		start_scene.getStylesheets().clear();
		start_scene.getStylesheets().add(getClass().getResource(style_filename).toExternalForm());
	}
}