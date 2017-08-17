package kr.ac.jbnu.ssel.tomp2p;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.Iterator;

import net.tomp2p.dht.FutureGet;
import net.tomp2p.dht.PeerBuilderDHT;
import net.tomp2p.dht.PeerDHT;
import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.p2p.PeerBuilder;
import net.tomp2p.peers.Number160;
import net.tomp2p.storage.Data;

import static kr.ac.jbnu.ssel.tomp2p.Constants.DHT_KEY;

public class P2PManager
{
	final private PeerDHT peer;

    public P2PManager(int peerId) throws Exception {

        peer = new PeerBuilderDHT(new PeerBuilder(Number160.createHash(peerId)).ports(4000 + peerId).start()).start();

//        FutureBootstrap fb = this.peer.peer().bootstrap().inetAddress(InetAddress.getByName("127.0.0.1")).ports(4001).start();
//        FutureBootstrap fb = this.peer.peer().bootstrap().inetAddress(new MyIPAddr().obtainMyIP()).ports(4001).start();
        InetAddress address = Inet4Address.getByName("192.168.0.67");
        FutureBootstrap fb = this.peer.peer().bootstrap().inetAddress(address).ports(4001).start();
        
        fb.awaitUninterruptibly();
        if(fb.isSuccess()) {
            peer.peer().discover().peerAddress(fb.bootstrapTo().iterator().next()).start().awaitUninterruptibly();
        }
    }

    public String get() throws ClassNotFoundException, IOException {
        FutureGet futureGet = peer.get(Number160.createHash(DHT_KEY)).start();
        futureGet.awaitUninterruptibly();
        if (futureGet.isSuccess()) {
            return futureGet.dataMap().values().iterator().next().object().toString();
        }
        return "not found";
    }

    public void store(String value) throws IOException {
        peer.add(Number160.createHash(DHT_KEY)).data(new Data(value)).start().awaitUninterruptibly();
    }
    
    public Iterator<Data> getAll()
    {
    	 FutureGet futureGet = peer.get( Number160.createHash(DHT_KEY) ).all().start();
         futureGet.awaitUninterruptibly();
         System.out.println( "size" + futureGet.dataMap().size() );
         Iterator<Data> iterator = futureGet.dataMap().values().iterator();
         return iterator;
    }
}
