package com.enonic.app.gitpull.authentication;

import org.eclipse.jgit.transport.CredentialsProvider;

public
interface GitAuthenticationEntry
{
    CredentialsProvider getCredentialsProvider();
}
