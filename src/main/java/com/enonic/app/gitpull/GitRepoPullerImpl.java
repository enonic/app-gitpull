package com.enonic.app.gitpull;

import org.eclipse.jgit.api.Git;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enonic.app.gitpull.connection.GitConnection;

final class GitRepoPullerImpl
    implements GitRepoPuller
{
    private final static Logger LOG = LoggerFactory.getLogger( GitRepoPullerImpl.class );

    @Override
    public void pull( final GitConnection connection )
    {
        if ( !doPull( connection ) )
        {
            doClone( connection );
        }
    }

    private boolean doPull( final GitConnection connection )
    {
        final Git git = doOpen( connection );
        if ( git == null )
        {
            LOG.info( "Local repo [" + connection.getName() + "] not found in [" + connection.getDir() + "]" );
            return false;
        }

        LOG.info( "Pulling repo " + git.getRepository() );
        doPull( git, connection );

        return true;
    }

    private void doPull( final Git git, final GitConnection entry )
    {
        entry.pull( git );
    }

    private Git doOpen( final GitConnection connection )
    {
        try
        {
            return Git.open( connection.getDir() );
        }
        catch ( final Exception e )
        {
            return null;
        }
    }

    private void doClone( final GitConnection connection )
    {
        connection.gitClone();
    }
}
