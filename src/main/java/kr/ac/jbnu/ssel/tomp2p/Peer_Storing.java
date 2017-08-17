package kr.ac.jbnu.ssel.tomp2p;

public class Peer_Storing
{
	public static void main(String[] args) throws Exception
	{
		int peerID = 1;
		P2PManager dns = new P2PManager(peerID);
		dns.store("value1");
		dns.store("value2");
		dns.store("value3");
		dns.store("value4");
	}
}
