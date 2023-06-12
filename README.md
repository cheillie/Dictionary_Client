# Project Overview
This assignment is a Java program that uses the Java socket related classes to create a client
program to retrieve definitions from a dictionary server using the client/server protocol described in RFC 2229
(https://tools.ietf.org/html/rfc2229) \
The program will read input from the
command line and, based on those commands, communicate with a dictionary server to ultimately retrieve word
definitions\

## Run
The program will be run at the command line by doing\
`java -jar CSdict.jar [-d].`\
Only print the the Status responses if the -d command line option is provided

## Commands
The following table describes the commands that can be entered at the command line:

| Application Command | Description                                                                                                                                                                                                                                                                                                                                 |
|---------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| open SERVER PORT    | Opens a new TCP/IP connection to an dictionary server. The server's name and the port number the server is listening on are specified by the command's parameters. The server name can be either a domain name or an IP address in dotted form. Both the SERVER and PORT values must be provided. This command is considered an unexpected  |
| dict                | Retrieve and print the list of all the dictionaries the server supports. Each line will consist of a single word that is the the name of a dictionary followed by some information about the dictionary. You simply have to print each of these lines as returned by the server                                                             |
| set DICTIONARY      | Set the dictionary to retrieve subsequent definitions and/or matches from. The name of the dictionary is either the first word on one of the lines returned by the dict command or one of the required virtual databases defined in section 3.4 of the RFC                                                                                  |
| define WORD         | Retrieve and print all the definitions for WORD. WORD is looked up in the dictionary or dictionaries as specified through the set command.                                                                                                                                                                                                  |
| match WORD          | Retrieve and print all the exact matches for WORD. WORD is looked up in the dictionary or dictionaries as specified through the set command. The responses from the server will consist of one or more lines of form: database word                                                                                                         |
| prefixmatch WORD    | Retrieve and print all the prefix matches. for WORD. WORD is looked up in the dictionary or dictionaries as specified through the set command                                                                                                                                                                                               |
| close               | After sending the appropriate command to the server and receiving a response, closes the established connection and enters a state where the next command expected is an open or quit                                                                                                                                                       |
| quit                | Closes any established connection and exits the program. This command is valid at any time                                                                                                                                                                                                                                                  |# Dictionary_Client
