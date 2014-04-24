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
package fr.paris.lutece.plugins.rss.service.type.rss;

import com.sun.syndication.feed.WireFeed;
import com.sun.syndication.feed.rss.Channel;
import com.sun.syndication.feed.rss.Image;
import com.sun.syndication.feed.rss.Item;

import fr.paris.lutece.plugins.rss.service.type.AbstractFeedTypeProvider;
import fr.paris.lutece.portal.business.rss.IFeedResource;
import fr.paris.lutece.portal.business.rss.IFeedResourceItem;

import java.util.List;


/**
 *
 * RssFeedTypeProvider
 *
 */
public abstract class AbstractRssFeedTypeProvider extends AbstractFeedTypeProvider
{
    /**
    * Gets the entries for the given items.
    * @param listItems the items
    * @param nMaxItems max item
    * @return the list
    */
    public abstract List<Item> getRSSItems( List<IFeedResourceItem> listItems, int nMaxItems );

    /**
    * The entry from the item
    * @param item the item
    * @return the entry
    */
    public abstract Item getRSSItem( IFeedResourceItem resourceItem );

    /**
     * {@inheritDoc}
     */
    public WireFeed getWireFeed( String strFeedType, IFeedResource resource, String strEncoding, int nMaxItems )
    {
        // RSS
        Channel rss = new Channel( strFeedType );
        rss.setLanguage( resource.getLanguage(  ) );
        rss.setTitle( resource.getTitle(  ) );
        rss.setEncoding( strEncoding );
        rss.setDescription( resource.getDescription(  ) );
        rss.setLink( resource.getLink(  ) );

        // image
        if ( resource.getImage(  ) != null )
        {
            Image image = new Image(  );
            image.setTitle( resource.getImage(  ).getTitle(  ) );
            image.setUrl( resource.getImage(  ).getUrl(  ) );
            image.setLink( resource.getImage(  ).getLink(  ) );
            rss.setImage( image );
        }

        // items
        rss.setItems( getRSSItems( resource.getItems(  ), nMaxItems ) );

        return rss;
    }
}
