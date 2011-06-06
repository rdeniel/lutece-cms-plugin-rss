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

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.rss.business.RssFeed;
import fr.paris.lutece.plugins.rss.business.RssFeedHome;
import fr.paris.lutece.plugins.rss.business.parameter.RssFeedParameterHome;
import fr.paris.lutece.plugins.rss.business.portlet.RssPortletHome;
import fr.paris.lutece.plugins.rss.service.RssContentLoader;
import fr.paris.lutece.plugins.rss.service.RssContentService;
import fr.paris.lutece.plugins.rss.service.RssParsingException;
import fr.paris.lutece.portal.business.portlet.PortletType;
import fr.paris.lutece.portal.business.portlet.PortletTypeHome;
import fr.paris.lutece.portal.business.rbac.RBAC;
import fr.paris.lutece.portal.business.style.Style;
import fr.paris.lutece.portal.business.style.StyleHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.rbac.RBACService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.user.AdminUserResourceIdService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.service.workgroup.AdminWorkgroupService;
import fr.paris.lutece.portal.web.admin.PluginAdminPageJspBean;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.html.Paginator;
import fr.paris.lutece.util.url.UrlItem;


/**
 *
 * @author Pierre
 */
public class RssFeedsJspBean extends PluginAdminPageJspBean
{
    public static final String RIGHT_RSS_FEEDS_MANAGEMENT = "RSS_FEEDS_MANAGEMENT";

    // Templates
    private static final String TEMPLATE_FEEDS = "admin/plugins/rss/manage_rss_feeds.html";
    private static final String TEMPLATE_CREATE_RSS_FEED = "admin/plugins/rss/create_rss_feed.html";
    private static final String TEMPLATE_MODIFY_RSS_FEED = "admin/plugins/rss/modify_rss_feed.html";
    private static final String TEMPLATE_MANAGE_ADVANCED_PARAMETERS = "admin/plugins/rss/manage_advanced_parameters.html";

    // Parameters
    private static final String PARAMETER_RSS_FEED_ID = "id_rss_feed";
    private static final String PARAMETER_RSS_FEED_NAME = "rss_feed_name";
    private static final String PARAMETER_RSS_FEED_URL = "rss_feed_url";
    private static final String PARAMETER_RSS_FEED_IS_ACTIVE = "rss_feed_is_active";
    private static final String PARAMETER_RSS_FEED_NEW_ORDER = "new_order";
    private static final String PARAMETER_PAGE_INDEX_ACTIVE = "page_index_active";
    private static final String PARAMETER_ITEMS_PER_PAGE_ACTIVE = "items_per_page_active";
    private static final String PARAMETER_PAGE_INDEX_INACTIVE = "page_index_inactive";
    private static final String PARAMETER_ITEMS_PER_PAGE_INACTIVE = "items_per_page_inactive";
    private static final String PARAMETER_WORKGROUP_KEY = "workgroup_key";
    private static final String PARAMETER_INCLUDE_STYLE = "rss_style";

    // Properties
    private static final String PROPERTY_PAGE_TITLE_FEEDS = "rss.manage_rss_feeds.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE = "rss.create_rss_feed.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY = "rss.modify_rss_feed.pageTitle";
    private static final String PROPERTY_RSS_PER_PAGE = "rss.rssPerPage";
    private static final String PROPERTY_RSS_INCLUDE_BEGIN = "rss.manage_rss_feeds.include.begin";
    private static final String PROPERTY_RSS_INCLUDE_END = "rss.manage_rss_feeds.include.end";

    //Bookmarks
    private static final String MARK_ACTIVE_RSS_FEEDS_LIST = "active_rss_feeds_list";
    private static final String MARK_INACTIVE_RSS_FEEDS_LIST = "inactive_rss_feeds_list";
    private static final String MARK_RSS_FEED = "rss_feed";
    private static final String MARK_NB_ACTIVE_RSS_FEED = "nb_active_rss_feed";
    private static final String MARK_PAGINATOR_ACTIVE = "paginator_active";
    private static final String MARK_NB_ITEMS_PER_PAGE_ACTIVE = "nb_items_per_page_active";
    private static final String MARK_PAGINATOR_INACTIVE = "paginator_inactive";
    private static final String MARK_NB_ITEMS_PER_PAGE_INACTIVE = "nb_items_per_page_inactive";
    private static final String MARK_USER_WORKGROUP_LIST = "user_workgroup_list";
    private static final String MARK_WORKGROUP_SELECTED = "selected_workgroup";
    private static final String MARK_RSS_INCLUDE_TAG = "rss_include_tag";
    private static final String MARK_LIST_STYLE_RSS = "rss_style_list";
    private static final String MARK_PERMISSION_ADVANCED_PARAMETER = "permission_advanced_parameter";

    // JSP
    private static final String JSP_DELETE_RSS_FEED = "jsp/admin/plugins/rss/DoDeleteRssFeed.jsp";
    private static final String JSP_ACTIVATE_RSS_FEED = "jsp/admin/plugins/rss/DoActivateRssFeed.jsp";

    //Messages
    private static final String MESSAGE_CONFIRM_DELETE_RSS_FEED = "rss.message.confirmDeleteRssFeed";
    private static final String MESSAGE_RSS_LINKED_PORTLET = "rss.message.linkedToPortlet";

    //Constante
    private static final String CONSTANTE_RSS_PORTLET_TYPE = "RSS_PORTLET";
    private static final String ZERO = "0";
    private static final String TRUE = "true";

    //Variables
    private int _nItemsPerPageActive;
    private String _strCurrentPageIndexActive;
    private int _nItemsPerPageInactive;
    private String _strCurrentPageIndexInactive;    

    /**
     * Returns external rss feeds management form
     *
     * @param request The Http request
     * @return Html form
     */
    public String getManageRssFeeds( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_FEEDS );
        _nItemsPerPageActive = getItemsPerPage( request, true );
        _strCurrentPageIndexActive = getPageIndex( request, true );
        _nItemsPerPageInactive = getItemsPerPage( request, false );
        _strCurrentPageIndexInactive = getPageIndex( request, false );

        List<RssFeed> listRssFeedActive = RssFeedHome.getRssFeeds( true );
        int nActiveRssFeeds = listRssFeedActive.size(  );
        listRssFeedActive = (List<RssFeed>) AdminWorkgroupService.getAuthorizedCollection( listRssFeedActive, getUser(  ) );

        HashMap<String, Object> model = new HashMap<String, Object>(  );
        String strIncludeBegin = I18nService.getLocalizedString( PROPERTY_RSS_INCLUDE_BEGIN, getLocale(  ) );
        String strIncludeEnd = I18nService.getLocalizedString( PROPERTY_RSS_INCLUDE_END, getLocale(  ) );

        for ( RssFeed rssFeed : listRssFeedActive )
        {
            String strRssMarker = strIncludeBegin + RssFeedInclude.getRssMarkerPrefix(  ) +
                String.valueOf( rssFeed.getId(  ) ) + strIncludeEnd;
            rssFeed.setIncludeTag( strRssMarker );
        }
        
        boolean bPermissionAdvancedParameter = RBACService.isAuthorized( AdminUser.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID,
                AdminUserResourceIdService.PERMISSION_MANAGE_ADVANCED_PARAMETERS, getUser(  ) ) ;

        Paginator paginator_active = new Paginator( listRssFeedActive, _nItemsPerPageActive, getHomeUrl( request ),
                PARAMETER_PAGE_INDEX_ACTIVE, _strCurrentPageIndexActive );
        
        List<RssFeed> listRssFeedInactive = RssFeedHome.getRssFeeds( false );
        listRssFeedInactive = (List<RssFeed>) AdminWorkgroupService.getAuthorizedCollection( listRssFeedInactive, getUser(  ) );
        
        Paginator paginator_inactive = new Paginator( listRssFeedInactive, _nItemsPerPageInactive, getHomeUrl( request ),
                PARAMETER_PAGE_INDEX_INACTIVE, _strCurrentPageIndexInactive );

        model.put( MARK_PAGINATOR_ACTIVE, paginator_active );
        model.put( MARK_PAGINATOR_INACTIVE, paginator_inactive );
        model.put( MARK_NB_ITEMS_PER_PAGE_ACTIVE, "" + _nItemsPerPageActive );
        model.put( MARK_NB_ITEMS_PER_PAGE_INACTIVE, "" + _nItemsPerPageInactive );
        model.put( MARK_ACTIVE_RSS_FEEDS_LIST, paginator_active.getPageItems(  ) );
        model.put( MARK_INACTIVE_RSS_FEEDS_LIST, paginator_inactive.getPageItems(  ) );
        model.put( MARK_NB_ACTIVE_RSS_FEED, Integer.toString( nActiveRssFeeds ) );
        model.put( MARK_PERMISSION_ADVANCED_PARAMETER, bPermissionAdvancedParameter );

        HtmlTemplate templateList = AppTemplateService.getTemplate( TEMPLATE_FEEDS, getLocale(  ), model );

        return getAdminPage( templateList.getHtml(  ) );
    }

    /**
     * Confirms the removal of a feed
     *
     * @param request The Http request
     * @return The Jsp URL of the process result
     */
    public String doConfirmDeleteRssFeed( HttpServletRequest request )
    {
        String strIdFeed = request.getParameter( PARAMETER_RSS_FEED_ID );
        int nIdFeed = Integer.parseInt( strIdFeed );
        String strActive = request.getParameter( PARAMETER_RSS_FEED_IS_ACTIVE );
        UrlItem deleteUrl = new UrlItem( JSP_DELETE_RSS_FEED );
        deleteUrl.addParameter( PARAMETER_RSS_FEED_ID, strIdFeed );
        deleteUrl.addParameter( PARAMETER_RSS_FEED_IS_ACTIVE, strActive );

        String strUrl = AdminMessageService.getMessageUrl( request, MESSAGE_RSS_LINKED_PORTLET, AdminMessage.TYPE_STOP );

        if ( checkNoPortletLinked( nIdFeed ) )
        {
            RssFeed rss = RssFeedHome.findByPrimaryKey( nIdFeed, strActive.equalsIgnoreCase( TRUE ) );
            Object[] messageArgs = { rss.getName(  ) };
            strUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_DELETE_RSS_FEED, messageArgs,
                    deleteUrl.getUrl(  ), AdminMessage.TYPE_CONFIRMATION );
        }

        return strUrl;
    }
    
    /**
     * Confirms the De/activation of a feed
     *
     * @param request The Http request
     * @return The Jsp URL of the process result
     */
    public String doConfirmActivateRssFeed( HttpServletRequest request )
    {
        String strIdFeed = request.getParameter( PARAMETER_RSS_FEED_ID );
        int nIdFeed = Integer.parseInt( strIdFeed );
        String strActive = request.getParameter( PARAMETER_RSS_FEED_IS_ACTIVE );
        UrlItem activateUrl = new UrlItem( JSP_ACTIVATE_RSS_FEED );
        activateUrl.addParameter( PARAMETER_RSS_FEED_ID, strIdFeed );
        activateUrl.addParameter( PARAMETER_RSS_FEED_IS_ACTIVE, strActive );

        String strUrl = AdminMessageService.getMessageUrl( request, MESSAGE_RSS_LINKED_PORTLET, AdminMessage.TYPE_STOP );

        if ( checkNoPortletLinked( nIdFeed ) )
        {
            RssFeed rss = RssFeedHome.findByPrimaryKey( nIdFeed, strActive.equalsIgnoreCase( TRUE ) );
            Object[] messageArgs = { rss.getName(  ) };
            strUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_DELETE_RSS_FEED, messageArgs,
                    activateUrl.getUrl(  ), AdminMessage.TYPE_CONFIRMATION );
        }

        return strUrl;
    }
    

    /**
     * Returns the creation form of a feed
     *
     * @param request The Http request
     * @return Html form
     */
    public String getCreateRssFeed( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_CREATE );

        HashMap<String, Object> model = new HashMap<String, Object>(  );
        ReferenceList refListWorkGroups = AdminWorkgroupService.getUserWorkgroups( getUser(  ), getLocale(  ) );

        ReferenceList refListStyleRss = getStyleList(  );

        model.put( MARK_LIST_STYLE_RSS, refListStyleRss );
        model.put( MARK_USER_WORKGROUP_LIST, refListWorkGroups );

        //RSS-24 : the first workgroup will be selected by default
        if ( !refListWorkGroups.isEmpty(  ) )
        {
            model.put( MARK_WORKGROUP_SELECTED, refListWorkGroups.get( 0 ).getCode(  ) );
        }
        
        for( ReferenceItem param : RssFeedParameterHome.findAll( getPlugin(  ) ) )
        {
        	model.put( param.getCode(  ), param.getName(  ) );
        }

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CREATE_RSS_FEED, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Process the data capture form of a new rss feed
     *
     * @param request The Http Request
     * @return The Jsp URL of the process result
     */
    public String doCreateRssFeed( HttpServletRequest request )
    {
        String strWorkgroup = request.getParameter( PARAMETER_WORKGROUP_KEY );
        String strIdIncludeStyle = request.getParameter( PARAMETER_INCLUDE_STYLE );
        boolean bActive = request.getParameter( PARAMETER_RSS_FEED_IS_ACTIVE ).equalsIgnoreCase( TRUE );

        RssFeed rss = new RssFeed(  );
        rss.setName( request.getParameter( PARAMETER_RSS_FEED_NAME ) );
        rss.setUrl( request.getParameter( PARAMETER_RSS_FEED_URL ) );
        rss.setWorkgroup( strWorkgroup );
        rss.setIdIncludeStyle( Integer.parseInt( strIdIncludeStyle ) );
        rss.setIsActive( bActive );
        
        

        // Mandatory fields
        if ( request.getParameter( PARAMETER_RSS_FEED_NAME ).equals( "" ) ||
                request.getParameter( PARAMETER_RSS_FEED_URL ).equals( "" ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        RssFeedHome.create( rss );

        // Load the content to fetch the RSS feed a first time
        if( bActive )
        {
        	RssContentService.getInstance(  ).getRssContent( rss.getId(  ) );
        }

        return getHomeUrl( request );
    }

    /**
     * Process modification of a feed
     * @param request The Http request
     * @return String The url of the administration console
     */
    public String doModifyRssFeed( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_RSS_FEED_ID ) );
        String strFeedUrl = request.getParameter( PARAMETER_RSS_FEED_URL );
        String strFeedName = request.getParameter( PARAMETER_RSS_FEED_NAME );
        String strWorkgroup = request.getParameter( PARAMETER_WORKGROUP_KEY );
        String strIdIncludeStyle = request.getParameter( PARAMETER_INCLUDE_STYLE );
        boolean bActive = request.getParameter( PARAMETER_RSS_FEED_IS_ACTIVE ).equalsIgnoreCase( TRUE );

        RssFeed rss = RssFeedHome.findByPrimaryKey( nId, bActive );
        rss.setName( strFeedName );
        rss.setUrl( strFeedUrl );
        rss.setWorkgroup( strWorkgroup );
        rss.setIdIncludeStyle( Integer.parseInt( strIdIncludeStyle ) );

        // Mandatory fields
        if ( request.getParameter( PARAMETER_RSS_FEED_URL ).equals( "" ) ||
                request.getParameter( PARAMETER_RSS_FEED_NAME ).equals( "" ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        RssFeedHome.update( rss );

        return getHomeUrl( request );
    }

    /**
     * Returns the form to update info about a rss feed
     *
     * @param request The Http request
     * @return The HTML form to update info
     */
    public String getModifyRssFeed( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_MODIFY );

        HashMap<String, Object> model = new HashMap<String, Object>(  );

        ReferenceList refListWorkGroups = AdminWorkgroupService.getUserWorkgroups( getUser(  ), getLocale(  ) );
        model.put( MARK_USER_WORKGROUP_LIST, refListWorkGroups );

        ReferenceList refListStyleRss = getStyleList(  );
        model.put( MARK_LIST_STYLE_RSS, refListStyleRss );

        int nId = Integer.parseInt( request.getParameter( PARAMETER_RSS_FEED_ID ) );
        boolean bActive = TRUE.equalsIgnoreCase( request.getParameter( PARAMETER_RSS_FEED_IS_ACTIVE ) );
        RssFeed rssFeed = RssFeedHome.findByPrimaryKey( nId, bActive );
        model.put( MARK_RSS_FEED, rssFeed );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MODIFY_RSS_FEED, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Process removal of a feed
     * @param request The Http request
     * @return String The url of the administration console
     */
    public String doDeleteRssFeed( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_RSS_FEED_ID ) );
        boolean bActive = request.getParameter( PARAMETER_RSS_FEED_IS_ACTIVE ).equalsIgnoreCase( TRUE );
        RssFeed rssFeed = RssFeedHome.findByPrimaryKey( nId, bActive );
        RssFeedHome.remove( rssFeed );

        return getHomeUrl( request );
    }

    /**
     * Process reloading of an active feed
     * @param request The Http request
     * @return String The url of the administration console
     */
    public String doFetchRssFeed( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_RSS_FEED_ID ) );

        try
        {
            RssContentLoader.fetchRssFeed( nId );
        }
        catch ( RssParsingException ex )
        {
            AppLogService.error( ex.getMessage(  ), ex );
        }

        return getHomeUrl( request );
    }
    
    /**
     * Process Des/activation of a feed
     * @param request The Http request
     * @return String The url of the administration console
     */
    public String doActivateRssFeed( HttpServletRequest request )
    {
    	int nId = Integer.parseInt( request.getParameter( PARAMETER_RSS_FEED_ID ) );
    	boolean bActive = request.getParameter( PARAMETER_RSS_FEED_IS_ACTIVE ).equalsIgnoreCase( TRUE );
    	
    	RssFeed rssFeed = RssFeedHome.findByPrimaryKey( nId, bActive );
    	
    	//Deactivate the feed
    	RssFeedHome.setActive( rssFeed, !bActive );
    	
    	return getHomeUrl( request );
    }
    
    /**
     * Change the order of an active feed in the list
     * @param request The Http request
     * @return String The url of the administration console
     */
    public String doChangeRssFeedOrder( HttpServletRequest request )
    {
    	int nId = Integer.parseInt( request.getParameter( PARAMETER_RSS_FEED_ID ) );
    	int nNewOrder = Integer.parseInt( request.getParameter( PARAMETER_RSS_FEED_NEW_ORDER ) );
    	
    	RssFeed rssFeed = RssFeedHome.findByPrimaryKey( nId, true );
    	
    	RssFeedHome.updateOrder( rssFeed, nNewOrder );
    	
    	return getHomeUrl( request );
    }

    /**
     * Used by the paginator to fetch a number of items
     * @param request The HttpRequest
     * @param bActive <code>true</code> to get the number of items for the active feed list
     * @return The number of items
     */
    private int getItemsPerPage( HttpServletRequest request, boolean bActive )
    {
        int nItemsPerPage;
        String strItemsPerPage = request.getParameter( ( bActive ? PARAMETER_ITEMS_PER_PAGE_ACTIVE : PARAMETER_ITEMS_PER_PAGE_INACTIVE ) );

        if ( strItemsPerPage != null )
        {
            nItemsPerPage = Integer.parseInt( strItemsPerPage );
        }
        else
        {
            if ( ( bActive ? _nItemsPerPageActive : _nItemsPerPageInactive ) != 0 )
            {
                nItemsPerPage = ( bActive ? _nItemsPerPageActive : _nItemsPerPageInactive );
            }
            else
            {
                nItemsPerPage = AppPropertiesService.getPropertyInt( PROPERTY_RSS_PER_PAGE, 10 );
            }
        }

        return nItemsPerPage;
    }

    /**
     * Fetches the page index
     * @param request The HttpRequest
     * @param bActive <code>true</code> to get the page index for the active feed list
     * @return The PageIndex
     */
    private String getPageIndex( HttpServletRequest request, boolean bActive )
    {
        String strPageIndex = request.getParameter( bActive ? PARAMETER_PAGE_INDEX_ACTIVE : PARAMETER_PAGE_INDEX_INACTIVE );
        strPageIndex = ( strPageIndex != null ) ? strPageIndex : ( bActive ? _strCurrentPageIndexActive :_strCurrentPageIndexInactive );

        return strPageIndex;
    }

    /**
     * Checks if the feed is linked to a portlet
     * @param nIdRssFeed The id of the rss feed
     * @return boolean
     */
    public boolean checkNoPortletLinked( int nIdRssFeed )
    {
        return RssPortletHome.checkNoPortletLinked( nIdRssFeed );
    }

    /**
     * Return the reference list for rss style
     * @return the reference list for rss style
     */
    public ReferenceList getStyleList(  )
    {
        Collection<Style> stylesList = StyleHome.getStylesList(  );

        ReferenceList stylesListWithLabels = new ReferenceList(  );

        stylesListWithLabels.addItem( -1, "-Aucun style-" );

        for ( Style style : stylesList )
        {
            PortletType portletType = PortletTypeHome.findByPrimaryKey( style.getPortletTypeId(  ) );

            if ( ( portletType.getId(  ) != null ) && portletType.getId(  ).equals( CONSTANTE_RSS_PORTLET_TYPE ) )
            {
                stylesListWithLabels.addItem( style.getId(  ), style.getDescription(  ) );
            }
        }

        return stylesListWithLabels;
    }
    
    /**
     * Return RSS advanced parameters
     * @param request The Http request
     * @return Html form
     */
    public String getManageAdvancedParameters( HttpServletRequest request ) throws AccessDeniedException
    {
        if ( !RBACService.isAuthorized( AdminUser.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID,
                AdminUserResourceIdService.PERMISSION_MANAGE_ADVANCED_PARAMETERS, getUser( ) ) )
        {
    		throw new AccessDeniedException(  );
    	}
        Map<String, Object> model = new HashMap<String, Object>( );
        for( ReferenceItem param : RssFeedParameterHome.findAll( getPlugin(  ) ) )
        {
        	model.put( param.getCode(  ), param.getName(  ) );
        }
        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MANAGE_ADVANCED_PARAMETERS, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }
    
    /**
     * Modify RSS feed parameters default values
     * @param request the request
     * @return JSP return to the feature home
     * @throws AccessDeniedException if the user is not authorized to manage advanced parameters
     */
    public String doModifyRssAdvancedParameters ( HttpServletRequest request ) 
    	throws AccessDeniedException
    {
    	if ( !RBACService.isAuthorized( AdminUser.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID,
                AdminUserResourceIdService.PERMISSION_MANAGE_ADVANCED_PARAMETERS, getUser( ) ) )
        {
    		throw new AccessDeniedException(  );
    	}
    	
    	for( ReferenceItem param : RssFeedParameterHome.findAll( getPlugin(  ) ) )
    	{
    		String strParamValue = request.getParameter( param.getCode(  ) );
    		param.setName( StringUtils.isBlank( strParamValue ) ? ZERO : strParamValue );
    		RssFeedParameterHome.update( param, getPlugin(  ) );
    	}
    	
    	return getHomeUrl( request );
    }
    
}
