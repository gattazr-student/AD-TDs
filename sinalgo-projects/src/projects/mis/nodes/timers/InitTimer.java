package projects.mis.nodes.timers;

import projects.mis.nodes.nodeImplementations.MISNode;
import sinalgo.nodes.timers.Timer;

public class InitTimer extends Timer {

	/** the function "start" is called when the timer is over */
	@Override
	public void fire() {
		// The node here must be a DfsNode
		try {
			MISNode n = (MISNode) getTargetNode();
			n.start();
		} catch (ClassCastException aException) {
			aException.printStackTrace();
		}

	}
}
