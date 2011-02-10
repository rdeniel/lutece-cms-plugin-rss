<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:useBean id="rssPortlet" scope="session" class="fr.paris.lutece.plugins.rss.web.portlet.RssPortletJspBean" />

<%
    rssPortlet.init( request, rssPortlet.RIGHT_MANAGE_ADMIN_SITE );
    response.sendRedirect( rssPortlet.doCreate( request ) );
%>





