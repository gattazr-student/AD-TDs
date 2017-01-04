package projects.bfs.nodes.timers;

import projects.stable.nodes.nodeImplementations.StableNode;
import sinalgo.nodes.timers.Timer;

public class WaitTimer extends Timer {

	@Override
	public void fire() {
		StableNode n = (StableNode) this.node;
		n.envoi();
	}
}