package de.aditosoftware.vaadin.addon;

import com.vaadin.server.AbstractExtension;
import com.vaadin.ui.AbstractComponent;
import de.aditosoftware.vaadin.addon.client.TestLabelExtensionState;

import java.util.*;

/**
 * Implements the extension which manages the test labels for a single specific component. This
 * extension is always scoped to just one component.
 */
public class TestLabelExtension extends AbstractExtension {
  /**
   * Constructor which just accepts an {@link AbstractComponent}.
   *
   * @param component The component to apply the extension to.
   */
  protected TestLabelExtension(AbstractComponent component) {
    super(component);
  }

  /**
   * Will return all currently applied labels. The returned map is immutable (use getter and setters
   * for labels instead!)
   *
   * @return All applied labels as immutable map.
   */
  protected Map<String, String> getLabels() {
    return Collections.unmodifiableMap(getState(false).labels);
  }

  /**
   * Will apply the given key and value as a new label to the internal mapping. This will always
   * override existing labels with the same key. This apply multiple labels at one just {@link
   * #setLabels(Map)}. Attention: This will not remove the key from the mapping if the value is
   * set to {@code null}. To delete a key use {@link #deleteLabel(String)}.
   *
   * @param key The key of the new label.
   * @param value The value of the new label.
   */
  protected void setLabel(String key, String value) {
    // Access the state with a mark as dirty and put the new key/value label.
    getState().labels.put(key, value);
  }

  /**
   * Will apply the given map as labels. This will only iterate over the map and put all labels into
   * the actual internal mapping. If the given map contains keys which already exist, the existing
   * ones will be overridden.
   *
   * @param labels The labels to apply.
   */
  protected void setLabels(Map<String, String> labels) {
    // Just skip if null or empty.
    if (labels == null || labels.isEmpty()) return;

    // Load the map which holds the mapping and do not mark as dirty on state access.
    Map<String, String> mapping = getState(false).labels;

    // Go through all given labels and put them into the mapping.
    labels.forEach(mapping::put);

    // Mark the current state as dirty.
    markAsDirty();
  }

  /**
   * Will completely delete the given key from the internal mapping. This will just skip the
   * deletion if the key is not set on the mapping.
   *
   * @param key The key to delete from the mapping.
   */
  protected void deleteLabel(String key) {
    // Access the state without a mark as dirty to check if the key even exists. Just skip if it
    // does not exist.
    if (!getState(false).labels.containsKey(key)) return;

    // Remove the key with a mark as dirtyy.
    getState().labels.remove(key);
  }

  @Override
  protected TestLabelExtensionState getState() {
    return (TestLabelExtensionState) super.getState();
  }

  @Override
  protected TestLabelExtensionState getState(boolean markAsDirty) {
    return (TestLabelExtensionState) super.getState(markAsDirty);
  }
}
