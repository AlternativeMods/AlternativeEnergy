package jkmau5.alternativeenergy.util.interfaces;

import jkmau5.alternativeenergy.gui.button.MultiButtonController;

/**
 * No description given
 *
 * @author jk-5
 */
public interface ILockable extends IOwnable {
    MultiButtonController getLockController();
    boolean isLocked();
    String getInvName();
}
