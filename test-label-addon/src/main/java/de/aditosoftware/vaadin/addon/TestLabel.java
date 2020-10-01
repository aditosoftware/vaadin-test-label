package de.aditosoftware.vaadin.addon;

import com.vaadin.ui.AbstractComponent;
import de.aditosoftware.vaadin.addon.utils.TestLabelPair;

/**
 * Implements a wrapper for the actual {@link TestLabelExtension} with some convenience methods. To
 * create a new TestLabel for a given component, just use {@link #apply(AbstractComponent)}.
 */
public class TestLabel {
  // The instance of the extension, which is managed by this object.
  private final TestLabelExtension extension;

  private TestLabel(TestLabelExtension extension) {
    this.extension = extension;
  }

  /**
   * Will register a new {@link TestLabel} on the given {@link AbstractComponent}. This will return
   * an instanceof of {@link TestLabel} which supports further interaction with the labels. This
   * will also check if there is already an TestLabelExtension registered on the component. If there
   * is already one, it will just wrap the existing one.
   *
   * @param component The component on which the extension shall be applied to.o
   * @return The TestLabel instance.
   */
  public static TestLabel apply(AbstractComponent component) {
    // TODO: Check for existing extension on the component.

    // Create the extension and register it on the given component.
    TestLabelExtension extension = new TestLabelExtension(component);

    // Create a new instance of the TestLabel with the created extension.
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
    // If the value is null, we can just delete the label if it exists.
    if (value == null) extension.deleteLabel(key);
    else extension.setLabel(key, value);

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
    // Skip if there are no pairs to add.
    if (pairs == null || pairs.length == 0) return this;

    // Iterate through the array and add each pair as label.
    for (TestLabelPair pair : pairs)
      if (pair != null) extension.setLabel(pair.getKey(), pair.getValue());

    return this;
  }
}
