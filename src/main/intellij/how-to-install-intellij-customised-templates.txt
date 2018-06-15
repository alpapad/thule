# HashCode/Equals and toString templates
These templates have been written to take advantage of Java8 features as well as being SonarQube compliant

# Codestyle template
The Codestyle template is identical to the template used by Google
(https://github.com/google/styleguide/blob/gh-pages/intellij-java-google-style.xml)
with the addition of ordering/sorting of method/field names in alphabetical order

# Installation
Copy the contents of intellij/config into your IntelliJ config directory, e.g. <home-directory>/.IntelliJIdea2017.3/config

# Configure Code Style Template
Goto Settings-->Editor-->Code Style-->Java-->Scheme and change the Scheme to "GoogleStyleWithOrdering"

# Configure HashCode/Equals template
When generating a HashCode/Equals method, ensure the template being used is "Objects.equals and hashCode (Java 8) equals".
This should only need to be done once.

# Configure toString template
When generating a toString method, ensure the template being used is "Objects.toString (Java8)"
This should only need to be done once.