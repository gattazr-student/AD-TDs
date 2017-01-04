package projects.bfs.nodes.nodeImplementations;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Iterator;

import projects.bfs.nodes.messages.BFSMessage;
import projects.stable.nodes.timers.InitTimer;
import projects.stable.nodes.timers.WaitTimer;
import sinalgo.configuration.WrongConfigurationException;
import sinalgo.gui.transformation.PositionTransformation;
import sinalgo.nodes.Node;
import sinalgo.nodes.edges.Edge;
import sinalgo.nodes.messages.Inbox;
import sinalgo.nodes.messages.Message;
import sinalgo.tools.Tools;

public class BFSNode extends Node {

	public static int D;

	private static int nbNoeuds() {
		return Tools.getNodeList().size();
	}

	private int pDistance;
	private int[] pDistancesVoisins;
	private int pPere;

	private void actions() {
		if (regle1()) {
			this.pDistance = minVoisinsD() + 1;
		}
		if (regle2()) {
			for (int wI = 0; wI < this.pDistancesVoisins.length; wI++) {
				if (this.pDistancesVoisins[wI] == this.pDistance - 1) {
					this.pPere = getVoisin(wI);
					break;
				}
			}
		}
	}

	@Override
	public void checkRequirements() throws WrongConfigurationException {
	}

	// affichage du noeud
	@Override
	public void draw(Graphics g, PositionTransformation pt, boolean highlight) {
		this.setColor(Color.YELLOW);
		String text = "" + this.pDistance;
		super.drawNodeAsDiskWithText(g, pt, highlight, text, 20, Color.black);
	}

	/**
	 * Envoi régulier de l'état à tous les noeuds voisins
	 */
	public void envoi() {
		// la fonction broadcast envoie un message au successeur
		broadcast(new BFSMessage(this.ID, this.pDistance));
		// waitTimer appelle de nouveau envoi
		// dans 20 unités de temps
		(new WaitTimer()).startRelative(20, this);
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
		if (indice >= nbVoisins() || indice < 0) {
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
			if (m instanceof BFSMessage) {
				BFSMessage msg = (BFSMessage) m;
				this.pDistancesVoisins[getIndex(msg.ID)] = msg.distance;
			}
		}
		// l’état du voisin peut avoir changé donc MAJ de l'état du noeud
		this.actions();
	}

	@Override
	public void init() {
		(new InitTimer()).startRelative(1, this);
	}

	private int minVoisins() {
		int wMin = this.pDistancesVoisins[0];
		for (int wI = 1; wI < this.pDistancesVoisins.length; wI++) {
			wMin = Math.min(wMin, this.pDistancesVoisins[wI]);
		}
		return wMin;
	}

	private int minVoisinsD() {
		return Math.min(minVoisins(), D - 1);
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

	private boolean regle1() {
		return this.pDistance != minVoisinsD() + 1;
	}

	private boolean regle2() {
		return (this.pDistance == minVoisins() + 1)
				&& (this.pDistance != this.pDistancesVoisins[getIndex(this.pPere)] + 1);
	}

	public void start() {
		D = nbNoeuds();
		if (this.ID == 1) {
			this.pPere = 0;
			this.pDistance = 0;
		} else {
			this.pPere = -1;
			this.pDistance = D;
		}
		this.pDistancesVoisins = new int[nbVoisins()];
		(new WaitTimer()).startRelative(20, this);
	}

}
