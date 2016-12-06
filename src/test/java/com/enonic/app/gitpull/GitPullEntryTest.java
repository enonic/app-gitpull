package com.enonic.app.gitpull;

import java.io.File;

import org.junit.Test;

import static org.junit.Assert.*;

public class GitPullEntryTest
{
    @Test
    public void testIsValid()
    {
        final GitPullEntry entry = new GitPullEntry();
        assertFalse( entry.isValid() );

        entry.name = "name";
        assertFalse( entry.isValid() );

        entry.uri = "uri";
        assertFalse( entry.isValid() );

        entry.dir = new File( "." );
        assertTrue( entry.isValid() );
    }

    @Test
    public void testGetCredentialsProvider()
    {
        final GitPullEntry entry = new GitPullEntry();
        assertNull( entry.getCredentialsProvider() );

        entry.user = "user";
        entry.password = "password";
        assertNotNull( entry.getCredentialsProvider() );
    }
}
