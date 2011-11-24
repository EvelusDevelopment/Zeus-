package org.sini.zeus.awt.registries;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import org.sini.zeus.awt.Registry;
/**
 * MouseRegistery.java
 * @version 1.0.0
 * @author Evelus Development (SiniSoul)
 */
public class MouseRegistry implements MouseListener, MouseMotionListener, MouseWheelListener, Registry {

    /**
     * The mouse clicked mask for the mouse state.
     */
    private static final int CLICKEDMASK = 0x1;

    /**
     * The mouse pressed mask for the mouse state.
     */
    private static final int PRESSEDMASK = 0x2;

    /**
     * The mouse entered mask for the mouse state.
     */
    private static final int ENTEREDMASK = 0x4;

    /**
     * The location stack for this mouse movements.
     */
    private Point[] locationstack = null;

    /**
     * The current write position in the location stack.
     */
    private int lwriteposition = 0;

    /**
     * The current read position in the location stack.
     */
    private int lreadposition = 0;

    /**
     * The scroll stack for this mouse wheel movements.
     */
    private int[] scrollstack = null;

    /**
     * The current write position in the scroll stack.
     */
    private int swriteposition = 0;

    /**
     * The current read position in the scroll stack.
     */
    private int sreadposition = 0;

    /**
     * The current mouse state.
     */
    private int state = 0;

    /**
     * The mouse was clicked.
     * @param e The mouse event.
     */
    public void mouseClicked(MouseEvent e) {
        state |= CLICKEDMASK | ((e.getClickCount() & 0x3) << 30) | ((e.getButton() & 0x3) << 28);
    }

    /**
     * Queries if the mouse was clicked and then pops its value.
     * @return If the mouse was clicked.
     */
    public boolean queryClicked() {
        boolean bool = (state & CLICKEDMASK) != 0;
        state &= ~CLICKEDMASK;
        return bool;
    }

    /**
     * Queries the amount of times the mouse was clicked and then pops its value.
     * @return The amount of times the mouse was clicked.
     */
    public int queryClickCount() {
        int amount = state >> 30;
        state &= ~(0x3 << 30);
        return amount;
    }

    /**
     * Queries the the id of the button clicked and pops the value.
     * @return The button id that was clicked.
     */
    public int queryButtonId() {
        int id = (state >> 28) & 0x3;
        state &= ~(0x3 << 28);
        return id;
    }

    /**
     * The mouse was pressed.
     * @param e The mouse event.
     */
    public void mousePressed(MouseEvent e) {
        state |= PRESSEDMASK;
        captureLocation(e);
    }

    /**
     * Queries if the mouse is pressed but does not pop its value.
     * @return If the mouse is pressed.
     */
    public boolean queryPressed() {
        return (state & PRESSEDMASK) != 0;
    }

    /**
     * The mouse was released.
     * @param e The mouse event.
     */
    public void mouseReleased(MouseEvent e) {
        state &= ~PRESSEDMASK;
    }

    /**
     * The mouse has entered the component.
     * @param e The mouse event.
     */
    public void mouseEntered(MouseEvent e) {
        state |= ENTEREDMASK;
    }

    /**
     * Queries if the mouse is entered but does not pop its value.
     * @return If the mouse was entered into the component.
     */
    public boolean queryEntered() {
        return (state & ENTEREDMASK) != 0;
    }

    /**
     * The mouse has exited the component.
     * @param e The mouse event.
     */
    public void mouseExited(MouseEvent e) {
        state &= ~ENTEREDMASK;
    }

    /**
     * The mouse was dragged.
     * @param e The mouse event.
     */
    public void mouseDragged(MouseEvent e) {
        captureLocation(e);
    }

    /**
     * The mouse was moved.
     * @param e The mouse event.
     */
    public void mouseMoved(MouseEvent e) {
        captureLocation(e);
    }

    /**
     * Polls a location from the location stack.
     * @return The location at the read position.
     */
    public Point pollLocation() {
        if(lreadposition == lwriteposition)
            return null;
        return locationstack[lreadposition];
    }

    /**
     * Pops a location from the location stack.
     * @return The location at the read position.
     */
    public Point popLocation() {
        if(lreadposition == lwriteposition)
            return null;
        Point p = locationstack[lreadposition];
        lreadposition = lreadposition + 1 & 0x7F;
        return p;
    }

    /**
     * The mouse wheel was moved.
     * @param e The mouse wheel event.
     */
    public void mouseWheelMoved(MouseWheelEvent e) {
        scrollstack[swriteposition] = e.getScrollAmount();
        swriteposition = swriteposition + 1 & 0x7F;
    }

    /**
     * Polls a scroll amount from the scroll stack.
     * @return The scroll amount at the read position.
     */
    public int pollScroll() {
        if(sreadposition == swriteposition)
            return -1;
        return scrollstack[sreadposition];
    }

   /**
     * Pops a scroll amount from the scroll stack.
     * @return The scroll amount at the read position.
     */
    public int popScroll() {
        if(sreadposition == swriteposition)
            return -1;
        int value = scrollstack[sreadposition];
        sreadposition = sreadposition + 1 & 0x7F;
        return value;
    }

    /**
     * Captures a location update even if it hadn't happened.
     * @param e The mouse event.
     */
    private void captureLocation(MouseEvent e) {
        locationstack[lwriteposition] = e.getPoint();
        lwriteposition = lwriteposition + 1 & 0x7F;
    }

    /**
     * Binds this registry to a component.
     * @param component The component to bind the registry to.
     */
    public void bind(Component component) {
        component.addMouseListener(this);
        component.addMouseMotionListener(this);
    }

    /**
     * Unbinds this registry to a component.
     * @param component The component to unbind the registry from.
     */
    public void unbind(Component component) {
        component.removeMouseListener(this);
        component.removeMouseMotionListener(this);
    }

    /**
     */
    public MouseRegistry() {
        locationstack = new Point[256];
        scrollstack = new int[256];
    }
}
