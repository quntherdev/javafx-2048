package modeleConsole;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import javafx.scene.layout.StackPane;

public class GrilleConsolle {
	private ArrayList<ArrayList<TuileConsole>> grille;
	private Integer dimensions;
	
	public GrilleConsolle(int dimensions) {
		grille = new ArrayList<ArrayList<TuileConsole>>();
		
		this.dimensions = dimensions;
	}
	
	public Integer getDimensions() {
		return dimensions;
	}
	
	public ArrayList<ArrayList<TuileConsole>> getGrille() {
		return grille;
	}
	public void setGrille(ArrayList<ArrayList<TuileConsole>> grille) {
		this.grille = grille;
	}
	
	public Integer getNbCasesVides() {
		int nbVides=0;
		
		for(int i=0; i<dimensions; i++) {
			for(int j=0; j<dimensions; j++) {
				if(((TuileConsole)grille.get(i).get(j)).getValeur()==null) nbVides++;
			}
		}
		
		return nbVides;
	}

	public Integer getNbCases() {
		return dimensions;
	}
	public void setNbCases(int dimensions) {
		this.dimensions = dimensions;
	}
	
	public void remplirGrille(){
		ArrayList<TuileConsole> rangee;
		
		for(int i=0; i<dimensions; i++) {
			rangee = new ArrayList<TuileConsole>();
			
			for(int j=0; j<dimensions; j++) {
				rangee.add(new TuileConsole(null));
			}
			grille.add(rangee);
		}
		
		
		
		for(int i=0; i<2; i++) {
			try {
				poserTuileCaseAleatoire();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}

	
	public void poserTuileCaseAleatoire() throws Exception { // 2 tuiles à faire apparaître au début de chaque partie, et 1 après chaque glissement
		Random r = new Random();
		int random_row, random_col;
		
		if(getNbCasesVides()<=0) throw new Exception("Toutes les cases sont occupées");
		
		do {
			random_row = r.nextInt(dimensions);
			random_col = r.nextInt(dimensions);
		}while(estCaseVide(random_row, random_col));
		
		grille.get(random_row).set(random_col,new TuileConsole(2));
	}

	
	public ArrayList<TuileConsole> formatter_rangee(ArrayList<TuileConsole> rangee, SensDeGlissementConsole sens){
		ArrayList<TuileConsole> rangee_nettoyee = rangee;

		if(sens == SensDeGlissementConsole.valueOf("B") || sens==SensDeGlissementConsole.valueOf("G")) {
			Collections.reverse(rangee_nettoyee);
		}
				
		rangee_nettoyee = retirerCasesNulles(rangee_nettoyee);
		
		return rangee_nettoyee;
	}
	
	
	
	public ArrayList<TuileConsole> retirerCasesNulles(ArrayList<TuileConsole> rangee) {
		ArrayList<TuileConsole> rangee_nettoye = new ArrayList<TuileConsole>();
		int nbNull=0;
		
		for(int i=0; i<rangee.size(); i++) {
			if(rangee.get(i).getValeur()!=null) {
				rangee_nettoye.add(rangee.get(i));
			}else {
				nbNull++;
			}
		}
		
		for(int i=0; i<nbNull; i++) {
			rangee_nettoye.add(new TuileConsole(null));
		}
		return rangee_nettoye;
	}
	
	
	public ArrayList<TuileConsole> fusionner(ArrayList<TuileConsole> rangee, SensDeGlissementConsole sens) { // à placer dans l'ordre		
		/* exemple avec  2,2,2,4,8,2,2,4,4
		 * 1/ on colle chaque élément quand case vide car on va vérifier i avec i+1 et si i=2,i+1=0 et i+2=2, i et i+2 doivent fusionner
		 * 2/ on inverse la list pour que ce soit + facile à manipuler
		 * 3/ on match le + à droite avec sa tuile voisine et ainsi de suite et on assemble si possible
		 * 4/ si ça match on dit la tuile voisine est 0 pour pouvoir ensuite réaffecter les tuiles à des cases
		 * 5/ on réaffecte les tuiles aux cases
		 */
		
		//4,4, 2,2, 8,4, 2,0, 2
		
		// la liste est débarassée des tuiles vides non rangées à ce niveau
		
		for(int i=0; i<rangee.size()-1; i+=2) {
			if(rangee.get(i+1).getValeur()==null) break;
			
			if(rangee.get(i).getValeur()==rangee.get(i+1).getValeur()) {
				rangee.get(i).setValeur(rangee.get(i).getValeur()*2);
				rangee.set(i+1,new TuileConsole(null));
			}else {
				i--;
			}
		}

		return retirerCasesNulles(rangee);
	}
	
	
	public void glissement(SensDeGlissementConsole sensDeGlissement) {
		
		if(sensDeGlissement==SensDeGlissementConsole.valueOf("G") || sensDeGlissement==SensDeGlissementConsole.valueOf("D")) {
			
			ArrayList<TuileConsole> rangee = new ArrayList<TuileConsole>();

			for(int i=0; i<grille.size(); i++) { // dans la double boucle on ajoute rangée par rangée dans une list qu'on crée pour faire la fusion
				for(int j=0; j<grille.size(); j++) {
					rangee.add(grille.get(i).get(j));	
				}
				
				rangee = formatter_rangee(rangee, sensDeGlissement);
				rangee = fusionner(rangee, sensDeGlissement);
		
				if(sensDeGlissement==SensDeGlissementConsole.valueOf("D")) {
					Collections.reverse(rangee);
				}
				
				for(int j=0; j<grille.size(); j++) { // on remplace par la nouvelle rangée
					grille.get(i).set(j, rangee.get(j));
				}
				rangee.clear();

			}
			
			
		}else if(sensDeGlissement==SensDeGlissementConsole.valueOf("B") || sensDeGlissement==SensDeGlissementConsole.valueOf("H")) {
			ArrayList<TuileConsole> rangee = new ArrayList<TuileConsole>();

			for(int i=0; i<grille.size(); i++) { // dans la double boucle on ajoute colonne par colonne dans une list qu'on crée pour faire la fusion
				for(int j=0; j<grille.size(); j++) {
					rangee.add(grille.get(j).get(i));	
				}
				
				rangee = formatter_rangee(rangee, sensDeGlissement);
				rangee = fusionner(rangee, sensDeGlissement);

				if(sensDeGlissement==SensDeGlissementConsole.valueOf("B")) {
					Collections.reverse(rangee);
				}
				
				for(int j=0; j<grille.size(); j++) { // on remplace par la nouvelle colonne
					grille.get(j).set(i, rangee.get(j));
				}
				rangee.clear();

			}

		}
		
		if(getNbCasesVides()>0) {
			try {
				poserTuileCaseAleatoire();
			} catch (Exception e) {
				bloque();
			}			
		}

	}
	
	
	
	public boolean estCaseVide(int row, int col) {
		if(grille.get(row).get(col).getValeur()==null) return false;
		return true;
	}
	
	public boolean bloque() { // fin
		if(getNbCasesVides()==0) {
			System.out.println("FIN !!!");
			return true;
		}
		return false;
	}
	
	public String toString() {
		String grille_affichage = "";
		
		for(int i=0; i<grille.size(); i++) {
			for(int j=0; j<grille.size(); j++) {
				
				if(grille.get(i).get(j).getValeur()==null) {
					grille_affichage += "X |";
				}else {
					grille_affichage += grille.get(i).get(j).getValeur() + " |";
				}
				
			}
			grille_affichage += "\n";
		}
		
		return grille_affichage;	
	}
	
}
