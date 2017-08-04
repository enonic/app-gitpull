package com.enonic.app.gitpull;

import org.eclipse.jgit.api.Git;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class GitRepoPullerImpl
    implements GitRepoPuller
{
    private final static Logger LOG = LoggerFactory.getLogger( GitRepoPullerImpl.class );

    @Override
    public void pull( final GitPullEntry entry )
    {
        if ( !doPull( entry ) )
        {
            doClone( entry );
        }
    }

    private boolean doPull( final GitPullEntry entry )
    {
        final Git git = doOpen( entry );
        if ( git == null )
        {
            return false;
        }

        doPull( git, entry );
        return true;
    }

    void doPull( final Git git, final GitPullEntry entry )
    {
        try
        {
            git.reset();
            git.pull().setCredentialsProvider( entry.getCredentialsProvider() ).call();
            LOG.info( "Pulled in changes from git repository [" + entry.name + "]" );
        }
        catch ( final Exception e )
        {
            LOG.error( "Error pulling git repository [" + entry.name + "]", e );
        }
    }

    private Git doOpen( final GitPullEntry entry )
    {
        try
        {
            return Git.open( entry.dir );
        }
        catch ( final Exception e )
        {
            return null;
        }
    }

    private void doClone( final GitPullEntry entry )
    {
        try
        {
            Git.cloneRepository().
                setCredentialsProvider( entry.getCredentialsProvider() ).
                setDirectory( entry.dir ).
                setURI( entry.url ).
                setBare( false ).
                call();
            LOG.info( "Cloned git repository [" + entry.name + "]" );
        }
        catch ( final Exception e )
        {
            LOG.error( "Error cloning git repository [" + entry.name + "]", e );
        }
    }
}
