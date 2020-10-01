package de.aditosoftware.vaadin.addon.utils;

import de.aditosoftware.vaadin.addon.TestLabel;

/**
 * Defines a global interceptor for the {@link TestLabel}. This can define if the TestLabel is
 * active.
 */
public interface TestLabelInterceptor {
  /**
   * Defines if the TestLabel is active or not.
   *
   * @return If the TestLabel is active.
   */
  boolean active();
}
