package com.miven.demo.nio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;


/**
 * @author mingzhi.xie
 */
public class Client {
    public SocketChannel client = null;
    public InetSocketAddress isa = null;
    public RecvThread rt = null;

    public Client() {}
    
	public void makeConnection()
    {
		try {
			
			client = SocketChannel.open();
			isa = new InetSocketAddress("localhost",4900);
			client.connect(isa);
			client.configureBlocking(false);
			receiveMessage();    
		} catch(IOException e) {
			e.printStackTrace();
		}
		while (true) {
			if (sendMessage() == -1) {
				break;
			}
		}

		try {
			client.close();
			System.exit(0);
		} catch(IOException e) {
			e.printStackTrace();
		}
    }
    
	public int sendMessage() {
		System.out.println("Inside SendMessage");
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String msg;
		ByteBuffer byteBuffer;
		int nBytes = 0;
		try
		{
			msg = in.readLine();
			System.out.println("msg is " + msg);
			byteBuffer = ByteBuffer.wrap(msg.getBytes());
			nBytes = client.write(byteBuffer);
			System.out.println("nBytes is "+nBytes);
			if ("quit".equals(msg) || "shutdown".equals(msg)) {
				System.out.println("time to stop the client");
				interruptThread();
				try {
					Thread.sleep(5000);
				} catch(Exception e) {
					e.printStackTrace();
				}
				client.close();
				return -1;
			}
	    
		} catch(IOException e) {
			e.printStackTrace();
		}
		System.out.println("Wrote " + nBytes + " bytes to the server");
		return nBytes;
    }

    public void receiveMessage() {
		rt = new RecvThread("Receive Thread",client);
		rt.start();

    }

    public void interruptThread() {
		rt.val = false;
    }

    public static void main(String[] args) {
		Client cl = new Client();
		cl.makeConnection();
    }

    public class RecvThread extends Thread {
		public SocketChannel sc;
		public boolean val = true;
	
		public RecvThread(String str,SocketChannel client) {
			super(str);
			sc = client;
		}
	
		@Override
		public void run() {
			System.out.println("Inside receive msg");
			ByteBuffer buf = ByteBuffer.allocate(2048);
			try {
				while (val) {
					while (client.read(buf) > 0){
						buf.flip();
						Charset charset = StandardCharsets.US_ASCII;
						CharsetDecoder decoder = charset.newDecoder();
						CharBuffer charBuffer = decoder.decode(buf);
						String result = charBuffer.toString();
						System.out.println(result);
						buf.flip();
						
					}
				}
			}
			catch(IOException e) {
				e.printStackTrace();
			}
		}
    }
}

