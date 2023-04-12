package modele;

import java.util.Scanner;

public class BancDeTest {
	public static void main(String[] args) {
		Grille plateau = new Grille(4);
		plateau.remplirGrille();
	
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		
		while(true) {
			System.out.println(plateau);
			String entree = scanner.nextLine();
			
			plateau.glissement(SensDeGlissement.valueOf(entree));
		}
	}
}
