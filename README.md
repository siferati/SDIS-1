# SDIS Project 1 - Distributed Backup Service

## How to build

* Open a terminal in ``SDIS-1`` directory
* Type ``./gradlew build``

## How to run

* Open a terminal in ``SDIS-1`` directory
* Type ``./gradlew run`` to start listening
* Open a new terminal in ``SDIS-1`` directory
* Type ``./gradlew run -Pa=<CH>,<MSG>`` to send a message to chosen channel

### Arguments

* ``<CH>`` Multicast Channel
  * ``MC`` Main Channel
  * ``MDB`` Data Backup Channel
  * ``MDR`` Data Restore Channel


* ``<MSG>`` Message
  * ``PUTCHUNK``


* ``<EXIT>`` Exit
  * ``exit`` Call program with only this argument to close all listeners

## How to clean

* Open a terminal in ``SDIS-1`` directory
* Type ``./gradlew clean``


## Generating Docs

### Using ``gradle``

* Open terminal in ``SDIS-1`` directory
* Type ``./gradlew javadoc``
* Navigate to ``build/docs/javadoc`` directory
* Open ``index.html`` in browser
