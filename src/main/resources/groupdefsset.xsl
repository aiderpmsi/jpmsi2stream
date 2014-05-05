<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:output encoding="UTF-8" method="xml" indent="yes" />

	<xsl:param name="group" select="'default value'" />

	<xsl:template match="/">
		<group>
			<elements>
				<xsl:for-each select="/groups/group[@name = $group][1]">
					<xsl:for-each select="element">
						<element>
							<xsl:value-of select="text()" />
						</element>
					</xsl:for-each>
				</xsl:for-each>
			</elements>
		</group>
	</xsl:template>

</xsl:stylesheet>