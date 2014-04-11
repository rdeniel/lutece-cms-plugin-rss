/*
 * Copyright (c) 2002-2012, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.rss.service;

import fr.paris.lutece.plugins.rss.business.parameter.RssFeedParameterHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.resource.ResourceService;
import fr.paris.lutece.util.ReferenceItem;

import java.util.HashMap;
import java.util.Map;


public class RssAdvanceParamService extends ResourceService
{
    private static final String MARK_LOCALE = "locale";
    private static final String PROPERTY_NAME = "rss.param.service.name";
    private static final String PROPERTY_CACHE = "rss.param.service.cache";
    private static final String PROPERTY_LOADERS = "rss.param.service.loaders";
    private static RssAdvanceParamService _singleton = new RssAdvanceParamService(  );

    /** Creates a new instance of AgendaService */
    private RssAdvanceParamService(  )
    {
        super(  );
        setNameKey( PROPERTY_NAME );
        setCacheKey( PROPERTY_CACHE );
    }

    @Override
    protected String getLoadersProperty(  )
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Returns the instance of the singleton
     *
     * @return The instance of the singleton
     */
    public static RssAdvanceParamService getInstance(  )
    {
        return _singleton;
    }

    /**
     * Build the model for the advanced parameters page
     * @param user the admin user who tries to access the page
     * @return the model for the page
     */
    public Map<String, Object> getManageAdvancedParameters( AdminUser user )
    {
        Map<String, Object> model = new HashMap<String, Object>(  );

        for ( ReferenceItem param : RssFeedParameterHome.findAll( PluginService.getPlugin( RssPlugin.PLUGIN_NAME ) ) )
        {
            model.put( param.getCode(  ), param.getName(  ) );
        }

        model.put( MARK_LOCALE, user.getLocale(  ) );

        return model;
    }
}
