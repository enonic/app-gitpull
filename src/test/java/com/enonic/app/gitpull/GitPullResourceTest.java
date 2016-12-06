package com.enonic.app.gitpull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class GitPullResourceTest
{
    private GitPullResource resource;

    private GitPullService service;

    @Before
    public void setup()
    {
        this.service = Mockito.mock( GitPullService.class );
        this.resource = new GitPullResource();
        this.resource.setService( this.service );
    }

    @Test
    public void testPullAll()
    {
        this.resource.pullAll();
        Mockito.verify( this.service, Mockito.times( 1 ) ).pullAll();
    }
}
