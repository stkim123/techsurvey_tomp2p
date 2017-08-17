package kr.ac.jbnu.ssel.tomp2p;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class MyIPAddr
{
	public static void main(String[] args) throws Exception
	{
		InetAddress myIP = new MyIPAddr().obtainMyIP();
		System.out.println("myIP:"+ myIP.getHostAddress()) ;
		
		System.out.println("myIP:"+ InetAddress.getByName("127.0.0.1")) ;
	}

	public InetAddress obtainMyIP()
	{
		InetAddress myIP = null;
		String ip;
	    try {
	        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
	        while (interfaces.hasMoreElements()) {
	            NetworkInterface iface = interfaces.nextElement();
	            // filters out 127.0.0.1 and inactive interfaces
	            if (iface.isLoopback() || !iface.isUp())
	                continue;

	            Enumeration<InetAddress> addresses = iface.getInetAddresses();
	            while(addresses.hasMoreElements()) {
	                InetAddress addr = addresses.nextElement();
	                ip = addr.getHostAddress();
	                if(ip.indexOf(".")!= -1)
	                {
	                	myIP = addr;
	                	System.out.println(iface.getDisplayName() + " " + ip);	
	                }
	                
	            }
	        }
	    } catch (SocketException e) {
	        throw new RuntimeException(e);
	    }
	    return myIP;
	}
	
}
