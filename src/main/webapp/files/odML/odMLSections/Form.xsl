<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
xmlns:fn="http://www.w3.org/2005/02/xpath-functions">

<!-- This stylesheet is for viewing odml-terminologies in a web browser -->
<!-- Please note: only those elements will be displayed terminology related -->
<!-- e.g. uncertainty, id, and valueComments will not be shown -->
<!-- For viewing real metadata files use the odml.xsl stylesheet -->
<!-- ************************************************  -->
<!--                   root template                   -->
<xsl:template match="odML">
   <!-- if there is a base url definition read it and later pass it to the sections template -->
   <xsl:variable name="repository" select="repository"/>
   <html>
   		<!-- css -->
		<link rel="stylesheet" type="text/css" href="tst.css"/>
      <body>
         <h1>odML - Metadata</h1>
         
         <div id="contentContainer">
            <hr align="left" width="30%"/>
            <!-- apply the section template  -->
	         <xsl:if test="section">
               <xsl:for-each select="section">
                  <xsl:call-template name="sectionTemplate">
                     <xsl:with-param name="navigation">0</xsl:with-param>
                     <xsl:with-param name="anchorBase">Sec</xsl:with-param>
                     <xsl:with-param name="url" select="$repository"/>
                  </xsl:call-template>
               </xsl:for-each>
            </xsl:if>
         </div>
      </body>
   </html> 
</xsl:template>

<!-- ************************************************  -->
<!--              section template.                    -->
<xsl:template name="sectionTemplate" match="section">
   <xsl:param name="navigation"/>   
   <xsl:param name="anchorBase"/>
   <xsl:param name="url"/>
   <xsl:variable name="anchorName" select="concat($anchorBase,position())"/>
   <!-- set new repository if specified within this section otherwise use the old one -->
   <xsl:variable name="repository">
	   <xsl:choose>
		   <xsl:when test="repository">
			   <xsl:value-of select ="repository"/>        
   		</xsl:when>
	   	<xsl:otherwise>
			   <xsl:value-of select ="$url"/>
         </xsl:otherwise>
	   </xsl:choose>
	</xsl:variable>
	<!-- print out the content -->
   <xsl:choose>
      <!--  fill the navigation container if this is the task (navigation param = 1) -->
      <xsl:when test="$navigation = 1">
         <!-- create a link to the anchor in the content container  -->
         <ol><font size="normal"><a href="#{$anchorName}">
	          <xsl:value-of select="name"/> (type: <xsl:value-of select="type"/>)</a></font>
            <!--  recursive call if there are subsections  -->
            <xsl:if test="section">
               <xsl:for-each select="section">
                  <xsl:call-template name="sectionTemplate">
                     <xsl:with-param name="navigation" select="$navigation"/>
                     <xsl:with-param name="anchorBase" select="concat($anchorName,'SubSec')"/>
                     <xsl:with-param name="url" select="$repository"/>
                  </xsl:call-template>      
               </xsl:for-each>
            </xsl:if> 
         </ol>
      </xsl:when>
      <!--  otherwise use template to display the content (navigation !=1) -->
      <xsl:otherwise>
       
	 <h2> <xsl:value-of select="name"/> Section</h2>
	 	<xsl:if test="definition">
			<p><b>Definition: </b><xsl:value-of select="definition"/><br/></p>
		</xsl:if>
	      <!--  Check if there are any properties  -->
         <xsl:if test="property">
		 <xsl:variable name="propName" select="name"/>
			<div id="{$propName}Form">
			    <table class="dataTable" border="1" rules="none" width="30%"><font size="-1">
				<thead>
				   <tr>
					  <th align="left"><font size="+1" color="black"><b>Attribute</b></font></th>
					  <th align="left"><font size="+1" color="black"><b>Value</b></font></th>
					  <th align="left"><font size="+1" color="black"><b>Unit</b></font></th>
				   </tr>
			   </thead>
			   <tbody>
               <xsl:for-each select="property">
					<xsl:variable name="anchor">
					   <xsl:value-of select ="name"/>
					</xsl:variable>
					<xsl:variable name="info">
					   <xsl:value-of select ="definition"/>
					</xsl:variable>

					<tr>
					    <td width="15%"><a name="{$anchor}"/>
							<p title="{$info}"><xsl:value-of select="name"/>
								<xsl:if test="required">
									<xsl:variable name="req" select="required"/>
									<xsl:if test="$req='yes'">
												*								
									</xsl:if>
								</xsl:if></p>
						</td>
						<td width="10%">
							<xsl:choose>
								  <xsl:when test="count(value) &gt; 1">
									<select name="{$anchor}Select">
										<xsl:for-each select="value">
											<xsl:variable name="selectVal" select="value" />
											<option value="{$selectVal}" ><xsl:value-of select="text()"/></option>
										</xsl:for-each> 
									</select>
								  </xsl:when>
								  <xsl:otherwise>
								  		<form>
											<input type="text" name="{$propName}"/>										
										</form>
										<!--<form>
										<xsl:variable name="pokus" select="value/type" />
											<xsl:choose>
											<xsl:when test="$pokus='string'">
												 <input type="url" name="{$propName}"/>										
											</xsl:when>
											 <xsl:otherwise>
												<input type="number" name="{$propName}"/>
											</xsl:otherwise>
											</xsl:choose>
										</form>-->
								  </xsl:otherwise>
							</xsl:choose>
						</td>
						<td width="5%">
							<xsl:for-each select="value">
							   <p><xsl:value-of select="unit"/></p>
							</xsl:for-each>      
						</td>
					</tr>
               </xsl:for-each>
			   	</tbody>
			   </font>
			   </table>
			   <br/>
			</div>
         </xsl:if>  
         <a href="#top"><tiny>Top</tiny></a>
         <hr align="left" width="30%"/>  
         <!--  recursive call if there are subsections  -->
         <xsl:if test="section">
            <xsl:for-each select="section">
               <xsl:call-template name="sectionTemplate">
                  <xsl:with-param name="navigation" select="$navigation"/>
                  <xsl:with-param name="anchorBase" select="concat($anchorName,'SubSec')"/> 
			         <xsl:with-param name="url" select="$repository"/>
               </xsl:call-template>      
            </xsl:for-each>
         </xsl:if> 
      </xsl:otherwise>
   </xsl:choose> 
</xsl:template>
	
</xsl:stylesheet>
