<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:output encoding="UTF-8" method="xml" indent="yes" />

	<xsl:param name="group" select="'default value'" />

	<xsl:template match="/">
		<group>
			<elements>
				<xsl:for-each select="/groups/group[@name = $group][1]">
					<element>
						<xsl:for-each select="element">
							<xsl:value-of select="text()" />
						</xsl:for-each>
					</element>
				</xsl:for-each>
			</elements>
		</group>
	</xsl:template>

</xsl:stylesheet>