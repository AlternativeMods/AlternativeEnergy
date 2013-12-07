package jkmau5.alternativeenergy.gui.button;

import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;

/**
 * Represents an enum of button states
 *
 * @author jk-5
 * @param <T> The button state that shit controller is for
 */
public class MultiButtonController<T extends IMultiButtonState> {

    private int currentState;
    private final T[] validStates;

    private MultiButtonController(int startState, T... validStates) {
        this.currentState = startState;
        this.validStates = validStates;
    }

    public static <T extends IMultiButtonState> MultiButtonController getController(int startState, T... validStates) {
        return new MultiButtonController<T>(startState, validStates);
    }

    public MultiButtonController copy() {
        return new MultiButtonController(currentState, validStates.clone());
    }

    public T[] getValidStates() {
        return validStates;
    }

    public int incrementState() {
        int newState = currentState + 1;
        if (newState >= validStates.length) {
            newState = 0;
        }
        currentState = newState;
        return currentState;
    }

    public int decrementState() {
        int newState = this.currentState - 1;
        if (newState < 0) {
            newState = this.validStates.length - 1;
        }
        this.currentState = newState;
        return this.currentState;
    }

    public void setCurrentState(int state) {
        currentState = state;
    }

    public void setCurrentState(T state) {
        for (int i = 0; i < validStates.length; i++) {
            if (validStates[i] == state) {
                currentState = i;
                return;
            }
        }
    }

    public int getCurrentState() {
        return currentState;
    }

    public T getButtonState() {
        return validStates[currentState];
    }

    public void writeToNBT(NBTTagCompound nbt, String tag) {
        nbt.setString(tag, getButtonState().name());
    }

    public void readFromNBT(NBTTagCompound nbt, String tag) {
        if (nbt.getTag(tag) instanceof NBTTagString) {
            String name = nbt.getString(tag);
            for (int i = 0; i < validStates.length; i++) {
                if (validStates[i].name().equals(name)) {
                    currentState = i;
                    break;
                }
            }
        } else if (nbt.getTag(tag) instanceof NBTTagByte) {
            currentState = nbt.getByte(tag);
        }
    }
}