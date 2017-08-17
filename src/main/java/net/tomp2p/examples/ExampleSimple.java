package net.tomp2p.examples;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.Random;

import net.tomp2p.connection.Bindings;
import net.tomp2p.dht.FutureGet;
import net.tomp2p.dht.FuturePut;
import net.tomp2p.dht.PeerBuilderDHT;
import net.tomp2p.dht.PeerDHT;
import net.tomp2p.futures.BaseFutureAdapter;
import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.futures.FutureDiscover;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerBuilder;
import net.tomp2p.peers.Number160;
import net.tomp2p.storage.Data;

public class ExampleSimple
{

	final private PeerDHT peer;

	public ExampleSimple(int peerId) throws Exception
	{

		peer = new PeerBuilderDHT(new PeerBuilder(Number160.createHash(peerId)).ports(4000 + peerId).start()).start();

		FutureBootstrap fb = this.peer.peer().bootstrap().inetAddress(InetAddress.getByName("127.0.0.1")).ports(4001)
				.start();
		fb.awaitUninterruptibly();
		if (fb.isSuccess())
		{
			peer.peer().discover().peerAddress(fb.bootstrapTo().iterator().next()).start().awaitUninterruptibly();
		}
	}

	private String get(String name) throws ClassNotFoundException, IOException
	{
		FutureGet futureGet = peer.get(Number160.createHash(name)).start();
		futureGet.awaitUninterruptibly();
		if (futureGet.isSuccess())
		{
			return futureGet.dataMap().values().iterator().next().object().toString();
		}
		return "not found";
	}

	private void store(String name, String ip) throws IOException
	{
		peer.put(Number160.createHash(name)).data(new Data(ip)).start().awaitUninterruptibly();
	}

	public static void main(String[] args) throws NumberFormatException, Exception
	{

		// ?ûú?ç§ ?îº?ñ¥ID ?Éù?Ñ±, 4001?è¨?ä∏Î°? ?àò?ã†??Í∏?
		Random rnd = new Random();
		Bindings b = new Bindings();
		b.addInterface("eth0");
		// create a peer with a random peerID, on port 4000, listening to the interface
		// eth0
		Peer peer = new PeerBuilder(new Number160(rnd)).ports(4001).start();
		// ?ÉàÎ°úÏö¥ ?îº?ñ¥ ?Éù?Ñ±, 4002?è¨?ìúÎ°? ?àò?ã†??Í∏?
		Peer another = new PeerBuilder(new Number160(rnd)).masterPeer(peer).ports(4002).start();
		//
		FutureDiscover future = another.discover().peerAddress(peer.peerAddress()).start();
		future.awaitUninterruptibly();

		// FutureBootstrap futureBootstrap =
		// another.bootstrap().setPeerAddress(peer.getPeerAddress()).start();
		// futureBootstrap.awaitUninterruptibly();

		PeerDHT pdht = new PeerBuilderDHT(another).start();
		Data data = new Data("test");
		Number160 nr = new Number160(rnd);
		FuturePut futurePut = pdht.put(nr).data(data).start();
		futurePut.awaitUninterruptibly();

		// peer.shutdown();
		// or peer.shutdown().awaitUninterruptibly();

		FutureGet futureGet = pdht.get(nr).start();
		// you need to call futureDHT.awaitUninterruptibly() to get any data;
		Data result = futureGet.data();
		System.out.println("done!?:" + result.isEmpty());

		futureGet.addListener(new BaseFutureAdapter<FutureGet>()
		{
			public void operationComplete(FutureGet future) throws Exception
			{
				System.out.println("data:" + future.data());

				if (future.isSuccess())
				{ // this flag indicates if the future was successful
					System.out.println("success");
				} else
				{
					System.out.println("failure");
				}
			}
		});

		InetAddress address = Inet4Address.getByName("192.168.1.20");
		FutureDiscover futureDiscover = peer.discover().inetAddress(address).ports(4000).start();
		futureDiscover.awaitUninterruptibly();
		FutureBootstrap futureBootstrap = peer.bootstrap().inetAddress(address).ports(4000).start();
		futureBootstrap.awaitUninterruptibly();

	}
}