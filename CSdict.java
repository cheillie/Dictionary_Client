import java.lang.System;
import java.io.IOException;
import java.util.Arrays;

public class CSdict {
    static final int MAX_LEN = 255;
    static boolean debugOn = false;
    private static final int PERMITTED_ARGUMENT_COUNT = 1;

    public static void main(String [] args) {
		if (!verifyArgs(args)) return;

		CommandList commandList = new CommandList(debugOn);

		while (true) {	
			try {
				byte[] cmdString = new byte[MAX_LEN];
				System.out.print("csdict> ");
				System.in.read(cmdString);

				// Convert the command string to ASII
				String inputString = new String(cmdString, "ASCII");
				// Split the string into words
				String[] inputs = inputString.trim().split("( |\t)+");
				// Set the command
				String command = inputs[0].toLowerCase().trim();
				// Remainder of the inputs is the arguments.
				String[] arguments = Arrays.copyOfRange(inputs, 1, inputs.length);

				runCommands(commandList, command, arguments);

			} catch (Exception e) {
				System.out.println("998 Input error while reading commands, terminating.");
				System.exit(0);
			}
		}
	}

	public static boolean verifyArgs(String[] args) {
		if (args.length == PERMITTED_ARGUMENT_COUNT) {
            debugOn = args[0].equals("-d");
            if (debugOn) {
                System.out.println("Debugging output enabled");
				return true;
            } else {
                System.out.println("997 Invalid command line option - Only -d is allowed");
                return false;
            }
        } else if (args.length > PERMITTED_ARGUMENT_COUNT) {
            System.out.println("996 Too many command line options - Only -d is allowed");
            return false;
        }
		return true;
	}

	public static void runCommands(CommandList commandList, String command, String[] arguments) {
		switch (command) {
			case "open" -> commandList.open(arguments);
			case "dict" -> commandList.dict(arguments);
			case "set" -> commandList.set(arguments);
			case "define" -> commandList.define(arguments);
			case "match" -> commandList.match(arguments);
			case "prefixmatch" -> commandList.prefixMatch(arguments);
			case "close" -> commandList.close(arguments);
			case "quit" -> commandList.quit(arguments);
			default -> System.out.println("900 Invalid command");
		}
	}
}