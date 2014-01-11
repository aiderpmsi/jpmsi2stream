<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:output encoding="UTF-8" method="xml" indent="yes" />

	<xsl:param name="linetype" select="'default value'" />

	<xsl:template match="/">
		<linetype>
			<xsl:for-each select="/linetypes/linetype[ids/id/text() = $linetype][1]">
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
								<xsl:value-of select="pattern" />
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
			</xsl:for-each>
		</linetype>
	</xsl:template>
</xsl:stylesheet>