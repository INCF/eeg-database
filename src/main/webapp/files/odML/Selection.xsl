<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
xmlns:fn="http://www.w3.org/2005/02/xpath-functions">
<!-- ************************************************  -->
<!--                   root template                   -->
<xsl:template match="odML">
  <html>
  <link rel="stylesheet" type="text/css" href="SelectionStyle.css"/>
  <body>
		<div>
			<table class="dataTable" rules="none" width="50%">
			<!--<caption><h2>Sections</h2></caption>-->
			<tr>
			<th><h3>Section</h3></th>
			<th><h3>Subsection</h3></th>
			</tr>
			<tr><th>Required sections</th><th/></tr>
			<xsl:if test="section">
				<xsl:for-each select="section">
				<xsl:variable name="req" select="required"/>
				<xsl:if test="$req='yes' and not(section)">
					<xsl:call-template name="sectionTemplate">
					</xsl:call-template>
				</xsl:if>
				</xsl:for-each>
				<xsl:for-each select="section">
				<xsl:variable name="req" select="required"/>
				<xsl:if test="$req='yes' and section">
					<xsl:call-template name="sectionTemplate">
					</xsl:call-template>
				</xsl:if>
				</xsl:for-each>
				
				<tr><th>Optional sections</th><th/></tr>
				<xsl:for-each select="section">
				<xsl:variable name="req" select="required"/>
				<xsl:if test="$req='no'">
					<xsl:call-template name="sectionTemplate">
					</xsl:call-template>
				</xsl:if>
				</xsl:for-each>
			</xsl:if>
			</table>			
		</div>			
   </body>
  </html> 
</xsl:template>

<!-- ************************************************  -->
<!--              section template.                    -->
<xsl:template name="sectionTemplate" match="section">
	<!-- the anchor of this section -->
	<xsl:variable name="anchor" select ="type"/>            
		<tr valign="top" align="left" width="50%">
			<xsl:choose>
				<xsl:when test="section">
					<td width="50%">
						<xsl:call-template name="subsectionTemplate"/>
					</td>
					<td width="50%">
						<xsl:for-each select="section"> 
							<xsl:call-template name="subsectionTemplate"/>
							<br/>
						</xsl:for-each> 
					</td>					
				</xsl:when>
				<xsl:otherwise>
					<td>
						<xsl:call-template name="subsectionTemplate"/>
					</td>
					<td/>
				</xsl:otherwise>
			</xsl:choose>
		</tr>
</xsl:template>

<!-- ************************************************  -->
<!--              subsection template.                 -->
<xsl:template name="subsectionTemplate" match="section">
	<xsl:if test="required">
		<xsl:variable name="req" select="required"/>
		<xsl:choose>
			<xsl:when test="$req='no'">
				<input type="checkbox"/><xsl:value-of select="name"/>
			</xsl:when>
			 <xsl:otherwise>
				<xsl:value-of select="name"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:if>
</xsl:template>
</xsl:stylesheet>
