package projects.abp.nodes.nodeImplementations;

import java.awt.Color;
import java.util.Random;

import projects.abp.nodes.messages.ACKMessage;
import projects.abp.nodes.messages.DataMessage;
import projects.abp.nodes.timers.InitTimer;
import projects.abp.nodes.timers.RegularTimer;
import sinalgo.nodes.Node;
import sinalgo.nodes.messages.Inbox;
import sinalgo.nodes.messages.Message;

/**
 *
 *
 */
public class ABPNode extends Node {

	private int pBuffer;
	private Node pNeighbor;
	private boolean pTag;

	@Override
	public void checkRequirements() throws sinalgo.configuration.WrongConfigurationException {
	}

	@Override
	public void draw(java.awt.Graphics g, sinalgo.gui.transformation.PositionTransformation pt, boolean highlight) {
		// draw the node as a circle with the text inside
		Color wColor = this.pTag ? Color.YELLOW : Color.BLUE;
		setColor(wColor);

		String wText = String.format("%d", this.ID);
		super.drawNodeAsDiskWithText(g, pt, highlight, wText, 20, Color.black);
	}

	@Override
	public void handleMessages(Inbox aInbox) {
		while (aInbox.hasNext()) {
			Message wMessage = aInbox.next();

			if (this.ID == 1) {
				if (wMessage instanceof ACKMessage) {
					ACKMessage wACKMessage = (ACKMessage) wMessage;
					if (wACKMessage.getTag() == this.pTag) {
						this.pBuffer = -1;
						System.out.println(String.format("1 : Received %s", wACKMessage));
					}
				}
			} else if (this.ID == 2) {
				if (wMessage instanceof DataMessage) {
					DataMessage wDataMessage = (DataMessage) wMessage;
					if (wDataMessage.getTag() != this.pTag) {
						this.pTag = !this.pTag;
						this.pBuffer = wDataMessage.getInt();
						System.out.println(String.format("2 : Received %s", wDataMessage));
					}
					ACKMessage wACKMessage = new ACKMessage(this.pTag);
					send(wACKMessage, this.pNeighbor);
					System.out.println(String.format("1 : Sent %s", wACKMessage));
				}
			}
		}
	}

	/*
	 * Actions made at the creation of the node
	 */
	@Override
	public void init() {
		/* Initialize variables */
		this.pTag = true;
		(new InitTimer()).startRelative(1, this);
	}

	@Override
	public void neighborhoodChange() {
	}

	@Override
	public void postStep() {
	};

	@Override
	public void preStep() {
	};

	public void regular() {
		if (this.pBuffer == -1) {
			this.pBuffer = new Random().nextInt(1000);
			this.pTag = !this.pTag;
		}

		if (this.pBuffer != -1) {
			DataMessage wMessage = new DataMessage(this.pTag, this.pBuffer);
			send(wMessage, this.pNeighbor);
			System.out.println(String.format("1 : Sent %s", wMessage));
		}

		new RegularTimer().startRelative(50, this);
	}

	public void start() {
		this.pTag = true;
		this.pBuffer = -1;
		this.pNeighbor = this.outgoingConnections.iterator().next().endNode;

		if (this.ID == 1) {
			new RegularTimer().startRelative(1, this);
		}
	}

	@Override
	public String toString() {
		return String.format("%s: ID=[%s]", getClass().getSimpleName(), this.ID);
	}
}
