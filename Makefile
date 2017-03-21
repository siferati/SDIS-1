# output directory (added to classpath)
BIN = bin/

# input directory
SRC = src/

# java compiler
JC = javac

# compile flags
JFLAGS = -d $(BIN) -cp $(SRC)

sourcefiles = $(addprefix $(SRC), \
	Peer.java \
	ControlChannelListener.java \
	ControlChannelMessenger.java \
	ChannelListener.java \
	ChannelMessenger.java)

classfiles = $(sourcefiles:.java=.class)

all: $(classfiles)

%.class: %.java
	$(JC) $(JFLAGS) $<

clean:
	$(RM)  $(BIN)*.class
