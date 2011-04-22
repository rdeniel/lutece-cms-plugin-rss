<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:include page="../../AdminHeader.jsp" />

<jsp:useBean id="rssFeed" scope="session" class="fr.paris.lutece.plugins.rss.web.RssFeedsJspBean" />

<% rssFeed.init( request, fr.paris.lutece.plugins.rss.web.RssFeedsJspBean.RIGHT_RSS_FEEDS_MANAGEMENT ); %>
<%= rssFeed.getManageAdvancedParameters( request ) %>

<%@ include file="../../AdminFooter.jsp" %>