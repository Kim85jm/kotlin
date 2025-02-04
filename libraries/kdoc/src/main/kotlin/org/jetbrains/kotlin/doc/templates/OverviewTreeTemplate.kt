package org.jetbrains.kotlin.doc.templates

import kotlin.*
import org.jetbrains.kotlin.template.*
import kotlin.io.*
import kotlin.util.*
import java.util.*
import org.jetbrains.kotlin.doc.model.KModel

class OverviewTreeTemplate(val model: KModel) : KDocTemplate() {
    override fun render() {
        println("""<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3.org/TR/html4/frameset.dtd">
<!--NewPage-->
<HTML>
<HEAD>
<!-- Generated by javadoc on Mon Jan 09 13:32:00 EST 2012-->
<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
<TITLE>
Class Hierarchy (${model.title})
</TITLE>

<META NAME="date" CONTENT="2012-01-09">

<LINK REL ="stylesheet" TYPE="text/css" HREF="stylesheet.css" TITLE="Style">

<SCRIPT type="text/javascript">
function windowTitle()
{
    if (location.href.indexOf('is-external=true') == -1) {
        parent.document.title="Class Hierarchy (${model.title})";
    }
}
</SCRIPT>
<NOSCRIPT>
</NOSCRIPT>

</HEAD>

<BODY BGCOLOR="white" onload="windowTitle();">
<HR>


<!-- ========= START OF TOP NAVBAR ======= -->
<A NAME="navbar_top"><!-- --></A>
<A HREF="#skip-navbar_top" title="Skip navigation links"></A>
<TABLE BORDER="0" WIDTH="100%" CELLPADDING="1" CELLSPACING="0" SUMMARY="">
<TR>
<TD COLSPAN=2 BGCOLOR="#EEEEFF" CLASS="NavBarCell1">
<A NAME="navbar_top_firstrow"><!-- --></A>
<TABLE BORDER="0" CELLPADDING="0" CELLSPACING="3" SUMMARY="">
  <TR ALIGN="center" VALIGN="top">
  <TD BGCOLOR="#EEEEFF" CLASS="NavBarCell1">    <A HREF="overview-summary.html"><FONT CLASS="NavBarFont1"><B>Overview</B></FONT></A>&nbsp;</TD>
  <TD BGCOLOR="#EEEEFF" CLASS="NavBarCell1">    <FONT CLASS="NavBarFont1">Package</FONT>&nbsp;</TD>
  <TD BGCOLOR="#EEEEFF" CLASS="NavBarCell1">    <FONT CLASS="NavBarFont1">Class</FONT>&nbsp;</TD>
  <TD BGCOLOR="#EEEEFF" CLASS="NavBarCell1">    <FONT CLASS="NavBarFont1">Use</FONT>&nbsp;</TD>
  <TD BGCOLOR="#FFFFFF" CLASS="NavBarCell1Rev"> &nbsp;<FONT CLASS="NavBarFont1Rev"><B>Tree</B></FONT>&nbsp;</TD>
  <TD BGCOLOR="#EEEEFF" CLASS="NavBarCell1">    <A HREF="deprecated-list.html"><FONT CLASS="NavBarFont1"><B>Deprecated</B></FONT></A>&nbsp;</TD>
  <TD BGCOLOR="#EEEEFF" CLASS="NavBarCell1">    <A HREF="index-all.html"><FONT CLASS="NavBarFont1"><B>Index</B></FONT></A>&nbsp;</TD>
  <TD BGCOLOR="#EEEEFF" CLASS="NavBarCell1">    <A HREF="help-doc.html"><FONT CLASS="NavBarFont1"><B>Help</B></FONT></A>&nbsp;</TD>
  </TR>
</TABLE>
</TD>
<TD ALIGN="right" VALIGN="top" ROWSPAN=3><EM>
</EM>
</TD>
</TR>

<TR>
<TD BGCOLOR="white" CLASS="NavBarCell2"><FONT SIZE="-2">
&nbsp;PREV&nbsp;
&nbsp;NEXT</FONT></TD>
<TD BGCOLOR="white" CLASS="NavBarCell2"><FONT SIZE="-2">
  <A HREF="index.html?overview-tree.html" target="_top"><B>FRAMES</B></A>  &nbsp;
&nbsp;<A HREF="overview-tree.html" target="_top"><B>NO FRAMES</B></A>  &nbsp;
&nbsp;<SCRIPT type="text/javascript">
  <!--
  if(window==top) {
    document.writeln('<A HREF="allclasses-noframe.html"><B>All Classes</B></A>');
  }
  //-->
</SCRIPT>
<NOSCRIPT>
  <A HREF="allclasses-noframe.html"><B>All Classes</B></A>
</NOSCRIPT>


</FONT></TD>
</TR>
</TABLE>
<A NAME="skip-navbar_top"></A>
<!-- ========= END OF TOP NAVBAR ========= -->

<HR>
<CENTER>
<H2>
Hierarchy For All Packages</H2>
</CENTER>
<DL>
<DT><B>Package Hierarchies:</B><DD>""")

        var first = true
        for (p in model.packages) {
            if (first) {
                first = false
            } else {
                print(""",
<A HREF="${p.nameAsPath}/package-tree.html">${p.nameAsPath}</A>""")
            }
        }
        println("""</DL>
<HR>
<H2>
Class Hierarchy
</H2>""")

        // TODO walk the hierarchy...
        /*
<UL>
<LI TYPE="circle">java.util.<A HREF="http://download.oracle.com/javase/1.5.0/docs/api/java/util/AbstractQueue.html?is-external=true" title="class or interface in java.util"><B>AbstractQueue</B></A>&lt;E&gt; (implements java.util.<A HREF="http://download.oracle.com/javase/1.5.0/docs/api/java/util/Queue.html?is-external=true" title="class or interface in java.util">Queue</A>&lt;E&gt;)
<UL>
*/

        println("""<H2>
Interface Hierarchy
</H2>""")

        // TODO
        println("""<H2>
Annotation Type Hierarchy
</H2>""")

        // TODO
        println("""<H2>
Enum Hierarchy
</H2>
<HR>


<!-- ======= START OF BOTTOM NAVBAR ====== -->
<A NAME="navbar_bottom"><!-- --></A>
<A HREF="#skip-navbar_bottom" title="Skip navigation links"></A>
<TABLE BORDER="0" WIDTH="100%" CELLPADDING="1" CELLSPACING="0" SUMMARY="">
<TR>
<TD COLSPAN=2 BGCOLOR="#EEEEFF" CLASS="NavBarCell1">
<A NAME="navbar_bottom_firstrow"><!-- --></A>
<TABLE BORDER="0" CELLPADDING="0" CELLSPACING="3" SUMMARY="">
  <TR ALIGN="center" VALIGN="top">
  <TD BGCOLOR="#EEEEFF" CLASS="NavBarCell1">    <A HREF="overview-summary.html"><FONT CLASS="NavBarFont1"><B>Overview</B></FONT></A>&nbsp;</TD>
  <TD BGCOLOR="#EEEEFF" CLASS="NavBarCell1">    <FONT CLASS="NavBarFont1">Package</FONT>&nbsp;</TD>
  <TD BGCOLOR="#EEEEFF" CLASS="NavBarCell1">    <FONT CLASS="NavBarFont1">Class</FONT>&nbsp;</TD>
  <TD BGCOLOR="#EEEEFF" CLASS="NavBarCell1">    <FONT CLASS="NavBarFont1">Use</FONT>&nbsp;</TD>
  <TD BGCOLOR="#FFFFFF" CLASS="NavBarCell1Rev"> &nbsp;<FONT CLASS="NavBarFont1Rev"><B>Tree</B></FONT>&nbsp;</TD>
  <TD BGCOLOR="#EEEEFF" CLASS="NavBarCell1">    <A HREF="deprecated-list.html"><FONT CLASS="NavBarFont1"><B>Deprecated</B></FONT></A>&nbsp;</TD>
  <TD BGCOLOR="#EEEEFF" CLASS="NavBarCell1">    <A HREF="index-all.html"><FONT CLASS="NavBarFont1"><B>Index</B></FONT></A>&nbsp;</TD>
  <TD BGCOLOR="#EEEEFF" CLASS="NavBarCell1">    <A HREF="help-doc.html"><FONT CLASS="NavBarFont1"><B>Help</B></FONT></A>&nbsp;</TD>
  </TR>
</TABLE>
</TD>
<TD ALIGN="right" VALIGN="top" ROWSPAN=3><EM>
</EM>
</TD>
</TR>

<TR>
<TD BGCOLOR="white" CLASS="NavBarCell2"><FONT SIZE="-2">
&nbsp;PREV&nbsp;
&nbsp;NEXT</FONT></TD>
<TD BGCOLOR="white" CLASS="NavBarCell2"><FONT SIZE="-2">
  <A HREF="index.html?overview-tree.html" target="_top"><B>FRAMES</B></A>  &nbsp;
&nbsp;<A HREF="overview-tree.html" target="_top"><B>NO FRAMES</B></A>  &nbsp;
&nbsp;<SCRIPT type="text/javascript">
  <!--
  if(window==top) {
    document.writeln('<A HREF="allclasses-noframe.html"><B>All Classes</B></A>');
  }
  //-->
</SCRIPT>
<NOSCRIPT>
  <A HREF="allclasses-noframe.html"><B>All Classes</B></A>
</NOSCRIPT>


</FONT></TD>
</TR>
</TABLE>
<A NAME="skip-navbar_bottom"></A>
<!-- ======== END OF BOTTOM NAVBAR ======= -->

<HR>
Copyright &#169; 2010-2012. All Rights Reserved.
</BODY>
</HTML>""")
    }
}
