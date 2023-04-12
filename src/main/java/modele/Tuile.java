package modele;

import java.util.HashMap;

public class Tuile {

	private static HashMap<Integer,String> couleurs_tuiles;
	static {
		couleurs_tuiles = new HashMap<>();
		couleurs_tuiles.put(2, "#eee4da");
		couleurs_tuiles.put(4, "#ede0c8");
		couleurs_tuiles.put(8, "#f2b179");
		couleurs_tuiles.put(16, "#f59563");
		couleurs_tuiles.put(32, "#f67c5f");
		couleurs_tuiles.put(64, "#fe6534");
		couleurs_tuiles.put(128, "#ecc76d");
		couleurs_tuiles.put(256, "#ecc55c");
		couleurs_tuiles.put(512, "#ebc44b");
		couleurs_tuiles.put(1024, "#edbc33");
		couleurs_tuiles.put(2048, "#e7ce09");
		couleurs_tuiles.put(4096, "#2285d0");
		couleurs_tuiles.put(8192, "#000000");
	}
	
	private Integer valeur;
	
	public Tuile(Integer valeur) {
		this.valeur = valeur;
	}
	
	
	public String getCouleurHexa() {
		return couleurs_tuiles.get(valeur);
	}
	
	public Integer getValeur() {
		return valeur;
	}
	
	
	public void setValeur(int valeur) {
		this.valeur = valeur;
	}
	
}
