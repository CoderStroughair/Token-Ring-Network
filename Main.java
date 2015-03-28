
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JSpinner;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.SpinnerNumberModel;
import java.awt.Component;

public class Main {
	@SuppressWarnings("deprecation")
	public static void main(String args[]) throws Exception{
		
		/*
		 * GUI elements
		 */
		JFrame frm;
		JPanel Main_Screen;
		JButton but_1,but_2,but_3,b4;
		
		/*
		 * Number of initial nodes and first nodes port number
		 */
		final int numNodes = 3;
		final int initialP = 4444; // first port number
		
		final ArrayList<Node> nodes = new ArrayList<Node>();
		
		/*
		 * GUI Logic
		 */
		frm = new JFrame("Command centre");
		frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frm.setUndecorated(true);
		frm.getRootPane().setWindowDecorationStyle(JRootPane.PLAIN_DIALOG);
		Main_Screen = new JPanel();
		Main_Screen.setBorder(new LineBorder(new Color(0, 0, 0)));


		JSpinner num_nodes = new JSpinner();
		num_nodes.setBounds(29, 62, 49, 37);
		num_nodes.setAlignmentY(Component.TOP_ALIGNMENT);
		num_nodes.setAlignmentX(Component.LEFT_ALIGNMENT);
		num_nodes.setModel(new SpinnerNumberModel(2, 2, 6, 1));
		but_1 = new JButton("INITIALIZE");
		but_1.setBounds(10, 1, 87, 57);
		but_1.addActionListener(new ActionListener()
		{

			/*
			 * Initialize initial nodes
			 */
			public void actionPerformed(ActionEvent e) {
				for(int i=0; i<numNodes; i++){
					try {
						Node n = new Node(i, initialP+i);
						nodes.add(n);
						nodes.get(i).frm.move(i*400, 100);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
				
				for(int i=0; i<nodes.size()-1; i++){
					nodes.get(i).setDownNeigh(nodes.get(i+1));
				}
				nodes.get(nodes.size()-1).setDownNeigh(nodes.get(0));
				nodes.get(0).makeMonitor();
			}
		});
		Main_Screen.setLayout(null);
		//pnl.add(b4);

		Main_Screen.add(num_nodes);
		Main_Screen.add(but_1);
		
		but_2 = new JButton("START");
		but_2.setBounds(107, 1, 107, 98);
		but_2.addActionListener(new ActionListener() {
			
			/*
			 * Start nodes 
			 */
			public void actionPerformed(ActionEvent e) {
				try {
					nodes.get(0).injectToken();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				for(int i=0; i<nodes.size(); i++){
					nodes.get(i).start();
				}
			}
		});
		Main_Screen.add(but_2);
		
		but_3 = new JButton("KILL MONITOR");
		but_3.setBounds(212, 1, 137, 98);
		but_3.addActionListener(new ActionListener() {
			/*
			 * Kill the monitor node
			 */
			public void actionPerformed(ActionEvent e) {
				for(int i=0; i<nodes.size(); i++){
					if(nodes.get(i).isMonitor) nodes.get(i).disconnect();	
				}
			}
		});
		Main_Screen.add(but_3);
		
		b4 = new JButton("Add");
		b4.setBounds(265, 11, 75, 30);
		b4.addActionListener(new ActionListener() {
			
			/*
			 * Dynamically add a new node and reconfigure system
			 */
			public void actionPerformed(ActionEvent e) {
				try {
					Node n = new Node(nodes.size(), initialP+nodes.size());
					n.frm.move(nodes.size()*400, 100);
					n.start();
					nodes.add(n);
					for(int i=0; i<nodes.size()-1; i++){
						nodes.get(i).setDownNeigh(nodes.get(i+1));
					}
					nodes.get(nodes.size()-1).setDownNeigh(nodes.get(0));
					
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		frm.getContentPane().add(Main_Screen);
				
		frm.setSize(350, 100);
		frm.setVisible(true);	
	}
}