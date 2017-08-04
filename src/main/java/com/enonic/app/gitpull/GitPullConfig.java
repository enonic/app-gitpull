package com.enonic.app.gitpull;

import java.io.File;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;

final class GitPullConfig
{
    private final Map<String, GitPullEntry> entries;

    private GitPullConfig()
    {
        this.entries = Maps.newHashMap();
    }

    Map<String, GitPullEntry> toMap()
    {
        return this.entries;
    }

    static GitPullConfig create( final Map<String, String> props )
    {
        final GitPullConfig config = new GitPullConfig();
        for ( final String name : findNames( props ) )
        {
            final GitPullEntry entry = createEntry( name, props );
            if ( entry.isValid() )
            {
                config.entries.put( name, entry );
            }
        }

        return config;
    }

    private static GitPullEntry createEntry( final String name, final Map<String, String> props )
    {
        final GitPullEntry entry = new GitPullEntry();
        entry.name = name;
        entry.url = Strings.emptyToNull( props.get( name + ".url" ) );
        entry.user = Strings.emptyToNull( props.get( name + ".user" ) );
        entry.password = Strings.emptyToNull( props.get( name + ".password" ) );

        final String dir = Strings.emptyToNull( props.get( name + ".dir" ) );
        entry.dir = dir != null ? new File( dir ) : null;
        return entry;
    }

    private static Set<String> findNames( final Map<String, String> props )
    {
        return props.
            keySet().
            stream().
            map( str -> str.split( "\\." ) ).
            map( array -> array[0] ).
            collect( Collectors.toSet() );
    }
}
