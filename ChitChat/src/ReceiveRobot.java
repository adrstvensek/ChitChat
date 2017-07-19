
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URIBuilder;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;



public class ReceiveRobot extends TimerTask {
	private ChatFrame chat;

	public ReceiveRobot(ChatFrame chat) {
		this.chat = chat;
	}

	/**
	 * Activate the robot!
	 */
	public void activate() {
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(this, 5000, 1000);
	}
	
	@Override
	public void run() {
		if (chat.getStatus()) {
		 try {
	        	URI uri = new URIBuilder("http://chitchat.andrej.com/messages")
	        	          .addParameter("username", chat.getUsername())
	        	          .build();

	        	String responseBody = Request.Get(uri)
	        	          .execute()
	        	          .returnContent()
	        	          .asString();
	        	ObjectMapper mapper = new ObjectMapper();
	      		mapper.setDateFormat(new ISO8601DateFormat());
	            TypeReference<List<PrejetaSporocila>> t = new TypeReference<List<PrejetaSporocila>>() { };
	      		List<PrejetaSporocila> prejetaSporocila = mapper.readValue(responseBody, t);
	      		
	      		for (PrejetaSporocila prejetoSporocilo:prejetaSporocila){
	    			chat.zapisiPrejetoSporocilo(prejetoSporocilo);
	    			}
	      			
	        	  //System.out.println(responseBody);
	        } catch (IOException e) {
	            e.printStackTrace();
	        } catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
