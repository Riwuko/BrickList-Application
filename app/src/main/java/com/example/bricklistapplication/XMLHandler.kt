package com.example.bricklistapplication

import org.w3c.dom.Element
import org.w3c.dom.Node
import org.xml.sax.InputSource
import java.io.File
import java.io.StringReader
import javax.xml.parsers.DocumentBuilderFactory

class XMLHandler(filePath: String) {

    fun readXML(filePath: String): ArrayList<ArrayList<String>> {
        val xmlFile = File(filePath)
        val dbFactory = DocumentBuilderFactory.newInstance()
        val dBuilder = dbFactory.newDocumentBuilder()
        val xmlInput = InputSource(StringReader(xmlFile.readText()))
        val doc = dBuilder.parse(xmlInput)

        val itemList = doc.getElementsByTagName("ITEM")
        var allItemsFeatures: ArrayList<ArrayList<String>> = ArrayList()

        for (temp in 0 until itemList.length) {
            val node = itemList.item(temp)
            if (node.nodeType == Node.ELEMENT_NODE) {
                val singleItem = node as Element
                if (singleItem.getElementsByTagName("ALTERNATE").item(0).textContent == "N") {
                    val singleItemFeature: ArrayList<String> = ArrayList()
                    singleItemFeature.add(singleItem.getElementsByTagName("ITEMTYPE").item(0).textContent)
                    singleItemFeature.add(singleItem.getElementsByTagName("QTY").item(0).textContent)
                    singleItemFeature.add(singleItem.getElementsByTagName("ITEMID").item(0).textContent)
                    singleItemFeature.add(singleItem.getElementsByTagName("COLOR").item(0).textContent)

                    allItemsFeatures.add(singleItemFeature)
                }
            }
        }
        return allItemsFeatures
    }

}