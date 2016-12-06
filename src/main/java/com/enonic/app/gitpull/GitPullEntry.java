package com.enonic.app.gitpull;

import java.io.File;

import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

final class GitPullEntry
{
    String name;

    String uri;

    String user;

    String password;

    File dir;

    boolean isValid()
    {
        return ( this.name != null ) && ( this.uri != null ) && ( this.dir != null );
    }

    CredentialsProvider getCredentialsProvider()
    {
        return ( this.user != null && this.password != null ) ? new UsernamePasswordCredentialsProvider( this.user, this.password ) : null;
    }
}
