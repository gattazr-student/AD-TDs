package projects.bfs.nodes.timers;

import projects.stable.nodes.nodeImplementations.StableNode;
import sinalgo.nodes.timers.Timer;

public class InitTimer extends Timer {

	/** the function "start" is called when the timer is over */
	@Override
	public void fire() {
		// The node here must be a DfsNode
		try {
			StableNode n = (StableNode) getTargetNode();
			n.start();
		} catch (ClassCastException aException) {
			aException.printStackTrace();
		}

	}
}
