package com.example.bricklistapplication

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.bricklistapplication.Model.SinglePackageElement
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.xml.sax.InputSource
import java.io.File
import java.io.StringReader
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.Transformer
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

class XMLHandler() {

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

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun exportXML(partsList: ArrayList<SinglePackageElement>, filePath: String): Boolean {
        try {
            val docBuilder: DocumentBuilder =
                DocumentBuilderFactory.newInstance().newDocumentBuilder()
            val doc: Document = docBuilder.newDocument()
            val rootElement: Element = doc.createElement("INVENTORY")
            for (part in partsList) {
                if ((part.getQuantityInStore()!! < part.getQuantityInSet()!!) and (part.getElementID()!=0)) {
                    val item: Element = doc.createElement("ITEM")
                    val itemType: Element = doc.createElement("ITEMTYPE")
                    itemType.appendChild(doc.createTextNode(part.getElementTypeCode()!!))
                    item.appendChild(itemType)

                    val itemID: Element = doc.createElement("ITEMID")
                    itemID.appendChild(doc.createTextNode(part.getElementCode()))
                    item.appendChild(itemID)

                    val color: Element = doc.createElement("COLOR")
                    color.appendChild(doc.createTextNode(part.getElementColorID().toString()))
                    item.appendChild(color)

                    val qtyFilled: Element = doc.createElement("QTYFILLED")
                    qtyFilled.appendChild(
                        doc.createTextNode(
                            (part.getQuantityInSet()!!
                                .toInt() - part.getQuantityInStore()!!).toString()
                        )
                    )
                    item.appendChild(qtyFilled)
                    rootElement.appendChild(item)
                }
            }
            doc.appendChild(rootElement)
            val transformer: Transformer = TransformerFactory.newInstance().newTransformer()
            transformer.setOutputProperty(OutputKeys.INDENT, "yes")
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2")
            transformer.transform(DOMSource(doc), StreamResult(filePath))
            return true

        }catch(e:Exception){
            Log.e("XML", e.toString())
        }
        return false
    }

}