# java compiler
JCC = javac

# output directory (added to classpath)
OUTDIR = bin/

# compilation flags
JFLAGS = -g -d $(OUTDIR)

# typing 'make' will invoke the first target entry in the makefile
# (the default one in this case)
default: Peer.class ControlChannelListener.class ControlChannelMessenger.class

# this target entry builds the Average class
# the Average.class file is dependent on the Average.java file
# and the rule associated with this entry gives the command to create it
Peer.class: Peer.java
	$(JCC) $(JFLAGS) Peer.java

ControlChannelListener.class: ControlChannelListener.java
	$(JCC) $(JFLAGS) ControlChannelListener.java

ControlChannelMessenger.class: ControlChannelMessenger.java
	$(JCC) $(JFLAGS) ControlChannelMessenger.java

# To start over from scratch, type 'make clean'.
# Removes all .class files, so that the next make rebuilds them
clean:
	$(RM) $(OUTDIR)*.class
