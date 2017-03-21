# java compiler
JCC = javac

# output directory (added to classpath)
OUTDIR = bin/

# input directory
INDIR = src/

# compilation flags
JFLAGS = -g -d $(OUTDIR) -cp $(INDIR)

# typing 'make' will invoke the first target entry in the makefile
# (the default one in this case)
default: Peer.class ControlChannelListener.class ControlChannelMessenger.class

# this target entry builds the Average class
# the Average.class file is dependent on the Average.java file
# and the rule associated with this entry gives the command to create it
Peer.class: $(INDIR)Peer.java
	$(JCC) $(JFLAGS) $(INDIR)Peer.java

ControlChannelListener.class: $(INDIR)ControlChannelListener.java
	$(JCC) $(JFLAGS) $(INDIR)ControlChannelListener.java

ControlChannelMessenger.class: $(INDIR)ControlChannelMessenger.java
	$(JCC) $(JFLAGS) $(INDIR)ControlChannelMessenger.java

# To start over from scratch, type 'make clean'.
# Removes all .class files, so that the next make rebuilds them
clean:
	$(RM) $(OUTDIR)*.class
