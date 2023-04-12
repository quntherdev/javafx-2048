package modele;

import java.util.ArrayList;
import java.util.Random;

public class Grille {
	private ArrayList<ArrayList<Tuile>> grille;
	private Integer dimensions;
	
	public Grille(int dimensions) {
		grille = new ArrayList<ArrayList<Tuile>>();
		
		this.dimensions = dimensions;
	}
	
	public Integer getDimensions() {
		return dimensions;
	}
	
	public ArrayList<ArrayList<Tuile>> getGrille() {
		return grille;
	}
	public void setGrille(ArrayList<ArrayList<Tuile>> grille) {
		this.grille = grille;
	}
	
	public Integer getNbCasesVides() {
		int nbVides=0;
		
		for(int i=0; i<dimensions; i++) {
			for(int j=0; j<dimensions; j++) {
				if(((Tuile)grille.get(i).get(j)).getValeur()==null) nbVides++;
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
		ArrayList<Tuile> rangee;
		
		for(int i=0; i<dimensions; i++) {
			rangee = new ArrayList<Tuile>();
			
			for(int j=0; j<dimensions; j++) {
				rangee.add(new Tuile(null));
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

	/*
	 *  2 tuiles à faire apparaître au début de chaque partie, et 1 après chaque glissement
	 *  pas de vérification à faire sur le nb de cases vides car on appelle bloque() juste avant
	 */

	public void poserTuileCaseAleatoire() {
		Random r = new Random();
		int random_row, random_col;
		
		do {
			random_row = r.nextInt(dimensions);
			random_col = r.nextInt(dimensions);
		}while(estCaseVide(random_row, random_col));
		
		double proba = r.nextDouble(); // >= 0.1 = tuile de 2 / tuile de 4 sinon
		if(proba>=0.1) {
			grille.get(random_row).set(random_col,new Tuile(2));
		}else {
			grille.get(random_row).set(random_col,new Tuile(4));
		}
	}

	
	public ArrayList<Tuile> formatter_rangee(ArrayList<Tuile> rangee, SensDeGlissement sens){
		ArrayList<Tuile> rangee_nettoyee = rangee;

		/*if(sens == SensDeGlissement.valueOf("B") || sens==SensDeGlissement.valueOf("G")) {
			Collections.reverse(rangee_nettoyee);
		}*/
				
		rangee_nettoyee = placerCasesNullesALaFin(rangee_nettoyee);
		
		return rangee_nettoyee;
	}
	
	
	public ArrayList<Tuile> placerCasesNullesAuDebut(ArrayList<Tuile> rangee){
		ArrayList<Tuile> rangee_nettoye = new ArrayList<Tuile>();
		int nbNull=0;
		
		for(int i=0; i<rangee.size(); i++) {
			if(rangee.get(i).getValeur()==null) {
				nbNull++;
			}
		}
		
		for(int i=0; i<nbNull; i++) {
			rangee_nettoye.add(new Tuile(null));
		}
		
		for(int i=0; i<rangee.size(); i++) {
			if(rangee.get(i).getValeur()!=null) {
				rangee_nettoye.add(rangee.get(i));
			}
		}
		
		return rangee_nettoye;
	}
	
	
	public ArrayList<Tuile> placerCasesNullesALaFin(ArrayList<Tuile> rangee) { //
		ArrayList<Tuile> rangee_nettoye = new ArrayList<Tuile>();
		int nbNull=0;
		
		for(int i=0; i<rangee.size(); i++) {
			if(rangee.get(i).getValeur()!=null) {
				rangee_nettoye.add(rangee.get(i));
			}else {
				nbNull++;
			}
		}
		
		for(int i=0; i<nbNull; i++) {
			rangee_nettoye.add(new Tuile(null));
		}
		return rangee_nettoye;
	}
	
	
	public ArrayList<Tuile> fusionner(ArrayList<Tuile> rangee, SensDeGlissement sens) { // à placer dans l'ordre		
		/* exemple avec  2,2,2,4,8,2,2,4,4
		 * 1/ on colle chaque élément quand case vide car on va vérifier i avec i+1 et si i=2,i+1=0 et i+2=2, i et i+2 doivent fusionner
		 * 2/ on inverse la list pour que ce soit + facile à manipuler
		 * 3/ on match le + à droite avec sa tuile voisine et ainsi de suite et on assemble si possible
		 * 4/ si ça match on dit la tuile voisine est 0 pour pouvoir ensuite réaffecter les tuiles à des cases
		 * 5/ on réaffecte les tuiles aux cases
		 */
		

		// la liste est débarassée des tuiles vides non rangées à ce niveau
		for(int i=0; i<rangee.size()-1; i+=2) {
			if(rangee.get(i+1).getValeur()==null) break;

			if(rangee.get(i).getValeur().intValue()==rangee.get(i+1).getValeur().intValue()) {
				rangee.get(i).setValeur(rangee.get(i).getValeur()*2);
				rangee.set(i+1,new Tuile(null));
			}else {
				i--;
			}
		}

		if(sens==SensDeGlissement.D || sens==SensDeGlissement.B) {
			return placerCasesNullesAuDebut(rangee);
		}
		
		return placerCasesNullesALaFin(rangee);
	}
	
	
	public void glissement(SensDeGlissement sensDeGlissement) {
		
		/*
		 * quand un joueur fait un glissement, c'est possible qu'aucun changement ait lieu donc dans ce cas on
		 * ne fait pas apparaître une nouvelle tuile de 2 ou 4
		 */
		int nb_changements=0;
		
		if(sensDeGlissement==SensDeGlissement.valueOf("G") || sensDeGlissement==SensDeGlissement.valueOf("D")) {
			ArrayList<Tuile> rangee = new ArrayList<Tuile>();
			
			for(int i=0; i<grille.size(); i++) { // dans la double boucle on ajoute rangée par rangée dans une list qu'on crée pour faire la fusion
				for(int j=0; j<grille.size(); j++) {
					rangee.add(grille.get(i).get(j));	
				}
				
				rangee = formatter_rangee(rangee, sensDeGlissement);
				rangee = fusionner(rangee, sensDeGlissement);
				
				for(int j=0; j<grille.size(); j++) { // on remplace par la nouvelle rangée
					if(grille.get(i).get(j).getValeur()!=rangee.get(j).getValeur()) nb_changements++;
					grille.get(i).set(j, rangee.get(j));
				}
				rangee.clear();

			}			
			
		}else if(sensDeGlissement==SensDeGlissement.valueOf("B") || sensDeGlissement==SensDeGlissement.valueOf("H")) {
			ArrayList<Tuile> rangee = new ArrayList<Tuile>();

			for(int i=0; i<grille.size(); i++) { // dans la double boucle on ajoute colonne par colonne dans une list qu'on crée pour faire la fusion
				for(int j=0; j<grille.size(); j++) {
					rangee.add(grille.get(j).get(i));	
				}
				
				rangee = formatter_rangee(rangee, sensDeGlissement);
				rangee = fusionner(rangee, sensDeGlissement);
				
				for(int j=0; j<grille.size(); j++) { // on remplace par la nouvelle colonne
					if(grille.get(j).get(i).getValeur()!=rangee.get(j).getValeur()) nb_changements++;
					grille.get(j).set(i, rangee.get(j));
				}
				rangee.clear();

			}

		}
		
		if(bloque()) {
			System.exit(0);			
		}
		
		if(nb_changements!=0) poserTuileCaseAleatoire();
	}
	
	
	public boolean estCaseVide(int row, int col) {
		if(grille.get(row).get(col).getValeur()==null) return false;
		return true;
	}
	
	public boolean bloque() { // fin
		if(getNbCasesVides()<=0) {
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
