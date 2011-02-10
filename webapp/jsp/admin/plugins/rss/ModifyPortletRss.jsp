<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:include page="../../PortletAdminHeader.jsp" />

<jsp:useBean id="rssPortlet" scope="session" class="fr.paris.lutece.plugins.rss.web.portlet.RssPortletJspBean" />

<% rssPortlet.init( request, rssPortlet.RIGHT_MANAGE_ADMIN_SITE ); %>
<%= rssPortlet.getModify ( request ) %>

<%@ include file="../../AdminFooter.jsp" %>
