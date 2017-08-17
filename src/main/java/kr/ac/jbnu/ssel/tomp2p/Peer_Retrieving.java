package kr.ac.jbnu.ssel.tomp2p;

import java.util.Iterator;

import net.tomp2p.storage.Data;

public class Peer_Retrieving
{
	public static void main(String[] args) throws Exception
	{
		int peerID = 2;
		
		P2PManager dns = new P2PManager(peerID);
		
		System.out.println(" Value:" + dns.get());
		
		for(int i = 0 ; i < 100 ; i++)
		{
			System.out.println(i + ")=====================================");
			Iterator<Data> data = dns.getAll();
			while(data.hasNext())
			{
				String value = (String)data.next().object();
				System.out.println("values:"+ value);
			}
		}
	}
}
