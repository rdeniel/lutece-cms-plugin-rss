<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:include page="../../AdminHeader.jsp" />

<jsp:useBean id="rss" scope="session" class="fr.paris.lutece.plugins.rss.web.RssJspBean" />

<% rss.init( request, rss.RIGHT_RSS_MANAGEMENT ); %>
<%= rss.getModifyRssFile( request ) %>

<%@ include file="../../AdminFooter.jsp" %>