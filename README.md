# Test Label for Vaadin 8

Advanced identification for components.


## Getting started
Here is a simple example on how to use this extension.

```java
// Use any component:
final Button component = new Button();

// Apply the TestLabel to the component and set the labels. 
TestLabel.apply(component).setLabel("type", "button").setLabel("test", "test");

```

## Building and running demo

git clone <url of the MyComponent repository>
mvn clean install
cd demo
mvn jetty:run

To see the demo, navigate to http://localhost:8080/
