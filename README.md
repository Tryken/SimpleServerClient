# SimpleServerClient (Alpha not stable)

>Welcome to our simplification of server/client sockets.
I think that as a programmer you shouldn't waste time on such things as taking care of a server client system because it can be much easier.
Our system supports by default an event system, AES 256bit encryption, chat functions, server password protection, maximum connection number and much more.

## Features

* event system
* 256bit AES encryption
* password protection
* maximum user count
* json datapackage de/serialization
* encrypted  chat out of the box

---

## Example


### Server

>This small example shows the basic implementation of a server.

```java
public class MyServer extends Server {

	public MyServer(int port, String password, int maxClients) {
		super(port, maxClients, password);
	}

        //register Stuff
	@Override
	public void onInit() {

	}

	@Override
	public void onStart() {

	}

	@Override
	public void onStop() {

	}

        //Update loop 20 ticks/ps
	@Override
	public void onUpdate(float deltaTime) {

	}
}

public class Main {

    public static main(String[] args) {

        MyServer server = new MyServer(25565, "", -1);
        server.start();
    }
}
```

### Client

>This small example shows the basic implementation of a client.

```java
public class MyClient extends Client {

	public MyClient(String host, int port) {
		super(host, port);
	}

	@Override
	public void onConnected() {

	}

	@Override
	public void onDisconnected() {

	}

	@SuppressWarnings("resource")
	@Override
	public String onPasswordRequired() {

		System.out.print("Server password: ");
		return new Scanner(System.in).nextLine();
	}

	@Override
	public void onAuthenticated() {

		while (true) {

			@SuppressWarnings("resource")
			String message = new Scanner(System.in).nextLine();
			sendDataPackage(new MessageDataPackage(message));
		}
	}
}

public class Main {

    public static main(String[] args) {

        MyClient client = new MyClient("localhost", 25565);
        client.connect();
    }
}
```