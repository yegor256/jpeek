<?xml version="1.0"?>
<!--
The MIT License (MIT)

Copyright (c) 2017-2018 Yegor Bugayenko

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included
in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:math="http://www.w3.org/2005/xpath-functions/math" version="2.0">
  <xsl:template match="skeleton">
    <metric>
      <xsl:apply-templates select="@*"/>
      <title>MWE</title>
      <description>
        <xsl:text><![CDATA[
          MWE = max( Oi * Di )

          where:
          Oi = sum( Pdi ) / n: Occupancy of topic i in a class;
          Di = sum( -Qdi * log(Qdi) ) / log(n): Distribution of topic i in a class;
          Qdi = Pdi / sum( Pdi ): distribution of topic i in a method d;
          Pdi - probability of topic i in a method d;
          n - number of methods.
        ]]></xsl:text>
      </description>
      <xsl:apply-templates select="node()"/>
    </metric>
  </xsl:template>
  <xsl:template match="class">
    <!--
    @todo #234: To calculate the Maximal Weighted Entropy(MWE)
     the "topic" is needed. To calculate the topics: a method contains words
     excluding programming language keywords, stop words and punctuation.
     So, to calculate the MWE metric each variable's name is a topic.
     A High Cohesive Class has two characteristics:
     - High Occupancy (Oi): the main topic is present in all the methods.
     - High Distribution (Di): all the methods are related to the same topic.
     For more information check the PDF [lyu09] in the papers.
    -->
    <xsl:variable name="n" select="count(methods/method)"/>
    <xsl:variable name="Oi">
      <xsl:for-each-group select="methods/method/topics/topic" group-by="@name">
        <xsl:variable name="topic" select="current-grouping-key()"/>
        <oi topic="{$topic}" value="{sum(current-group()/@p) div $n}"/>
      </xsl:for-each-group>
    </xsl:variable>
    <xsl:variable name="Qdi">
      <xsl:variable name="cur" select="."/>
      <xsl:for-each-group select="methods/method" group-by="@name">
        <xsl:variable name="method" select="current-grouping-key()"/>
        <xsl:for-each-group select="current-group()/topics/topic" group-by="@name">
          <xsl:variable name="topic" select="current-grouping-key()"/>
          <xsl:variable name="pdi" select="$cur/methods/method[@name=$method]/topics/topic[@name=$topic]/@p"/>
          <xsl:variable name="pdSum" select="sum($cur/methods/method/topics/topic[@name=$topic]/@p)"/>
          <qdi method="{$method}" topic="{$topic}" value="{$pdi div $pdSum}"/>
        </xsl:for-each-group>
      </xsl:for-each-group>
    </xsl:variable>
    <xsl:variable name="Di">
      <xsl:for-each-group select="$Qdi/qdi" group-by="@topic">
        <xsl:variable name="qdi" select="."/>
        <di topic="{current-grouping-key()}" value="(sum(-1 * $qdi * math:log10($qdi))) div math:log10($n)"/>
      </xsl:for-each-group>
    </xsl:variable>
    <xsl:variable name="OiDi">
      <xsl:for-each select="$Oi/oi">
        <xsl:variable name="topic" select="@topic"/>
        <xsl:variable name="Di" select="$Di/di[@topic=$topic]/@value"/>
        <xsl:variable name="Oi" select="$Oi/oi[@topic=$topic]/@value"/>
        <oidi v="{$Di * $Oi}"/>
      </xsl:for-each>
    </xsl:variable>
    <xsl:copy>
      <!--xsl:attribute name="value" select="max($OiDi/oidi/@v)"/-->
      <xsl:attribute name="value" select="format-number(1.0, '0.####')"/>
      <xsl:apply-templates select="@*"/>
    </xsl:copy>
  </xsl:template>
  <xsl:template match="node()|@*">
    <xsl:copy>
      <xsl:apply-templates select="node()|@*"/>
    </xsl:copy>
  </xsl:template>
</xsl:stylesheet>
