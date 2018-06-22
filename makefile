JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
		$(JC) $(JFLAGS) $*.java
CLASSES = HuffmanNode.java BinaryHeap.java encoder.java decoder.java
default: classes
classes: $(CLASSES:.java=.class)
clean:
		$(RM) *.class