import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PrejetaSporocila {
	private Boolean global;
	private String recipient;
	private String sender;
	private String text;
	private Date sent_at;
	
	public PrejetaSporocila() {
		super();
	}

	public PrejetaSporocila(Boolean global, String recipient, String sender, String text, Date sent_at) {
		this.global = global;
		this.recipient = recipient;
		this.sender = sender;
		this.text = text;
		this.sent_at = sent_at;
	}

	//public String toJsonString() {
		//return "PrejetaSporocila [global=" + global + ", recipient=" + recipient + ", "
		//		+ "sender=" + sender + ", text=" + text + ", sent_at=" + sent_at + "]";
	//}
	
	@Override
	public String toString() {
		return "PrejetaSporocila [global=" + global + ", recipient=" + recipient + ", "
				+ "sender=" + sender + ", text=" + text + ", sent_at=" + sent_at + "]";
	}

	@JsonProperty("global")
	public Boolean getGlobal() {
		return global;
	}

	public void setGlobal(Boolean global) {
		this.global = global;
	}
	
	
	@JsonProperty("recipient")
	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	@JsonProperty("sender")
	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}
	@JsonProperty("text")
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	@JsonProperty("sent_at")
	public Date getSent_at() {
		return sent_at;
	}

	public void setSent_at(Date sent_at) {
		this.sent_at = sent_at;
	}
	
	
}
