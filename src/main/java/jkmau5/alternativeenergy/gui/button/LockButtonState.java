package jkmau5.alternativeenergy.gui.button;

import jkmau5.alternativeenergy.client.render.ToolTip;

public enum LockButtonState implements IMultiButtonState {

    UNLOCKED(new DefaultButtonTextureSet(224, 0, 16, 16)),
    LOCKED(new DefaultButtonTextureSet(240, 0, 16, 16));

    public static final LockButtonState[] VALUES = values();
    private final IButtonTextureSet texture;

    private LockButtonState(IButtonTextureSet texture) {
        this.texture = texture;
    }

    public String getLabel() {
        return "";
    }

    public IButtonTextureSet getTextureSet() {
        return this.texture;
    }

    public ToolTip getToolTip() {
        return null;
    }
}