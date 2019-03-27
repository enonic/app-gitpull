package com.enonic.app.gitpull.connection;

import org.eclipse.jgit.api.Git;

import com.google.common.base.Preconditions;

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
            git.pull().setCredentialsProvider( this.authenticationEntry.getCredentialsProvider() ).call();
            LOG.info( "Pulled in changes from git repository [" + this.name + "]" );
        }
        catch ( final Exception e )
        {
            LOG.error( "Error pulling git repository [" + this.name + "]", e );
        }
    }

    public void gitClone()
    {
        try
        {
            Git.cloneRepository().
                setCredentialsProvider( this.authenticationEntry.getCredentialsProvider() ).
                setDirectory( this.dir ).
                setURI( this.url ).
                setBare( false ).
                call();
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
