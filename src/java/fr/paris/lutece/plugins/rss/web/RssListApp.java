package fr.paris.lutece.plugins.rss.web;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.rss.business.RssFeed;
import fr.paris.lutece.plugins.rss.business.RssFeedHome;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.web.xpages.XPage;
import fr.paris.lutece.portal.web.xpages.XPageApplication;
import fr.paris.lutece.util.html.HtmlTemplate;

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
        
        Locale locale = request.getLocale();
        
        Map<String,Object> model = new HashMap<String,Object>();
        // population de la liste des flux
        List<RssFeed> listRssFeedList = RssFeedHome.getRssFeeds(  );
        model.put( MARK_RSS_FEEDS_LIST, listRssFeedList ); 
        
        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_RSS_LIST , locale, model );
        String strPageTitle = I18nService.getLocalizedString( PROPERTY_PAGE_TITLE , locale );
        String strPagePathLabel = I18nService.getLocalizedString( PROPERTY_PAGE_PATH_LABEL , locale );

        page.setContent( template.getHtml() );
        page.setTitle( strPageTitle );
        page.setPathLabel( strPagePathLabel );
        
        return page;
    }

}
