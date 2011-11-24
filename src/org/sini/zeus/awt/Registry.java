package org.sini.zeus.awt;

import java.awt.Component;

/**
 * Registry.java
 * @version 1.0.0
 * @author Evelus Development (SiniSoul)
 */
public interface Registry {

    /**
     * Binds this registry to a component.
     * @param component The component to bind the registry to.
     */
    public abstract void bind(Component component);

    /**
     * Unbinds this registry to a component.
     * @param component The component to unbind the registry from.
     */
    public abstract void unbind(Component component);

}
