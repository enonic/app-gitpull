package com.enonic.app.gitpull.connection;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

abstract class BaseGitConnection
    implements GitConnection
{
    final String name;

    final String url;

    final File dir;

    final Logger LOG = LoggerFactory.getLogger( this.getClass() );

    BaseGitConnection( final Builder builder )
    {
        name = builder.name;
        url = builder.url;
        dir = builder.dir;
    }

    @Override
    public String getName()
    {
        return name;
    }

    public String getUrl()
    {
        return url;
    }

    public File getDir()
    {
        return dir;
    }


    public static class Builder<B extends Builder>
    {
        private String name;

        private String url;

        private File dir;

        public Builder()
        {
        }

        @SuppressWarnings("unchecked")
        public B name( final String val )
        {
            name = val;
            return (B) this;
        }

        @SuppressWarnings("unchecked")
        public B url( final String val )
        {
            url = val;
            return (B) this;
        }

        @SuppressWarnings("unchecked")
        public B dir( final File val )
        {
            dir = val;
            return (B) this;
        }

        void validate()
        {
            Preconditions.checkNotNull( this.name, "Name is missing" );
            Preconditions.checkNotNull( this.url, "URL is missing" );
            Preconditions.checkNotNull( this.dir, "dir is missing" );
        }

    }
}
