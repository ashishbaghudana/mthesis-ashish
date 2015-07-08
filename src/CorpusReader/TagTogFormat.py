__author__ = 'Ashish'
from nltk.tokenize import sent_tokenize
import json

class TagTogReader(object):
    def __init__(self, file_location):
        self.file_location = file_location
        self.documents = {}
        self.symbols = [',', '.', ')', ':', ';', '[', ']']
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
                    json_single['annotatable']['parts']={}
                    json_single['anncomplete']=False
                    json_single['sources']=[]
                    json_single['sources'].append({"name": "MEDLINE", "id": pmid.rstrip(), "url": "null"})
                    json_single['entities']=entities
                    json_single['relations']=[]
                    self.json_content[pmid.rstrip()]=json_single
                entities = []
                pmid = line.split(":")[1]
                tempString = ""
            elif str(line)=='\n':
                continue
            else:
                tempWord = str(line).split('\t')
                if str(line).split()[0] in self.symbols or tempString.endswith('('):
                    tempString = tempString + tempWord[0]
                else:
                    if tempWord[1].startswith("B-"):
                        classId = "e_"+str(id)
                        id += 1
                        entity = {}
                        entity['classId']=classId
                        entity['offsets']=[{'start': len(tempString), 'text': tempWord[0]}]
                        entity['confidence']={'state': "", 'who': ['genia4er'], 'prob': 1.0000}
                        entities.append(entity)
                    tempString = tempString + ' ' + tempWord[0]
        data_file.close()

    def createJSON(self):
        for key in self.json_content:
            cutoff = len(self.documents[key]['abstract'])
            for i in range(len(self.json_content[key]['entities'])):
                if self.json_content[key]['entities'][i]['offsets'][0]['start']>cutoff:
                    self.json_content[key]['entities'][i]['part']='s2h1'
                else:
                    self.json_content[key]['entities'][i]['part']='s1h1'
            f = open(str(key)+'.json', 'w')
            f.write(json.dumps(self.json_content[key]))
            f.close()

    def writeHTML(self, key):
        title = self.documents.get(key)['abstract']
        abstr = self.documents.get(key)['text']

        doctype = "<!DOCTYPE html>\n"

        header = "<html>\n" \
                 "\t<head>\n" \
                 "\t\t<title>MEDLINE: " + key.rstrip() + "</title>\n" \
                 "\t</head>\n"

        body = "\t<body>\n" \
               "\t\t<article>\n" \
               '\t\t\t<section data-type="title">\n' \
               '\t\t\t\t<h2 id="s1h1">' + title + "</h2>\n" \
               "\t\t\t</section>\n"

        abstract = '\t\t\t<section data-type="abstract">\n' \
                   '\t\t\t\t<h3 id="s2h1">' \
                   'Abstract' \
                   '</h3>\n'

        content = '\t\t\t\t<p id = "s2s1p1">'

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

tagtog = TagTogReader('D:\\Genia4ERtask2.iob2')
tagtog.parse()
#tagtog.convert_to_html()
tagtog.createJSON()