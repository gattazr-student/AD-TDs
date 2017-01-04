package projects.mis.nodes.timers;

import projects.mis.nodes.nodeImplementations.MISNode;
import sinalgo.nodes.timers.Timer;

public class WaitTimer extends Timer {

	@Override
	public void fire() {
		MISNode n = (MISNode) this.node;
		n.envoi();
	}
}