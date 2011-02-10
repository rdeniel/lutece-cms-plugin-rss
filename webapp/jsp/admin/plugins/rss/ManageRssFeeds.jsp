<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:include page="../../AdminHeader.jsp" />

<jsp:useBean id="rssFeed" scope="session" class="fr.paris.lutece.plugins.rss.web.RssFeedsJspBean" />


<% rssFeed.init( request, rssFeed.RIGHT_RSS_FEEDS_MANAGEMENT ); %>
<%= rssFeed.getManageRssFeeds( request ) %>

<%@ include file="../../AdminFooter.jsp" %>

