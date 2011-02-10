<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:useBean id="rssFeed" scope="session" class="fr.paris.lutece.plugins.rss.web.RssFeedsJspBean" />

<%
    rssFeed.init( request, rssFeed.RIGHT_RSS_FEEDS_MANAGEMENT );
    response.sendRedirect( rssFeed.doFetchRssFeed( request ) );
%>


