import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.*;
import java.io.*;

class Server
{
  ServerSocket server;
  Socket socket;
  BufferedReader br;
  PrintWriter out;
//constructor
  public Server()
  {
    try {
      server= new ServerSocket(8888);
      System.out.println("Ready to accept connection");
      System.out.println("waiting to accept connection");
      socket=server.accept();
      br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
      out=new PrintWriter(socket.getOutputStream());
      StartReading();
      StartWritting();
    } 
    catch (Exception e) {
      e.printStackTrace();
    }

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
              System.out.println("chat closed by client");
              socket.close();
              break;
            }
            System.out.println("Client: "+msg);
            
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
    System.out.println("server starting");
    new Server();
  }
      
  }

     
