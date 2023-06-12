public class CommandList {
    private final Server server;
    private final boolean debugOn;
    private final String INCORRECT_NUM_ARGS_MESSAGE = "901 Incorrect number of arguments";
    private final String INVALID_ARGUMENT_MESSAGE = "902 Invalid Argument";
	private final String SUPPLIED_COMMAND_MESSAGE = "903 Supplied command not expected at this time";
    private final String PROCESSING_ERROR_MESSAGE = "999 Processing error. ";
    private String currDict;

    public CommandList(boolean debugOn) {
        this.debugOn = debugOn;
        server = new Server(debugOn);
    }

    public void open(String[] arguments) {
        try {
            if (!server.isConnected()) {
                checkNumArgs(arguments, 2);
                server.createSocketConnection(arguments[0], Integer.parseInt(arguments[1]));
                currDict = "*";
            } else {
                System.out.println(SUPPLIED_COMMAND_MESSAGE);
            }
        } catch (NumberFormatException e) {
            System.out.println(INVALID_ARGUMENT_MESSAGE);
        } catch (NumArgsException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(PROCESSING_ERROR_MESSAGE + "Something unexpected happened. The command could not be run.");
        }
    }

    public void dict(String[] arguments) {
        try {
            isSuppliedCommand();
            checkNumArgs(arguments, 0);

            server.getDict();
		} catch (NumArgsException | SuppliedCommandException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(PROCESSING_ERROR_MESSAGE + "Something unexpected happened. The command could not be run.");
        }
    }

    public void set(String[] arguments) {
        try {
            isSuppliedCommand();
            checkNumArgs(arguments, 1);

            currDict = arguments[0];
        } catch (NumArgsException | SuppliedCommandException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(PROCESSING_ERROR_MESSAGE + "Something unexpected happened. The command could not be run.");
        }
    }

    public void define(String[] arguments) {
        try {
            isSuppliedCommand();
            checkNumArgs(arguments, 1);

            server.getDefine(currDict, arguments[0]);
        } catch (NumArgsException | SuppliedCommandException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(PROCESSING_ERROR_MESSAGE + "Something unexpected happened. The command could not be run.");
        }
    }

    public void match(String[] arguments) {
        try {
            isSuppliedCommand();
            checkNumArgs(arguments, 1);

            server.getMatch(currDict, "exact", arguments[0], false);
        } catch (NumArgsException | SuppliedCommandException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(PROCESSING_ERROR_MESSAGE + "Something unexpected happened. The command could not be run.");
        }
    }

    public void prefixMatch(String[] arguments) {
        try {
            isSuppliedCommand();
            checkNumArgs(arguments, 1);

            server.getMatch(currDict, "prefix", arguments[0], false);
        } catch (NumArgsException | SuppliedCommandException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(PROCESSING_ERROR_MESSAGE + "Something unexpected happened. The command could not be run.");
        }
    }

    public void close(String[] arguments) {
        try {
            isSuppliedCommand();
            checkNumArgs(arguments, 0);

            server.closeSocketConnection();
        } catch (NumArgsException | SuppliedCommandException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(PROCESSING_ERROR_MESSAGE + "Something unexpected happened. The command could not be run.");
        }
    }

    public void quit(String[] arguments) {
        try {
            checkNumArgs(arguments, 0);
            if (debugOn) {
                    System.out.println("> QUIT");
                }
            if (server.isConnected()) {
                server.closeSocketConnection();
            }
            System.exit(0);
        } catch (NumArgsException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(PROCESSING_ERROR_MESSAGE + "Something unexpected happened. The command could not be run.");
        }
    }

    public void checkNumArgs(String[] args, int allowed) throws NumArgsException {
        if (args.length != allowed) {
            throw new NumArgsException(INCORRECT_NUM_ARGS_MESSAGE);
        }
    }

    public void isSuppliedCommand() throws SuppliedCommandException {
        if (!server.isConnected()) {
            throw new SuppliedCommandException(SUPPLIED_COMMAND_MESSAGE);
        }
    }
}