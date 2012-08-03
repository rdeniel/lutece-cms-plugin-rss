<?xml version="1.0" ?>

<!--
Author:  Kevin A Burton (burton@apache.org)
Author:  Santiago Gala (sgala@hisitech.com)
Author:  Raphaïel Luta (raphael@apache.org)
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
                xmlns:downlevel="http://my.netscape.com/rdf/simple/0.9/"
                exclude-result-prefixes="downlevel rdf"
                version="1.0">

    <xsl:output indent="yes"
                 method="html"
                 omit-xml-declaration="yes"/>

    <xsl:param name="itemdisplayed" select="number(15)"/>
    <xsl:param name="showdescription" select="'true'"/>
    <xsl:param name="showtitle" select="'true'"/>

<xsl:template match="portlet">
	
		
	<xsl:variable name="device_class">
	<xsl:choose>
		<xsl:when test="string(display-on-small-device)='0'">hidden-phone</xsl:when>
		<xsl:otherwise></xsl:otherwise>
	</xsl:choose>
	</xsl:variable>

	<div class="portlet {$device_class} append-bottom -lutece-border-radius">

        <xsl:if test="not(string(display-portlet-title)='1')">
			<h3 class="portlet-header">
				<xsl:value-of disable-output-escaping="no" select="portlet-name" />
			</h3>
			<br />
        </xsl:if>

		<div class="portlet-content">
		    <xsl:choose>
		    <xsl:when test="not(rss)">
			    <xsl:text disable-output-escaping="yes"><![CDATA[&nbsp;]]></xsl:text>
			</xsl:when>
			<xsl:otherwise>
			    <xsl:apply-templates select="rss" />
			</xsl:otherwise>
			</xsl:choose>
		</div>
	</div>
</xsl:template>


    <xsl:template match="rss">
      <div>
        <xsl:apply-templates select="channel"/>
        <xsl:apply-templates select="textinput"/>
      </div>
    </xsl:template>

    <xsl:template match="/rdf:RDF">
      <div>
        <xsl:apply-templates select="downlevel:channel"/>
        <xsl:apply-templates select="downlevel:textinput"/>
      </div>
    </xsl:template>

    <xsl:template match="channel">
      <h2><xsl:value-of disable-output-escaping="yes" select="title"/></h2>
      <xsl:variable name="description" select="description"/>
      <xsl:if test="$showtitle = 'true' and $description">
        <p>
          <xsl:apply-templates select="image|../image" mode="channel"/>
          <strong><xsl:value-of disable-output-escaping="yes" select="$description"/></strong>
        </p>
      </xsl:if>
      <ul>
        <xsl:apply-templates select="item[$itemdisplayed>=position()]"/>
      </ul>
    </xsl:template>

    <xsl:template match="downlevel:channel">
      <xsl:variable name="description" select="downlevel:description"/>
      <xsl:if test="$showtitle = 'true' and $description">
        <p>
        <xsl:choose>
         <xsl:when test="count(../downlevel:image)">
          <xsl:apply-templates select="../downlevel:image" mode="channel"/>
          <xsl:value-of select="$description"/>
         </xsl:when>
         <xsl:otherwise>
          <a href="{downlevel:link}">
            <xsl:value-of disable-output-escaping="yes" select="$description"/></a>
         </xsl:otherwise>
        </xsl:choose>
        </p>
      </xsl:if>
      <ul>
        <xsl:apply-templates select="../downlevel:item[$itemdisplayed>=position()]"/>
      </ul>
    </xsl:template>

    <xsl:template match="item">
      <xsl:variable name="description" select="description"/>
      <li>
        <blockquote>
        <a href="{link}"><xsl:value-of select="title"/></a>
        <xsl:if test="$showdescription = 'true' and $description">
          <br/><xsl:value-of disable-output-escaping="yes" select="substring($description,0,400)"/>
          <xsl:text>...</xsl:text>
          <p align="right"><a href="{link}" target="_blank"><xsl:text>Lire la suite ></xsl:text></a></p>
        </xsl:if>
        </blockquote>
      </li>
    </xsl:template>

    <xsl:template match="downlevel:item">
      <xsl:variable name="description" select="downlevel:description"/>
      <li>
        <a href="{downlevel:link}"><xsl:value-of select="downlevel:title"/></a>
        <xsl:if test="$showdescription = 'true' and $description">
          <br/><xsl:value-of disable-output-escaping="yes" select="$description"/>
        </xsl:if>
      </li>
    </xsl:template>

    <xsl:template match="textinput">
      <form action="{link}">
        <xsl:value-of select="description"/><br/>
        <input type="text" name="{name}" value=""/>
        <input type="submit" name="submit" value="{title}"/>
      </form>
    </xsl:template>

    <xsl:template match="downlevel:textinput">
      <form action="{downlevel:link}">
        <xsl:value-of select="downlevel:description"/><br/>
        <input type="text" name="{downlevel:name}" value=""/>
        <input type="submit" name="submit" value="{downlevel:title}"/>
      </form>
    </xsl:template>

    <xsl:template match="image" mode="channel">
      <a href="{link}">
        <xsl:element name="img">
          <xsl:attribute name="align">right</xsl:attribute>
          <xsl:attribute name="border">0</xsl:attribute>
          <xsl:if test="title">
            <xsl:attribute name="alt">
              <xsl:value-of select="title"/>
            </xsl:attribute>
          </xsl:if>
          <xsl:if test="url">
            <xsl:attribute name="src">
              <xsl:value-of select="url"/>
            </xsl:attribute>
          </xsl:if>
          <xsl:if test="width">
            <xsl:attribute name="width">
              <xsl:value-of select="width"/>
            </xsl:attribute>
          </xsl:if>
          <xsl:if test="height">
            <xsl:attribute name="height">
              <xsl:value-of select="height"/>
            </xsl:attribute>
          </xsl:if>
        </xsl:element>
      </a>
    </xsl:template>

    <xsl:template match="downlevel:image" mode="channel">
      <a href="{downlevel:link}">
        <xsl:element name="img">
          <xsl:attribute name="align">right</xsl:attribute>
          <xsl:attribute name="border">0</xsl:attribute>
          <xsl:if test="downlevel:title">
            <xsl:attribute name="alt">
              <xsl:value-of select="downlevel:title"/>
            </xsl:attribute>
          </xsl:if>
          <xsl:if test="downlevel:url">
            <xsl:attribute name="src">
              <xsl:value-of select="downlevel:url"/>
            </xsl:attribute>
          </xsl:if>
        </xsl:element>
      </a>
    </xsl:template>

    <!-- We ignore images unless we are inside a channel -->
    <xsl:template match="image">
    </xsl:template>
    <xsl:template match="downlevel:image">
    </xsl:template>

</xsl:stylesheet>



