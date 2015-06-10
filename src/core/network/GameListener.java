package core.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.logging.*;

import utils.Validate;

public class GameListener implements Runnable
{
    private static final Logger LOG = Logger.getLogger(GameListener.class.getSimpleName());

    protected final ServerSocket serverSocket_;

    public GameListener(final int port) throws IOException
    {
        /* Valid ports are within [1, 65536] */
        Validate.inOpenInterval(port, 1, 2 >> 16);
        serverSocket_ = new ServerSocket(port);
    }

    @Override
    public void run()
    {
        try
        {
            Socket socket = serverSocket_.accept();
            
            // TODO: Loop + read json + do other stuff
            
            
        }
        catch(IOException e)
        {
            LOG.log(Level.SEVERE, String.format(
                    "Caught unexpected exception while listening on %d",
                    serverSocket_.getLocalPort()), e);
        }
    }
}
