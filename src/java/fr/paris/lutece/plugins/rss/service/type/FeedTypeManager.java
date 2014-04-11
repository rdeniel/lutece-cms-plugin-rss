/*
 * Copyright (c) 2002-2013, Mairie de Paris
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
package fr.paris.lutece.plugins.rss.service.type;

import com.sun.syndication.feed.WireFeed;

import fr.paris.lutece.plugins.rss.service.RssPlugin;
import fr.paris.lutece.portal.business.rss.IFeedResource;
import fr.paris.lutece.portal.service.spring.SpringContextService;

import java.util.List;


/**
 *
 * FeedTypeManager
 *
 */
public final class FeedTypeManager
{
    private static final String BEAN_FEED_TYPE_MANAGER = "rss.feedTypeManager";
    private List<IFeedTypeProvider> _listDefaultProviders;
    private List<IFeedTypeProvider> _listProviders;

    /**
     * Get the instance of the manager
     * @return the manager
     */
    public static FeedTypeManager getManager(  )
    {
        return (FeedTypeManager) SpringContextService.getPluginBean( RssPlugin.PLUGIN_NAME, BEAN_FEED_TYPE_MANAGER );
    }

    /**
     * Set the list of providers
     * @param listProviders the list of providers
     */
    public void setProvidersList( List<IFeedTypeProvider> listProviders )
    {
        _listProviders = listProviders;
    }

    /**
     * Set the default providers list
     * @param listDefaultProviders the default providers list
     */
    public void setDefaultProvidersList( List<IFeedTypeProvider> listDefaultProviders )
    {
        _listDefaultProviders = listDefaultProviders;
    }

    /**
     * Get the wire feed
     * @param strFeedType the feed type
     * @param resource the resource
     * @param strEncoding the encoding
     * @param nMaxItems the max items
     * @return the wire feed
     */
    public WireFeed getWireFeed( String strFeedType, IFeedResource resource, String strEncoding, int nMaxItems )
    {
        // Get from providers first
        for ( IFeedTypeProvider provider : _listProviders )
        {
            if ( provider.isInvoked( strFeedType ) )
            {
                return provider.getWireFeed( strFeedType, resource, strEncoding, nMaxItems );
            }
        }

        // If no providers are invoked, then get from default providers
        for ( IFeedTypeProvider defaultProvider : _listDefaultProviders )
        {
            if ( defaultProvider.isInvoked( strFeedType ) )
            {
                return defaultProvider.getWireFeed( strFeedType, resource, strEncoding, nMaxItems );
            }
        }

        return null;
    }
}
