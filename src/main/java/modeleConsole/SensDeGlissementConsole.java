package modeleConsole;

public enum SensDeGlissementConsole {
	H("HAUT"),
	B("BAS"),
	G("GAUCHE"),
	D("DROITE");

	private final String sens;	
	
	SensDeGlissementConsole(String sens) {
		this.sens = sens;
	}
	
	public String getSens() {
		return sens;
	}
}
