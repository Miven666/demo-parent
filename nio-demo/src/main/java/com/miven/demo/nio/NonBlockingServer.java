package com.miven.demo.nio;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;


/**
 * @author mingzhi.xie
 */
public class NonBlockingServer {
    public Selector sel = null;
    public ServerSocketChannel server = null;
    public SocketChannel socket = null;
    public int port = 4900;
    String result = null;


    public NonBlockingServer() {
		System.out.println("Inside default ctor");
    }

    public void initializeOperations() throws IOException {
		System.out.println("Inside initialization");
		sel = Selector.open();
		server = ServerSocketChannel.open();
		server.configureBlocking(false);
		InetAddress ia = InetAddress.getLocalHost();
		System.out.println(ia);
		InetSocketAddress isa = new InetSocketAddress(ia,port);
		server.socket().bind(isa);
    }
    
	public void startServer() throws IOException {
		System.out.println("Inside start server");
        initializeOperations();
		System.out.println("Abt to block on select()");
		SelectionKey acceptKey = server.register(sel, SelectionKey.OP_ACCEPT );	
	
		while (acceptKey.selector().select() > 0 ) {
	    
			Set<SelectionKey> readyKeys = sel.selectedKeys();
			Iterator<SelectionKey> it = readyKeys.iterator();

			while (it.hasNext()) {
				SelectionKey key = it.next();
				it.remove();
                
				if (key.isAcceptable()) {
					System.out.println("Key is Acceptable");
					ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
					socket = ssc.accept();
					socket.configureBlocking(false);
					socket.register(sel,SelectionKey.OP_READ|SelectionKey.OP_WRITE);
				}
				if (key.isReadable()) {
					System.out.println("Key is readable");
					String ret = readMessage(key);
					if (ret.length() > 0) {
						writeMessage(socket,ret);
					}
				}
				if (key.isWritable()) {
					System.out.println("THe key is writable");
					String ret = readMessage(key);
					socket = (SocketChannel)key.channel();
					if (result.length() > 0 ) {
						writeMessage(socket,ret);
					}
				}
			}
		}
    }

    public void writeMessage(SocketChannel socket,String ret) {
		System.out.println("Inside the loop");

		if ("quit".equals(ret) || "shutdown".equals(ret)) {
			return;
		}
		File file = new File(ret);
		try {
			RandomAccessFile rdm = new RandomAccessFile(file,"r");
			FileChannel fc = rdm.getChannel();
			ByteBuffer buffer = ByteBuffer.allocate(1024);
			fc.read(buffer);
			buffer.flip();

			Charset set = StandardCharsets.US_ASCII;
			CharsetDecoder dec = set.newDecoder();
			CharBuffer charBuf = dec.decode(buffer);
			System.out.println(charBuf.toString());
			buffer = ByteBuffer.wrap((charBuf.toString()).getBytes());
			int nBytes = socket.write(buffer);
			System.out.println("nBytes = " + nBytes);
			result = null;
		} catch(Exception e) {
			e.printStackTrace();
		}

    }
  
    public String readMessage(SelectionKey key)
    {
		socket = (SocketChannel)key.channel();
        ByteBuffer buf = ByteBuffer.allocate(1024);
		try {
			socket.read(buf);
			buf.flip();
			Charset charset = StandardCharsets.US_ASCII;
			CharsetDecoder decoder = charset.newDecoder();
			CharBuffer charBuffer = decoder.decode(buf);
			result = charBuffer.toString();
	    
        } catch(IOException e) {
			e.printStackTrace();
		}
		return result;
    }

    public static void main(String[] args) {
		NonBlockingServer nb = new NonBlockingServer();
		try {
			nb.startServer();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
	}
}




