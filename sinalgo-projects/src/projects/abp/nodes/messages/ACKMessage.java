package projects.abp.nodes.messages;

import sinalgo.nodes.messages.Message;

/**
 * SimpleIntMessage
 */
public class ACKMessage extends Message {

	private boolean pTag;

	public ACKMessage(boolean aTag) {
		this.pTag = aTag;
	}

	@Override
	public Message clone() {
		return this;
	}

	public boolean getTag() {
		return this.pTag;
	}

	@Override
	public String toString() {
		return String.format("%s: tag=[%b]", getClass().getSimpleName(), getTag());
	}

}
