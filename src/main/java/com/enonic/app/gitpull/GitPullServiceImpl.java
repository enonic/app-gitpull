package com.enonic.app.gitpull;

import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

@Component(immediate = true)
public final class GitPullServiceImpl
    implements GitPullService
{
    private GitPullConfig config;

    GitRepoPuller repoPuller;

    public GitPullServiceImpl()
    {
        this.repoPuller = new GitRepoPullerImpl();
    }

    @Activate
    public void activate( final Map<String, String> config )
    {
        this.config = GitPullConfig.create( config );
        pullAll();
    }

    @Override
    public void pullAll()
    {
        this.config.toMap().values().forEach( this::pull );
    }

    private void pull( final GitPullEntry entry )
    {
        this.repoPuller.pull( entry );
    }
}
