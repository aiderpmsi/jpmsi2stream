<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:output encoding="UTF-8" method="xml" indent="yes" />

	<xsl:param name="cmd" select="'default value'" />

	<xsl:template match="/">
		<cmd>
			<id>{$cmd}</id>
			<actes>
				<xsl:for-each select="/actes/acte[CMD/text() = $cmd]">
					<acte>
						<xsl:value-of select="@id" />
					</acte>
				</xsl:for-each>
			</actes>
		</cmd>
	</xsl:template>

</xsl:stylesheet>