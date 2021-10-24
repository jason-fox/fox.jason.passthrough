<?xml version="1.0" encoding="utf-8"?>
<!--
	This file is part of the DITA Passthrough project.
	See the accompanying LICENSE file for applicable licenses.
-->
<xsl:stylesheet
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:xs="http://www.w3.org/2001/XMLSchema"
  xmlns:ditaarch="http://dita.oasis-open.org/architecture/2005/"
  xmlns:dita-ot="http://dita-ot.sourceforge.net/ns/201007/dita-ot"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  exclude-result-prefixes="xs ditaarch dita-ot xsi"
  version="2.0"
>

	<!-- Import the standard org.dita.normalize functions -->
	<xsl:import href="plugin:org.dita.normalize:xsl/normalize.xsl"/>


	<xsl:template match="/">
		<xsl:for-each select="job/files/file[@format = ('dita', 'ditamap')]">
			<xsl:variable name="output.uri">
				<xsl:choose>
					<xsl:when test="ends-with(@uri, '.ditamap')">
						<xsl:value-of select="concat($output.dir.uri, @uri)"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="replace(concat($output.dir.uri, @uri), '\.\w*$', '.dita')"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:variable>

			<xsl:for-each select="document(@uri, .)">
				<xsl:choose>
					<xsl:when test="*/@xsi:noNamespaceSchemaLocation">
						<xsl:result-document href="{$output.uri}">
							<xsl:apply-templates/>
						</xsl:result-document>
					</xsl:when>
					<xsl:otherwise>
						<xsl:result-document
              href="{$output.uri}"
              doctype-public="{dita-ot:get-doctype-public(.)}"
              doctype-system="{dita-ot:get-doctype-system(.)}"
            >
							<xsl:apply-templates/>
						</xsl:result-document>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:for-each>
		</xsl:for-each>
	</xsl:template>

	<xsl:template match="@href[../@format='dita']">
		<xsl:attribute name="href">
			<xsl:value-of select="replace(., '\.\w*$', '.dita')"/>
		</xsl:attribute>
	</xsl:template>

</xsl:stylesheet>
