package projects.stable.nodes.messages;

import sinalgo.nodes.messages.Message;

public class StableMessage extends Message {

	public int state;

	public StableMessage(int aState) {
		this.state = aState;
	}

	@Override
	public Message clone() {
		return this;
	}

}
