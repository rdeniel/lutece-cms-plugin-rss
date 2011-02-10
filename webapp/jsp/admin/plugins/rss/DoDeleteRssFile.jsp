<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:useBean id="rss" scope="session" class="fr.paris.lutece.plugins.rss.web.RssJspBean" />

<%
    rss.init( request, rss.RIGHT_RSS_MANAGEMENT );
    response.sendRedirect( rss.doDeleteRssFile( request ) );
%>




