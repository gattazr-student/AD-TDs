package projects.abp.nodes.messages;

import sinalgo.nodes.messages.Message;

/**
 * DataMessage
 */
public class DataMessage extends Message {

	private int pInt;
	private boolean pTag;

	public DataMessage(boolean aTag, int aInt) {
		this.pTag = aTag;
		this.pInt = aInt;
	}

	@Override
	public Message clone() {
		return this;
	}

	public int getInt() {
		return this.pInt;
	}

	public boolean getTag() {
		return this.pTag;
	}

	@Override
	public String toString() {
		return String.format("%s: tag=[%b], int=[%d]", getClass().getSimpleName(), getTag(), getInt());
	}

}
