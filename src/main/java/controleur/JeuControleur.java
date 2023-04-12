package controleur;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import modele.Grille;
import modele.SaveScore;
import modele.SensDeGlissement;
import modele.Tuile;

public class JeuControleur implements Initializable{
	
	protected ReadOnlyDoubleProperty stackWidthProperty;
	protected ReadOnlyDoubleProperty stackHeightProperty;
	
    protected int grid_size = 450;
    protected static int victory_value = 2048;
    
	@FXML protected Label scoreNomLabel;
	@FXML protected Label meilleurScoreNomLabel;
	
	@FXML protected Label score_numLabel;
	@FXML protected Label meilleur_score_numLabel;
	
	@FXML protected BorderPane mainBorderPane;
	
	@FXML protected Button recommencerButton;
	@FXML protected Button partagerButton;
	
	@FXML protected GridPane gridPane;
	
	protected ControleurGlobal controleurGlobal;
    protected boolean alreadyExecuted = false;
	
	Grille grille;
	Parent root;
	
	public JeuControleur(ControleurGlobal controleurGlobal, int dimensions) {
		this.controleurGlobal = controleurGlobal;
		FXMLLoader fxml = new FXMLLoader(getClass().getResource(ControleurGlobal.resource_path+"/fxml/Jeu.fxml"));
		fxml.setController(this);
		
		grille = new Grille(dimensions);
		grille.remplirGrille();
		
		try {
			root = fxml.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		scoreNomLabel.setAlignment(Pos.CENTER);
		meilleurScoreNomLabel.setAlignment(Pos.CENTER);
		
		score_numLabel.setAlignment(Pos.CENTER);
		score_numLabel.setText("0");
		
		meilleur_score_numLabel.setAlignment(Pos.CENTER);
		meilleur_score_numLabel.setText("0");
	}
	
	
	
	public void initialize(URL arg0, ResourceBundle arg1) {
		Image home_icon = new Image(ControleurGlobal.resource_path+"/images/home_icon.png");
		ImageView home_iconView = new ImageView(home_icon);
		home_iconView.setFitWidth(35);
		home_iconView.setFitHeight(35);
		recommencerButton.setGraphic(home_iconView);
		
		Image share_icon = new Image(ControleurGlobal.resource_path+"/images/share_icon.png");
		ImageView share_iconView = new ImageView(share_icon);
		share_iconView.setFitWidth(35);
		share_iconView.setFitHeight(35);
		partagerButton.setGraphic(share_iconView);
		
		recommencerButton.setOnAction((e)->{
			try {
				ControleurGlobal controleur = new ControleurGlobal();
				controleur.switchToControleurGlobal();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});

		
		mainBorderPane.addEventFilter(KeyEvent.KEY_PRESSED, event ->{
			score_numLabel.setText(String.valueOf(Integer.valueOf(score_numLabel.getText())+2));
			
			switch(event.getCode()) {
			case DOWN:
				grille.glissement(SensDeGlissement.B);
				mettre_a_jourGrille();
				System.out.println("glissement bas");
				if(aGagne()) alertVictoire();
				break;
			case UP:
				grille.glissement(SensDeGlissement.H);
				mettre_a_jourGrille();
				System.out.println("glissement haut");
				if(aGagne()) alertVictoire();
				break;
			case LEFT:
				grille.glissement(SensDeGlissement.G);
				mettre_a_jourGrille();
				System.out.println("glissement gauche");
				if(aGagne()) alertVictoire();
				break;
			case RIGHT:
				grille.glissement(SensDeGlissement.D);
				mettre_a_jourGrille();
				System.out.println("glissement droite");
				if(aGagne()) alertVictoire();
				break;
				default:
					break;
			}
			//System.out.println(grille.toString());
		});
		
	}
	
	//appeler après que show() soit appelé, pour accéder à getWidth() sans soucis de retour 0 ou -1
	public void	mettre_a_jourGrille() {
		for(int r=0; r<grille.getDimensions(); r++) {
			for(int c=0; c<grille.getDimensions(); c++) {
				
				if(grille.getGrille().get(r).get(c).getValeur()==null) {
					StackPane stackPane = getCase(r, c);
					stackPane.getChildren().clear();
					
					TuileControleur tuileControleur = new TuileControleur(this, new Tuile(null));          	
					stackPane.getChildren().add(tuileControleur.couleurTuilePane);
				}else {
					StackPane stackPane = getCase(r, c);
					
					TuileControleur tuileControleur = new TuileControleur(this,grille.getGrille().get(r).get(c));            	
			    	stackPane.getChildren().add(tuileControleur.couleurTuilePane);
				}
			}
		}
		System.out.println();
	}
	
	
	public boolean aGagne() {
		for(int r=0; r<grille.getDimensions(); r++) {
			for(int c=0; c<grille.getDimensions(); c++) {
				Integer val = grille.getGrille().get(r).get(c).getValeur();
				if(val!=null && val==victory_value) {
					System.out.println("VICTOIRE");
					SaveScore save = new SaveScore(r, c);
					save.saveScore();
					return true;
				}
			}
		}
		return false;
	}
	
	
	public void alertVictoire() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		
		DialogPane dialog = alert.getDialogPane();
		dialog.getStylesheets().add(getClass().getResource(ControleurGlobal.resource_path+"/style/alert.css").toExternalForm());
		dialog.getStyleClass().add("dialog");
		
		alert.setTitle("Vous avez gagné");
		alert.setHeaderText("Félicitations ! Vous venez d'atteindre le score "+victory_value+", qui fait de vous un gagnant du jeu !"
				+"Vous pouvez désormais partager votre partie, ou bien continuer et réaliser un score encore plus élevé !");

		ButtonType buttonTypeOne = new ButtonType("Continuer");
		ButtonType buttonTypeThree = new ButtonType("Partager puis terminer");
		ButtonType buttonTypeCancel = new ButtonType("Terminer", ButtonData.CANCEL_CLOSE);

		alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeThree, buttonTypeCancel);

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == buttonTypeOne){
		    // ... user chose "One"
		} else if (result.get() == buttonTypeThree) {
		    // ... user chose "Three"
		} else {
		    // ... user chose CANCEL or closed the dialog
		}
	}
	
	public void viderGrille() { // vide les stackpanes
		for(int r=0; r<grille.getDimensions(); r++) {
			for(int c=0; c<grille.getDimensions(); c++) {
				
			}
		}
	}
	
	/*
	 * pour que stackPane.getWidth() ne renvoie pas 0 ou -1, il faut ajouter les stackpanes AVANT le stage.show();
	 * et faire un getWidth() APRES le stage.show();
	 */
	public void prepareGrid() throws Exception {
		/*
		 * Don't use setGridLinesVisible(true): the documentation explicitly states this is for debug only.
		 * Instead, place a pane in all the grid cells (even the empty ones), and style the pane so you see the borders.
		 * (This gives you the opportunity to control the borders very carefully, so you can avoid double borders, etc.)
		 * Then add the content to each pane. You can also register the mouse listeners with the pane, which means you don't have to do
		 *  the ugly math to figure out which cell was clicked.
		 */
		if(alreadyExecuted) throw new Exception("GridPane déjà apparu.");

        gridPane = new GridPane();
        gridPane.setGridLinesVisible(true); // pour debug seulement, ne pas activer pour la prod, on peut pas styliser, il faut mettre un stackpane
                
        gridPane.setMinWidth(grid_size);
        gridPane.setMinHeight(grid_size);
        
        gridPane.setPrefWidth(grid_size);
        gridPane.setPrefHeight(grid_size);

        gridPane.setMaxWidth(grid_size);
        gridPane.setMaxHeight(grid_size);
        
        
        final int numCols = grille.getDimensions();
        final int numRows = grille.getDimensions();
        
        for (int i = 0; i < numCols; i++) { // ajoute colonnes
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPercentWidth(100.0 / numCols);
            gridPane.getColumnConstraints().add(colConst);
        }
        
        for (int i = 0; i < numRows; i++) { // ajoute rangées
            RowConstraints rowConst = new RowConstraints();
            rowConst.setPercentHeight(100.0 / numRows);
            gridPane.getRowConstraints().add(rowConst);
        }
        
        mainBorderPane.setCenter(gridPane);
        gridPane.setStyle("-fx-background-color: #baac9f;"); 

        
        for(int r=0; r<grille.getDimensions(); r++) { // ajout des stack panes sur chaque case pour ensuite ajouter les tuiles
            for(int c=0; c<grille.getDimensions(); c++) {
            	StackPane stackPane = new StackPane();
            	
            	if(stackWidthProperty == null) stackWidthProperty = stackPane.widthProperty();
            	if(stackHeightProperty == null) stackHeightProperty = stackPane.heightProperty();
            	
            	stackPane.setStyle("-fx-background-color: #cdc1b5;"
            			+ "-fx-border-width: 5px 5px 5px 5px;"
            			+ "-fx-border-color: #baac9f;");
            	
            	gridPane.add(stackPane, c, r);
            }
        }
		
        alreadyExecuted = true;
	}
	
	
	@SuppressWarnings("static-access")
	public StackPane getCase(int row, int col) {
		// il faut rajouter +1 à la fin car le 1er enfant est un "Group" et non un "StackPane"
		return (StackPane) gridPane.getChildren().get((row*(grille.getDimensions())+col)+1);
	}
}
