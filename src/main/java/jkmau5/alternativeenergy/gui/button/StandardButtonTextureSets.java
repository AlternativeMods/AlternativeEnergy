package jkmau5.alternativeenergy.gui.button;

public enum StandardButtonTextureSets implements IButtonTextureSet {

    LARGE_BUTTON(0, 88, 20, 200),
    SMALL_BUTTON(0, 168, 15, 200),
    LOCKED_BUTTON(224, 0, 16, 16),
    UNLOCKED_BUTTON(240, 0, 16, 16),
    LEFT_BUTTON(204, 0, 16, 10),
    RIGHT_BUTTON(214, 0, 16, 10);

    private final int x;
    private final int y;
    private final int height;
    private final int width;

    private StandardButtonTextureSets(int x, int y, int height, int width) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getHeight() {
        return this.height;
    }

    public int getWidth() {
        return this.width;
    }
}