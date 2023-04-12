package modeleConsole;

import java.util.Scanner;

public class BancDeTestConsole {
	public static void main(String[] args) {
		GrilleConsolle plateau = new GrilleConsolle(4);
		plateau.remplirGrille();
				
		Scanner scanner = new Scanner(System.in);

		
		while(true) {
			System.out.println(plateau);
			String entree = scanner.nextLine();
			
			plateau.glissement(SensDeGlissementConsole.valueOf(entree));
		}
	}
}
