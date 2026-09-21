import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.net.Socket;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PingTest{
    static final int PORT = 6379;
    static final byte[] PING = "*1\r\n$4\r\nPING\r\n".getBytes();

    // Retry until the server is up. The connection returned is the one the
    // test uses, because the current server accepts only one client.

    static Socket connectWithRetry() throws Exception{
        for(int i=0; i<50;i++){
            try{
                return new Socket("localhost", PORT);
            } catch(IOException e){
                Thread.sleep(100);
            }
        }
        throw new IllegalStateException("server did not start");
    }
    static String sendPing(Socket s) throws IOException {
        s.getOutputStream().write(PING);
        s.getOutputStream().flush();

        byte[] buf = new byte[1024];
        int n = s.getInputStream().read(buf);
        return new String(buf, 0, n);
    }
    @Test
    void multiplePingsOnOneConnection() throws Exception{
        Thread server = new Thread(() -> Main.main(new String[]{}));
        server.setDaemon(true); //so the test JVM can exit
        server.start();

        try (Socket s = connectWithRetry()) {
            s.setSoTimeout(2000); // fail instead of hanging forever
            for (int i = 0; i < 3; i++) {
                assertEquals("+PONG\r\n", sendPing(s));
            }
        }
    }
}

