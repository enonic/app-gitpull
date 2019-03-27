package com.enonic.app.gitpull.authentication;

import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

public class UserPasswordAuthentication
    implements GitAuthenticationEntry
{
    private final String user;

    private final String password;

    public UserPasswordAuthentication( final String user, final String password )
    {
        this.user = user;
        this.password = password;
    }

    public CredentialsProvider getCredentialsProvider()
    {
        return ( this.user != null && this.password != null ) ? new UsernamePasswordCredentialsProvider( this.user, this.password ) : null;
    }
}
