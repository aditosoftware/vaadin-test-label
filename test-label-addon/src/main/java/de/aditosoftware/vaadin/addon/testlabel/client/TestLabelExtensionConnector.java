package de.aditosoftware.vaadin.addon.testlabel.client;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.*;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.extensions.AbstractExtensionConnector;
import com.vaadin.shared.ui.Connect;
import de.aditosoftware.vaadin.addon.testlabel.TestLabelExtension;

import java.util.*;

@Connect(TestLabelExtension.class)
public class TestLabelExtensionConnector extends AbstractExtensionConnector {
  private final Set<String> currentLabels = new HashSet<>();

  @Override
  protected void extend(ServerConnector pServerConnector) {
    applyLabels();
  }

  @Override
  public void onStateChanged(StateChangeEvent stateChangeEvent) {
    super.onStateChanged(stateChangeEvent);

    applyLabels();
  }

  @Override
  public TestLabelExtensionState getState() {
    return (TestLabelExtensionState) super.getState();
  }

  /**
   * Will apply the current labels from the state to the {@link Widget} of the parent connector.
   * This will remove all existing attributes from the element before applying the new attributes
   * based on the current state. After applying the labels, all keys will be added to the {@link
   * #currentLabels} Set.
   */
  void applyLabels() {
    // Load the actual widget.
    Widget widget = getParentWidget();
    if (widget == null) return;

    // Load the element from the widget.
    Element element = widget.getElement();

    // Remove the current labels from the widget.
    cleanCurrentLabels();

    // Load the map with all labels.
    Map<String, String> labels = getState().labels;

    // Iterate through the map and apply each attribute to the element.
    for (Map.Entry<String, String> pair : labels.entrySet()) {
      element.setAttribute(getAttributeName(pair.getKey()), pair.getValue());
      currentLabels.add(pair.getKey());
    }
  }

  /**
   * Will remove all currently set labels from the widget. Just relies on the {@link #currentLabels}
   * set which must be filled properly. After iterating through the labels and removing them, the
   * set will be cleared.
   */
  private void cleanCurrentLabels() {
    // No need to continue if the set is empty.
    if (currentLabels.isEmpty()) return;

    // Load the widget from the parent and exit if null.
    Widget widget = getParentWidget();
    if (widget == null) return;

    // Load the element of the widget.
    Element element = widget.getElement();

    // Iterate through the current labels and remove each attribute.
    for (String label : currentLabels) element.removeAttribute(getAttributeName(label));

    // Clear the set as all attributes have been removed.
    currentLabels.clear();
  }

  /**
   * Will return the actual name for the attribute based on the given key. This will load the prefix
   * from the state and append the given key to it.
   *
   * @param key The key for which the attribute name has to be built.
   * @return The actual name for the attribute for further usage:
   */
  private String getAttributeName(String key) {
    return getState().prefix + key;
  }

  /**
   * Will load the {@link Widget} which will later contain the labels. This may return null as we
   * first have to validate the parents.
   *
   * @return The Widget or null if not available.
   */
  private Widget getParentWidget() {
    // Only a parent of type HasWidget can contain a widget.
    if (!(getParent() instanceof HasWidget)) return null;

    // Load the actual widget from the parent.
    Widget actualWidget = ((HasWidget) getParent()).getWidget();

    // Return the actual widget.
    return actualWidget;
  }
}
