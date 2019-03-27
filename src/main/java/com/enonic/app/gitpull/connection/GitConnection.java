package com.enonic.app.gitpull.connection;

import java.io.File;

import org.eclipse.jgit.api.Git;

public interface GitConnection
{
    String getName();

    File getDir();

    void pull( final Git git );

    void gitClone();


}
