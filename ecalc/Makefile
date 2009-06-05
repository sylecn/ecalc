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
	MachineTest.java \
	BaseScreen.java \
	ScreenManager.java \
	ConsoleScreen.java \
	ConsoleScreenTest.java \
	NoSuchOperationException.java \
	Keyboard.java \
	KeyboardTest.java \
	ConsoleKeyboard.java \
	NoNumberKeyForGivenNumber.java \
	NoOpKeyForGivenString.java \
	LazyConsoleScreen.java \
	Ecalc.java \
	IFScreen.java 

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class

test: check
check:default
	$(JUNIT) ecalc.KeysTest ecalc.MachineTest ecalc.ConsoleScreenTest \
	ecalc.KeyboardTest

check-junit:
	$(JUNIT) org.junit.tests.AllTests
tags:
	etags *.java
temp.class:Temp.java
	$(JC) Temp.java
temp:Temp.class
	$(JAVA) ecalc/Temp
main:default
	$(JAVA) ecalc/Ecalc