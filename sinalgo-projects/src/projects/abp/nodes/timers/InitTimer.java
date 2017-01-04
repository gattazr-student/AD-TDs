package projects.abp.nodes.timers;

import projects.abp.nodes.nodeImplementations.ABPNode;
import sinalgo.nodes.timers.Timer;

public class InitTimer extends Timer {

	/** the function "fire" is called when the timer is over */
	@Override
	public void fire() {
		// The node here must be a ABPNode
		try {
			ABPNode wNode = (ABPNode) getTargetNode();
			wNode.start();
		} catch (ClassCastException aException) {
			aException.printStackTrace();
		}

	}
}
