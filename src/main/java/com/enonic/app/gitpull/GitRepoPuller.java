package com.enonic.app.gitpull;

import com.enonic.app.gitpull.connection.GitConnection;

interface GitRepoPuller
{
    void pull( GitConnection connection );
}
