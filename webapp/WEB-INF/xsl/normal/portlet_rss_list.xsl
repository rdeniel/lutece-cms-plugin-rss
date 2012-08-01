<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:template match="portlet">
	
		
	<xsl:variable name="device_class">
	<xsl:choose>
		<xsl:when test="string(display-on-small-device)='0'">hide-for-small</xsl:when>
		<xsl:otherwise></xsl:otherwise>
	</xsl:choose>
	</xsl:variable>

	<div class="portlet {$device_class} append-bottom">
		<xsl:choose>
			<xsl:when test="not(string(display-portlet-title)='1')">
				<div class="portlet-header -lutece-border-radius-top">
					<xsl:value-of disable-output-escaping="yes" select="portlet-name" />
				</div>
				<div class="portlet-content -lutece-border-radius-bottom">
					<xsl:apply-templates select="rss-list-portlet" />
				</div>
			</xsl:when>
			<xsl:otherwise>
				<div class="portlet-content -lutece-border-radius">
					<xsl:apply-templates select="rss-list-portlet" />
				</div>
			</xsl:otherwise>
		</xsl:choose>
		</div>
	</xsl:template>
	
	<xsl:template match="rss-list-portlet">
		<xsl:variable name="url">
			<xsl:value-of select="rss-list-portlet-url" />
		</xsl:variable>
		<li>
			<div>
				<a href="{$url}"><xsl:value-of select="rss-list-portlet-name" /></a> 
			</div>
			<div>
				 <xsl:value-of select="rss-list-portlet-url" />
			</div>
		</li>
	</xsl:template>

</xsl:stylesheet>