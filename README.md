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
https://sequencediagram.org/index.html#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2GADEaMBUljAASij2SKoWckgQaIEA7gAWSGBiiKikALQAfOSUNFAAXDAA2gAKAPJkACoAujAA9D4GUAA6aADeAETtlMEAtih9pX0wfQA0U7jqydAc45MzUyjDwEgIK1MAvpjCJTAFrOxclOX9g1AjYxNTs33zqotQyw9rfRtbO58HbE43FgpyOonKUCiMUyUAAFJForFKJEAI4+NRgACUh2KohOhVk8iUKnU5XsKDAAFUOrCbndsYTFMo1Kp8UYdKUAGJITgwamURkwHRhOnAUaYRnElknUG4lTlNA+BAIHEiFRsyXM0kgSFyFD8uE3RkM7RS9Rs4ylBQcDh8jqM1VUPGnTUk1SlHUoPUKHxgVKw4C+1LGiWmrWs06W622n1+h1g9W5U6Ai5lCJQpFQSKqJVYFPAmWFI6XGDXDp3SblVZPQN++oQADW6ErU32jsohfgyHM5QATE4nN0y0MxWMYFXHlNa6l6020C3Vgd0BxTF5fP4AtB2OSYAAZCDRJIBNIZLLdvJF4ol6p1JqtAzqBJoIei0azF5vDgHYsgwr5ks9K+KDTB+SwAucBaymqKDlAgB48rCcGHqi6KxNiCaGC6YZumSFIGrS5ajiaRLhhaHIwNyvIGoKwowEBoYkW6nYYeUFjot0KAIAY5QAKwDqWfSjOowDkuMUwAKJQN4aY3DAaAQMwABmvicH0ByutKpzsuUcCpCgIANjAoHvAxTJMUml7QaxUDsWgnHcTAAAsTgAIwCUJqgifcElSdA5Q+MEQbQEgABeKDLN+cqYQS2Esh6uqZLG-rTiGGnmpGHLRjASXxlFnb-mm+7IWouaYAVzFXmmw63KOuwzH006zs2-ztiCpzZD2MD9oOvQDIRox1dMDVBk184tcuq7eH4gReCg6B7gevjMMe6SZJgHUXkU1DXtI4m7uJ9Tic0LQPqoT7dI1jboK1bIFeUAYjVdaDYuVUFOvKMBIUtiGLb6KEYuheVYYxcXZUFUChSgD11k9xFmZphSWjAJRIIpYSXXOplmhGb3gjAqMwJAc5yQpMDKQFtrdM5bn9B5XliX0knSf5wR+sFYURY6zoxSDpKfb9YDQzOsNY6RGXlBwnEUoYGPXWlOOWe9MH44phNPSTSkqRwVOue5LL01WTN+XRgVsxDHORdB+UQSWRVLdmpWvW9AGW8cFldjkYB9gOQ5Lpwk3roEkK2ru0IwAA4qOrIrae63nswztphU4cHcd9ijhdj2Yz+t022mQujS9edsixEejqHsSwhXYD-WhXOJjz8N80l7NQ7Lz2i+ZiPkSjaMwO3neabjH0E0T83yZrFMwDrNOCfromG75Mms6krecxhGqxXz5JgJHozV7CcPY2REIoGiGIwOnoyfdC9eYcmedkuXt9O4rAFTFfahiRU-SfwAktIMSLlewAGZHJPBPJkA0FYJhfB0AgUADZoG1VgU8T+AA5UcsC9gwEaK7Ds7sNpey6j7XoH8o7f1-qOABQDQHgKmJA-U-V7h9DgQggyyCBqoPIaMTBoxsG4L9iuTwU0NzYB8FAbA3B4AJUMHvQwMc1pEJLpVcoN4GhpwzqbYWc4hwYNHPg38ZwgQAXbrMfRoxwImKMaXT0ep5EFyeuY0cfCUCAytsDJu7owZm0ho4ucR8xbd3KL3dGWc5ZbwVltJW5RR7qwnmTLW080DUz1sJBePlmYmzkKvc24U1J303rzbxikeQcAcRYtxg90rBIomUy+o4hRhEqdUqJpdFTKg4lxQwfEnBpM8hkxmS8n7X2AAgXUHAwgoAAB7QlUAUjenjsblDsZkCpLiiKtJPjAVZhhP6FPdndHZsj5EOwQHmYu7sfxXD6P-QB5RgFgMMZ2Ih3seqljubQp5pgJoiMDgESwnE4LJBgAAKQgDyMu18AjwMQXHT2KjtpJyqJSO8LRP6Zxhro3oUjgCAqgHACAcEoDONGAA55D9rH3TMUZBB+LCXEtJSgABRdrGIpiWCiFaAHE0txfSol0AmUstMIsxuyyfG5L8e3QJXdtLI0oKjMJWKInFIqlZFWatiYJPJryGe-SDaZONgFHJa8FlAzFeGcopTODrLJdIGVCM5WUVtJ-JpDS7VbKuVFBUSoEBdIMDAXp+rBlGzTK6jgEA1BoAAOTMBmdCM1HiLU4RgAAKy5ba5l0h3x0soAy6ADqalyr8FoTI7rgL9xtDssZBZIDlpgMkDIqRaV4rzQKqAnrh7Kw6X6uy3SnJOBAcG7yQysljImWEeQc5E1K2tlStNGbRxnIuWyr1SKrgUvavHN5vtTBmH9n86aAQvB4q7F6WAwBsBSMIPERIKRVpngRYnNRu19qHWOsYG6lLUylDKpcxWeMQDcDwIyQ+Bzk2gyA+exkegDBgfltsqDeA6IdCMNoT1tSkOwHDehhD4sdnAYvUGND8gDnfuBDpQj0gdDLr-au59X6t2ex3b0IRmAgA

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
