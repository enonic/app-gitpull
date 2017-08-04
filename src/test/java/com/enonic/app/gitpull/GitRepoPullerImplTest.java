package com.enonic.app.gitpull;

import java.io.File;

import org.eclipse.jgit.api.Git;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.google.common.io.Files;

import static org.junit.Assert.*;

public class GitRepoPullerImplTest
{
    @Rule
    public final TemporaryFolder temporaryFolder = new TemporaryFolder();

    private Git sourceRepo;

    private GitPullEntry entry;

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

        this.entry = new GitPullEntry();
        this.entry.name = "test";
        this.entry.dir = this.temporaryFolder.newFolder( "checkout" );
    }

    @Test
    public void testPull()
        throws Exception
    {
        this.entry.url = this.gitRepoDir.toURI().toString();
        final GitRepoPuller puller = new GitRepoPullerImpl();
        puller.pull( this.entry );

        final File file1 = new File( this.entry.dir, "test.txt" );
        assertTrue( file1.exists() );

        Files.touch( new File( this.gitRepoDir, "other.txt" ) );
        this.sourceRepo.add().addFilepattern( "." ).call();
        this.sourceRepo.add().setUpdate( true ).addFilepattern( "." ).call();
        this.sourceRepo.commit().setAll( true ).setMessage( "New commit" ).call();

        puller.pull( this.entry );

        final File file2 = new File( this.entry.dir, "other.txt" );
        assertTrue( file2.exists() );
    }

    @Test
    public void testPull_error()
        throws Exception
    {
        final GitRepoPullerImpl puller = new GitRepoPullerImpl();
        puller.doPull( this.sourceRepo, this.entry );
    }

    @Test
    public void testClone_error()
        throws Exception
    {
        this.entry.url = "wrong-url";
        final GitRepoPuller puller = new GitRepoPullerImpl();
        puller.pull( this.entry );
    }
}
