/*
 * Copyright (c) 2002-2012, Mairie de Paris
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

import fr.paris.lutece.plugins.rss.business.RssFeed;
import fr.paris.lutece.plugins.rss.business.RssFeedHome;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.web.xpages.XPage;
import fr.paris.lutece.portal.web.xpages.XPageApplication;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 * This class populate the RSS list XPage
 */
public class RssListApp implements XPageApplication
{
    private static final String TEMPLATE_RSS_LIST = "skin/plugins/rss/rss_list.html";
    private static final String PROPERTY_PAGE_TITLE = "rssList.pageTitle";
    private static final String PROPERTY_PAGE_PATH_LABEL = "rssList.pagePathLabel";
    private static final String MARK_RSS_FEEDS_LIST = "rss_feeds_list";

    /**
     * This page give a list of RSS feeds
     */
    public XPage getPage( HttpServletRequest request, int nMode, Plugin plugin )
    {
        XPage page = new XPage(  );

        Locale locale = request.getLocale(  );

        Map<String, Object> model = new HashMap<String, Object>(  );

        // population de la liste des flux
        List<RssFeed> listRssFeedList = RssFeedHome.getRssFeeds(  );
        model.put( MARK_RSS_FEEDS_LIST, listRssFeedList );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_RSS_LIST, locale, model );
        String strPageTitle = I18nService.getLocalizedString( PROPERTY_PAGE_TITLE, locale );
        String strPagePathLabel = I18nService.getLocalizedString( PROPERTY_PAGE_PATH_LABEL, locale );

        page.setContent( template.getHtml(  ) );
        page.setTitle( strPageTitle );
        page.setPathLabel( strPagePathLabel );

        return page;
    }
}
