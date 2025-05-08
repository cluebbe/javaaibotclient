package bytegrammer.javaaibotclient;

import java.io.Console;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AIBotClient {

	public static void main(String[] args) {
		System.out.println("Hello Client!");
		// Create the console object
		Console cnsl = System.console();

		if (cnsl == null) {
			System.out.println("No console available");
			return;
		}

		// Read line
		String username = cnsl.readLine("Enter username : ");

		// Read password
		// into character array
		String password = new String(cnsl.readPassword("Enter password : "));

		// Read line
		String personToImpersonate = cnsl.readLine("Enter person to impersonate : ");

		// Print password
		System.out.println("Please start talking!");
		HttpClient client = HttpClient.newHttpClient();
		while (true) {
			// Read line
			String userMessage = cnsl.readLine(username + " : ");

			try {
				HttpRequest request = HttpRequest.newBuilder().uri(new URI("http://localhost:8081/chatbot"))
						.POST(HttpRequest.BodyPublishers.ofString("{ \"impersonate\":\"" + personToImpersonate
								+ "\", \"request\":\"" + userMessage + "\n}"))
						.build();
				HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
				
				String [] parts = response.body().replaceAll("{", "").replaceAll("}", "").trim().split(":");
				if(parts.length != 2) {
					throw new IllegalStateException("Server responded with invalid format.");
				}
				System.out.println(personToImpersonate + " : " + parts[1]);
		
			} catch (Exception e) {
				System.out.println("Something went wrong: " + e.getMessage());
			}

		}

	}
}
