package xpug.kata.birthday_greetings;

import static spark.Spark.*;

import java.util.Iterator;

import org.junit.Before;

import com.dumbster.smtp.SimpleSmtpServer;
import com.dumbster.smtp.SmtpMessage;

import spark.Request;

public class Main {
	public static void main(String[] args) {
		get("/", (request, response) -> sendGreetingsView());
		post("/sendGreetings", (request, response) -> {
			SimpleSmtpServer server;
			server = SimpleSmtpServer.start(1081);
			EmployeeRepository repository = new FileEmployeeRepository("employee_data.txt");
			BirthdayService service = new BirthdayService();
			EmailService mail = new SMTPMailService("localhost", 1081);
			service.sendGreetings("employee_data.txt", new OurDate("2008/10/08"), mail);
			service.sendGreetings(new OurDate("2008/10/08"));
			server.stop();
			return "<h2>Emails sent: "+service.quantityOfGreetingsSent()+"</h2>";
		});
	}

	private static String sendGreetingsView() {
		return "<html>"
				+ "<body>"
				+ 	 "<form method='post' action='/sendGreetings'>" 
				+	 	"<div><label>From:</label>"
				+ 		"<input type='text' name='from'></div>"
				+	 	"<div><label>Subject:</label>"
				+ 		"<input type='text' name='subject'></div>"
				+	 	"<div><label>Body:</label>"
				+ 		"<input type='text' name='body'></div>"
				+ 		"<div><input type='submit' value='Send To All'></div>"
				+		"</form>"
				+ "</body>"
				+ "</html>";
	}
}
