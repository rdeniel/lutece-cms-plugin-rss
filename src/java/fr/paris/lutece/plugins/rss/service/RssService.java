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
package fr.paris.lutece.plugins.rss.service;

import fr.paris.lutece.plugins.rss.business.RssFeed;
import fr.paris.lutece.plugins.rss.business.RssFeedHome;
import fr.paris.lutece.plugins.rss.business.RssGeneratedFile;
import fr.paris.lutece.plugins.rss.business.resourceRss.IResourceRssFactory;
import fr.paris.lutece.portal.business.rss.IResourceRss;
import fr.paris.lutece.portal.business.rss.IResourceRssType;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.util.ReferenceList;

import java.util.Collection;
import java.util.List;
import java.util.Locale;


/**
 * The rss service
 *
 */
public class RssService implements IRssService
{
    private static RssService _singleton = new RssService(  );
    private static final String BEAN_RESOURCE_RSS_FACTORY = "resourceRssFactory";
    private IResourceRssFactory _resourceRssFactory;

    /**
    * Initialize the Rss service
    *
    */
    public void init(  )
    {
        RssGeneratedFile.init(  );
    }

    /**
     * Returns the instance of the singleton
     *
     * @return The instance of the singleton
     */
    public static RssService getInstance(  )
    {
        if ( _singleton == null )
        {
            _singleton = new RssService(  );
        }

        return _singleton;
    }

    /**
     * Gets the RSS content
     * @param nIdRssFeed The RSS id
     * @return The content
     */
    public String getRssContent( int nIdRssFeed )
    {
        String rssContent = "";

        try
        {
            rssContent = RssContentLoader.fetchRssFeed( nIdRssFeed );
        }
        catch ( RssParsingException e )
        {
            AppLogService.error( e.getMessage(  ) );
        }

        return rssContent;
    }

    public ReferenceList getRefListRssFeed(  )
    {
        return RssFeedHome.getRssFeedsReferenceList(  );
    }

    public static List<RssFeed> getRssFeed(  )
    {
        return RssFeedHome.getRssFeeds(  );
    }

    /**
    *
    * @return a IResourceRssFactory Object
    */
    private IResourceRssFactory getResourceRssFactory(  )
    {
        if ( _resourceRssFactory == null )
        {
            _resourceRssFactory = (IResourceRssFactory) SpringContextService.getPluginBean( RssPlugin.PLUGIN_NAME,
                    BEAN_RESOURCE_RSS_FACTORY );
        }

        return _resourceRssFactory;
    }

    /**
     * return an instance of ResourceRss Object depending on the resource rss type
     * @param strKey the type resource rss key
     * @param locale the locale
     * @return an instance of ResourceRss Object
     */
    public IResourceRss getResourceRssInstance( String strKey, Locale locale )
    {
        IResourceRss resourceRss = getResourceRssFactory(  ).getResourceRss( strKey );
        resourceRss.getResourceRssType(  )
                   .setTitle( I18nService.getLocalizedString( resourceRss.getResourceRssType(  ).getTitleI18nKey(  ),
                locale ) );

        return resourceRss;
    }

    /**
    *
    * @return a list of ResourceRss type
    */
    public Collection<IResourceRssType> getResourceRssTypeList(  )
    {
        return getResourceRssFactory(  ).getAllResourceRssType(  );
    }

    /**
     * Return a reference list wich contains the ResourceRss types
     * @param locale the locale
     * @return a reference list wich contains the ResourceRss types
     */
    public ReferenceList getRefListResourceRssType( Locale locale )
    {
        Collection<IResourceRssType> listResourceRssType = getResourceRssTypeList(  );
        ReferenceList refListResourceRssType = new ReferenceList(  );

        if ( listResourceRssType != null )
        {
            for ( IResourceRssType resourceRssType : listResourceRssType )
            {
                refListResourceRssType.addItem( resourceRssType.getKey(  ),
                    I18nService.getLocalizedString( resourceRssType.getTitleI18nKey(  ), locale ) );
            }
        }

        return refListResourceRssType;
    }
}
