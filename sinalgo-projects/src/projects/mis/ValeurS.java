package projects.mis;

public enum ValeurS {
	Dominant, domine;
	public static ValeurS RandomInit() {
		return ((int) (Math.random() * 10000.0)) % 2 == 1 ? ValeurS.Dominant : ValeurS.domine;
	}
}
