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

import java.util.List;


/**
 *
 * @author Isabelle
 */
public interface IRssFeedDAO
{
    /**
     * Checks whether url referenced is pointed as an external feed
     * @param strUrl The url to be tested
     * @return The boolean result
     */
    boolean checkUrlNotUsed( String strUrl );

    /**
     * Delete a record from the table
     * @param nId Delete the rss feed
     */
    void delete( int nId );

    /**
     * Insert a new record in the table.
     * @param rssFeed The rssFeed object
     */
    void insert( RssFeed rssFeed );

    /**
     * Load the data of RssFeed from the table
     * @param nRssFeedId The identifier of RssFeed
     * @return the instance of the RssFeed
     */
    RssFeed load( int nRssFeedId );

    /**
     * Load the list of rssFeeds
     * @return A referenceList representing the RssFeeds
     */
    ReferenceList selectRssFeedReferenceList(  );

    /**
     * Load the list of rssFeeds
     * @return The List of the RssFeeds
     */
    List<RssFeed> selectRssFeeds(  );

    /**
     * Update the record in the table
     * @param rssFeed The reference of rssFeed
     */
    void store( RssFeed rssFeed );

    /**
     * Update the record in the table
     * @param rssFeed The reference of rssFeed
     */
    void storeLastFetchInfos( RssFeed rssFeed );
}
