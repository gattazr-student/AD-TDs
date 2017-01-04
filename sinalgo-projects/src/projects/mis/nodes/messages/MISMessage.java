package projects.mis.nodes.messages;

import projects.mis.ValeurS;
import sinalgo.nodes.messages.Message;

public class MISMessage extends Message {
	public int ID;
	public ValeurS pState;

	public MISMessage(int ID, ValeurS aState) {
		this.ID = ID;
		this.pState = aState;
	}

	@Override
	public Message clone() {
		return new MISMessage(this.ID, this.pState);
	}
}