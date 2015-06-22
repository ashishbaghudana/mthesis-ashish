from bs4 import BeautifulSoup as bs
import requests
import xml.etree.ElementTree as ET

class Pubmed(object):
	def __init__(self, pmid):
		self.pmid = pmid
		self.url = "http://www.ncbi.nlm.nih.gov/pubmed/"+str(self.pmid)
		self.url_xml = "http://eutils.ncbi.nlm.nih.gov/entrez/eutils/esummary.fcgi?db=pubmed&id="+str(self.pmid)
		try:
			r = requests.get(url)
			r_xml = requests.get()
			data = r.text
			soup = bs(data)
			self.abstract = soup.find("div", {"class": "abstr"})
			if (self.abstract):
				self.valid = True
				self.abstract = self.abstract.getText()
			else:
				self.valid = False
			tree = ET.parse()
		except ConnectionError:
