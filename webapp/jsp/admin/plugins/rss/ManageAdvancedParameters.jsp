<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:include page="../../AdminHeader.jsp" />

<jsp:useBean id="rss" scope="session" class="fr.paris.lutece.plugins.rss.web.RssFeedsJspBean" />

<% rss.init( request, fr.paris.lutece.plugins.rss.web.RssFeedsJspBean.RIGHT_RSS_FEEDS_MANAGEMENT ); %>
<%= rss.getManageAdvancedParameters( request ) %>

<%@ include file="../../AdminFooter.jsp" %>