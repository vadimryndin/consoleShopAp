package pl.coherentsolutions.service.util.configreader.impl;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import pl.coherentsolutions.service.exception.ConfigReaderException;
import pl.coherentsolutions.core.sorting.Sorting;
import pl.coherentsolutions.core.sorting.SortingCriterion;
import pl.coherentsolutions.service.util.configreader.ConfigReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SortingConfigReaderImpl implements ConfigReader {
    @Override
    public List<SortingCriterion> readConfig(String filePath) throws ConfigReaderException {
        List<SortingCriterion> sortingCriteria = new ArrayList<>();
        try {
            File xmlFile = new File(filePath);
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.parse(xmlFile);
            NodeList nodeList = document.getElementsByTagName("criterion");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                NamedNodeMap nodeAttributes = node.getAttributes();

                String criterion = nodeAttributes.getNamedItem("criterion").getNodeValue();
                Sorting sorting = Sorting.valueOf(nodeAttributes.getNamedItem("sorting").getNodeValue());

                sortingCriteria.add(new SortingCriterion(criterion, sorting));
            }
        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new ConfigReaderException("Error reading XML file config: " + e.getMessage(), e);
        }

        return sortingCriteria;
    }
}
