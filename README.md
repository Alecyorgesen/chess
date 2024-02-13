# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

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
| `mvn -pl shared tests`     | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

### Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```


https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDAEooDmSAzmFMARDQVqhFHXyFiwUgBF+wAIIgQKLl0wATeQCNgXFDA3bMmdlAgBXbDADEaYFQCerDt178kg2wHcAFkjAxRFRSAFoAPnJKGigALhgAbQAFAHkyABUAXRgAegt9KAAdNABvfMp7AFsUABoYXDVvaA06lErgJAQAX0xhGJgIl04ePgEhaNF4qFceSgAKcqgq2vq9LiaoFpg2joQASkw2YfcxvtEByLkwRWVVLnj2FDAAVQKFguWDq5uVNQvDbTxMgAUQAMsC4OkYItljAAGbmSrQgqYb5KX5cAaDI5uUaecYiFTxNAWBAIQ4zE74s4qf5o25qeIgab8FCveYw4DVOoNdbNL7ydF3f5GeIASQAciCWFDOdzVo1mq12p0YJL0ilkbQcSMPIIaQZBvSMUyWYEFBYwL53hUuSgBdchX9BqK1VLgTKtUs7XVgJbfOkIABrdBujUwP1W1GChmY0LYyl4-UTIkR-2BkNoCnHJMEqjneORPqUeKRgPB9C9aKULGRYLoMDxABMAAYW8USmWM+geugNCYzJZrDZoNJHjBQRBOGgfP5Aph62Ei9W4olUhlsjl9Gp8R25SteRsND0q9RKBEdVTk4SULEEFOkGgbd75Yf+dncXq86IIsa7rFHheN59wdH47giUUQXBSEvVhBEICRRYP11U4UxQAAeeM-0ZEkyWQq9vxUX8YxNZkUFZC0rWfT5o0dWMIMBSVpVlD4fTTK1uxndVNTLfDcwNTDQmw+4uwrLNiygQTL34tDYlEzMF2QBtBIk2IElbFtMg7eSe0wPtTHMKxbDMFBQ0ndhLGYGw-ACIIlKXKIz1XBIZDBYF0mBTdty4Xd7HTMTTxiC9Ey-A1Yg0FAECeFA5h0tADmk0K0OIuiTQiqLzX9WL-MzUCnS4BjYlc8EPPY8tM3hREyr4pKb0E4TYlw8kJKkkLUJvRrSXJRcwBUlc1I0zI9LQfsDKHWxpg0Cc3BgABxO1MWsuc7JCZhBlUxJZvczd2DtYo4sC89wkS9r8yJZAeHm6ouGyjixIStrqWS8IGpiJA4UcSjrTivL6PCUUtqhMtKoQ6qTqeuqsJI-9tAgCAoukGrTp-F7ocZC6wCu1Q5l+jFCsBmBduukGkSJ1QkYhs6MKh1L-wxlI4Sx9QWvjcHrypu83AZpnFNWvqnIGtstNKenGYW3sRoHQzh2wCwoGwKL4DNAwsdnWzeYbf4NuSNIslyMn9py9AOzJiU7R6CTgpzWqObI1ksbmU2fR+in2ZR17KHez6spd4T8fcsqSbBx63ZUeq0fuWH4fIrM2cIlAUrAxk7cCB2neqXHwP+wF1yhMmw01Mn1DjgSaaT+4ybFGRDsk1mQ-jgC7SrjWMH5mJBc0k2m5kCXRsHIybEcSL728GAACkIEfOa7VsbQEFAIMW7W5cBbXZ49ZyA2-LuzMOx6uB4egOpK57murc-ZGiQAK0ntAHbimp98PqAahPh7rcvhPUdpxk3o+r7brlXQJnNQ-sgb+iDrxEuaFw4-0jnDBGsd64GkTvlACTwHZP3vC-N+tFy5gMJjPeCpMFqu3jrA8ujdqgmGgTeVBsZYj6DAOkcilQD7YLwflQqSR17wHsmAdh0AC6EOobQqmFC0FNRrq1D+lNJhSJ6m3Es6khbDX7P3YcZhgDOEQORWAwBsAK0IF+NW84epa36okYq7lPK5CMGfY6yDZIgARlAHGZCUHf0oelaKCgyTuL9tnIqblSrADJGVTiXA6iLCiRGEaIjyZiNEBIhhUiWahCSUSBR-ClFxBUZpNRmAgA
