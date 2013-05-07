/*
 * Copyright (c) 2002-2013, Mairie de Paris
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

import java.io.File;
import java.io.StringReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.xml.transform.stream.StreamSource;

import fr.paris.lutece.plugins.rss.business.RssFeed;
import fr.paris.lutece.plugins.rss.business.RssFeedHome;
import fr.paris.lutece.plugins.rss.service.RssContent;
import fr.paris.lutece.plugins.rss.service.RssContentService;
import fr.paris.lutece.plugins.rss.service.RssPlugin;
import fr.paris.lutece.portal.business.style.ModeHome;
import fr.paris.lutece.portal.business.style.Style;
import fr.paris.lutece.portal.business.style.StyleHome;
import fr.paris.lutece.portal.business.stylesheet.StyleSheet;
import fr.paris.lutece.portal.service.content.PageData;
import fr.paris.lutece.portal.service.html.XmlTransformerService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.includes.PageInclude;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.html.HtmlTemplate;


/**
 * This class provides the user interface to manage rss features
 */
public class RssFeedInclude implements PageInclude
{
    /**
     * The bookmark in the page frameset
     */
    private static final String MARK_RSS = "rss";
    private static final String MARK_PLUGIN = "plugin";
    private static final String MARK_LOCALE = "locale";

    /**
     * The list of the rss files
     */
    private static final String MARK_RSS_FILES_LIST = "rss_list";

    /**
     * The template used to generate the rss links in the html code
     */
    private static final String TEMPLATE_RSS_INCLUDE = "skin/plugins/rss/rss_feed_include.html";

    // Properties
    private static final String PROPERTY_RSS_MARKER_PREFIX = "rss.include.marker.prefix";
    private static final String PROPERTY_PATH_XSL = "path.stylesheet";

    // Default Marker prefix if nothing defined in properties file
    private static final String MARK_DEFAULT_RSS_MARKER_PREFIX = "rss_";

    //Constantes
    private static final int CONSTANTE_RSS_STATUT_OK = 0;

    /**
     * Substitue specific Freemarker markers in the page template.
     * @param rootModel the HashMap containing markers to substitute
     * @param data A PageData object containing applications data
     * @param nMode The current mode
     * @param request The HTTP request
     */
    public void fillTemplate( Map<String, Object> rootModel, PageData data, int nMode, HttpServletRequest request )
    {
        Plugin plugin = PluginService.getPlugin( RssPlugin.PLUGIN_NAME );
        Locale locale = getLocale( request );

        HashMap<String, Collection<RssFeed>> model = new HashMap<String, Collection<RssFeed>>(  );
        model.put( MARK_RSS_FILES_LIST, RssFeedHome.getRssFeeds(  ) );

        List<RssFeed> listRss = RssFeedHome.getRssFeeds(  );

        for ( RssFeed rssFeed : listRss )
        {
            if ( rssFeed.getLastFetchStatus(  ) == CONSTANTE_RSS_STATUT_OK )
            {
                rootModel.put( getRssMarkerPrefix(  ) + String.valueOf( rssFeed.getId(  ) ),
                    getTemplateHtmlForRss( rssFeed, plugin, locale ) );
            }
        }
    }

    public static String getRssMarkerPrefix(  )
    {
        return AppPropertiesService.getProperty( PROPERTY_RSS_MARKER_PREFIX, MARK_DEFAULT_RSS_MARKER_PREFIX );
    }

    /**
     * Generate the HTML code for the specified {@link RssFeed}
     * @param quicklinks The {@link RssFeed}
     * @param plugin The {@link Plugin}
     * @param locale The {@link Locale}
     * @return The HTML code
     */
    private String getTemplateHtmlForRss( RssFeed rss, Plugin plugin, Locale locale )
    {
        HashMap<String, Object> model = new HashMap<String, Object>(  );

        RssContent rssContent = RssContentService.getInstance(  ).getRssContent( String.valueOf( rss.getId(  ) ) );

        String strHtml = "";

        if ( rss.getIdIncludeStyle(  ) != -1 )
        {
            Style rssStyle = StyleHome.findByPrimaryKey( rss.getIdIncludeStyle(  ) );
            Collection<StyleSheet> rssStyleSheetList = StyleHome.getStyleSheetList( rssStyle.getId(  ) );
            String strXslDirectory = null;
            String strXslFileName = null;

            for ( StyleSheet rssStyleSheet : rssStyleSheetList )
            {
                strXslDirectory = AppPathService.getPath( PROPERTY_PATH_XSL ) +
                    ModeHome.findByPrimaryKey( rssStyleSheet.getModeId(  ) ).getPath(  );
                strXslFileName = rssStyleSheet.getFile(  );
            }

            String strXslPath = strXslDirectory + strXslFileName;

            File fileXsl = new File( strXslPath );
            StreamSource sourceStyleSheet = new StreamSource( fileXsl );
            StringReader srInput = new StringReader( rssContent.getContent(  ) );
            StreamSource sourceDocument = new StreamSource( srInput );

            try
            {
                XmlTransformerService xmlTransformerService = new XmlTransformerService(  );
                strHtml = xmlTransformerService.transformBySourceWithXslCache( sourceDocument, sourceStyleSheet,
                        strXslPath, null, null );
            }
            catch ( Exception e )
            {
                AppLogService.error( e );
            }
        }
        else
        {
            strHtml = rssContent.getContent(  );
        }

        model.put( MARK_RSS, strHtml );

        model.put( MARK_PLUGIN, plugin );
        model.put( MARK_LOCALE, locale );

        HtmlTemplate templateList = AppTemplateService.getTemplate( TEMPLATE_RSS_INCLUDE, Locale.getDefault(  ), model );

        return templateList.getHtml(  );
    }

    /**
     * Get the locale from request of the default locale if request is null
     * @param request The {@link HttpServletRequest}
     * @return The locale
     */
    private Locale getLocale( HttpServletRequest request )
    {
        Locale locale = null;

        if ( request != null )
        {
            locale = request.getLocale(  );
        }
        else
        {
            locale = I18nService.getDefaultLocale(  );
        }

        return locale;
    }
}
