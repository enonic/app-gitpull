package com.enonic.app.gitpull.connection;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import com.enonic.app.gitpull.GitPullConstants;

abstract class BaseGitConnection
    implements GitConnection
{
    final String name;

    final String url;

    final File dir;

    final Integer timeout;

    final String ref;

    final Logger LOG = LoggerFactory.getLogger( this.getClass() );

    BaseGitConnection( final Builder builder )
    {
        name = builder.name;
        url = builder.url;
        dir = builder.dir;
        ref = builder.ref;
        timeout = builder.timeout != null ? builder.timeout : GitPullConstants.DEFAULT_TIMEOUT_IN_SECONDS;
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

    @Override
    public File getDir()
    {
        return dir;
    }

    public Integer getTimeout()
    {
        return timeout;
    }

    public String getRef()
    {
        return ref;
    }

    public static class Builder<B extends Builder>
    {
        private String name;

        private String url;

        private File dir;

        private Integer timeout;

        private String ref;

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

        @SuppressWarnings("unchecked")
        public B timeout( final Integer val )
        {
            timeout = val;
            return (B) this;
        }

        @SuppressWarnings("unchecked")
        public B ref( final String val )
        {
            ref = val;
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
