package com.enonic.app.gitpull.connection;

import java.io.File;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.transport.JschConfigSessionFactory;
import org.eclipse.jgit.transport.OpenSshConfig;
import org.eclipse.jgit.transport.SshSessionFactory;
import org.eclipse.jgit.transport.SshTransport;
import org.eclipse.jgit.util.FS;

import com.google.common.base.Preconditions;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class GitSSHConnection
    extends BaseGitConnection
{
    private final File privateKeyLocation;

    private final SshSessionFactory sessionFactory;

    private final boolean strictHostKeyChecking;

    private GitSSHConnection( final Builder builder )
    {
        super( builder );
        this.privateKeyLocation = builder.privateKeyLocation;
        this.strictHostKeyChecking = builder.strictHostKeyChecking;
        this.sessionFactory = createFactory();
    }

    private SshSessionFactory createFactory()
    {
        return new JschConfigSessionFactory()
        {
            @Override
            protected void configure( final OpenSshConfig.Host hc, final Session session )
            {
                session.setConfig( "StrictHostKeyChecking", strictHostKeyChecking ? "yes" : "no" );
            }

            @Override
            protected JSch createDefaultJSch( FS fs )
                throws JSchException
            {
                JSch defaultJSch = super.createDefaultJSch( fs );
                defaultJSch.addIdentity( privateKeyLocation.getAbsolutePath() );
                return defaultJSch;
            }
        };
    }

    @Override
    public void pull( final Git git )
    {
        try
        {
            git.reset();
            git.pull().
                setTransportConfigCallback( transport -> {
                    SshTransport sshTransport = (SshTransport) transport;
                    sshTransport.setSshSessionFactory( createFactory() );
                } ).
                call();
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
            Git.cloneRepository().
                setURI( this.url ).
                setDirectory( this.dir ).
                setBare( false ).
                setTransportConfigCallback( transport -> {
                    SshTransport sshTransport = (SshTransport) transport;
                    sshTransport.setSshSessionFactory( this.sessionFactory );
                } ).
                call();
        }
        catch ( final Exception e )
        {
            LOG.error( "Error cloning git repository [" + this.name + "]", e );
        }
    }

    public static Builder create()
    {
        return new Builder();
    }

    public static final class Builder
        extends BaseGitConnection.Builder<Builder>
    {
        private File privateKeyLocation;

        private boolean strictHostKeyChecking = true;

        Builder()
        {
        }

        public Builder privateKeyLocation( final File privateKeyLocation )
        {
            this.privateKeyLocation = privateKeyLocation;
            return this;
        }

        public Builder strictHostKeyChecking( final boolean strictHostKeyChecking )
        {
            this.strictHostKeyChecking = strictHostKeyChecking;
            return this;
        }

        protected void validate()
        {
            super.validate();
            Preconditions.checkNotNull( this.privateKeyLocation, "Private-key location must be set" );
        }

        public GitSSHConnection build()
        {
            validate();
            return new GitSSHConnection( this );
        }
    }
}
