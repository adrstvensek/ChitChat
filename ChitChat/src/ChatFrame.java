import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

public class ChatFrame extends JFrame implements ActionListener, KeyListener {
	private JTextField vzdevek;
	private JTextArea osebe;
	private JTextArea prejetaSporocila;
	private JTextArea output;
	private JTextField input;
	private JTextField prejemnik;
	private JList listPrejemnikov;
    private DefaultListModel listModelPrejemnikov;
	private JButton prijavi;
	private JButton odjavi;
	private boolean prijavljen=false;
	private boolean isFirstRun=true;
	UporabnikiRobot preverjaPrijavljeneUporabnike;
	ReceiveRobot preverjaNovaSporocila;
	public ChatFrame() {
		super();
		
		preverjaPrijavljeneUporabnike = new UporabnikiRobot(this);
		preverjaNovaSporocila = new ReceiveRobot(this);
		
		setTitle("ChitChat");
		
		Container pane = this.getContentPane();
		pane.setLayout(new GridBagLayout());
		
		FlowLayout layout = new FlowLayout(FlowLayout.LEFT);
		JLabel napis = new JLabel("Vzdevek: ");
		JPanel panel = new JPanel();
		prijavi = new JButton("Prijavi");
		odjavi = new JButton("Odjavi");
		prijavi.addActionListener(this);
		odjavi.addActionListener(this);
		odjavi.setEnabled(false);
		
		
		panel.setLayout(layout);
		panel.add(napis);

		String ime = System.getProperty("user.name");
		this.vzdevek = new JTextField(ime, 40);
		
		this.vzdevek.setText(ime);

		
		GridBagConstraints v = new GridBagConstraints();
		v.weightx = 1.0;
		v.gridx = 0;
		v.gridy = 0;
		v.fill = GridBagConstraints.HORIZONTAL;
		
		
		panel.add(vzdevek);
		vzdevek.addKeyListener(this);
		
		pane.add(panel, v);
		panel.add(prijavi);
		panel.add(odjavi);

		this.output = new JTextArea(20, 40);
		this.output.setEditable(false);
		GridBagConstraints outputConstraint = new GridBagConstraints();
		outputConstraint.gridx = 0;
		outputConstraint.gridy = 1;
		outputConstraint.fill = GridBagConstraints.BOTH;
		outputConstraint.weightx = 1.0;
		outputConstraint.weighty = 1.0;
		JScrollPane scrollPane = new JScrollPane(output);
		pane.add(scrollPane, outputConstraint);
		
		this.osebe = new JTextArea(20, 20);
		this.osebe.setEditable(false);
		GridBagConstraints outputConstraintOsebe = new GridBagConstraints();
		outputConstraintOsebe.gridx = 1;
		outputConstraintOsebe.gridy = 1;
		outputConstraintOsebe.fill = GridBagConstraints.BOTH;
		outputConstraintOsebe.weightx = 1.0;
		outputConstraintOsebe.weighty = 1.0;
		JScrollPane scrollPaneOsebe = new JScrollPane(osebe);
		pane.add(scrollPaneOsebe, outputConstraintOsebe);
		
		this.prejetaSporocila = new JTextArea(20, 40);
		this.prejetaSporocila.setEditable(false);
		GridBagConstraints outputConstraintPrejeto = new GridBagConstraints();
		outputConstraintPrejeto.gridx = 2;
		outputConstraintPrejeto.gridy = 1;
		outputConstraintPrejeto.fill = GridBagConstraints.BOTH;
		outputConstraintPrejeto.weightx = 1.0;
		outputConstraintPrejeto.weighty = 1.0;
		JScrollPane scrollPanePrejeto = new JScrollPane(prejetaSporocila);
		pane.add(scrollPanePrejeto, outputConstraintPrejeto);
		
		this.prejemnik = new JTextField(10);
		this.prejemnik.setEditable(false);
		GridBagConstraints inputConstraintp = new GridBagConstraints();
		inputConstraintp.gridx = 1;
		inputConstraintp.gridy = 2;		
		inputConstraintp.fill = GridBagConstraints.BOTH;
		inputConstraintp.weightx = 0.3;
		inputConstraintp.weighty = 0.0;
		pane.add(prejemnik, inputConstraintp);
		prejemnik.addKeyListener(this);
		
		this.input = new JTextField(30);
		this.input.setEditable(false);
		GridBagConstraints inputConstraint = new GridBagConstraints();
		inputConstraint.gridx = 0;
		inputConstraint.gridy = 2;		
		inputConstraint.fill = GridBagConstraints.BOTH;
		inputConstraint.weightx = 0.7;
		inputConstraint.weighty = 0;
		pane.add(input, inputConstraint);
		input.addKeyListener(this);
		
		addWindowListener(new WindowAdapter(){
			public void windowOpened(WindowEvent e){
				input.requestFocusInWindow();
			}
		});
	}

	/**
	 * @param person - the person sending the message
	 * @param message - the message content
	 */
	public void addMessage(String person, String message) {
		String chat = this.output.getText();
		this.output.setText(chat + person + ": " + message + "\n");
	}
	
	
	public void zapisiUporabnike(Uporabnik oseba) {
	    this.osebe.append(oseba.getUsername() + "\n");
	}
	
	public void zapisiPrejetoSporocilo(PrejetaSporocila prejetoSporocilo) {
		this.prejetaSporocila.append(prejetoSporocilo.getSender() + ": " + 
									prejetoSporocilo.getText() + "\n");
	}
	public void pobrisi(){
		this.osebe.setText(null);
	}
	public boolean getStatus(){
		return prijavljen;
	}
	public String getUsername(){
		return this.vzdevek.getText();
	}
	public void prijavi() throws URISyntaxException {
		try {
			URI uri = new URIBuilder("http://chitchat.andrej.com/users")
					.addParameter("username", this.vzdevek.getText()).build();
			HttpResponse response = Request.Post(uri).execute().returnResponse();
			InputStream responseText = null;
			if (response.getStatusLine().getStatusCode()==200) {
				//Èe uspe prijava
				if (isFirstRun) {
					preverjaPrijavljeneUporabnike.activate();
					preverjaNovaSporocila.activate();
					isFirstRun=false;

				}
				prijavljen=true;
				this.odjavi.setEnabled(true);
				this.prijavi.setEnabled(false);
				this.input.setEditable(true);
				this.prejemnik.setEditable(true);
				responseText=response.getEntity().getContent();
							}else if(response.getStatusLine().getStatusCode()==403){
				//Èe prijava ne uspe
				responseText=response.getEntity().getContent();
			}
			System.out.println("responseText: " + getStringFromInputStream(responseText));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void odjavi() throws URISyntaxException {
		try {
			URI uri = new URIBuilder("http://chitchat.andrej.com/users")
					.addParameter("username", this.vzdevek.getText()).build();

			String responseBody = Request.Delete(uri).execute().returnContent().asString();

			System.out.println(responseBody);
			prijavljen=false;
			this.odjavi.setEnabled(false);
			this.prijavi.setEnabled(true);
			this.input.setEditable(false);
			this.prejemnik.setEditable(false);
			this.osebe.setText("");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private static String getStringFromInputStream(InputStream is) {
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();

		String line;
		try {
			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sb.toString();
	}

	public void actionPerformed(ActionEvent e)  {
		if (e.getSource() == prijavi) {
	        try {
				prijavi();
			} catch (URISyntaxException e1) {
				e1.printStackTrace();
			}
        }if (e.getSource() == odjavi){
        	try {
				odjavi();
			} catch (URISyntaxException e1) {
				e1.printStackTrace();
			}
		}
         
	}

	@Override
	public void keyTyped(KeyEvent e) {
		if (e.getSource() == this.input) {
			if (e.getKeyChar() == '\n') {
				this.addMessage(this.vzdevek.getText(), this.input.getText());
				PrejetaSporocila sporocilo = new PrejetaSporocila();
				if (this.prejemnik.getText().equals("")) {
					sporocilo.setGlobal(true);
				}else {
					sporocilo.setGlobal(false);
					sporocilo.setRecipient(this.prejemnik.getText());
				}
				sporocilo.setText(this.input.getText());
				ObjectMapper mapper = new ObjectMapper();
				mapper.setDateFormat(new ISO8601DateFormat());
				String message;
				try {
					message = mapper.writeValueAsString(sporocilo);
					try {
			        	URI uri = new URIBuilder("http://chitchat.andrej.com/messages")
			        	          .addParameter("username", this.vzdevek.getText())
			        	          .build();

			        	     	  String responseBody = Request.Post(uri)
			        	          .bodyString(message, ContentType.APPLICATION_JSON)
			        	          .execute()
			        	          .returnContent()
			        	          .asString();

			        	  System.out.println(responseBody);
			        } catch (IOException e1) {
			            e1.printStackTrace();
			        } catch (URISyntaxException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} catch (JsonProcessingException e2) {
					e2.printStackTrace();
				}

				this.input.setText("");
			}
		}	
				
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
