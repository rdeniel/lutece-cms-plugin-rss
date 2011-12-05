package fr.paris.lutece.plugins.rss.service.type.atom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.sun.syndication.feed.atom.Entry;

import fr.paris.lutece.portal.business.rss.IFeedResourceItem;

/**
 *
 * AtomFeedTypeProvider
 *
 */
public class DefaultAtomFeedTypeProvider extends AbstractAtomFeedTypeProvider
{
	private static final String ATOM_PREFIX = "atom";
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isInvoked( String strFeedType )
	{
		return ATOM_PREFIX.startsWith( strFeedType );
	}
	
	/**
     * {@inheritDoc}
     */
    public List<Entry> getATOMEntries( List<IFeedResourceItem> listItems, int nMaxItems )
    {
    	List<Entry> listEntries = new ArrayList<Entry>(  );
    	
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
					break;
				}
			}
		}
    	
    	return listEntries;
    }
    
    /**
     * {@inheritDoc}
     */
    public Entry getATOMEntry( IFeedResourceItem resourceItem )
    {
    	Entry entry = new Entry(  );
    	
    	entry.setTitle( resourceItem.getTitle(  ) );
    	entry.setPublished( resourceItem.getDate(  ) );
    	com.sun.syndication.feed.atom.Content summary = new com.sun.syndication.feed.atom.Content(  );
    	summary.setValue( resourceItem.getTitle(  ) );
    	entry.setSummary( summary );
    	
    	com.sun.syndication.feed.atom.Content content = new com.sun.syndication.feed.atom.Content(  );
    	content.setValue( resourceItem.getDescription(  ) );
    	// we don't handle multi-content
    	entry.setContents( Collections.singletonList( content ) );
    	
    	return entry;
    }
}
