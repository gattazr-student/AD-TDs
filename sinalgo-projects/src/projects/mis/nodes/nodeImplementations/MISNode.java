package projects.mis.nodes.nodeImplementations;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Iterator;

import projects.mis.EtatVoisin;
import projects.mis.ValeurS;
import projects.mis.nodes.messages.MISMessage;
import projects.mis.nodes.timers.InitTimer;
import projects.mis.nodes.timers.WaitTimer;
import sinalgo.configuration.WrongConfigurationException;
import sinalgo.gui.transformation.PositionTransformation;
import sinalgo.nodes.Node;
import sinalgo.nodes.edges.Edge;
import sinalgo.nodes.messages.Inbox;
import sinalgo.nodes.messages.Message;

public class MISNode extends Node {

	// pour stocker le dernier état connu de chaque voisin
	private EtatVoisin pNeighborsStates[];
	// état du noeud initialisé au hasard,
	// mais dans son domaine de définition
	private ValeurS pState = ValeurS.RandomInit();

	// Gère la partie "action" des deux règles
	private void actions() {
		if (this.leave()) {
			this.pState = ValeurS.domine;
		}
		if (this.join()) {
			this.pState = ValeurS.Dominant;
		}
	}

	@Override
	public void checkRequirements() throws WrongConfigurationException {
	}

	// la couleur du noeud dépend de son état
	private Color Couleur() {
		if (this.pState == ValeurS.Dominant) {
			return Color.red;
		}
		return Color.yellow;
	}

	// affichage du noeud
	@Override
	public void draw(Graphics g, PositionTransformation pt, boolean highlight) {
		this.setColor(this.Couleur());
		String text = "" + this.ID;
		super.drawNodeAsDiskWithText(g, pt, highlight, text, 20, Color.black);
	}

	/**
	 * Envoi régulier de l'état à tous les noeuds voisins
	 */
	public void envoi() {
		// la fonction broadcast envoie un message à tous les voisins du noeud
		this.broadcast(new MISMessage(this.ID, this.pState));
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
		if (indice >= this.nbVoisins() || indice < 0) {
			return -1;
		}
		Iterator<Edge> iter = this.outgoingConnections.iterator();
		for (int j = 0; j < indice; j++) {
			iter.next();
		}
		return iter.next().endNode.ID;
	}

	/*
	 * Gestion de la réception de messages. La fonction est appelée
	 * régulièrement même si aucun message n'a été reçu
	 */
	@Override
	public void handleMessages(Inbox inbox) {
		// on parcourt la liste de tous les messages reçus
		while (inbox.hasNext()) {
			Message m = inbox.next();
			if (m instanceof MISMessage) {
				MISMessage msg = (MISMessage) m;
				// MAJ de l’état du voisin
				this.pNeighborsStates[this.getIndex(msg.ID)].pState = msg.pState;
				this.pNeighborsStates[this.getIndex(msg.ID)].ID = msg.ID;
			}
		}
		// l’état des voisins peut avoir changé donc MAJ de l'état du noeud
		this.actions();
	}

	@Override
	public void init() {
		(new InitTimer()).startRelative(1, this);
	}

	/**
	 * Garde de la règle Join
	 *
	 * @return true si la règle Join est activable
	 */
	private boolean join() {
		if (this.pState == ValeurS.Dominant) {
			return false;
		}
		for (int i = 0; i < this.nbVoisins(); i++) {
			if (this.pNeighborsStates[i].pState == ValeurS.Dominant && this.pNeighborsStates[i].ID < this.ID) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Garde de la règle Leave
	 *
	 * @return true si la règle Leave est activable
	 */
	private boolean leave() {
		if (this.pState == ValeurS.domine) {
			return false;
		}
		for (int i = 0; i < this.nbVoisins(); i++) {
			if (this.pNeighborsStates[i].pState == ValeurS.Dominant && this.pNeighborsStates[i].ID < this.ID) {
				return true;
			}
		}
		return false;
	}

	private int nbVoisins() {
		return this.outgoingConnections.size();
	}

	@Override
	public void neighborhoodChange() {
		if (this.nbVoisins() > 0) {
			this.pNeighborsStates = new EtatVoisin[this.nbVoisins()];
			for (int i = 0; i < this.nbVoisins(); i++) {
				this.pNeighborsStates[i] = new EtatVoisin();
			}
		}
	}

	@Override
	public void postStep() {
	}

	@Override
	public void preStep() {
	}

	public void start() {
		if (this.nbVoisins() > 0) {
			// création et initialisation
			// du tableau où seront stockés
			// les derniers états connus de chaque voisin
			this.pNeighborsStates = new EtatVoisin[this.nbVoisins()];
			for (int i = 0; i < this.nbVoisins(); i++) {
				this.pNeighborsStates[i] = new EtatVoisin();
			}
		}
		(new WaitTimer()).startRelative(20, this);
	}
}