/*
 * Copyright (c) 2002-2009, Mairie de Paris
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
package fr.paris.lutece.plugins.rss.business;

import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * This class provides Data Access methods for RssFeed objects
 */
public final class RssFeedDAO implements IRssFeedDAO
{
    // Constants
    private static final String SQL_QUERY_NEW_PK = " SELECT max( id_rss_feed ) FROM rss_feed ";
    private static final String SQL_QUERY_SELECT = " SELECT id_rss_feed, name, url, last_fetch_date, last_fetch_status, last_fetch_error, workgroup_key, include_style FROM rss_feed WHERE id_rss_feed = ?  ";
    private static final String SQL_QUERY_INSERT = " INSERT INTO rss_feed ( id_rss_feed, name, url, last_fetch_date, last_fetch_status, last_fetch_error, workgroup_key, include_style ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = " DELETE FROM rss_feed WHERE id_rss_feed = ?  ";
    private static final String SQL_QUERY_UPDATE = " UPDATE rss_feed SET id_rss_feed = ?, name = ?, url = ?, workgroup_key = ?, include_style = ? WHERE id_rss_feed = ?  ";
    private static final String SQL_QUERY_SELECTALL = " SELECT id_rss_feed, name, url, last_fetch_date, last_fetch_status, last_fetch_error, workgroup_key, include_style FROM rss_feed ";
    private static final String SQL_QUERY_UPDATE_LAST_FETCH_INFOS = " UPDATE rss_feed SET last_fetch_date = ?, last_fetch_status = ?, last_fetch_error = ? WHERE id_rss_feed = ?  ";
    private static final String SQL_QUERY_SELECT_URL = "SELECT url from rss_feed WHERE url = ? ";

    /**
     * Generates a new primary key
     * @return The new primary key
     */
    private int newPrimaryKey(  )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK );
        daoUtil.executeQuery(  );

        int nKey;

        if ( !daoUtil.next(  ) )
        {
            // if the table is empty
            nKey = 1;
        }

        nKey = daoUtil.getInt( 1 ) + 1;
        daoUtil.free(  );

        return nKey;
    }

    /**
     * Insert a new record in the table.
     * @param rssFeed The rssFeed object
     */
    public void insert( RssFeed rssFeed )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT );
        rssFeed.setId( newPrimaryKey(  ) );
        daoUtil.setInt( 1, rssFeed.getId(  ) );
        daoUtil.setString( 2, rssFeed.getName(  ) );
        daoUtil.setString( 3, rssFeed.getUrl(  ) );
        daoUtil.setTimestamp( 4, rssFeed.getLastFetchDate(  ) );
        daoUtil.setInt( 5, rssFeed.getLastFetchStatus(  ) );
        daoUtil.setString( 6, rssFeed.getLastFetchError(  ) );
        daoUtil.setString( 7, rssFeed.getWorkgroup(  ) );
        daoUtil.setInt( 8, rssFeed.getIdIncludeStyle(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Load the data of RssFeed from the table
     * @return the instance of the RssFeed
     * @param nRssFeedId The identifier of RssFeed
     */
    public RssFeed load( int nRssFeedId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT );
        daoUtil.setInt( 1, nRssFeedId );
        daoUtil.executeQuery(  );

        RssFeed rssFeed = null;

        if ( daoUtil.next(  ) )
        {
            rssFeed = new RssFeed(  );
            rssFeed.setId( daoUtil.getInt( 1 ) );
            rssFeed.setName( daoUtil.getString( 2 ) );
            rssFeed.setUrl( daoUtil.getString( 3 ) );
            rssFeed.setLastFetchDate( daoUtil.getTimestamp( 4 ) );
            rssFeed.setLastFetchStatus( daoUtil.getInt( 5 ) );
            rssFeed.setLastFetchError( daoUtil.getString( 6 ) );
            rssFeed.setWorkgroup( daoUtil.getString( 7 ) );
            rssFeed.setIdIncludeStyle( daoUtil.getInt( 8 ) );
        }

        daoUtil.free(  );

        return rssFeed;
    }

    /**
     * Delete a record from the table
     * @param nId Delete the rss feed
     */
    public void delete( int nId )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE );
        daoUtil.setInt( 1, nId );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Update the record in the table
     * @param rssFeed The reference of rssFeed
     */
    public void store( RssFeed rssFeed )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE );
        daoUtil.setInt( 1, rssFeed.getId(  ) );
        daoUtil.setString( 2, rssFeed.getName(  ) );
        daoUtil.setString( 3, rssFeed.getUrl(  ) );
        daoUtil.setString( 4, rssFeed.getWorkgroup(  ) );
        daoUtil.setInt( 5, rssFeed.getIdIncludeStyle(  ) );

        daoUtil.setInt( 6, rssFeed.getId(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Update the record in the table
     * @param rssFeed The reference of rssFeed
     */
    public void storeLastFetchInfos( RssFeed rssFeed )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE_LAST_FETCH_INFOS );
        daoUtil.setTimestamp( 1, rssFeed.getLastFetchDate(  ) );
        daoUtil.setInt( 2, rssFeed.getLastFetchStatus(  ) );
        daoUtil.setString( 3, rssFeed.getLastFetchError(  ) );
        daoUtil.setInt( 4, rssFeed.getId(  ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Load the list of rssFeeds
     * @return The List of the RssFeeds
     */
    public List<RssFeed> selectRssFeeds(  )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL );
        daoUtil.executeQuery(  );

        List<RssFeed> list = new ArrayList<RssFeed>(  );

        while ( daoUtil.next(  ) )
        {
            RssFeed rssFeed = new RssFeed(  );

            rssFeed.setId( daoUtil.getInt( 1 ) );
            rssFeed.setName( daoUtil.getString( 2 ) );
            rssFeed.setUrl( daoUtil.getString( 3 ) );
            rssFeed.setLastFetchDate( daoUtil.getTimestamp( 4 ) );
            rssFeed.setLastFetchStatus( daoUtil.getInt( 5 ) );
            rssFeed.setLastFetchError( daoUtil.getString( 6 ) );
            rssFeed.setWorkgroup( daoUtil.getString( 7 ) );
            rssFeed.setIdIncludeStyle( daoUtil.getInt( 8 ) );

            list.add( rssFeed );
        }

        daoUtil.free(  );

        return list;
    }

    /**
     * Checks whether url referenced is pointed as an external feed
     * @param strUrl The url to be tested
     * @return The boolean result
     */
    public boolean checkUrlNotUsed( String strUrl )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_URL );
        daoUtil.setString( 1, strUrl );
        daoUtil.executeQuery(  );

        if ( daoUtil.next(  ) )
        {
            daoUtil.free(  );

            return false;
        }

        daoUtil.free(  );

        return true;
    }

    /**
     * Load the list of rssFeeds
     * @return A referenceList representing the RssFeeds
     */
    public ReferenceList selectRssFeedReferenceList(  )
    {
        ReferenceList listRssFeeds = new ReferenceList(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            RssFeed rssFeed = new RssFeed(  );
            rssFeed.setId( daoUtil.getInt( 1 ) );
            rssFeed.setName( daoUtil.getString( 2 ) );

            listRssFeeds.addItem( rssFeed.getId(  ), rssFeed.getName(  ) );
        }

        daoUtil.free(  );

        return listRssFeeds;
    }
}
