package proj3;

import java.util.Scanner;

public class CLIThread extends Thread {

	public CLIThread() {

	}

	public void run() {
		ReadFromSystemIn();
	}

	public void ReadFromSystemIn() {
		Scanner sc = new Scanner(System.in);
		System.out.println("Printing the file passed in:");
		while (sc.hasNextLine()) {
			String input = sc.nextLine();
			ParseInput(input);
		}
	}

	public void ParseInput(String input) {
		input += " ";

		String command = input.substring(0, input.indexOf(' '));
		String message = input.substring(input.indexOf(' ') + 1);

		if (command.equals("Read")) {
			System.out.println("Reading!!");
		}
		
		else if (command.equals("Append")) {
			if (message.length() > 0
					&& message.charAt(message.length() - 1) == ' ') {
				message = message.substring(0, message.length() - 1);
			}
			System.out.println("Appending " + message + "$");
		}
	}

}
