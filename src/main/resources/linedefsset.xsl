<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:param name="linetype" select="'default value'" />
	<xsl:template match="/linetypes/linetype[ids/id/text() = $linetype]">
		<linetype>
			<name>
				<xsl:value-of select="@name" />
			</name>
			<elements>
				<xsl:for-each select="element">
					<element>
						<name>
							<xsl:value-of select="@name" />
						</name>
						<pattern>
							<xsl:value-of select="name" />
						</pattern>
						<in>
							<xsl:value-of select="in" />
						</in>
						<out>
							<xsl:value-of select="out" />
						</out>
					</element>
				</xsl:for-each>
			</elements>
		</linetype>
	</xsl:template>
</xsl:stylesheet>