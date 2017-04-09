# SDIS Project 1 - Distributed Backup Service

## How to build

* Open a terminal in ``SDIS-1`` directory
* Type ``./gradlew build``

## How to run

### Using ``gradle``

* Open a terminal in ``SDIS-1`` directory
* Type ``./gradlew run -Pa=<MC_addr>:<MC_port>,<MDB_addr>:<MDB_port>,<MDR_addr>:<MDR_port>,<PROTO_VERSION>,<SERVER_ID>,<ACCESS_POINT>`` to start listening
* Open a new terminal in ``SDIS-1`` directory
* Type ``./gradlew run -Pa=<MC_addr>:<MC_port>,<MDB_addr>:<MDB_port>,<MDR_addr>:<MDR_port>,<PROTO_VERSION>,<SERVER_ID>,<ACCESS_POINT>,<CH>,<MSG>`` to send a message to chosen channel

### Using ``-jar``

* Open a terminal in ``SDIS-1`` directory
* Type ``java -jar build/libs/SDIS-1.jar <MC_addr>:<MC_port> <MDB_addr>:<MDB_port> <MDR_addr>:<MDR_port> <PROTO_VERSION> <SERVER_ID> <ACCESS_POINT>`` to start listening
* Open a new terminal in ``SDIS-1`` directory
* Type ``java -jar build/libs/SDIS-1.jar <MC_addr>:<MC_port> <MDB_addr>:<MDB_port> <MDR_addr>:<MDR_port> <PROTO_VERSION> <SERVER_ID> <ACCESS_POINT> <CH> <MSG>`` to send a message to chosen channel

### Arguments

* ``<CH>`` Multicast Channel
  * ``MC`` Main Channel
  * ``MDB`` Data Backup Channel
  * ``MDR`` Data Restore Channel


* ``<MSG>`` Message
  * ``PUTCHUNK``


* ``<MC_addr>:<MC_port>``
  * 230.0.0.1:8081


* ``<MDB_addr>:<MDB_port>``
  * 230.0.0.2:8082


* ``<MDR_addr>:<MDR_port>``
  * 230.0.0.3:8083


* ``<PROTO_VERSION>``
  * 1.0


* ``<SERVER_ID>``
  * ``Int`` **Different number for each terminal!**


* ``<ACCESS_POINT>``
  * ola

## How to clean

* Open a terminal in ``SDIS-1`` directory
* Type ``./gradlew clean``


## Generating Docs

* Open terminal in ``SDIS-1`` directory
* Type ``./gradlew javadoc``
* Navigate to ``build/docs/javadoc`` directory
* Open ``index.html`` in browser
