package com.enonic.app.gitpull;

import java.io.File;

import org.eclipse.jgit.api.Git;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.google.common.io.Files;

import com.enonic.app.gitpull.authentication.UserPasswordAuthentication;
import com.enonic.app.gitpull.connection.GitHTTPSConnection;

import static org.junit.Assert.*;

public class GitRepoPullerImplTest
{
    @Rule
    public final TemporaryFolder temporaryFolder = new TemporaryFolder();

    private Git sourceRepo;

    private File gitRepoDir;

    @Before
    public void setup()
        throws Exception
    {
        this.gitRepoDir = this.temporaryFolder.newFolder( "git" );
        this.sourceRepo = Git.init().setBare( false ).setDirectory( this.gitRepoDir ).call();

        Files.touch( new File( this.gitRepoDir, "test.txt" ) );
        this.sourceRepo.add().addFilepattern( "." ).call();
        this.sourceRepo.add().setUpdate( true ).addFilepattern( "." ).call();
        this.sourceRepo.commit().setAll( true ).setMessage( "Initial commit" ).call();
    }

    @Test
    public void testPull()
        throws Exception
    {
        final String repoDir = this.gitRepoDir.toURI().toString();

        final GitHTTPSConnection conn = GitHTTPSConnection.create().
            name( "test" ).
            url( repoDir ).
            dir( this.temporaryFolder.newFolder( "checkout" ) ).
            authenticationEntry( new UserPasswordAuthentication( "fisk", "ost" ) ).
            build();

        final GitRepoPuller puller = new GitRepoPullerImpl();

        puller.pull( conn );

        final File file1 = new File( conn.getDir(), "test.txt" );
        assertTrue( file1.exists() );

        Files.touch( new File( this.gitRepoDir, "other.txt" ) );
        this.sourceRepo.add().addFilepattern( "." ).call();
        this.sourceRepo.add().setUpdate( true ).addFilepattern( "." ).call();
        this.sourceRepo.commit().setAll( true ).setMessage( "New commit" ).call();

        puller.pull( conn );

        final File file2 = new File( conn.getDir(), "other.txt" );
        assertTrue( file2.exists() );
    }
}
