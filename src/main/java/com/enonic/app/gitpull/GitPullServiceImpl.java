package com.enonic.app.gitpull;

import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

import com.enonic.app.gitpull.connection.GitConnection;

@Component(immediate = true, configurationPid = "com.enonic.app.gitpull")
final class GitPullServiceImpl
    implements GitPullService
{
    private GitConnectionConfig config;

    GitRepoPuller repoPuller;

    public GitPullServiceImpl()
    {
        this.repoPuller = new GitRepoPullerImpl();
    }

    @Activate
    public void activate( final Map<String, String> config )
    {
        this.config = GitConnectionConfig.create( config );
        pullAll();
    }

    @Override
    public void pullAll()
    {
        this.config.toMap().values().forEach( this::pull );
    }

    private void pull( final GitConnection connection )
    {
        this.repoPuller.pull( connection );
    }
}
