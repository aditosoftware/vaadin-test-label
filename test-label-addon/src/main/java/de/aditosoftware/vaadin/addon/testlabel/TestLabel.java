package de.aditosoftware.vaadin.addon.testlabel;

import com.vaadin.ui.AbstractComponent;
import de.aditosoftware.vaadin.addon.testlabel.utils.*;

import java.util.*;

/**
 * Implements a wrapper for the actual {@link TestLabelExtension} with some convenience methods. To
 * create a new TestLabel for a given component, just use {@link #apply(AbstractComponent)}.
 */
public class TestLabel {
  /**
   * Defines the global static interceptor for the TestLabel. The interceptor defines globally if
   * the TestLabel is active or not. This must never be null -> use {@link
   * DefaultTestLabelInterceptor} as default.
   */
  private static TestLabelInterceptor interceptor = DefaultTestLabelInterceptor.INSTANCE;

  // The instance of the extension, which is managed by this object. The extension may be null if
  // the interceptor defines an inactive testlabel.
  private final TestLabelExtension extension;

  private TestLabel(TestLabelExtension extension) {
    this.extension = extension;
  }

  /**
   * Will set the interceptor if the TestLabel statically. If null is given, the default interceptor
   * will be set.
   *
   * @param interceptor The new interceptor to use or null to reset to the default.
   */
  public static void setInterceptor(TestLabelInterceptor interceptor) {
    // Set the default instance if the given interceptor is null.
    if (interceptor == null) TestLabel.interceptor = DefaultTestLabelInterceptor.INSTANCE;
    else TestLabel.interceptor = interceptor;
  }

  /**
   * Will return if there is currently a interceptor defined. As the property must never be null,
   * this only returns true if the current interceptor instance is not of type {@link
   * DefaultTestLabelInterceptor}.
   *
   * @return The there is custom interceptor set.
   */
  public static boolean hasInterceptor() {
    // Just to be sure and check against null. This will return false if the interceptor is the
    // default one, as the consumer of this method is only interested in custom set interceptors.
    if (interceptor == null || interceptor instanceof DefaultTestLabelInterceptor) return false;
    else return true;
  }

  /**
   * Will register a new {@link TestLabel} on the given {@link AbstractComponent}. This will return
   * an instanceof of {@link TestLabel} which supports further interaction with the labels. This
   * will also check if there is already an TestLabelExtension registered on the component. If there
   * is already one, it will just wrap the existing one.
   *
   * <p>If the interceptor of the TestLabel defines an inactive TestLabel, this method will just
   * return a dummy TestLabel which is not actually attached to a real component. The dummy is not
   * reactive new interceptor changes, which means that this method is the only place where the
   * interceptor will be checked.
   *
   * @param component The component on which the extension shall be applied to.o
   * @return The TestLabel instance.
   */
  public static TestLabel apply(AbstractComponent component) {
    // If there is a interceptor and the interceptor defines inactivity, return a dummy TestLabel.
    if (interceptor != null && !interceptor.active()) return new TestLabel(null);

    // Try to load an existing extension from the component. The variable will be null if there is
    // no existing extension on the component.
    TestLabelExtension extension = getExistingExtension(component);
    // Create the extension and apply to the component if there is none yet.
    if (extension == null) extension = new TestLabelExtension(component);

    // Create a new instance of the TestLabel with the extension.
    return new TestLabel(extension);
  }

  /**
   * Will add the label with the given key and value. The return value is this to allow a builder
   * like pattern.
   *
   * @param key The key of the label.
   * @param value The value of the label.
   * @return This.
   */
  public TestLabel setLabel(String key, String value) {
    applyExtensionAware(
        () -> {
          // If the value is null, we can just delete the label if it exists.
          if (value == null) extension.deleteLabel(key);
          else extension.setLabel(key, value);
        });

    return this;
  }

  /**
   * Will add the given labels with each given key and value. The return value is this to allow a
   * builder like pattern.
   *
   * @param pairs The pairs to add.
   * @return This.
   */
  public TestLabel setLabel(TestLabelPair... pairs) {
    applyExtensionAware(
        () -> {
          // Skip if there are no pairs to add.
          if (pairs == null || pairs.length == 0) return;

          // Iterate through the array and add each pair as label.
          for (TestLabelPair pair : pairs)
            if (pair != null) extension.setLabel(pair.getKey(), pair.getValue());
        });

    return this;
  }

  /**
   * Will return all existing labels as a Key/Value map. If there is no extension, then an empty map
   * will be return by default. The returned map is always immutable.
   *
   * @return The map with all labels. Always immutable and never null.
   */
  public Map<String, String> getLabels() {
    // If there is no extension, just return an empty map to avoid null values.
    if (extension == null) return Collections.emptyMap();

    // Load the labels from the extension and wrap it into a unmodifiable map.
    return Collections.unmodifiableMap(extension.getLabels());
  }

  /**
   * Will return if this TestLabel runs in dry mode. Dry mode in this context means, that the
   * TestLabel is not attached to a real component and only works as something like a dummy. A
   * TestLabel is in dry mode when the global interceptor defines an inactive TestLabel.
   *
   * @return If this TestLabel is in dry mode.
   */
  public boolean isDry() {
    return extension == null;
  }

  /**
   * Will apply the given {@link Runnable} safely when an extension is available. If the {@link
   * #extension} is null, the runnable WON'T be executed.
   *
   * @param scope The scoped runnable to run if the extension is available.
   */
  private void applyExtensionAware(Runnable scope) {
    // Run the runnable if the extension is available.
    if (extension != null) scope.run();
  }

  /**
   * Will check if there is already an extension of type {@link TestLabelExtension} registered on
   * the given component and will return this instance if available. If yet no extension exists,
   * this will just return null.
   *
   * @param component The component to check against.
   * @return The instance of the extension or null if non found.
   */
  private static TestLabelExtension getExistingExtension(AbstractComponent component) {
    return component.getExtensions().stream()
        .filter(it -> it instanceof TestLabelExtension)
        .map(TestLabelExtension.class::cast)
        .findFirst()
        .orElse(null);
  }

  /**
   * Implements a TestLabelInterceptor with sane default values. This will be used when no explicit
   * interceptor is defined by the user. This class shall be treated as a static singleton. The
   * singleton instance is provided by {@link #INSTANCE}.
   */
  private static class DefaultTestLabelInterceptor implements TestLabelInterceptor {
    private static final TestLabelInterceptor INSTANCE = new DefaultTestLabelInterceptor();

    @Override
    public boolean active() {
      return true;
    }
  }
}
