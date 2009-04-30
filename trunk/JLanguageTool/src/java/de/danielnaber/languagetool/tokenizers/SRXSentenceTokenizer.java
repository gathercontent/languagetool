/* LanguageTool, a natural language style checker 
 * Copyright (C) 2009 Daniel Naber (http://www.danielnaber.de)
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301
 * USA
 */
package de.danielnaber.languagetool.tokenizers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.segment.TextIterator;
import net.sourceforge.segment.srx.SrxDocument;
import net.sourceforge.segment.srx.SrxParser;
import net.sourceforge.segment.srx.SrxTextIterator;
import net.sourceforge.segment.srx.io.Srx2Parser;

import de.danielnaber.languagetool.tools.Tools;

/**
 * Class to tokenize sentences using an SRX file.
 * 
 * @author Marcin Miłkowski
 * 
 */
public class SRXSentenceTokenizer extends SentenceTokenizer {
  
  BufferedReader srxReader;
  SrxDocument document;
  String language;
  String parCode;

  public static final String RULES = "/resource/segment.srx";
  
  public SRXSentenceTokenizer(final String language) {
    this.language = language;

    try {      
      srxReader = new BufferedReader(new InputStreamReader(Tools
          .getStream(RULES), "utf-8"));      
      SrxParser srxParser = new Srx2Parser();
      document = srxParser.parse(srxReader);
      
    } catch (UnsupportedEncodingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    setSingleLineBreaksMarksParagraph(false);    
  }

  @Override
  public List<String> tokenize(String text) {
    final List<String> segments = new ArrayList<String>();
    
    TextIterator textIterator = new SrxTextIterator(document, language + parCode, text);
    while (textIterator.hasNext()) {
      segments.add(textIterator.next());
    }
    return segments;
  }

  public boolean singleLineBreaksMarksPara() {
    return "_one".equals(parCode);
  }
  
  /**
   * @param lineBreakParagraphs if <code>true</code>, single lines breaks are assumed to end a paragraph,
   *  with <code>false</code>, only two ore more consecutive line breaks end a paragraph
   */
  public void setSingleLineBreaksMarksParagraph(final boolean lineBreakParagraphs) {
    if (lineBreakParagraphs) {
      parCode = "_one";
    } else {
      parCode = "_two";
    }
  }

  
  protected void finalize() throws Throwable {
    super.finalize();
    if (srxReader != null) {
      srxReader.close();
    }
  }

}
