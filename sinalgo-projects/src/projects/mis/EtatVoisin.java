package projects.mis;

public class EtatVoisin {
	public int ID;
	public ValeurS pState;

	public EtatVoisin() {
		this.ID = (int) (Math.random() * 10000.0);
		this.pState = ValeurS.RandomInit();
	}
}
