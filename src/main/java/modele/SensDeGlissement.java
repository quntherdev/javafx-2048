package modele;

public enum SensDeGlissement {
	H("HAUT"),
	B("BAS"),
	G("GAUCHE"),
	D("DROITE");

	private final String sens;	
	
	SensDeGlissement(String sens) {
		this.sens = sens;
	}
	
	public String getSens() {
		return sens;
	}
}
