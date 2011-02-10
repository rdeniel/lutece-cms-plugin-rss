<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<jsp:useBean id="rss" scope="request" class="fr.paris.lutece.plugins.rss.web.RssJspBean" />
<jsp:useBean id="user" scope="session" class="fr.paris.lutece.portal.web.user.UserJspBean" />

<%
    if( user.check( "RSS_MANAGEMENT" ) )
    {
		response.sendRedirect( rss.doUpdate( request, user ) );
    }
    else
    {
        response.sendRedirect( user.getPluginAccessDeniedUrl() );
    }
%>

<%@ include file="../../AdminFooter.jsp" %>
