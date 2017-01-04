package projects.abp.nodes.timers;

import projects.abp.nodes.nodeImplementations.ABPNode;
import sinalgo.nodes.timers.Timer;

public class RegularTimer extends Timer {

	/** the function "regular" is called when the timer is over */
	@Override
	public void fire() {
		// The node here must be a DfsNode
		try {
			ABPNode wNode = (ABPNode) getTargetNode();
			wNode.regular();
		} catch (ClassCastException aException) {
			aException.printStackTrace();
		}

	}
}
