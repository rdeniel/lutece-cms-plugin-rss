package fr.paris.lutece.plugins.rss.service.type.rss;

import java.util.List;

import com.sun.syndication.feed.WireFeed;
import com.sun.syndication.feed.rss.Channel;
import com.sun.syndication.feed.rss.Image;
import com.sun.syndication.feed.rss.Item;

import fr.paris.lutece.plugins.rss.service.type.AbstractFeedTypeProvider;
import fr.paris.lutece.portal.business.rss.IFeedResource;
import fr.paris.lutece.portal.business.rss.IFeedResourceItem;

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
