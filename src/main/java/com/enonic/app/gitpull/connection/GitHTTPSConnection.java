package com.enonic.app.gitpull.connection;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullCommand;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import com.enonic.app.gitpull.authentication.GitAuthenticationEntry;

public class GitHTTPSConnection
    extends BaseGitConnection
{
    private final GitAuthenticationEntry authenticationEntry;

    private GitHTTPSConnection( final Builder builder )
    {
        super( builder );
        authenticationEntry = builder.authenticationEntry;
    }

    @Override
    public void pull( final Git git )
    {
        try
        {
            git.reset();

            final PullCommand pullCommand = git.pull().
                setCredentialsProvider( this.authenticationEntry.getCredentialsProvider() ).
                setTimeout( this.timeout );

            if ( !Strings.isNullOrEmpty( this.ref ) )
            {
                pullCommand.setRemoteBranchName( this.ref );
            }

            pullCommand.call();
            LOG.info( "Pulled in changes from git repository [" + this.name + "]" );
        }
        catch ( final Exception e )
        {
            LOG.error( "Error pulling git repository [" + this.name + "]", e );
        }
    }

    @Override
    public void gitClone()
    {
        try
        {
            final CloneCommand cloneCommand = Git.cloneRepository().
                setCredentialsProvider( this.authenticationEntry.getCredentialsProvider() ).
                setDirectory( this.dir ).
                setURI( this.url ).
                setBare( false ).
                setTimeout( this.timeout );

            if ( !Strings.isNullOrEmpty( this.ref ) )
            {
                cloneCommand.setBranch( this.ref );
            }

            cloneCommand.call();
        }
        catch ( final Exception e )
        {
            LOG.error( "Error cloning git repository [" + this.name + "]", e );
        }
    }

    public GitAuthenticationEntry getAuthenticationEntry()
    {
        return authenticationEntry;
    }

    public static Builder create()
    {
        return new Builder();
    }

    public static final class Builder
        extends BaseGitConnection.Builder<Builder>
    {

        private GitAuthenticationEntry authenticationEntry;

        Builder()
        {
        }

        public Builder authenticationEntry( final GitAuthenticationEntry val )
        {
            authenticationEntry = val;
            return this;
        }

        protected void validate()
        {
            super.validate();
            Preconditions.checkNotNull( this.authenticationEntry, "Authentication is missing" );
        }

        public GitHTTPSConnection build()
        {
            validate();
            return new GitHTTPSConnection( this );
        }
    }
}
