package sockets.jframes;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Formcliente extends JFrame implements ActionListener,Runnable {
	
	JTextField txtmensaje;
	JButton btnenviar;
	JButton btnenviar1;
	JTextArea txtmensajes;
	int var=0;
	ServerSocket servidor;
	Socket cli;
	

	public Formcliente() {
		txtmensaje = new JTextField();
		txtmensaje.setBounds(10, 10, 200, 20);
		add(txtmensaje);
		
		btnenviar= new JButton();
		btnenviar.setText("Enviar");
		btnenviar.setBounds(10, 40, 200, 20);
		btnenviar.addActionListener(this);
		add(btnenviar);
		
		btnenviar1= new JButton();
		btnenviar1.setText("Cerrar Chat");
		btnenviar1.setBounds(10, 80, 200, 20);
		btnenviar1.addActionListener(this);
		add(btnenviar1);
		
		
		setLayout(null);
		setSize(400, 400);
		setVisible(true);
		
		txtmensajes = new JTextArea();
		txtmensajes.setBounds(220,10,250,400);
		add(txtmensajes);
		
		setLayout(null);
		setSize(500,460);
		setVisible(true);
		
		try {
			servidor=new ServerSocket(9090);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Thread hilo = new Thread(this);
		hilo.start();
	}

	public static void main(String[] args) {
		
		new Formcliente();

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==btnenviar) {
			try {
				//Socket cli = new Socket("192.168.0.109",9090);
				//Socket cli = new Socket("127.0.0.1",9090);
				//Socket cli = new Socket("fe80::c5e5:1a4b:15f1:643a%7",9090);
				//Socket cli = new Socket("fe80::a944:c848:ce7e:c7fa%11",9090);
				Socket cli = new Socket("2001:db8:acad:1::11",9090);
				Socket cli1 = new Socket("2001:db8:acad:1::10",9090);
				DataOutputStream flujo = new DataOutputStream(cli.getOutputStream());
				DataOutputStream flujo1 = new DataOutputStream(cli1.getOutputStream());

				flujo.writeUTF("Camilo: "+txtmensaje.getText());
				flujo1.writeUTF("Camilo: "+txtmensaje.getText());
				cli.close();
				cli1.close();
				
				txtmensaje.setText("");
				
			} catch (Exception ex) {
				System.out.println("Error en cliente"+ ex.getMessage());
			}
		}else if(e.getSource()==btnenviar1) {
			try {
				Socket cli = new Socket("2001:db8:acad:1::11",9090);
				Socket cli1 = new Socket("2001:db8:acad:1::10",9090);
				DataOutputStream flujo = new DataOutputStream(cli.getOutputStream());
				DataOutputStream flujo1 = new DataOutputStream(cli1.getOutputStream());

				flujo.writeUTF("Camilo: "+"Se ha finalizado la conversacion");
				flujo1.writeUTF("Camilo: "+"Se ha finalizado la conversacion");
				cli.close();
				cli1.close();
				
				btnenviar.setEnabled(false);
				txtmensaje.setEditable(false);

				
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
		
	}

	@Override
	public void run() {
		try {
			
			//ServerSocket servidor = new ServerSocket(9090);
			//Socket cli;
			
			while(true) {

			cli= servidor.accept();
			DataInputStream flujo = new DataInputStream(cli.getInputStream());
			String msg=flujo.readUTF();
			txtmensajes.append("\n" + msg);
			cli.close();
			
			if(msg.equalsIgnoreCase("Camilo: Se ha finalizado la conversacion")) {
				servidor.close();
				break;
			}else if(msg.equalsIgnoreCase("Andres: Se ha finalizado la conversacion")) {
				servidor.close();
				break;
			}
		 
			}	
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}

//netstat -aon
//taskkill /pid 544 /F
