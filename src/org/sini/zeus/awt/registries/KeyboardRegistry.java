package org.sini.zeus.awt.registries;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import org.sini.zeus.awt.Registry;

/**
 * KeyboardRegistry.java
 * @version 1.0.0
 * @author Evelus Development (SiniSoul)
 */
public class KeyboardRegistry implements KeyListener, Registry {

    /**
     * The key typed mask for the keyboard states.
     */
    private static final int TYPEDMASK = 0x1;

    /**
     * The pressed mask for the keyboard states.
     */
    private static final int PRESSEDMASK = 0x2;

    /**
     * The key states array.
     */
    private byte[] states = null;
    
    /**
     * The key codes array.
     */
    private byte[] codes = null;
    
    /**
     * The cache stack of keys activated.
     */
    private char[] cachestack = null;
    
    /**
     * The current read position on the active stack.
     */
    private int activepos = 0;
    
    /**
     * A key was typed.
     * @param e The key event.
     */
    @Override
    public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();
        states[c] |= TYPEDMASK;
    }

    /**
     * Queries if a key is typed and then pops its value.
     * @return If the specified key was typed.
     */
    public boolean queryTyped(char c) {
        boolean bool = (states[c] & TYPEDMASK) != 0;
        states[c] &= ~TYPEDMASK;
        return bool;
    }

    /**
     * A key was pressed.
     * @param e The key event.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        char c = e.getKeyChar();
        int code = e.getKeyCode();
        if((int) c !=  65535)
            states[c] |= PRESSEDMASK;
        if(code != -1)
            codes[code] |= PRESSEDMASK;
        if(c != '`' && c >= 0x20 && c < 0x7E || c == '\n' || c == '\t' || c == '\r') {
            cachestack[activepos] = c;
            activepos = (activepos + 1) % cachestack.length;
        }
    }
    
    /**
     * Queries if a key is typed but does not pop its value.
     * @return If the specified key was typed.
     */
    public boolean popCode(int code) {
        boolean bool = (codes[code] & PRESSEDMASK) != 0;
        codes[code] &= ~PRESSEDMASK;
        return bool;
    }

    /**
     * Queries if a key is typed but does not pop its value.
     * @return If the specified key was typed.
     */
    public boolean queryPressed(char c) {
        return (states[c] & PRESSEDMASK) != 0;
    }

    /**
     * A key was released.
     * @param e The key event.
     */
    @Override
    public void keyReleased(KeyEvent e) {
        char c = e.getKeyChar();
        if((int) c != 65535)
            states[c] &= ~PRESSEDMASK;
    }

    /**
     * Binds this registry to a component.
     * @param component The component to bind the registry to.
     */
    @Override
    public void bind(Component component) {
        component.addKeyListener(this);
    }

    /**
     * Unbinds this registry to a component.
     * @param component The component to unbind the registry from.
     */
    @Override
    public void unbind(Component component) {
        component.removeKeyListener(this);
    }
    
    /**
     * Pops the string written since the last pop.
     * @return The string of the keys pressed.
     */
    public String pollTypedString() {
        if(activepos <= 0)
            return null;
        String str = new String(cachestack, 0, activepos);
        return str;
    }
    
    /**
     * Pops the string written since the last pop.
     * @return The string of the keys pressed.
     */
    public String popTypedString() {
        if(activepos <= 0)
            return null;
        String str = new String(cachestack, 0, activepos);
        activepos = 0;
        return str;
    }

    /**
     */
    public KeyboardRegistry() {
        cachestack = new char[256];
        states = new byte[256];
        codes = new byte[256];
    }
}
