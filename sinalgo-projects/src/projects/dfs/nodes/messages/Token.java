package projects.dfs.nodes.messages;

import sinalgo.nodes.messages.Message;

/**
 * Toekn
 */
public class Token extends Message {

	/**
	 * Creation of a Token
	 */
	public Token() {
	}

	@Override
	public Message clone() {
		// Since a message cannot be modified after creation, returning 'this'
		// is fine
		return this;
	}

	@Override
	public String toString() {
		return String.format("%s", getClass().getSimpleName());
	}

}
