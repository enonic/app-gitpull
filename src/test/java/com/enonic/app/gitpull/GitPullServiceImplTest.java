package com.enonic.app.gitpull;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.common.collect.Maps;

public class GitPullServiceImplTest
{
    private GitRepoPuller repoPuller;

    private GitPullServiceImpl service;

    @Before
    public void setup()
    {
        this.repoPuller = Mockito.mock( GitRepoPuller.class );
        this.service = new GitPullServiceImpl();
        this.service.repoPuller = this.repoPuller;
    }

    @Test
    public void testPullAll()
    {
        final Map<String, String> props = Maps.newHashMap();
        props.put( "a.url", "url" );
        props.put( "a.dir", "dir" );
        props.put( "b.url", "url" );
        props.put( "b.dir", "dir" );

        this.service.activate( props );
        Mockito.verify( this.repoPuller, Mockito.times( 2 ) ).pull( Mockito.any() );

        this.service.pullAll();
        Mockito.verify( this.repoPuller, Mockito.times( 4 ) ).pull( Mockito.any() );
    }
}
