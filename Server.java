import java.net.*;
import java.io.*;

public class Server {
	private final boolean debugOn;
	private boolean connected = false;
    private Socket echoSocket;
	private PrintWriter out;
	private BufferedReader in;
	private final String PROCESSING_ERROR_MESSAGE = "999 Processing error. ";
	private final String CONTROL_CONNECTION_IO_MESSAGE = "925 Control connection I/O error, closing control connection.";


	public Server(boolean debugOn) {
		this.debugOn = debugOn;
	}

	public boolean isConnected() {
		return this.connected;
	}

    public void createSocketConnection(String hostName, int portNumber) {
		try {
			echoSocket = new Socket();
			SocketAddress socketAddress = new InetSocketAddress(hostName, portNumber);
			echoSocket.connect(socketAddress, 30000);

			out = new PrintWriter(echoSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));

			System.out.println("Connection Established");
			echoSocket.setSoTimeout(30000);
			debugPrint(in.readLine(), true);
			this.connected = true;
		} catch (SocketTimeoutException e) {
			for (StackTraceElement elem : e.getStackTrace()) {
				// connect timeout
				if ("timedFinishConnect".equals(elem.getMethodName())) {
					String control_connection_message = String.format("920 Control connection to %s on port %d failed to open.", hostName, portNumber);
					System.out.println(control_connection_message);
					break;
				// read timeout
				} else if ("timedRead".equals(elem.getMethodName())){
					System.out.println(CONTROL_CONNECTION_IO_MESSAGE);
					break;
				}
			}
		} catch (UnknownHostException e) {
			System.out.println(PROCESSING_ERROR_MESSAGE + "Unknown host name.");
		} catch (Exception e) {
			System.out.println(PROCESSING_ERROR_MESSAGE + "Cannot connect to server.");
		}
	}

	public void closeSocketConnection() {
		try {
			out.println("quit");
			String line;
			while ((line = in.readLine()) != null) {
				if (line.startsWith("221")) {
					debugPrint(line, true);
					break;
				}
			}
			echoSocket.close();
			connected = false;
		} catch (Exception e) {
			System.out.println(PROCESSING_ERROR_MESSAGE + "Couldn't close connection properly.");
		}
	}

	public void getDict() {
		try {
			out.println("show db");
			String line;
			while ((line = in.readLine()) != null) {
				if (line.startsWith("554")) {
					System.out.println(PROCESSING_ERROR_MESSAGE + "No databases present.");
				}

				if (line.startsWith("250")) {
					debugPrint(line, true);
					break;
				} else {
					System.out.println(line);
				}
			}
		} catch (IOException e) {
			System.out.println(CONTROL_CONNECTION_IO_MESSAGE);
			closeSocketConnection();
		} catch (Exception e) {
			System.out.println(PROCESSING_ERROR_MESSAGE + "Something unexpected happened. The command could not be run.");
		}
	}

	public void getDefine(String currDict, String word) {
		try {
			out.println("define " + currDict + " " + word);
			debugPrint("DEFINE " + currDict + " " + word, false);
			String line;
			while ((line = in.readLine()) != null) {
				if (line.startsWith("550")) {
					System.out.println(PROCESSING_ERROR_MESSAGE + "Invalid database.");
					break;
				}

				if (line.startsWith("552")) {
					System.out.println("****No definition found****");
					getMatch(currDict, ".", word, true);
					break;
				}

				if (line.startsWith("250")) {
					debugPrint(line, true);
					break;
				}

				if (line.startsWith("150")) {
					debugPrint(line, true);
					continue;
				}

				if (line.startsWith("151")) {
					debugPrint(line, true);
					System.out.println("@ " + line.substring(word.length() + 7));
				} else {
					System.out.println(line);
				}
			}
		} catch (IOException e) {
			System.out.println(CONTROL_CONNECTION_IO_MESSAGE);
			closeSocketConnection();
		} catch (Exception e) {
			System.out.println(PROCESSING_ERROR_MESSAGE + "Something unexpected happened. The command could not be run.");
		}
	}

	public void getMatch(String currDict, String strategy, String word, boolean fromDefine) {
		try {
			out.println("match " + currDict + " " + strategy + " " + word);
			debugPrint("MATCH " + currDict + " " + strategy + " " + word, false);
			String line;
			while ((line = in.readLine()) != null) {
				if (line.startsWith("550")) {
					System.out.println(PROCESSING_ERROR_MESSAGE + "Invalid database.");
					break;
				}

				if (line.startsWith("551")) {
					System.out.println(PROCESSING_ERROR_MESSAGE + "Invalid strategy.");
					break;
				}

				if (line.startsWith("552")) {
					if (fromDefine) {
						System.out.println("****No matches found****");
					} else {
						System.out.println("****No matching word(s) found****");
					}	
					break;
				}

				if (line.startsWith("250")) {
					debugPrint(line, true);
					break;
				}

				if (line.startsWith("152")) {
					debugPrint(line, true);
				} else {
					System.out.println(line);
				}
			}
		} catch (IOException e) {
			System.out.println(CONTROL_CONNECTION_IO_MESSAGE);
			closeSocketConnection();
		} catch (Exception e) {
			System.out.println(PROCESSING_ERROR_MESSAGE + "Something unexpected happened. The command could not be run.");
		}
	}

	public void debugPrint(String msg, boolean isStatusMessage) {
		if (debugOn) {
			if (isStatusMessage) {
				System.out.println("<-- " + msg);
			} else {
				System.out.println("> " + msg);
			}
		}
	}
}