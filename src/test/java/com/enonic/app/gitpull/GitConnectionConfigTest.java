package com.enonic.app.gitpull;

import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.google.common.collect.Maps;

import com.enonic.app.gitpull.authentication.GitAuthenticationEntry;
import com.enonic.app.gitpull.connection.GitConnection;
import com.enonic.app.gitpull.connection.GitHTTPSConnection;

import static org.junit.Assert.*;

public class GitConnectionConfigTest
{
    @Rule
    public final TemporaryFolder temporaryFolder = new TemporaryFolder();


    @Test
    public void https_no_user_password()
    {
        final Map<String, String> props = Maps.newHashMap();
        props.put( "repo.url", "https://fisk" );
        props.put( "repo.dir", "dir" );

        final GitConnectionConfig config = GitConnectionConfig.create( props );
        final Map<String, GitConnection> entries = config.toMap();

        assertEquals( 1, entries.size() );
    }

    @Test
    public void ssh_connection()
    {
        final Map<String, String> props = Maps.newHashMap();
        props.put( "repo.url", "ssh://fisk" );
        props.put( "repo.dir", "dir" );
        props.put( "repo.keyPath", temporaryFolder.getRoot().getAbsolutePath() );

        final GitConnectionConfig config = GitConnectionConfig.create( props );
        final Map<String, GitConnection> entries = config.toMap();

        assertEquals( 1, entries.size() );
    }


    @Test
    public void https_user_password()
    {
        final Map<String, String> props = Maps.newHashMap();
        props.put( "repo.url", "https://fisk" );
        props.put( "repo.dir", "dir" );
        props.put( "repo.user", "user" );
        props.put( "repo.password", "password" );

        final GitConnectionConfig config = GitConnectionConfig.create( props );
        final Map<String, GitConnection> entries = config.toMap();

        assertEquals( 1, entries.size() );
        final GitConnection connection = entries.get( "repo" );
        assertTrue( connection instanceof GitHTTPSConnection );
        GitAuthenticationEntry authenticationEntry = ( (GitHTTPSConnection) connection ).getAuthenticationEntry();
        assertNotNull( authenticationEntry );
        assertNotNull( authenticationEntry.getCredentialsProvider() );
    }

    @Test
    public void https_branch()
    {
        final Map<String, String> props = Maps.newHashMap();
        props.put( "repo.url", "https://fisk" );
        props.put( "repo.dir", "dir" );
        props.put( "repo.user", "user" );
        props.put( "repo.password", "password" );
        props.put( "repo.ref", "develop" );

        final GitConnectionConfig config = GitConnectionConfig.create( props );
        final Map<String, GitConnection> entries = config.toMap();

        assertEquals( 1, entries.size() );
        final GitConnection connection = entries.get( "repo" );
        assertTrue( connection instanceof GitHTTPSConnection );

        final String ref = ( (GitHTTPSConnection) connection ).getRef();
        assertNotNull( ref );
        assertEquals( "develop", ref );
    }

    @Test
    public void https_timeout()
    {
        final Map<String, String> props = Maps.newHashMap();
        props.put( "repo.url", "https://fisk" );
        props.put( "repo.dir", "dir" );
        props.put( "repo.user", "user" );
        props.put( "repo.password", "password" );
        props.put( "repo.timeout", "30" );

        final GitConnectionConfig config = GitConnectionConfig.create( props );
        final Map<String, GitConnection> entries = config.toMap();

        assertEquals( 1, entries.size() );
        final GitConnection connection = entries.get( "repo" );
        assertTrue( connection instanceof GitHTTPSConnection );

        final Integer timeout = ( (GitHTTPSConnection) connection ).getTimeout();
        assertNotNull( timeout );
        assertEquals( 30, timeout.intValue() );
    }

}
