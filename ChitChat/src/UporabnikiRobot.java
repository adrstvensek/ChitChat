import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.client.fluent.Request;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

public class UporabnikiRobot extends TimerTask {
	private ChatFrame chat;
	private Timer timer;

	public UporabnikiRobot(ChatFrame chat) {
		this.chat = chat;
	}

	/**
	 * Activate the robot!
	 */
	public void activate() {
		timer = new Timer();
		timer.scheduleAtFixedRate(this, 5000, 1000);
	}
	
	@Override
	public void run() {
		if (chat.getStatus()) {
			try {
		        String users = Request.Get("http://chitchat.andrej.com/users")
		                .execute()
		                .returnContent().asString();
		        ObjectMapper mapper = new ObjectMapper();
				mapper.setDateFormat(new ISO8601DateFormat());
		        TypeReference<List<Uporabnik>> t = new TypeReference<List<Uporabnik>>() { };
				List<Uporabnik> osebe = mapper.readValue(users, t);
				chat.pobrisi();
				for (Uporabnik oseba:osebe){
					chat.zapisiUporabnike(oseba);
				}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
}
