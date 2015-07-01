__author__ = 'Ashish'
from nltk.tokenize import sent_tokenize
import json

class TagTogReader(object):
    def __init__(self, file_location):
        self.file_location = file_location
        self.documents = {}
        self.symbols = [',', '.', ')', ':', ';', '[', ']']

    def parse(self):
        data_file = open(self.file_location)
        tempString = ""
        for line in data_file:
            if str(line).strip().startswith("###MEDLINE"):
                if tempString!="":
                    self.documents[pmid.rstrip()]=tempString.lstrip().rstrip()
                pmid = line.split(":")[1]
                tempString = ""
            elif str(line)=='\n':
                continue
            else:
                tempWord = str(line).split('\t')
                if str(line).split()[0] in self.symbols or tempString.endswith('('):
                    tempString = tempString + tempWord[0]
                else:
                    tempString = tempString + ' ' + tempWord[0]
        data_file.close()

    def createJSONContent(self):

        self.json={}
        self.json['annotatable']={}
        self.json['annotatable']['parts']={}
        self.json['anncomplete']=False
        self.json['sources']=[]
        self.json['entities']=[]
        self.json['relations']=[]

        data_file = open(self.file_location)
        entities = []
        for line in data_file:
            if str(line).rstrip().lstrip().startswith("###MEDLINE"):
                self.json['sources'].append({"name": "MEDLINE", "id": pmid, "url": "null"})
                for entity in entities:
                    self.json['entities'].append(entity)
                pmid = line.split(":")[1]

        return

    def writeHTML(self, key, data):
        sentences = sent_tokenize(data)

        doctype = "<!DOCTYPE html>\n"

        header = "<html>\n" \
                 "\t<head>\n" \
                 "\t\t<title>MEDLINE: " + key.rstrip() + "</title>\n" \
                 "\t</head>\n"

        body = "\t<body>\n" \
               "\t\t<article>\n" \
               '\t\t\t<section data-type="title">\n' \
               '\t\t\t\t<h2 id="s1h1">' + sentences[0] + "</h2>\n" \
               "\t\t\t</section>\n"

        abstract = '\t\t\t<section data-type="abstract">\n' \
                   '\t\t\t\t<h3 id="s2h1">' \
                   'Abstract' \
                   '</h3>\n'

        content = '\t\t\t\t<p id = "s2s1p1">'

        text = ' '.join(sentences[1:])

        content_close = '<\p>\n'

        abstract_close = "\t\t\t</section>\n"

        body_close = "\t\t</article>\n" \
                     "\t</body>\n"

        html_close = "</html>"

        html_file = open(str(key).rstrip()+'.html', 'w')
        html_file.write(doctype)
        html_file.write(header)
        html_file.write(body)
        html_file.write(abstract)
        html_file.write(content)
        html_file.write(text)
        html_file.write(content_close)
        html_file.write(abstract_close)
        html_file.write(body_close)
        html_file.write(html_close)

    def convert_to_html(self):
        for pmid in self.documents.keys():
            self.writeHTML(pmid, self.documents[pmid])

    def print_documents(self):
        for pmid  in self.documents.keys():
            print pmid, self.documents[pmid]

tagtog = TagTogReader('D:\\Genia4ERtask2.iob2')
tagtog.parse()
tagtog.convert_to_html()