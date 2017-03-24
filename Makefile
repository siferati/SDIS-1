# output directory
BIN = bin/

# input directory
SRC = src/

# java compiler
JC = javac

# compile flags
JFLAGS = -d $(BIN) -cp $(SRC)

# list of all .java files to compile
sourcefiles = $(addprefix $(SRC), \
	BackupChannelListener.java \
	ChannelListener.java \
	ChannelMessenger.java \
	ControlChannelListener.java \
	Peer.java \
	RestoreChannelListener.java)

# list of all .class files to generate
classfiles = $(patsubst $(SRC)%.java,$(BIN)%.class,$(sourcefiles))

# default target
all: $(classfiles)

# compile .java to .class
$(BIN)%.class : $(SRC)%.java
	$(JC) $(JFLAGS) $<

# remove .class files
clean:
	$(RM)  $(BIN)*.class
