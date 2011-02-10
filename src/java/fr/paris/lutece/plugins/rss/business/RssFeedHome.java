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

import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.util.ReferenceList;

import java.util.List;


/**
 * This class provides instances management methods (create, find, ...) for RssFeed objects
 */
public final class RssFeedHome
{
    // Static variable pointed at the DAO instance
    private static IRssFeedDAO _dao = (IRssFeedDAO) SpringContextService.getPluginBean( "rss", "rssFeedDAO" );

    /**
     * Private constructor - this class need not be instantiated
     */
    private RssFeedHome(  )
    {
    }

    /**
     * Creation of an instance of rssFeed
     * @param rssFeed The instance of the rssFeed which contains the informations to store
     * @return The  instance of rssFeed which has been created with its primary key.
     */
    public static RssFeed create( RssFeed rssFeed )
    {
        _dao.insert( rssFeed );

        return rssFeed;
    }

    /**
     * Update of the rssFeed which is specified in parameter
     * @param rssFeed The instance of the rssFeed which contains the data to store
     * @return The instance of the  rssFeed which has been updated
     */
    public static RssFeed update( RssFeed rssFeed )
    {
        _dao.store( rssFeed );

        return rssFeed;
    }

    /**
     * Update of the rssFeed which is specified in parameter
     * @param rssFeed The instance of the rssFeed which contains the data to store
     * @return The instance of the  rssFeed which has been updated
     */
    public static RssFeed updateLastFetchInfos( RssFeed rssFeed )
    {
        _dao.storeLastFetchInfos( rssFeed );

        return rssFeed;
    }

    /**
     * Remove the RssFeed whose identifier is specified in parameter
     * @param nId The RssFeed ID
     */
    public static void remove( int nId )
    {
        _dao.delete( nId );
    }

    ///////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Returns an instance of a rssFeed whose identifier is specified in parameter
     * @param nKey The Primary key of the rssFeed
     * @return An instance of rssFeed
     */
    public static RssFeed findByPrimaryKey( int nKey )
    {
        return _dao.load( nKey );
    }

    /**
     * Returns a list of rss feeds
     * @return A List of rssFeeds
     */
    public static List<RssFeed> getRssFeeds(  )
    {
        return _dao.selectRssFeeds(  );
    }

    /**
     * Returns a list of rssFeeds objects
     * @return A list of rssFeeds
     */
    public static ReferenceList getRssFeedsReferenceList(  )
    {
        return _dao.selectRssFeedReferenceList(  );
    }

    /**
     * Checks whether the a url is found in one of the external feeds
     * @param strUrl The url to be tested
     * @return The boolean result
     */
    public static boolean checkUrlNotUsed( String strUrl )
    {
        return _dao.checkUrlNotUsed( strUrl );
    }
}
