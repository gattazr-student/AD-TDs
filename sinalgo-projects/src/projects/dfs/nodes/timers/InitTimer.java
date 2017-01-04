package projects.dfs.nodes.timers;

import projects.dfs.nodes.nodeImplementations.DfsNode;
import sinalgo.nodes.timers.Timer;

public class InitTimer extends Timer {

	/** the function "fire" is called when the timer is over */
	@Override
	public void fire() {
		// The node here must be a DfsNode
		try {
			DfsNode wNode = (DfsNode) getTargetNode();
			wNode.start();
		} catch (ClassCastException aException) {
			aException.printStackTrace();
		}

	}
}
