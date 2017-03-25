# SDIS Project 1 - Distributed Backup Service

## How to build

* Open a terminal in ``SDIS-1`` directory
* Type ``./gradlew build``

## How to run

### Using ``gradle``

* Open a terminal in ``SDIS-1`` directory
* Type ``./gradlew run`` to start listening
* Open a new terminal in ``SDIS-1`` directory
* Type ``./gradlew run -Pa=MC|MDB|MDR,<MESSAGE>`` to send a message to chosen channel

### Using ``-classpath``

* Open a terminal in ``SDIS-1`` directory
* Type ``java -cp build/classes/main Peer`` to start listening
* Open a new terminal in ``SDIS-1`` directory
* Type ``java -cp build/classes/main Peer MC|MDB|MDR <MESSAGE>`` to send a message to chosen channel

### Using ``-jar``

* Open a terminal in ``SDIS-1`` directory
* Type ``java -jar build/libs/SDIS-1.jar`` to start listening
* Open a new terminal in ``SDIS-1`` directory
* Type ``java -jar build/libs/SDIS-1.jar MC|MDB|MDR <MESSAGE>`` to send a message to chosen channel

## How to clean

* Open a terminal in ``SDIS-1`` directory
* Type ``./gradlew clean``


## Generating Docs

### Using ``gradle``

* Open terminal in ``SDIS-1`` directory
* Type ``./gradlew javadoc``
* Navigate to ``build/docs/javadoc`` directory
* Open ``index.html`` in browser

### Using ``javadoc``

* Open terminal in ``SDIS-1`` directory
* Type ``javadoc -d build/docs/javadoc src/main/java/*.java``
* Navigate to ``build/docs/javadoc`` directory
* Open ``index.html`` in browser
