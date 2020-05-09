package com.example.bricklistapplication

import org.w3c.dom.Node
import org.xml.sax.InputSource
import java.io.File
import java.io.StringReader
import org.w3c.dom.Element
import javax.xml.parsers.DocumentBuilderFactory

class XMLHandler(filePath: String) {

    fun readXML(filePath: String) {
        val xmlFile = File(filePath)
        val dbFactory = DocumentBuilderFactory.newInstance()
        val dBuilder = dbFactory.newDocumentBuilder()
        val xmlInput = InputSource(StringReader(xmlFile.readText()))
        val doc = dBuilder.parse(xmlInput)

        val itemList = doc.getElementsByTagName("ITEM")

        for (temp in 0 until itemList.length) {

            val node = itemList.item(temp)
            System.out.println("\nCurrent Element :" + node.nodeName)
            if (node.nodeType == Node.ELEMENT_NODE) {
                val eElement = node as Element
                if (eElement.getElementsByTagName("ALTERNATE").item(0).textContent == "N") {

                    val itemTypeID = eElement.getElementsByTagName("ITEMTYPE").item(0).textContent
                    val quantityNeeded =
                        eElement.getElementsByTagName("QTY").item(0).textContent.toInt()
                    val itemID = eElement.getElementsByTagName("ITEMID").item(0).textContent
                    val colorID = eElement.getElementsByTagName("COLOR").item(0).textContent
                    println(itemTypeID)
                    println(quantityNeeded)
                    println(itemID)
                    println(colorID)
//                    val part = InventoryPart(inventoryID,itemTypeID,itemID,quantityNeeded,0,colorID,extras)
//                    DBHepler.insertInventoryPart(part)
//                    DBHepler.close()
                }
            }
        }
    }

}