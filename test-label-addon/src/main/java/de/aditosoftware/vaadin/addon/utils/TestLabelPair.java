package de.aditosoftware.vaadin.addon.utils;

import java.util.Objects;

/** Implements a holder class, which has the {@link #key} and {@link #value} for a label. */
public class TestLabelPair {
  private final String key;
  private final String value;

  public TestLabelPair(String key, String value) {
    Objects.requireNonNull(key);
    Objects.requireNonNull(value);

    this.key = key;
    this.value = value;
  }

  public String getKey() {
    return key;
  }

  public String getValue() {
    return value;
  }
}
