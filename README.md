# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

## Phase 2 Sequence Diagram
https://sequencediagram.org/index.html#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2GADEaMBUljAASij2SKoWckgQaIEA7gAWSGBiiKikALQAfOSUNFAAXDAA2gAKAPJkACoAujAA9D4GUAA6aADeAETtlMEAtih9pX0wfQA0U7jqydAc45MzUyjDwEgIK1MAvpjCJTAFrOxclOX9g1AjYxNTs33zqotQyw9rfRtbO58HbE43FgpyOonKUCiMUyUAAFJForFKJEAI4+NRgACUh2KohOhVk8iUKnU5XsKDAAFUOrCbndsYTFMo1Kp8UYdKUAGJITgwamURkwHRhOnAUaYRnElknUG4lTlNA+BAIHEiFRsyXM0kgSFyFD8uE3RkM7RS9Rs4ylBQcDh8jqM1VUPGnTUk1SlHUoPUKHxgVKw4C+1LGiWmrWs06W622n1+h1g9W5U6Ai5lCJQpFQSKqJVYFPAmWFI6XGDXDp3SblVZPQN++oQADW6ErU32jsohfgyHM5QATE4nN0y0MxWMYFXHlNa6l6020C3Vgd0BxTF5fP4AtB2OSYAAZCDRJIBNIZLLdvJF4ol6p1JqtAzqBJoIei0azF5vDgHYsgwr5ks9K+KDTB+SwAucBaymqKDlAgB48rCcGHqi6KxNiCaGC6YZumSFIGrS5ajiaRLhhaHIwNyvIGoKwowEBoYkW6nYYeUFjot0KAIAY5QAKwDqWfSjOowDkuMUwAKJQN4aY3DAaAQMwABmvicH0ByutKpzsuUcCpCgIANjAoHvAxTJMUml7QaxUDsWgnHcTAAAsTgAIwCUJqgifcElSdA5Q+MEQbQEgABeKDLHsMCOs6yYQSW+7IWouaYP+IJQSUVwDIRoy7DMfTTrOzb-O2aWFNkPYwP2g69FlI45Z80z5UGhXzsVy6rt4fiBF4KDoHuB6+Mwx7pJkmDlReRTUNe0jibu4n1OJzQtA+qhPt0BWNugJVsql5QBs1m1oNiqVsixMBIYNiEDb6KEYuhcqYQS2EsrB11gPtdaHcRZmaYUlpGJxFKGBtc6mWaEbpeCMBIIpMCQHOckKTAykBRw3TOW5-QeV5Yl9JJ0n+YFfrBWFX7RYmsVAvFb3ZslJ0WZNGWlt+V6lV2ORgH2A5DkunAdeugSQrau7QjAADio6ssNp5jeezDpdeYvzUt9ijutB2gz+O1xWmH0zl9KU66dD3lBLowi7EsIW2At1oeTj0yM9pIwLGqQkygestd94NkeUJQw2EINbRp5oM2dMNw4diNKSptro657ksjjVb435dHBMTUCheFan2xqTvujA5JgGbKDW7C3ukZG5GQmiGJF6O53QnnDO7Q35vN-TCtpj0Uyq0JYkVP0-coAAktIYkub2ADMjlPCemQGhWExfDoCCgA2S+jisXwjwAcqOK+RY0LNTWz42c5V3O9H3kuD8Po7j5PM9z1MC-6tl9x9Kv68GVv9XfyePvQ+39j68xXJ4TqG5sA+CgNgbg8BdSZHFo3aWo0L7GzPuUG8DQVZqyJvrOcQ5gGjFPscSmqYrhBzQLMEhKBwJU1-IzKGno9Sl09odWho4D6jHutBfOjEXouyClnMKHC5yV3Mn9ci-tFKBw1sHAuzETbQ1hvDPq8kY6oxgPHTGgkk6iRTr5GSGc3aiJzmQ50T1BHO0UjyDg7C6GSN+tpCi9j26GFonQsGVdIbyjkkqBAHEuKGD4k4ROwlDE+QJh4mAwAEC6g4GEFAAAPaEqhc4YQET9Z2rDMiOO4URHxUjXF5MMCPFuFDgTlDKaXWmCA8xGzDqzTKI8n7lCnrPMhHYGYXy5tVUsbSJ4dJfgcMwfNIECwCJYTicFkgwAAFIQB5Cg0YgQ14b1lhzTBTNqiUjvC0Ee6tPpEN6PA4AMyoBwAgHBKAXDRjj26Uwtu4j0DvnXpc65tz7lj2kMdJplknT+KWTydh1D3kXMoF86APzx58KBQ7EOhc7GcAKQ8v5xSXH-UoraEeQowhDMxaHPxMEAnKmCQYGAYSImeSiXjYxZJG4cAgGoNAAByZgqToSZIetk8G5RXbu1eUdIlEZpF+0oAHOJCi0CiuUVZVRkcEaaORrHHRaAMY0uTtEtOAU5BmOzhFFu1icmFwAFbLLQGi35ELPk3OgM40O4q6LYC0MgkesxgA2hgCAeJBZICxOSBkVIRkPlQvtVAOVzSFWKnJXZEJTknDTy1XS1OaZ4mJLCPIOcPL+Gtx1uUC1oLRz1MaYw+VTMehPM7H0q+1Uq2mHGRAtcXUAheAuV2L0sBgDYHgYQeIiQUgjTPNs7u2CZpzQWktYw20qmXENuWklNTuB4EZBXY1jsbGFxACuqAjI9AGHXUi32Prd1xOVEYbQlS-wFs7aunQpaF2pgrfOrWvS5b9J5k2zAQA

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
