package de.aditosoftware.vaadin.addon.testlabel.client;

import com.vaadin.shared.communication.SharedState;

import java.util.*;

/**
 * Implements the state for the TestLabelExtension. This just holds the labels as a Key/Value Map.
 */
public class TestLabelExtensionState extends SharedState {
  public final Map<String, String> labels = new HashMap<>();

  public String prefix = "data-test-";
}
