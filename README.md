# SDIS Project 1 - Distributed Backup Service

### Ubuntu

#### Running the project

* Open a terminal in ``SDIS-1`` directory
* Type ``make all`` to build the project
* Type ``java -cp bin Peer`` to start listening
* Open a new terminal in ``SDIS-1`` directory
* Type ``java -cp bin Peer MC|MDB|MDR <MESSAGE>`` to send a message to chosen channel

#### Generating Javadoc

* Open terminal in ``SDIS-1`` directory
* Type ``javadoc -d docs src/*.java``
* Navigate to ``docs`` directory
* Open ``index.html`` in browser
