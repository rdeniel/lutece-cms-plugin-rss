/*
 * Copyright (c) 2002-2014, Mairie de Paris
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
package fr.paris.lutece.plugins.rss.business.portlet;

import fr.paris.lutece.plugins.rss.business.RssFeed;
import fr.paris.lutece.plugins.rss.business.RssFeedHome;
import fr.paris.lutece.portal.business.portlet.Portlet;
import fr.paris.lutece.util.xml.XmlUtil;

import java.util.List;

import javax.servlet.http.HttpServletRequest;


/**
 * This class represents business objects RssPortlet
 */
public class RssListPortlet extends Portlet
{
    private static final String TAG_RSS_LIST_PORTLET = "rss-list-portlet";
    private static final String TAG_RSS_LIST_PORTLET_NAME = "rss-list-portlet-name";
    private static final String TAG_RSS_LIST_PORTLET_URL = "rss-list-portlet-url";

    /**
     * Sets the identifier of the portlet type to value specified
     */
    public RssListPortlet(  )
    {
        setPortletTypeId( RssListPortletHome.getInstance(  ).getPortletTypeId(  ) );
    }

    /**
     * Returns the Xml code of the Rss portlet with XML heading
     *
     * @param request The HTTP servlet request
     * @return the Xml code of the Rss portlet
     */
    public String getXmlDocument( HttpServletRequest request )
    {
        return XmlUtil.getXmlHeader(  ) + getXml( request );
    }

    /**
     * Returns the Xml code of the Rss portlet without XML heading
     *
     * @param request The HTTP servlet request
     * @return the Xml code of the Rss portlet content
     */
    public String getXml( HttpServletRequest request )
    {
        StringBuffer strXml = new StringBuffer(  );
        List<RssFeed> listRssFeedList = RssFeedHome.getRssFeeds(  );

        if ( listRssFeedList != null )
        {
            for ( RssFeed feed : listRssFeedList )
            {
                XmlUtil.beginElement( strXml, TAG_RSS_LIST_PORTLET );
                XmlUtil.addElementHtml( strXml, TAG_RSS_LIST_PORTLET_NAME, feed.getName(  ) );
                XmlUtil.addElementHtml( strXml, TAG_RSS_LIST_PORTLET_URL, feed.getUrl(  ) );
                XmlUtil.endElement( strXml, TAG_RSS_LIST_PORTLET );
            }
        }

        return addPortletTags( strXml );
    }

    /**
     * Updates the current instance of the RssPortlet object
     */
    public void update(  )
    {
        RssListPortletHome.getInstance(  ).update( this );
    }

    /**
     * Removes the current instance of the RssPortlet object
     */
    public void remove(  )
    {
        RssListPortletHome.getInstance(  ).remove( this );
    }
}
