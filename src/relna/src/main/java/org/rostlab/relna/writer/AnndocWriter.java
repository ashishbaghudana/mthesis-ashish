package org.rostlab.relna.writer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.UUID;
import org.rostlab.relna.corpus.*;
import org.json.*;
import org.apache.commons.io.FilenameUtils;;

public class AnndocWriter implements IWriter {
	
	private Document document;
	private String uuid;
	private JSONObject json;
	
	public AnndocWriter (Document doc) {
		this.document = doc;
		this.uuid = UUID.randomUUID().toString();
		this.uuid = this.uuid.replace("-", "");
		this.json = new JSONObject();
		this.createJSON();
	}

	public void write(String file) {
		try {
			FileWriter fw = new FileWriter(new File(file));
			fw.write(json.toString(4));
			fw.close();
			
			String htmlFile = FilenameUtils.removeExtension(file)+".html";
			fw = new FileWriter(new File(htmlFile));
			fw.write(this.generateHTMLPage());
			fw.close();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Deprecated
	public void write(Writer writer) {
		try {
			writer.write(json.toString(4));
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void createJSON() {
		json.accumulate("anncomplete", false);
		json.accumulate("annotatable", "s1h1");
		json.accumulate("annotatable", "s2h1");
		json.accumulate("annotatable", "s2s1p1");
		JSONObject sources = new JSONObject();
		sources.accumulate("name", "MEDLINE");
		sources.accumulate("id", this.document.documentId);
		sources.accumulate("url", null);
		json.accumulate("sources", sources);
		for (int key1 : document.getDNATokens().keySet()) {
			for (int key2 : document.getDNATokens().get(key1).keySet()) {
				for (Phrase phrase : document.getDNATokens().get(key1).get(key2)) {
					JSONObject entity = new JSONObject();
					JSONObject offsets = new JSONObject();
					JSONObject confidence = new JSONObject();
					entity.accumulate("classId", "e_2");
					offsets.accumulate("start", phrase.getStartOffset());
					offsets.accumulate("text", phrase.toString());
					entity.accumulate("offsets", offsets);
					confidence.accumulate("state", "");
					String [] who = {"Genia4ER"};
					confidence.accumulate("who", who);
					confidence.accumulate("prob", 1.000);
					entity.accumulate("confidence", confidence);
					if (key1==0)
						entity.accumulate("part", "s1h1");
					else
						entity.accumulate("part", "s2s1p1");
					json.accumulate("entities", entity);
				}
			}
		}
		
		for (int key1 : document.getRNATokens().keySet()) {
			for (int key2 : document.getRNATokens().get(key1).keySet()) {
				for (Phrase phrase : document.getRNATokens().get(key1).get(key2)) {
					JSONObject entity = new JSONObject();
					JSONObject offsets = new JSONObject();
					JSONObject confidence = new JSONObject();
					entity.accumulate("classId", "e_3");
					offsets.accumulate("start", phrase.getStartOffset());
					offsets.accumulate("text", phrase.toString());
					entity.accumulate("offsets", offsets);
					confidence.accumulate("state", "");
					String [] who = {"Genia4ER"};
					confidence.accumulate("who", who);
					confidence.accumulate("prob", 1.000);
					entity.accumulate("confidence", confidence);
					json.accumulate("entities", entity);
				}
			}
		}
		
		for (int key1 : document.getProteinTokens().keySet()) {
			for (int key2 : document.getProteinTokens().get(key1).keySet()) {
				for (Phrase phrase : document.getProteinTokens().get(key1).get(key2)) {
					JSONObject entity = new JSONObject();
					JSONObject offsets = new JSONObject();
					JSONObject confidence = new JSONObject();
					entity.accumulate("classId", "e_1");
					offsets.accumulate("start", phrase.getStartOffset());
					offsets.accumulate("text", phrase.toString());
					entity.accumulate("offsets", offsets);
					confidence.accumulate("state", "");
					String [] who = {"Genia4ER"};
					confidence.accumulate("who", who);
					confidence.accumulate("prob", 1.000);
					entity.accumulate("confidence", confidence);
					json.accumulate("entities", entity);
				}
			}
		}
	}
	
	private String generateHTMLPage() {
		StringBuilder s = new StringBuilder();
		s.append(generateHTMLHeader());
		s.append(generateHTMLBody());
		return s.toString();
	}
	
	private String generateHTMLHeader() {
		StringBuilder s = new StringBuilder(420);
		
/*		<!DOCTYPE html >
		<html id="4ae5a3ffbd0e0d5b74c5790d0f6e0954:1471-2229-8-71" data-origid="1471-2229-8-71" class="anndoc" data-anndoc-version="2.0" lang="" xml:lang="" xmlns="http://www.w3.org/1999/xhtml">
		  <head>
		    <meta charset="UTF-8"/>
		    <meta name="generator" content="org.rostlab.relna.annotator.NamedEntityRecognizer$"/>
		    <title>4ae5a3ffbd0e0d5b74c5790d0f6e0954:12345678</title>
		  </head>
*/		
		
		s.append("<DOCTYPE html >\n");
		s.append("<html id=\""+this.uuid+":"+this.document.documentId+"\" data-origid=\""+this.document.documentId+"\" class=\"anndoc\" data-anndoc-version\"2.0\" xml:lang=\"\" xmlns=\"http://www.w3.org/1999/xhtml\">\n");
		s.append("\t<head>\n");
		s.append("\t\t<meta charset=\"UTF-8\"/\n");
		s.append("\t\tmeta name=\"generator\" content=\"org.rostlab.relna.annotator.NamedEntityRecognizer$\"/>\n");
		s.append("\t\t<title>"+this.uuid+":"+this.document.documentId+"</title>\n");
		s.append("\t</head>\n");
		
		return s.toString();
	}
	
	private String generateHTMLBody() {
		StringBuilder s = new StringBuilder(400);
		s.append("\t<body>\n");
		s.append("\t\t<article>\n");
		s.append("\t\t\t<section data-type=\"title\">\n");
		s.append("\t\t\t\t<h2 id=\"s1h1\">");
		s.append(this.document.getSection(0));
		s.append("</h2>\n");
		s.append("\t\t\t<section data-type=\"abstract\">\n");
		s.append("\t\t\t\t<div class=\"subsections\">\n");
		s.append("\t\t\t\t\t<h3 id=\"s2h1\">Abstract</h3>");
		s.append("\t\t\t\t\t<p id = \"s2s1p1\">");
		s.append(this.document.getSection(1));
		s.append("</p>\n");
		s.append("\t\t\t\t</div>\n\t\t\t</section>\n");
		s.append("\t\t</article>\n");
		s.append("\t</body>\n");
		s.append("</html>");
		return s.toString();
	}
}
