package projects.dfs.nodes.nodeImplementations;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map.Entry;

import projects.dfs.nodes.messages.Token;
import projects.dfs.nodes.timers.InitTimer;
import sinalgo.nodes.Node;
import sinalgo.nodes.edges.Edge;
import sinalgo.nodes.messages.Inbox;
import sinalgo.nodes.messages.Message;

/**
 *
 *
 */
public class DfsNode extends Node {

	enum Status {
		CLEAR, DONE, VISITED
	}

	/**
	 * Indicate this node's father. null if the father is not determined yet.
	 * this if it has none
	 */
	private Node pFather;

	/** Status of the node */
	private Status pStatus = Status.CLEAR;

	/** Indicate whether Edges have been used before or not */
	private HashMap<Node, Boolean> pVisited;

	@Override
	public void checkRequirements() throws sinalgo.configuration.WrongConfigurationException {
	}

	@Override
	public void draw(java.awt.Graphics g, sinalgo.gui.transformation.PositionTransformation pt, boolean highlight) {
		// draw the node as a circle with the text inside
		Color wColor;
		switch (this.pStatus) {
		case CLEAR:
			wColor = Color.YELLOW;
			break;
		case DONE:
			wColor = Color.BLACK;
			break;
		case VISITED:
			wColor = Color.GREEN;
			break;
		default:
			wColor = Color.YELLOW;
		}

		setColor(wColor);
		String wText = String.format("%d", this.ID);
		super.drawNodeAsDiskWithText(g, pt, highlight, wText, 20, Color.black);
	}

	@Override
	public void handleMessages(Inbox aInbox) {
		while (aInbox.hasNext()) {
			Message wMessage = aInbox.next();
			if (wMessage instanceof Token) {
				this.pStatus = Status.VISITED;
				System.out.println(String.format("%s : Token received", this));

				Node wMessageSender = aInbox.getSender();

				// Set pFather if value not set yet
				if (this.pFather == null) {
					this.pFather = wMessageSender;
					System.out.println(String.format("%s : Setting pFather", this));
				}

				// If pVisited only contains true
				if (!this.pVisited.containsValue(false)) {

					System.out.println(String.format("%s : Decide", this));
					this.pStatus = Status.DONE;

				} else if (this.pFather != wMessageSender && this.pVisited.get(wMessageSender) == false) {

					// If the node has already been visited (loop detection),
					// send Token back to sender
					System.out.println(String.format("%s : Loop detected. Send back to %d", this, wMessageSender.ID));
					this.pVisited.put(wMessageSender, true);
					send(wMessage, wMessageSender);

				} else {
					// Look for an unvisited neighbor excluding this.pFather
					// Send to this.pFather if none found
					Node wNextToVisit = null;

					// Look for an unvisited Node besides this.pFather
					for (Entry<Node, Boolean> wEntry : this.pVisited.entrySet()) {
						if (wEntry.getValue() == false) {
							Node wNode = wEntry.getKey();
							if (wNode != this.pFather) {
								wNextToVisit = wNode;
							}
						}
					}

					// set wNextToVisit to this.pFather if still at null
					if (wNextToVisit == null) {
						wNextToVisit = this.pFather;
						this.pStatus = Status.DONE;
					}

					// Send token
					System.out.println(String.format("%s : Send token to %d", this, wNextToVisit.ID));
					this.pVisited.put(wNextToVisit, true);
					send(wMessage, wNextToVisit);
				}
			}
		}
		// Infinite loop
		if (this.pStatus == Status.DONE) {
			start();
		}
	}

	/*
	 * Actions made at the creation of the node
	 */
	@Override
	public void init() {
		/* Initialize variables */
		setColor(Color.YELLOW);
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

	/**
	 * Actions made at the initialization of the algorithm. (send first
	 * message). The initialization must be done before handling any incoming
	 * messages.
	 */
	public void start() {
		// Create the visited HashMap
		this.pVisited = new HashMap<>();
		for (Edge aEdge : this.outgoingConnections) {
			this.pVisited.put(aEdge.endNode, false);
		}
		this.pStatus = Status.CLEAR;

		// For initiator
		if (this.ID == 1) {
			// Set father
			this.pFather = this;

			// Pick first edge and send token to the end node
			Edge wEdge = this.outgoingConnections.iterator().next();
			this.pStatus = Status.VISITED;
			this.pVisited.put(wEdge.endNode, true);
			send(new Token(), wEdge.endNode);
		} else {
			this.pFather = null;
		}

	}

	@Override
	public String toString() {
		int wFatherID;
		if (this.pFather == null) {
			wFatherID = -1;
		} else {
			wFatherID = this.pFather.ID;
		}
		return String.format("%s: ID=[%s], pFather=[%d]", getClass().getSimpleName(), this.ID, wFatherID);
	}
}
