/*
 * Copyright (c) 2002-2011, Mairie de Paris
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
package fr.paris.lutece.plugins.rss.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.sun.syndication.feed.WireFeed;
import com.sun.syndication.feed.atom.Entry;
import com.sun.syndication.feed.atom.Feed;
import com.sun.syndication.feed.rss.Channel;
import com.sun.syndication.feed.rss.Content;
import com.sun.syndication.feed.rss.Guid;
import com.sun.syndication.feed.rss.Image;
import com.sun.syndication.feed.rss.Item;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.WireFeedOutput;

import fr.paris.lutece.portal.business.rss.IFeedResource;
import fr.paris.lutece.portal.business.rss.IFeedResourceItem;
import fr.paris.lutece.portal.business.rss.IResourceRss;
import fr.paris.lutece.portal.service.util.AppLogService;

/**
 * 
 * RSSUtil.
 */
public final class FeedUtil
{
	private static final String EMPTY_STRING = "";
	private static final String ATOM_PREFIX = "atom";
	private static final int MAX_ITEM_UNLIMITED = 0;
	
	/**
	 * Utility class
	 */
	private FeedUtil()
	{
		// nothing
	}
	
	/**
	 * Gets an XML for the given resource.
	 * @param resource resource
	 * @param strFeedType feed type
	 * @param strEncoding encoding
	 * @param nMaxItems max items
	 * @return the xml generated
	 */
	public static String getFeed( IFeedResource resource, String strFeedType, String strEncoding, int nMaxItems )
	{
    	if ( resource.getItems().isEmpty() )
    	{
    		// an empty feed will fail
    		return EMPTY_STRING;
    	}
    	
    	// WireFeed does not contain enough data, but can be passed for output generation.
    	WireFeed wireFeed;
    	
    	if ( isATOM( strFeedType ) )
    	{
    		// ATOM
    		Feed feed = new Feed( strFeedType );
        	feed.setLanguage( resource.getLanguage() );
        	feed.setTitle( resource.getTitle() );
        	feed.setEncoding( strEncoding );
        	
        	// items
        	feed.setEntries( getATOMEntries( resource.getItems(), nMaxItems ) );
        	
        	wireFeed = feed;
    	}
    	else
    	{
    		// RSS
    		Channel rss = new Channel( strFeedType );
    		rss.setLanguage( resource.getLanguage() );
    		rss.setTitle( resource.getTitle() );
    		rss.setEncoding( strEncoding );
    		rss.setDescription( resource.getDescription() );
    		rss.setLink( resource.getLink() );
    		
    		// image
    		if ( resource.getImage() != null )
    		{
				Image image = new Image();
				image.setTitle( resource.getImage().getTitle() );
				image.setUrl( resource.getImage().getUrl() );
				image.setLink( resource.getImage().getLink() );
				rss.setImage( image );
    		}
    		
    		// items
    		rss.setItems( getRSSItems( resource.getItems(), nMaxItems ) );
    		
    		wireFeed = rss;
    	}
    	
    	WireFeedOutput output = new WireFeedOutput();
    	String strXML;
    	try
		{
    		strXML = output.outputString( wireFeed );
		}
		catch ( IllegalArgumentException e )
		{
			AppLogService.error( e.getMessage(), e );
			strXML = EMPTY_STRING;
		}
		catch ( FeedException e )
		{
			AppLogService.error( e.getMessage(), e );
			strXML = EMPTY_STRING;
		}
		
		return strXML;
	}
	
	/**
	 * Gets an XML for the given resource.
	 * @param resourceRSS the resource
	 * @return the XML as String, encoding is {@link IResourceRss#getEncoding()}. Returns an empty String if no items. 
	 * Returns <code>{@link IResourceRss#createHtmlRss()}</code> if {@link IResourceRss#getFeed()} is not implemented yet.
	 */
	public static String getFeed( IResourceRss resourceRSS )
	{
    	IFeedResource resource = resourceRSS.getFeed();
    	
    	if ( resource == null )
    	{
    		// no implementation - use the deprecated
    		return resourceRSS.createHtmlRss();
    	}
    	
    	if ( resource.getItems().isEmpty() )
    	{
    		// an empty feed will fail
    		return EMPTY_STRING;
    	}
    	
    	return getFeed( resource, resourceRSS.getFeedType(), resourceRSS.getEncoding(), resourceRSS.getMaxItems() );
	}
	
    /**
     * Gets the atom entries
     * @param listItems the items
     * @param nMaxItems max item
     * @return the entries
     */
    private static List getATOMEntries( List<IFeedResourceItem> listItems, int nMaxItems )
    {
    	List listEntries = new ArrayList();
    	
    	boolean bLimit = nMaxItems != MAX_ITEM_UNLIMITED;
    	int nIndex = 1;
    	
    	for ( IFeedResourceItem item : listItems )
		{
			listEntries.add( getATOMEntry( item ) );
			if ( bLimit )
			{
				if ( nIndex < nMaxItems )
				{
					nIndex++;
				}
				else
				{
					// break
					break;
				}
			}
		}
    	
    	return listEntries;
    }
    
    /**
     * Gets the entries for the given items. 
     * @param listItems the items
     * @param nMaxItems max item
     * @return the list
     */
	private static List getRSSItems( List<IFeedResourceItem> listItems, int nMaxItems )
    {
    	List listEntries = new ArrayList();
    	
    	boolean bLimit = nMaxItems != MAX_ITEM_UNLIMITED;
    	int nIndex = 1;
		for ( IFeedResourceItem item : listItems )
		{
			listEntries.add( getRSSItem( item ) );
			if ( bLimit )
			{
				if ( nIndex < nMaxItems )
				{
					nIndex++;
				}
				else
				{
					// break
					break;
				}
			}
		}
		
		return listEntries;
    }
    
    /**
     * Builds an ATOM {@link Entry}
     * @param resourceItem the item
     * @return the entry
     */
    private static Entry getATOMEntry( IFeedResourceItem resourceItem )
    {
    	Entry entry = new Entry();
    	
    	entry.setTitle( resourceItem.getTitle() );
    	entry.setPublished( resourceItem.getDate() );
    	com.sun.syndication.feed.atom.Content summary = new com.sun.syndication.feed.atom.Content();
    	summary.setValue( resourceItem.getTitle() );
    	entry.setSummary( summary );
    	
    	com.sun.syndication.feed.atom.Content content = new com.sun.syndication.feed.atom.Content();
    	content.setValue( resourceItem.getDescription() );
    	// we don't handle multi-content
    	entry.setContents( Collections.singletonList( content ) );
    	
    	return entry;
    }
    
    /**
     * The entry from the item
     * @param item the item
     * @return the entry
     */
    private static Item getRSSItem( IFeedResourceItem resourceItem )
    {
    	Item item = new Item();
    	
    	item.setTitle( resourceItem.getTitle() );
    	item.setPubDate( resourceItem.getDate() );
    	item.setLink( resourceItem.getLink() );
    	
    	Guid guid = new Guid();
    	guid.setValue( resourceItem.getGUID() );
    	guid.setPermaLink( false );
    	item.setGuid( guid );
    	
    	Content content = new Content();
    	content.setValue( resourceItem.getDescription() );
    	item.setContent( content );

    	return item;
    }
    
    /**
     * <code>true</code> if this feed is ATOM
     * @return <code>true</code> if the feed is ATOM, <code>false</code> otherwise.
     */
    private static boolean isATOM( String strFeedType )
    {
    	return strFeedType.startsWith( ATOM_PREFIX );
    }

}
