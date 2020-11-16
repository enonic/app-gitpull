package com.enonic.app.gitpull;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import com.enonic.app.gitpull.authentication.GitAuthenticationEntry;
import com.enonic.app.gitpull.authentication.UserPasswordAuthentication;
import com.enonic.app.gitpull.connection.GitConnection;
import com.enonic.app.gitpull.connection.GitHTTPSConnection;
import com.enonic.app.gitpull.connection.GitSSHConnection;

final class GitConnectionConfig
{
    private final Map<String, GitConnection> entries;

    private GitConnectionConfig()
    {
        this.entries = Maps.newHashMap();
    }

    private static final Logger LOG = LoggerFactory.getLogger( GitConnectionConfig.class );

    private enum ConnectionType
    {
        SSH, HTTP, HTTPS
    }

    Map<String, GitConnection> toMap()
    {
        return this.entries;
    }

    static GitConnectionConfig create( final Map<String, String> props )
    {
        final GitConnectionConfig config = new GitConnectionConfig();

        for ( final String name : findNames( props ) )
        {
            final GitConnection connection = createConnection( name, props );
            if ( connection != null )
            {
                config.entries.put( name, connection );
            }
        }

        return config;
    }

    private static GitConnection createConnection( final String name, final Map<String, String> props )
    {
        final String url = Strings.emptyToNull( props.get( name + ".url" ) );

        if ( url == null )
        {
            LOG.error( "Missing url for config with name [{}]", name );
            return null;
        }

        final String dir = Strings.emptyToNull( props.get( name + ".dir" ) );

        final Integer timeout = getConnectionTimeout( name, props );

        final String ref = Strings.emptyToNull( props.get( name + ".ref" ) );

        final ConnectionType connectionType = getConnectionType( url );

        if ( connectionType == null )
        {
            LOG.error( "Unknown connection-type for repo with name [{}], url: [{}]", name, url );
            return null;
        }

        if ( connectionType == ConnectionType.HTTPS || connectionType == ConnectionType.HTTP )
        {
            GitAuthenticationEntry httpsAuthentication = createHttpsAuthentication( name, props );

            return GitHTTPSConnection.create().
                dir( dir != null ? new File( dir ) : null ).
                name( name ).
                url( url ).
                timeout( timeout ).
                ref( ref ).
                authenticationEntry( httpsAuthentication ).
                build();
        }

        if ( connectionType == ConnectionType.SSH )
        {
            String keyName = props.get( name + ".keyPath" );
            final String privateKey = Strings.emptyToNull( keyName );
            if ( privateKey == null || !Files.isReadable( Paths.get( privateKey ) ) )
            {
                LOG.error( "Cannot find or read private key-path for connection [{}] in path [{}]", name, keyName );
                return null;
            }

            final String strictHostKeyChecking = Strings.emptyToNull( props.get( name + ".strictHostKeyChecking" ) );

            return GitSSHConnection.create().
                dir( dir != null ? new File( dir ) : null ).
                name( name ).
                url( url ).
                timeout( timeout ).
                ref( ref ).
                strictHostKeyChecking( Strings.isNullOrEmpty( strictHostKeyChecking ) || Boolean.getBoolean( strictHostKeyChecking ) ).
                privateKeyLocation( new File( privateKey ) ).
                build();
        }

        LOG.error( "Unknown connection-type for repo with name [{}]", name );
        return null;
    }

    private static ConnectionType getConnectionType( final String url )
    {
        if ( url.toLowerCase().startsWith( "https://" ) )
        {
            return ConnectionType.HTTPS;
        }

        if ( url.toLowerCase().startsWith( "http://" ) )
        {
            return ConnectionType.HTTP;
        }

        if ( url.toLowerCase().startsWith( "ssh://" ) )
        {
            return ConnectionType.SSH;
        }

        LOG.warn( "No protocol specified, trying ssh" );
        return ConnectionType.SSH;
    }

    private static GitAuthenticationEntry createHttpsAuthentication( final String name, final Map<String, String> props )
    {
        return new UserPasswordAuthentication( props.get( name + ".user" ), props.get( name + ".password" ) );
    }

    private static Integer getConnectionTimeout( final String name, final Map<String, String> props )
    {
        final String timeoutAsString = Strings.emptyToNull( props.get( name + ".timeout" ) );

        return timeoutAsString != null ? Integer.parseInt( timeoutAsString ) : GitPullConstants.DEFAULT_TIMEOUT_IN_SECONDS;
    }

    private static Set<String> findNames( final Map<String, String> props )
    {
        return props.
            keySet().
            stream().
            filter( str -> !str.startsWith( "component." ) ).
            filter( str -> !str.startsWith( "service." ) ).
            filter( str -> !str.startsWith( "config." ) ).
            map( str -> str.split( "\\." ) ).
            map( array -> array[0] ).
            collect( Collectors.toSet() );
    }
}
