import java.net.Socket;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.*;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.io.BufferedReader;
import java.io.PrintWriter;

public class client extends JFrame {
  
  Socket socket;
  BufferedReader br;
  PrintWriter out;
  // components
  private JLabel heading = new JLabel("client ");
  private JTextArea messageArea=new JTextArea();
  private JTextField messageInput=new JTextField();
  private Font font=new Font("Roboto",Font.PLAIN,20);
  

  public client()
  {
    try {
      
      System.out.println("Connecting to server");
      socket=new Socket("127.0.0.1",8888);
      System.out.println("connected to server");
      br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
      out=new PrintWriter(socket.getOutputStream());
      createGUI();
      events();

      StartReading();
      //StartWritting();
      } 
    catch (Exception e) {
      //TODO: handle exception
      e.printStackTrace();
    }
  }
 
  private void events(){
    messageInput.addKeyListener(new KeyListener(){

      @Override
      public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
        
      }

      @Override
      public void keyPressed(KeyEvent e) {
        // TODO Auto-generated method stub
        
      }

      @Override
      public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub
        if(e.getKeyCode()==10){
          String contentToSend=messageInput.getText();
          messageArea.append("Me :"+contentToSend+"\n");
          out.println(contentToSend);
          out.flush();
          messageInput.setText("");
          messageInput.requestFocus();


        }
      }

    });
  }








  private void createGUI(){

    this.setTitle("client window");
    this.setSize(400,400);
    this.setLocationRelativeTo(null);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    //chat component coding
    heading.setFont(font);
    messageArea.setFont(font);
    messageInput.setFont(font);
    ImageIcon imageIcon = new ImageIcon(new ImageIcon("chat.png").getImage().getScaledInstance(28, 28, Image.SCALE_DEFAULT));
    heading.setHorizontalTextPosition(SwingConstants.CENTER);
    heading.setVerticalTextPosition(SwingConstants.BOTTOM);
    heading.setIcon(imageIcon);
    heading.setHorizontalAlignment(SwingConstants.CENTER);
    heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
    messageArea.setEditable(false);
    messageInput.setHorizontalAlignment(SwingConstants.CENTER);



    //frame layout
    this.setLayout(new BorderLayout());
      //adding the components to the frame
      this.add(heading,BorderLayout.NORTH);
      JScrollPane jscrollPane=new JScrollPane(messageArea);
      this.add(jscrollPane,BorderLayout.CENTER);
      this.add(messageInput,BorderLayout.SOUTH);



    this.setVisible(true);

  }

  public void StartReading()
  { 
  // multi thread data read
    Runnable r1=()->{
    System.out.println("Reading started");
    while (true)
     {
      try {
            String msg=br.readLine();
            if(msg.equals("exit chat"))
            {
              System.out.println("chat closed by server");
              JOptionPane.showMessageDialog(this, "chat terminated by server");
              messageInput.setEnabled(false);
              break;
            }
             messageArea.append("Server : "+msg+"\n");
           // System.out.println("Server: "+msg);
            
          } catch (Exception e) {
            //TODO: handle exception
            e.printStackTrace();
          }
        
        }


  };

  
  new Thread(r1).start();

  }
  public void StartWritting()
  {

   // multi thread data writting

    Runnable r2=()->{
      System.out.println("writter started");
      while(true)
      {
        try {
          BufferedReader br1=new BufferedReader(new InputStreamReader(System.in));
          String content=br1.readLine();
          out.println(content);
          out.flush();
       
       
        } catch (Exception e) {
          //TODO: handle exception
          e.printStackTrace();
        }
      }

    };

    new Thread(r2).start();



  }

public static void main(String[]args){
System.out.println("client  starting");
 new client();
}
}