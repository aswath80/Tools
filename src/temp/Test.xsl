<xsl:stylesheet version="1.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
   <xsl:template match="/">
      <xsl:variable name="x" select="'0'"></xsl:variable>
      <xsl:variable name="y">
         <xsl:choose>
            <xsl:when test="number($x)">
               <xsl:value-of select="concat($x,' is number')"></xsl:value-of>
            </xsl:when>
            <xsl:otherwise>
               <xsl:value-of select="concat($x,' is not number')"></xsl:value-of>
            </xsl:otherwise>
         </xsl:choose>
      </xsl:variable>
      <test>
         <value>
            <xsl:value-of select="$y"></xsl:value-of>
         </value>
      </test>
   </xsl:template>
</xsl:stylesheet>