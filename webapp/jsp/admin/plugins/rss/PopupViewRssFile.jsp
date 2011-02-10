<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:useBean id="rss" scope="request" class="fr.paris.lutece.plugins.rss.web.RssJspBean" />
<jsp:useBean id="user" scope="session" class="fr.paris.lutece.portal.web.user.UserJspBean" />

<%
    if( user.check( "RSS_MANAGEMENT" ) )
    {
%>
        <%= rss.getPopupRssFile( request, user ) %>
<%
    }
    else
    {
        response.sendRedirect( user.getPluginAccessDeniedUrl() );
    }
%>

