package com.enonic.app.gitpull;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.enonic.xp.jaxrs.JaxRsComponent;
import com.enonic.xp.security.RoleKeys;

@Component(immediate = true, property="group=api")
@Path("/api/ext/gitpull")
@RolesAllowed(RoleKeys.ADMIN_ID)
public final class GitPullResource
    implements JaxRsComponent
{
    private GitPullService service;

    @POST
    public void pullAll()
    {
        this.service.pullAll();
    }

    @Reference
    public void setService( final GitPullService service )
    {
        this.service = service;
    }
}
