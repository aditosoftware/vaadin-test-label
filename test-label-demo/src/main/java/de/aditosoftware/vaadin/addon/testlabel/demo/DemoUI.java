package de.aditosoftware.vaadin.addon.testlabel.demo;

import com.vaadin.annotations.*;
import com.vaadin.server.*;
import com.vaadin.ui.*;
import de.aditosoftware.vaadin.addon.testlabel.TestLabel;

import javax.servlet.annotation.WebServlet;

@Theme("demo")
@Title("MyComponent Add-on Demo")
@SuppressWarnings("serial")
public class DemoUI extends UI {

  static {
    TestLabel.setInterceptor(null);
  }

  @WebServlet(value = "/*", asyncSupported = true)
  @VaadinServletConfiguration(productionMode = false, ui = DemoUI.class)
  public static class Servlet extends VaadinServlet {}

  @Override
  protected void init(VaadinRequest request) {

    // Initialize our new UI component
    final Button component = new Button();

    TestLabel.apply(component).setLabel("type", "button").setLabel("test", "test");

    // Show it in the middle of the screen
    final VerticalLayout layout = new VerticalLayout();
    layout.setStyleName("demoContentLayout");
    layout.setSizeFull();
    layout.setMargin(false);
    layout.setSpacing(false);
    layout.addComponent(component);
    layout.setComponentAlignment(component, Alignment.MIDDLE_CENTER);
    setContent(layout);
  }
}
