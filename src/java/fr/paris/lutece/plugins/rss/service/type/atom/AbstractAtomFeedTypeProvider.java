package fr.paris.lutece.plugins.rss.service.type.atom;

import java.util.List;

import com.sun.syndication.feed.WireFeed;
import com.sun.syndication.feed.atom.Entry;
import com.sun.syndication.feed.atom.Feed;

import fr.paris.lutece.plugins.rss.service.type.AbstractFeedTypeProvider;
import fr.paris.lutece.portal.business.rss.IFeedResource;
import fr.paris.lutece.portal.business.rss.IFeedResourceItem;

/**
 *
 * AtomFeedTypeProvider
 *
 */
public abstract class AbstractAtomFeedTypeProvider extends AbstractFeedTypeProvider
{
	/**
     * Gets the atom entries
     * @param listItems the items
     * @param nMaxItems max item
     * @return the entries
     */
    public abstract List<Entry> getATOMEntries( List<IFeedResourceItem> listItems, int nMaxItems );
    
	/**
     * Builds an ATOM {@link Entry}
     * @param resourceItem the item
     * @return the entry
     */
    public abstract Entry getATOMEntry( IFeedResourceItem resourceItem );
    
	/**
	 * {@inheritDoc}
	 */
	public WireFeed getWireFeed( String strFeedType, IFeedResource resource, String strEncoding, int nMaxItems )
	{
    	// ATOM
		Feed feed = new Feed( strFeedType );
    	feed.setLanguage( resource.getLanguage() );
    	feed.setTitle( resource.getTitle() );
    	feed.setEncoding( strEncoding );
    	
    	// items
    	feed.setEntries( getATOMEntries( resource.getItems(), nMaxItems ) );
    	
    	return feed;
	}
}
