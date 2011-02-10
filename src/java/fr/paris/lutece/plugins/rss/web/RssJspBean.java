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
package fr.paris.lutece.plugins.rss.web;

import fr.paris.lutece.plugins.document.service.publishing.PublishingService;
import fr.paris.lutece.plugins.rss.business.RssFeedHome;
import fr.paris.lutece.plugins.rss.business.RssGeneratedFile;
import fr.paris.lutece.plugins.rss.business.RssGeneratedFileHome;
import fr.paris.lutece.plugins.rss.service.RssGeneratorService;
import fr.paris.lutece.plugins.rss.service.RssService;
import fr.paris.lutece.portal.business.portlet.Portlet;
import fr.paris.lutece.portal.business.rss.IResourceRss;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.portlet.PortletService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.service.workgroup.AdminWorkgroupService;
import fr.paris.lutece.portal.web.admin.PluginAdminPageJspBean;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.filesystem.UploadUtil;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.html.Paginator;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 * This class provides the user interface to manage pushrss objects
 * features ( publishing, unselecting, ... )
 */
public class RssJspBean extends PluginAdminPageJspBean
{
    /////////////////////////////////////////////////////////////////////////////////
    // Constants
    public static final String RIGHT_RSS_MANAGEMENT = "RSS_MANAGEMENT";
    private static final String PARAMETER_PUSH_RSS_PORTLET_ID = "rss_portlet_id";
    private static final String PARAMETER_PUSH_RSS_ID = "rss_id";
    private static final String PARAMETER_PUSH_RSS_NAME = "rss_name";
    private static final String PARAMETER_PUSH_RSS_DESCRIPTION = "rss_description";
    private static final String PARAMETER_RSS_TYPE = "rss_type";
    private static final String PARAMETER_RSS_RESOURCE_KEY = "rss_resource_key";
    private static final String PARAMETER_PAGE_INDEX = "page_index";
    private static final String PARAMETER_ITEMS_PER_PAGE = "items_per_page";
    private static final String PARAMETER_WORKGROUP_KEY = "workgroup_key";
    private static final String PARAMETER_CANCEL = "cancel";
    private static final String PARAMETER_APPLY = "apply";
    private static final String TEMPLATE_MANAGE_RSS_FILE = "admin/plugins/rss/manage_rss_file.html";
    private static final String TEMPLATE_CREATE_PUSH_RSS_FILE = "admin/plugins/rss/create_rss_file.html";
    private static final String TEMPLATE_CREATE_PUSH_RSS_FILE_PORTLET = "admin/plugins/rss/create_rss_file_portlet.html";
    private static final String TEMPLATE_CREATE_PUSH_RSS_FILE_RESOURCE = "admin/plugins/rss/create_rss_file_resource.html";
    private static final String TEMPLATE_MODIFY_PUSH_RSS_FILE_PORTLET = "admin/plugins/rss/modify_rss_file_portlet.html";
    private static final String TEMPLATE_MODIFY_PUSH_RSS_FILE_RESOURCE = "admin/plugins/rss/modify_rss_file_resource.html";
    private static final String MARK_RSS_FILE = "rssFile";
    private static final String MARK_PUSH_RSS_LIST = "rss_files_list";
    private static final String MARK_PUSH_RSS_URL = "rss_file_url";
    private static final String MARK_PORTLET_LIST = "portlet_list";
    private static final String MARK_RESOURCE_LIST = "resource_list";
    private static final String MARK_PAGINATOR = "paginator";
    private static final String MARK_NB_ITEMS_PER_PAGE = "nb_items_per_page";
    private static final String MARK_USER_WORKGROUP_LIST = "user_workgroup_list";
    private static final String MARK_WORKGROUP_SELECTED = "selected_workgroup";
    private static final String MARK_RSS_TYPE = "rss_type";
    private static final String MARK_RSS_NAME = "rss_name";
    private static final String MARK_RESOURCE_RSS = "resource_rss";
    private static final String MARK_RESOURCE_RSS_CONFIG = "resource_rss_config";
    private static final int STATE_OK = 0;
    private static final int STATE_PORTLET_MISSING = 1;
    private static final String PROPERTY_PATH_PLUGIN_WAREHOUSE = "path.plugins.warehouse";
    private static final String PROPERTY_FILE_TYPE = "rss.file.type";
    private static final String PROPERTY_NAME_MAX_LENGTH = "rss.name.max.length";
    private static final String PROPERTY_PAGE_TITLE_FILES = "rss.manage_rss_feeds.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE = "rss.create_rss_file.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY = "rss.modify_rss_file.pageTitle";
    private static final String PROPERTY_RSS_PER_PAGE = "rss.rssPerPage";

    //Messages
    private static final String MESSAGE_FILENAME_TOO_LONG = "rss.message.filenameTooLong";
    private static final String MESSAGE_FILENAME_ALREADY_EXISTS = "rss.message.filenameAlreadyExists";
    private static final String MESSAGE_NO_DOCUMENT_PORTLET = "rss.message.NoDocumentPortlet";
    private static final String MESSAGE_RESOURCE = "rss.message.noResource";
    private static final String MESSAGE_CONFIRM_DELETE_RSS_FILE = "rss.message.confirmRemoveRssFile";
    private static final String MESSAGE_RSS_LINKED_FEED = "rss.message.linkedToFeed";

    //JSPs
    private static final String JSP_CREATE_RESOURCE_RSS_FILE = "jsp/admin/plugins/rss/DoCreateRssFile.jsp";
    private static final String JSP_MODIFY_RESOURCE_RSS_FILE = "jsp/admin/plugins/rss/ModifyRssFile.jsp";
    private static final String JSP_MANAGE_RSS_FILE = "jsp/admin/plugins/rss/ManageRssFiles.jsp";
    private static final String JSP_DELETE_RSS_FILE = "jsp/admin/plugins/rss/DoDeleteRssFile.jsp";
    private static final String REGEX_ID = "^[\\d]+$";
    private static final String WORKGROUP_ALL = "all";
    private static final String STRING_EMPTY = "";
    private static final String PORTLET = "portlet";
    private static final String RESOURCE_RSS = "resourceRss";
    private int _nItemsPerPage;
    private String _strCurrentPageIndex;

    /**
     * Returns  rss files management form
     *
     * @param request The Http request
     * @return Html form
     */
    public String getManageRssFile( HttpServletRequest request ) //Implement paginator
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_FILES );
        _nItemsPerPage = getItemsPerPage( request );
        _strCurrentPageIndex = getPageIndex( request );

        List listRssFileList = RssGeneratedFileHome.getRssFileList(  );
        listRssFileList = (List) AdminWorkgroupService.getAuthorizedCollection( listRssFileList, getUser(  ) );

        Paginator paginator = new Paginator( listRssFileList, _nItemsPerPage, getHomeUrl( request ),
                PARAMETER_PAGE_INDEX, _strCurrentPageIndex );
        HashMap model = new HashMap(  );
        model.put( MARK_PAGINATOR, paginator );
        model.put( MARK_NB_ITEMS_PER_PAGE, "" + _nItemsPerPage );
        model.put( MARK_PUSH_RSS_LIST, paginator.getPageItems(  ) );
        model.put( MARK_PUSH_RSS_URL, getRssFileUrl( "", request ) );

        HtmlTemplate templateList = AppTemplateService.getTemplate( TEMPLATE_MANAGE_RSS_FILE, getLocale(  ), model );

        return getAdminPage( templateList.getHtml(  ) );
    }

    /**
     * Modification of a push RSS file
     *
     * @return The Jsp URL of the process result
     * @param request requete Http
     */
    public String doModifyRssFilePortlet( HttpServletRequest request )
    {
        // Recovery of parameters processing
        if ( request.getParameter( PARAMETER_CANCEL ) != null )
        {
            return getHomeUrl( request );
        }

        String strRssFileId = request.getParameter( PARAMETER_PUSH_RSS_ID );
        String strWorkgroup = request.getParameter( PARAMETER_WORKGROUP_KEY );

        int nRssFileId = Integer.parseInt( strRssFileId );
        String strPortletId = request.getParameter( PARAMETER_PUSH_RSS_PORTLET_ID );

        if ( ( strPortletId == null ) || !strPortletId.matches( REGEX_ID ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_NO_DOCUMENT_PORTLET, AdminMessage.TYPE_STOP );
        }

        int nPortletId = Integer.parseInt( strPortletId );
        String strRssFileName = request.getParameter( PARAMETER_PUSH_RSS_NAME );
        String strRssFileDescription = request.getParameter( PARAMETER_PUSH_RSS_DESCRIPTION );

        RssGeneratedFile rssFile = new RssGeneratedFile(  );
        rssFile.setPortletId( nPortletId );
        rssFile.setId( nRssFileId );
        rssFile.setName( strRssFileName );
        rssFile.setState( STATE_OK );
        rssFile.setWorkgroup( strWorkgroup );
        rssFile.setDescription( strRssFileDescription );

        // Update the database with the new push RSS file
        RssGeneratedFileHome.update( rssFile );

        // Check if the portlet does exist
        if ( !RssGeneratedFileHome.checkRssFilePortlet( nPortletId ) )
        {
            rssFile.setState( STATE_PORTLET_MISSING );
        }

        // Format the new file name
        strRssFileName = UploadUtil.cleanFileName( strRssFileName );

        // Call the create xml document method
        String strRssDocument = RssGeneratorService.createRssDocument( nPortletId, rssFile.getDescription(  ) );

        // Call the create file method
        RssGeneratorService.createFileRss( strRssFileName, strRssDocument );

        // Update the push Rss object in the database
        RssGeneratedFileHome.update( rssFile );

        // Display the page of publishing
        return getHomeUrl( request );
    }

    /**
     * Modification of a push RSS file
     *
     * @return The Jsp URL of the process result
     * @param request requete Http
     */
    public String doModifyRssFileResource( HttpServletRequest request )
    {
        if ( request.getParameter( PARAMETER_CANCEL ) != null )
        {
            return getHomeUrl( request );
        }

        // Recovery of parameters processing
        String strRssFileId = request.getParameter( PARAMETER_PUSH_RSS_ID );

        //String strWorkgroup = request.getParameter( PARAMETER_WORKGROUP_KEY );
        int nRssFileId = Integer.parseInt( strRssFileId );

        String strWorkgroup = request.getParameter( PARAMETER_WORKGROUP_KEY );
        String strRssFileName = request.getParameter( PARAMETER_PUSH_RSS_NAME );

        String strResourceRssKey = request.getParameter( PARAMETER_RSS_RESOURCE_KEY );
        IResourceRss resourceRss = RssService.getInstance(  ).getResourceRssInstance( strResourceRssKey, getLocale(  ) );

        if ( request.getParameter( PARAMETER_APPLY ) != null )
        {
            return getJspModifyRssResource( request, strRssFileId, request.getParameter( PARAMETER_PUSH_RSS_NAME ),
                request.getParameter( PARAMETER_WORKGROUP_KEY ), request.getParameter( PARAMETER_RSS_TYPE ),
                request.getParameter( PARAMETER_RSS_RESOURCE_KEY ), resourceRss.getParameterToApply( request ) );
        }

        String strNameMaxLength = AppPropertiesService.getProperty( PROPERTY_NAME_MAX_LENGTH );

        if ( strRssFileName.length(  ) > Integer.parseInt( strNameMaxLength ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_FILENAME_TOO_LONG, AdminMessage.TYPE_STOP );
        }

        // Format the new file name
        strRssFileName = UploadUtil.cleanFileName( strRssFileName );

        // Check the type of the name
        String strFileType = AppPropertiesService.getProperty( PROPERTY_FILE_TYPE );

        if ( !strRssFileName.toLowerCase(  ).endsWith( strFileType ) )
        {
            strRssFileName = strRssFileName + strFileType;
        }

        if ( ( resourceRss != null ) && ( request.getParameter( PARAMETER_CANCEL ) == null ) )
        {
            String strError = resourceRss.doValidateConfigForm( request, getLocale(  ) );

            if ( strError != null )
            {
                return strError;
            }
        }

        String strRssFileDescription = request.getParameter( PARAMETER_PUSH_RSS_DESCRIPTION );

        RssGeneratedFile rssFile = new RssGeneratedFile(  );
        rssFile.setId( nRssFileId );
        rssFile.setName( strRssFileName );
        rssFile.setState( STATE_OK );
        rssFile.setWorkgroup( strWorkgroup );
        rssFile.setDescription( resourceRss.getDescription(  ) );
        rssFile.setTypeResourceRss( resourceRss.getResourceRssType(  ).getKey(  ) );

        resourceRss.setId( rssFile.getId(  ) );

        // Check if the resource does exist
        if ( !resourceRss.checkResource(  ) )
        {
            rssFile.setState( STATE_PORTLET_MISSING );
        }

        // Update the database with the new push RSS file
        RssGeneratedFileHome.update( rssFile );

        //sauvegarde du coté directory
        resourceRss.doUpdateConfig( request, getLocale(  ) );

        // Check if a RSS file exists for this portlet
        //String strRssDocument = RssGeneratorService.createRssDocument( nPortletId, strRssFileDescription );
        String strRss = resourceRss.createHtmlRss(  );

        // Call the create file method
        RssGeneratorService.createFileRss( strRssFileName, strRss );

        // Display the page of publishing
        return getHomeUrl( request );
    }

    /**
     * Creates the push RSS file corresponding to the given portlet
     *
     * @return The Jsp URL of the process result
     * @param request requete Http
     */
    public String doCreateRssFile( HttpServletRequest request )
    {
        if ( request.getParameter( PARAMETER_CANCEL ) != null )
        {
            return getHomeUrl( request );
        }

        String strRssType = request.getParameter( PARAMETER_RSS_TYPE );
        String strRssResourceName = request.getParameter( PARAMETER_RSS_RESOURCE_KEY );

        if ( ( strRssType == null ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_NO_DOCUMENT_PORTLET, AdminMessage.TYPE_STOP );
        }

        if ( ( strRssType.equals( PORTLET ) ) )
        {
            return getCreateRssFilePortlet( request );
        }
        else if ( strRssType.equals( RESOURCE_RSS ) )
        {
            if ( ( strRssResourceName == null ) )
            {
                return AdminMessageService.getMessageUrl( request, MESSAGE_NO_DOCUMENT_PORTLET, AdminMessage.TYPE_STOP );
            }
            else
            {
                IResourceRss resourceRss = RssService.getInstance(  )
                                                     .getResourceRssInstance( strRssResourceName, getLocale(  ) );

                if ( resourceRss.contentResourceRss(  ) )
                {
                    HashMap model = new HashMap(  );

                    if ( request.getParameter( PARAMETER_PUSH_RSS_NAME ) != null )
                    {
                        model.put( MARK_RSS_NAME, request.getParameter( PARAMETER_PUSH_RSS_NAME ) );
                    }
                    else
                    {
                        model.put( MARK_RSS_NAME, STRING_EMPTY );
                    }

                    ReferenceList refListWorkGroups = AdminWorkgroupService.getUserWorkgroups( getUser(  ),
                            getLocale(  ) );

                    model.put( MARK_USER_WORKGROUP_LIST, refListWorkGroups );

                    //RSS-24 : the first workgroup will be selected by default
                    if ( !refListWorkGroups.isEmpty(  ) )
                    {
                        if ( request.getParameter( PARAMETER_WORKGROUP_KEY ) != null )
                        {
                            model.put( MARK_WORKGROUP_SELECTED, request.getParameter( PARAMETER_WORKGROUP_KEY ) );
                        }
                        else
                        {
                            model.put( MARK_WORKGROUP_SELECTED, WORKGROUP_ALL );
                        }
                    }

                    model.put( MARK_RSS_TYPE, RESOURCE_RSS );
                    model.put( MARK_RESOURCE_RSS, resourceRss.getResourceRssType(  ) );
                    model.put( MARK_RESOURCE_RSS_CONFIG,
                        resourceRss.getDisplayCreateConfigForm( request, getLocale(  ) ) );

                    setPageTitleProperty( PROPERTY_PAGE_TITLE_CREATE );

                    HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CREATE_PUSH_RSS_FILE_RESOURCE,
                            getLocale(  ), model );

                    return getAdminPage( template.getHtml(  ) );
                }
                else
                {
                    return AdminMessageService.getMessageUrl( request, MESSAGE_RESOURCE, AdminMessage.TYPE_STOP );
                }
            }
        }

        return null;
    }

    /**
     * Creates the push RSS file corresponding to the given portlet
     *
     * @return The Jsp URL of the process result
     * @param request requete Http
     */
    public String doCreateRssFilePortlet( HttpServletRequest request )
    {
        if ( request.getParameter( PARAMETER_CANCEL ) != null )
        {
            return getHomeUrl( request );
        }

        String strPortletId = request.getParameter( PARAMETER_PUSH_RSS_PORTLET_ID );
        String strRssFileName = request.getParameter( PARAMETER_PUSH_RSS_NAME );
        String strRssFileDescription = request.getParameter( PARAMETER_PUSH_RSS_DESCRIPTION );
        String strWorkgroup = request.getParameter( PARAMETER_WORKGROUP_KEY );

        if ( ( strPortletId == null ) || !strPortletId.matches( REGEX_ID ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_NO_DOCUMENT_PORTLET, AdminMessage.TYPE_STOP );
        }

        int nPortletId = Integer.parseInt( strPortletId );

        //Mandatory fields
        if ( request.getParameter( PARAMETER_PUSH_RSS_NAME ).equals( "" ) ||
                request.getParameter( PARAMETER_PUSH_RSS_DESCRIPTION ).equals( "" ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        // Check the file name length
        String strNameMaxLength = AppPropertiesService.getProperty( PROPERTY_NAME_MAX_LENGTH );

        if ( strRssFileName.length(  ) > Integer.parseInt( strNameMaxLength ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_FILENAME_TOO_LONG, AdminMessage.TYPE_STOP );
        }

        // Format the new file name
        strRssFileName = UploadUtil.cleanFileName( strRssFileName );

        // Check the type of the name
        String strFileType = AppPropertiesService.getProperty( PROPERTY_FILE_TYPE );

        if ( !strRssFileName.toLowerCase(  ).endsWith( strFileType ) )
        {
            strRssFileName = strRssFileName + strFileType;
        }

        // Verifies whether the file's name exists
        if ( RssGeneratedFileHome.checkRssFileFileName( strRssFileName ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_FILENAME_ALREADY_EXISTS, AdminMessage.TYPE_STOP );
        }

        // Check if a RSS file exists for this portlet
        String strRssDocument = RssGeneratorService.createRssDocument( nPortletId, strRssFileDescription );

        // Call the create file method
        RssGeneratorService.createFileRss( strRssFileName, strRssDocument );

        // Update the database with the new push RSS file
        RssGeneratedFile rssFile = new RssGeneratedFile(  );
        rssFile.setPortletId( nPortletId );
        rssFile.setName( strRssFileName );
        rssFile.setState( STATE_OK );
        rssFile.setDescription( strRssFileDescription );
        rssFile.setWorkgroup( strWorkgroup );

        RssGeneratedFileHome.create( rssFile );

        return getHomeUrl( request );
    }

    /**
     * Returns the creation form of a rss file
     *
     * @param request The Http request
     * @return Html form
     */
    public String getCreateRssFile( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_CREATE );

        ReferenceList referenceList = RssService.getInstance(  ).getRefListResourceRssType( getLocale(  ) );

        HashMap model = new HashMap(  );
        model.put( MARK_RESOURCE_LIST, referenceList );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CREATE_PUSH_RSS_FILE, getLocale(  ), model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Returns the creation form of a rss file
     *
     * @param request The Http request
     * @return Html form
     */
    public String getCreateRssFilePortlet( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_CREATE );

        ReferenceList refListWorkGroups = AdminWorkgroupService.getUserWorkgroups( getUser(  ), getLocale(  ) );

        Collection<Portlet> listInvolvedPortlets = PublishingService.getInstance(  ).getPublishedPortlets(  );
        //test authorization on all portlets
        listInvolvedPortlets = PortletService.getInstance(  )
                                             .getAuthorizedPortletCollection( listInvolvedPortlets, getUser(  ) );

        ReferenceList referenceList = new ReferenceList(  );

        for ( Portlet portlet : listInvolvedPortlets )
        {
            referenceList.addItem( portlet.getId(  ), portlet.getName(  ) );
        }

        HashMap model = new HashMap(  );
        model.put( MARK_PORTLET_LIST, referenceList );
        model.put( MARK_USER_WORKGROUP_LIST, refListWorkGroups );

        //RSS-24 : the first workgroup will be selected by default
        if ( !refListWorkGroups.isEmpty(  ) )
        {
            model.put( MARK_WORKGROUP_SELECTED, refListWorkGroups.get( 0 ).getCode(  ) );
        }

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CREATE_PUSH_RSS_FILE_PORTLET, getLocale(  ),
                model );

        return getAdminPage( template.getHtml(  ) );
    }

    /**
     * Returns the creation form of a rss file
     *
     * @param request The Http request
     * @return Html form
     */
    public String doCreateRssFileResource( HttpServletRequest request )
    {
        if ( request.getParameter( PARAMETER_CANCEL ) != null )
        {
            return getHomeUrl( request );
        }

        String strWorkgroup = request.getParameter( PARAMETER_WORKGROUP_KEY );
        String strRssFileName = request.getParameter( PARAMETER_PUSH_RSS_NAME );

        String strResourceRssKey = request.getParameter( PARAMETER_RSS_RESOURCE_KEY );
        IResourceRss resourceRss = RssService.getInstance(  ).getResourceRssInstance( strResourceRssKey, getLocale(  ) );

        if ( request.getParameter( PARAMETER_APPLY ) != null )
        {
            return getJspCreateRssResource( request, request.getParameter( PARAMETER_PUSH_RSS_NAME ),
                request.getParameter( PARAMETER_WORKGROUP_KEY ), request.getParameter( PARAMETER_RSS_TYPE ),
                request.getParameter( PARAMETER_RSS_RESOURCE_KEY ), resourceRss.getParameterToApply( request ) );
        }

        if ( strRssFileName.equals( "" ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        String strNameMaxLength = AppPropertiesService.getProperty( PROPERTY_NAME_MAX_LENGTH );

        if ( strRssFileName.length(  ) > Integer.parseInt( strNameMaxLength ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_FILENAME_TOO_LONG, AdminMessage.TYPE_STOP );
        }

        // Format the new file name
        strRssFileName = UploadUtil.cleanFileName( strRssFileName );

        // Check the type of the name
        String strFileType = AppPropertiesService.getProperty( PROPERTY_FILE_TYPE );

        if ( !strRssFileName.toLowerCase(  ).endsWith( strFileType ) )
        {
            strRssFileName = strRssFileName + strFileType;
        }

        // Verifies whether the file's name exists
        if ( RssGeneratedFileHome.checkRssFileFileName( strRssFileName ) )
        {
            return AdminMessageService.getMessageUrl( request, MESSAGE_FILENAME_ALREADY_EXISTS, AdminMessage.TYPE_STOP );
        }

        if ( ( resourceRss != null ) && ( request.getParameter( PARAMETER_CANCEL ) == null ) )
        {
            String strError = resourceRss.doValidateConfigForm( request, getLocale(  ) );

            if ( strError != null )
            {
                return strError;
            }
        }

        RssGeneratedFile rssFile = new RssGeneratedFile(  );
        rssFile.setName( strRssFileName );
        rssFile.setState( STATE_OK );
        rssFile.setDescription( resourceRss.getDescription(  ) );
        rssFile.setWorkgroup( strWorkgroup );
        rssFile.setTypeResourceRss( resourceRss.getResourceRssType(  ).getKey(  ) );
        RssGeneratedFileHome.create( rssFile );

        resourceRss.setId( rssFile.getId(  ) );

        //sauvegarde du coté directory
        resourceRss.doSaveConfig( request, getLocale(  ) );

        String strRss = resourceRss.createHtmlRss(  );

        // Call the create file method
        RssGeneratorService.createFileRss( strRssFileName, strRss );

        return getJspManageRssFile( request );
    }

    /**
     * return url of the jsp manage workflow
     * @param request The HTTP request
     * @return url of the jsp manage workflow
     */
    private String getJspManageRssFile( HttpServletRequest request )
    {
        return AppPathService.getBaseUrl( request ) + JSP_MANAGE_RSS_FILE;
    }

    /**
     * return url of the jsp create ResourceRss
     * @param request The HTTP request
     * @return url of the jsp create ResourceRss
     */
    private String getJspCreateRssResource( HttpServletRequest request, String strRssName, String strRssWorkgroup,
        String rss_type, String resourceKey, Map<String, String> parameterList )
    {
        String strParam = new String(  );

        for ( String parameter : parameterList.keySet(  ) )
        {
            strParam += ( parameter + "=" + parameterList.get( parameter ) + "&" );
        }

        return AppPathService.getBaseUrl( request ) + JSP_CREATE_RESOURCE_RSS_FILE + "?" + PARAMETER_PUSH_RSS_NAME +
        "=" + strRssName + "&" + PARAMETER_WORKGROUP_KEY + "=" + strRssWorkgroup + "&" + PARAMETER_RSS_TYPE + "=" +
        rss_type + "&" + PARAMETER_RSS_RESOURCE_KEY + "=" + resourceKey + "&" + strParam;
    }

    /**
     * return url of the jsp modify ResourceRss
     * @param request The HTTP request
     * @return url of the jsp modify ResourceRss
     */
    private String getJspModifyRssResource( HttpServletRequest request, String id_rss, String strRssName,
        String strRssWorkgroup, String rss_type, String resourceKey, Map<String, String> parameterList )
    {
        String strParam = new String(  );

        for ( String parameter : parameterList.keySet(  ) )
        {
            strParam += ( parameter + "=" + parameterList.get( parameter ) + "&" );
        }

        return AppPathService.getBaseUrl( request ) + JSP_MODIFY_RESOURCE_RSS_FILE + "?" + PARAMETER_PUSH_RSS_NAME +
        "=" + strRssName + "&" + PARAMETER_WORKGROUP_KEY + "=" + strRssWorkgroup + "&" + PARAMETER_PUSH_RSS_ID + "=" +
        id_rss + "&" + PARAMETER_RSS_TYPE + "=" + rss_type + "&" + PARAMETER_RSS_RESOURCE_KEY + "=" + resourceKey +
        "&" + strParam;
    }

    /**
     * Returns the form to update a rss file
     *
     * @param request The Http request
     * @return The HTML form to update info
     */
    public String getModifyRssFile( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_MODIFY );

        ReferenceList refListWorkGroups = AdminWorkgroupService.getUserWorkgroups( getUser(  ), getLocale(  ) );
        int nRssFileId = Integer.parseInt( request.getParameter( PARAMETER_PUSH_RSS_ID ) );
        RssGeneratedFile rss = RssGeneratedFileHome.findByPrimaryKey( nRssFileId );

        HashMap model = new HashMap(  );

        if ( request.getParameter( PARAMETER_WORKGROUP_KEY ) != null )
        {
            rss.setWorkgroup( request.getParameter( PARAMETER_WORKGROUP_KEY ) );
        }

        if ( request.getParameter( PARAMETER_PUSH_RSS_NAME ) != null )
        {
            rss.setName( request.getParameter( PARAMETER_PUSH_RSS_NAME ) );
        }

        model.put( MARK_RSS_FILE, rss );
        model.put( MARK_USER_WORKGROUP_LIST, refListWorkGroups );

        HtmlTemplate template = null;

        if ( ( rss.getTypeResourceRss(  ) != null ) && ( rss.getPortletId(  ) == 0 ) )
        {
            IResourceRss resourceRss = RssService.getInstance(  )
                                                 .getResourceRssInstance( rss.getTypeResourceRss(  ), getLocale(  ) );

            model.put( MARK_RESOURCE_RSS, resourceRss.getResourceRssType(  ) );
            resourceRss.setId( rss.getId(  ) );
            model.put( MARK_RESOURCE_RSS_CONFIG, resourceRss.getDisplayModifyConfigForm( request, getLocale(  ) ) );
            template = AppTemplateService.getTemplate( TEMPLATE_MODIFY_PUSH_RSS_FILE_RESOURCE, getLocale(  ), model );
        }
        else
        {
            Collection<Portlet> listInvolvedPortlets = PublishingService.getInstance(  ).getPublishedPortlets(  );
            //test authorization on all portlets
            listInvolvedPortlets = PortletService.getInstance(  )
                                                 .getAuthorizedPortletCollection( listInvolvedPortlets, getUser(  ) );

            ReferenceList referenceList = new ReferenceList(  );

            for ( Portlet portlet : listInvolvedPortlets )
            {
                referenceList.addItem( portlet.getId(  ), portlet.getName(  ) );
            }

            model.put( MARK_PORTLET_LIST, referenceList );

            template = AppTemplateService.getTemplate( TEMPLATE_MODIFY_PUSH_RSS_FILE_PORTLET, getLocale(  ), model );
        }

        return getAdminPage( template.getHtml(  ) );
    }

    ////////////////////////////////////////////////////////////////////////////
    // Private implementation

    /**
     * Utility method to build the URL of the given rssFile file
     *
     * @param strNameRssFile The HTML template to fill
     * @param request The HttpServletRequest
     * @return the URL to get the rssFile file
     */
    private String getRssFileUrl( String strNameRssFile, HttpServletRequest request )
    {
        String strBaseUrl = AppPathService.getBaseUrl( request );
        String strStockingDirectoryName = AppPropertiesService.getProperty( RssGeneratorService.PROPERTY_STORAGE_DIRECTORY_NAME );
        String strPluginWarehouse = AppPropertiesService.getProperty( PROPERTY_PATH_PLUGIN_WAREHOUSE );

        //Removes extra slash in the url
        if ( strPluginWarehouse.startsWith( "/" ) )
        {
            strPluginWarehouse = strPluginWarehouse.substring( 1 );
        }

        String strRssFileRepository = strPluginWarehouse + "/" + strStockingDirectoryName;
        strBaseUrl = strBaseUrl + strRssFileRepository + "/" + strNameRssFile;

        return strBaseUrl;
    }

    /**
     * Process removal of a file
     * @param request The Http request
     * @return String The url of the administration console
     */
    public String doDeleteRssFile( HttpServletRequest request )
    {
        int nIdFileRss = Integer.parseInt( request.getParameter( PARAMETER_PUSH_RSS_ID ) );
        RssGeneratedFile rssGeneratedFile = RssGeneratedFileHome.findByPrimaryKey( nIdFileRss );

        if ( ( rssGeneratedFile.getPortletId(  ) == 0 ) && ( rssGeneratedFile.getTypeResourceRss(  ) != null ) )
        {
            IResourceRss resourceRss = RssService.getInstance(  )
                                                 .getResourceRssInstance( rssGeneratedFile.getTypeResourceRss(  ),
                    getLocale(  ) );
            resourceRss.deleteResourceRssConfig( rssGeneratedFile.getId(  ) );
        }

        RssGeneratedFileHome.remove( nIdFileRss );

        return getHomeUrl( request );
    }

    /**
     * Confirms the removal of a rss file
     *
     * @param request The Http request
     * @return The Jsp URL of the process result
     */
    public String doConfirmDeleteRssFile( HttpServletRequest request )
    {
        String strIdFile = request.getParameter( PARAMETER_PUSH_RSS_ID );
        int nIdFile = Integer.parseInt( strIdFile );
        String strDeleteUrl = JSP_DELETE_RSS_FILE + "?" + PARAMETER_PUSH_RSS_ID + "=" + strIdFile;
        String strUrl = AdminMessageService.getMessageUrl( request, MESSAGE_RSS_LINKED_FEED, AdminMessage.TYPE_STOP );

        if ( checkNoRssExternalFeed( nIdFile, request ) )
        {
            RssGeneratedFile rssFile = RssGeneratedFileHome.findByPrimaryKey( nIdFile );
            Object[] messageArgs = { rssFile.getName(  ) };
            strUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_DELETE_RSS_FILE, messageArgs,
                    strDeleteUrl, AdminMessage.TYPE_CONFIRMATION );
        }

        return strUrl;
    }

    /**
     * Used by the paginator to fetch a number of items
     * @param request The HttpRequest
     * @return The number of items
     */
    private int getItemsPerPage( HttpServletRequest request )
    {
        int nItemsPerPage;
        String strItemsPerPage = request.getParameter( PARAMETER_ITEMS_PER_PAGE );

        if ( strItemsPerPage != null )
        {
            nItemsPerPage = Integer.parseInt( strItemsPerPage );
        }
        else
        {
            if ( _nItemsPerPage != 0 )
            {
                nItemsPerPage = _nItemsPerPage;
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
     * @return The PageIndex
     */
    private String getPageIndex( HttpServletRequest request )
    {
        String strPageIndex = request.getParameter( PARAMETER_PAGE_INDEX );
        strPageIndex = ( strPageIndex != null ) ? strPageIndex : _strCurrentPageIndex;

        return strPageIndex;
    }

    /**
     * Checks whether the generated rss file is linked as an external rss feed
     * @param nIdRssFile the identifier of the rss file
     * @param request The HttpRequest
     * @return a boolean
     */
    public boolean checkNoRssExternalFeed( int nIdRssFile, HttpServletRequest request )
    {
        RssGeneratedFile rssFile = RssGeneratedFileHome.findByPrimaryKey( nIdRssFile );
        String strUrlFile = getRssFileUrl( rssFile.getName(  ), request );

        //if the url of the file is used as an external feed
        return RssFeedHome.checkUrlNotUsed( strUrlFile );
    }
}
