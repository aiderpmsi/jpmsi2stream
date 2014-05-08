<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:output encoding="UTF-8" method="xml" indent="yes" />

	<xsl:param name="classe" select="'default value'" />

	<xsl:template match="/">
		<classe>
			<id>{$classe}</id>
			<actes>
				<xsl:for-each select="/actes/acte/regroupement/phase[../@type = $classe]">
					<acte>
						<xsl:value-of select="concat(../name/text(), '/', text())" />
					</acte>
				</xsl:for-each>
			</actes>
		</classe>
	</xsl:template>

</xsl:stylesheet>