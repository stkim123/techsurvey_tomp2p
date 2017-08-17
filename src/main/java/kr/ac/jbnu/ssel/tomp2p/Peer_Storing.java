package kr.ac.jbnu.ssel.tomp2p;

public class Peer_Storing
{
	public static void main(String[] args) throws Exception
	{
		int peerID = 1;
		int numOfMsg = 100;
		
		P2PManager dns = new P2PManager(peerID);
		for(int i = 0; i < numOfMsg ; i++)
		{
			String value = "value:#"+ i; 
			dns.store(value);
			System.out.println("store :"+ value);
			try
			{
				Thread.sleep(1000);
			} catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
}
