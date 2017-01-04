package projects.stable.nodes.nodeImplementations;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Iterator;

import projects.stable.nodes.messages.StableMessage;
import projects.stable.nodes.timers.InitTimer;
import projects.stable.nodes.timers.WaitTimer;
import sinalgo.configuration.WrongConfigurationException;
import sinalgo.gui.transformation.PositionTransformation;
import sinalgo.nodes.Node;
import sinalgo.nodes.edges.Edge;
import sinalgo.nodes.messages.Inbox;
import sinalgo.nodes.messages.Message;
import sinalgo.tools.Tools;

public class StableNode extends Node {

	private static int nbNoeuds() {
		return Tools.getNodeList().size();
	}

	private int pPrevState;

	private int pState;

	private void actions() {
		if (this.gardeRacine()) {
			this.pState = (this.pState + 1) % nbNoeuds();
		}
		if (this.gardeNoeud()) {
			this.pState = this.pPrevState;
		}
	}

	@Override
	public void checkRequirements() throws WrongConfigurationException {
	}

	// affichage du noeud
	@Override
	public void draw(Graphics g, PositionTransformation pt, boolean highlight) {
		if (this.ID == 1) {
			setColor(Color.RED);
		} else {
			setColor(Color.YELLOW);
		}
		String text = "" + this.pState;
		super.drawNodeAsDiskWithText(g, pt, highlight, text, 20, Color.black);
	}

	/**
	 * Envoi régulier de l'état à tous les noeuds voisins
	 */
	public void envoi() {
		// la fonction broadcast envoie un message au successeur
		send(new StableMessage(this.pState), successeur());
		// waitTimer appelle de nouveau envoi
		// dans 20 unités de temps
		(new WaitTimer()).startRelative(20, this);
	}

	private boolean gardeNoeud() {
		return (this.ID == 1) ? false : this.pState != this.pPrevState;
	}

	private boolean gardeRacine() {
		return (this.ID == 1) ? this.pState == this.pPrevState : false;
	}

	/**
	 * Retourne le numéro du canal permettant de communiquer avec le noeud
	 * d'identifiant ID. Si le noeud n'est pas trouvé dans les voisin du noeud,
	 * la fonction retourne -1
	 *
	 * @param ID
	 *            identifiant du noeud
	 * @return numéro du canal
	 */
	private int getIndex(int ID) {
		int j = 0;
		for (Edge e : this.outgoingConnections) {
			if (e.endNode.ID == ID) {
				return j;
			}
			j++;
		}
		return -1;
	}

	/**
	 * Retourne l'identifiant du noeud connecté au canal 'indice‘ du noeud. Si
	 * l'indice n'existe pas, la fonction retourne -1
	 *
	 * @param indice
	 * @return identifiant du noeud.
	 */
	private int getVoisin(int indice) {
		if (indice >= this.nbVoisins() || indice < 0) {
			return -1;
		}
		Iterator<Edge> iter = this.outgoingConnections.iterator();
		for (int j = 0; j < indice; j++) {
			iter.next();
		}
		return iter.next().endNode.ID;
	}

	@Override
	public void handleMessages(Inbox inbox) {
		// on parcourt la liste de tous les messages reçus
		while (inbox.hasNext()) {
			Message m = inbox.next();
			if (m instanceof StableMessage) {
				StableMessage msg = (StableMessage) m;
				this.pPrevState = msg.state;
			}
		}
		// l’état du voisin peut avoir changé donc MAJ de l'état du noeud
		this.actions();
	}

	@Override
	public void init() {
		(new InitTimer()).startRelative(1, this);
	}

	private int nbVoisins() {
		return this.outgoingConnections.size();
	}

	@Override
	public void neighborhoodChange() {
	}

	@Override
	public void postStep() {
	}

	@Override
	public void preStep() {
	}

	public void start() {
		this.pPrevState = (int) ((Math.random() * 10000) % nbNoeuds());
		this.pState = (int) ((Math.random() * 10000) % nbNoeuds());
		(new WaitTimer()).startRelative(20, this);
	}

	private Node successeur() {
		int succ = (this.ID % this.nbNoeuds()) + 1;
		for (Edge e : this.outgoingConnections) {
			if (e.endNode.ID == succ) {
				return e.endNode;
			}
		}
		return null;
	}

}
