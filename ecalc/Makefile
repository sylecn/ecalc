CLASSPATH = .:/home/sylecn/projects/ecalc/:/home/sylecn/fromsource/junit4.6/junit-4.6.jar:/home/sylecn/fromsource/junit4.6/
JC = javac -cp $(CLASSPATH)
JAVA = java -cp $(CLASSPATH)
JUNIT = $(JAVA) org.junit.runner.JUnitCore

.SUFFIXES: .java .class
.java.class:
	$(JC) $*.java

CLASSES = \
	Keys.java \
	KeysTest.java \
        Machine.java \
	MachineTest.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class

test: check
check:default
	$(JUNIT) ecalc.KeysTest ecalc.MachineTest

check-junit:
	$(JUNIT) org.junit.tests.AllTests
tags:
	etags *.java
