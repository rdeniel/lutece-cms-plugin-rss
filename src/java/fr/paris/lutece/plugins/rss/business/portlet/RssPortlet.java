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

import fr.paris.lutece.plugins.rss.service.RssContent;
import fr.paris.lutece.plugins.rss.service.RssContentService;
import fr.paris.lutece.portal.business.portlet.Portlet;
import fr.paris.lutece.util.xml.XmlUtil;

import javax.servlet.http.HttpServletRequest;


/**
 * This class represents business objects RssPortlet
 */
public class RssPortlet extends Portlet
{
    /////////////////////////////////////////////////////////////////////////////////
    // Constants
    private String _strRssFeedId;

    /**
     * Sets the identifier of the portlet type to value specified
     */
    public RssPortlet(  )
    {
        setPortletTypeId( RssPortletHome.getInstance(  ).getPortletTypeId(  ) );
    }

    /**
     * Sets the url of the portlet
     *
     * @param strRssFeedId the Rss portlet url
     */
    public void setRssFeedId( String strRssFeedId )
    {
        _strRssFeedId = strRssFeedId;
    }

    /**
     * Returns the Rss Feed Id of the Rss portlet
     *
     * @return the Rss Portlet url
     */
    public String getRssFeedId(  )
    {
        return _strRssFeedId;
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
        RssContent rssContent = RssContentService.getInstance(  ).getRssContent( getRssFeedId(  ) );
        String strDocument = rssContent.getContent(  );

        if ( strDocument != null )
        {
            /*  removes the Xml heading of the downloaded file RSS */
            int nPos = strDocument.indexOf( "<rss" );

            if ( nPos > 0 )
            {
                strXml.append( strDocument.substring( nPos, strDocument.length(  ) ) );
            }
        }

        return addPortletTags( strXml );
    }

    /**
     * Updates the current instance of the RssPortlet object
     */
    public void update(  )
    {
        RssPortletHome.getInstance(  ).update( this );
    }

    /**
     * Removes the current instance of the RssPortlet object
     */
    public void remove(  )
    {
        RssPortletHome.getInstance(  ).remove( this );
    }
}
