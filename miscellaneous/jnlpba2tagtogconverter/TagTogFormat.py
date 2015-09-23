__author__ = 'Ashish'
from nltk.tokenize import sent_tokenize
import json
import uuid

class TagTogReader(object):
	def __init__(self, file_location):
		self.file_location = file_location
		self.documents = {}
		self.symbols = [',', '.', '(', ')', ':', ';', '[', ']']
		self.punctuations = ['.', ',']
		self.json_content = {}

	def parse(self):
		data_file = open(self.file_location)
		tempString = ""
		id = 1
		for line in data_file:
			if str(line).strip().startswith("###MEDLINE"):
				if tempString!="":
					self.documents[pmid.rstrip()]={}
					sentences = sent_tokenize(tempString.lstrip().rstrip())
					self.documents[pmid.rstrip()]['abstract']=sentences[0]
					self.documents[pmid.rstrip()]['text'] = ' '.join(sentences[1:])
					id = 1
					json_single={}
					json_single['annotatable']={}
					json_single['annotatable']['parts']=["s1h1", "s2h1", "s2p1"]
					json_single['anncomplete']=False
					json_single['sources']=[]
					json_single['sources'].append({"name": "MEDLINE", "id": pmid.rstrip(), "url": None})
					json_single['entities']=entities
					json_single['relations']=[]
					json_single['metas']={}
					self.json_content[pmid.rstrip()]=json_single
				entities = []
				pmid = line.split(":")[1]
				tempString = ""
				is_title = True
			elif str(line)=='\n':
				continue
			else:
				tempWord = str(line).split('\t')
				if tempWord[0] in self.symbols:
					if tempWord[0].startswith('(') or tempWord[0].startswith('['):
						tempString = tempString + ' ' + tempWord[0]
					else:
						tempString = tempString + tempWord[0]
					if (tempWord[0]=="." and is_title==True):
						is_title = False;
					if (entity and tempWord[1].startswith("I-")):
						if tempWord[0]=="(":
							word = word+" "+tempWord[0]
							word.rstrip()
						elif word.endswith("("):
							word = word+tempWord[0];
						elif tempWord[0]==")":
							word = word+tempWord[0]
					if (isEntity and tempWord[1].startswith("O")):
						entity['offsets'][0]['text'] = word
						entities.append(entity)
						isEntity = False
						entity = {}
				else:
					if tempWord[1].startswith("B-protein") or tempWord[1].startswith("B-RNA") or tempWord[1].startswith("B-DNA"):
						if tempWord[1].startswith("B-protein"):
							classId = "e_1"
						elif tempWord[1].startswith("B-DNA"):
							classId = "e_2"
						else:
							classId = "e_3"
						entity = {}
						entity['classId']=classId
						word = tempWord[0]
						entity['offsets']=[{'start': len(tempString)}]
						entity['confidence']={'state': "", 'who': ['user:genia4er'], 'prob': 1.0000}
						isEntity = True
						if (is_title):
							entity['part']='s1h1'
						else:
							entity['part']='s2p1'
					elif tempWord[1].startswith("I-protein") or tempWord[1].startswith("I-RNA") or tempWord[1].startswith("I-DNA"):
						if word.endswith("("):
							word = word+tempWord[0]
						else:
							word = word+" "+tempWord[0]
					elif (tempWord[1].startswith("O") and isEntity):
						entity['offsets'][0]['text'] = word
						entities.append(entity)
						isEntity = False
						entity = {}
					if tempString.endswith('(') or tempString.endswith('['):
						tempString = tempString + tempWord[0]
					else:
						tempString = tempString + ' ' + tempWord[0]
		data_file.close()

	def createJSON(self):
		for key in self.json_content:
			cutoff = len(self.documents[key]['abstract'])
			for i in range(len(self.json_content[key]['entities'])):
				if self.json_content[key]['entities'][i]['offsets'][0]['start']>cutoff:
					self.json_content[key]['entities'][i]['part']='s2p1'
				else:
					self.json_content[key]['entities'][i]['part']='s1h1'
			f = open('/home/ashish/mthesis-ashish/resources/corpora/entity_recognition/jnlpba/anndoc/'+str(key)+'.ann.json', 'w')
			f.write(json.dumps(self.json_content[key], sort_keys=True, indent=2, separators=(',', ': ')))
			f.close()

	def writeHTML(self, key):
		title = self.documents.get(key)['abstract']
		abstr = self.documents.get(key)['text']

		doctype = "<!DOCTYPE html>\n"
		
		hashId = str(uuid.uuid4().hex) + ':' + key.rstrip()
	
		header = '<html id="'+ hashId +'" data-origid="'+key.rstrip() + '" class="anndoc" data-anndoc-version="2.0" lang="" xml:lang="" xmlns="http://www.w3.org/1999/xhtml">\n' \
				 "\t<head>\n" \
				 '\t\t<meta charset="UTF-8"/>\n' \
		 '\t\t<meta name="generator" content="org.rostlab.relna"/>\n' \
				 "\t\t<title>" + hashId + "</title>\n" \
				 "\t</head>\n"

		body = "\t<body>\n" \
			   "\t\t<article>\n" \
			   '\t\t\t<section data-type="title">\n' \
			   '\t\t\t\t<h2 id="s1h1">' + title + "</h2>\n" \
			   "\t\t\t</section>\n"

		abstract = 	'\t\t\t<section data-type="abstract">\n' \
					'\t\t\t\t<h3 id="s2h1">' \
				   	'Abstract' \
				   	'</h3>\n' \
					'\t\t\t\t<div class="content">\n'

		content = '\t\t\t\t\t<p id = "s2p1">'

		content_close = '</p>\n'

		abstract_close = "\t\t\t\t</div>\n\t\t\t</section>\n"

		body_close = "\t\t</article>\n" \
					 "\t</body>\n"

		html_close = "</html>"

		html_file = open('/home/ashish/mthesis-ashish/resources/corpora/entity_recognition/jnlpba/anndoc/'+str(key).rstrip()+'.html', 'w')
		html_file.write(doctype)
		html_file.write(header)
		html_file.write(body)
		html_file.write(abstract)
		html_file.write(content)
		html_file.write(abstr)
		html_file.write(content_close)
		html_file.write(abstract_close)
		html_file.write(body_close)
		html_file.write(html_close)
		return


	def convert_to_html(self):
		for pmid in self.documents.keys():
			self.writeHTML(pmid)

	def print_documents(self):
		for pmid  in self.documents.keys():
			print pmid, self.documents[pmid]

tagtog = TagTogReader('/home/ashish/mthesis-ashish/resources/corpora/entity_recognition/jnlpba/iob/train/Genia4ERtask2.iob2')
tagtog.parse()
tagtog.convert_to_html()
tagtog.createJSON()
