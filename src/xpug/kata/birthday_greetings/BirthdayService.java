package xpug.kata.birthday_greetings;

import static java.util.Arrays.asList;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class BirthdayService {
	int numberOfGreetingsSent;

	public void sendGreetings(String fileName, OurDate ourDate,
			EmailService mail) throws IOException, ParseException, AddressException, MessagingException {
		System.out.println("Abriendo archivo");
		BufferedReader in = new BufferedReader(new FileReader(fileName));
		String str = "";
		numberOfGreetingsSent = 0;
		str = in.readLine(); // skip header
		System.out.println("Primera linea de archivo");
		while ((str = in.readLine()) != null) {
			String[] employeeData = str.split(", ");
			Employee employee = new Employee(employeeData[1], employeeData[0],
					employeeData[2], employeeData[3]);
			if (employee.isBirthday(ourDate)) {
				mail.sendMessage("sender@here.com", employee);
				numberOfGreetingsSent++;
			}
		}
		
	}

	public static void main(String[] args) {
		EmailService mail = new SMTPMailService("localhost", 25); 
		BirthdayService service = new BirthdayService();
		try {
			service.sendGreetings("employee_data.txt",
					new OurDate("2008/10/08"), mail);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int quantityOfGreetingsSent() {
		return numberOfGreetingsSent;
	}
}
