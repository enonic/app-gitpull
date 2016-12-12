package com.enonic.app.gitpull;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

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
