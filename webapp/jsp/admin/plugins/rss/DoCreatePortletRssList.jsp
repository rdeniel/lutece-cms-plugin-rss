<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:useBean id="rssPortletList" scope="session" class="fr.paris.lutece.plugins.rss.web.portlet.RssListPortletJspBean" />

<%
rssPortletList.init( request, rssPortletList.RIGHT_MANAGE_ADMIN_SITE );
    response.sendRedirect( rssPortletList.doCreate( request ) );
%>





