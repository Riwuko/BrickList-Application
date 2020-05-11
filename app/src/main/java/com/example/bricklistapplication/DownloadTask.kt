package com.example.bricklistapplication

import android.webkit.URLUtil
import java.io.*
import java.net.HttpURLConnection
import java.net.URL


class DownloadTask() {
    private var downloadUrl: String? = null
    private var downloadedFilePath : String? = null
    private var downloadFileName : String? = null

    constructor(url: String, fileName: String, filePath: String) : this() {
        downloadUrl = url
        downloadedFilePath = filePath
        downloadFileName = fileName
    }

    fun downloadFromURL() : Boolean
    {
        var success = true
        val thread = Thread(Runnable {
            var input: InputStream? = null
            var output: OutputStream? = null
            if (URLUtil.isValidUrl(downloadUrl)) {
                val url = URL(downloadUrl)
                val connection = url.openConnection() as HttpURLConnection
                connection.connectTimeout = 500
                connection.connect()
                println("file name to "+ downloadFileName)
                println("url to "+url.toExternalForm())

                if (connection.responseCode != HttpURLConnection.HTTP_OK) {
                    success = false
                    return@Runnable
                }

                println("pathname to $downloadedFilePath$downloadFileName")
                input = connection.inputStream
                output = FileOutputStream(downloadedFilePath+downloadFileName)
                println("output file to $output")
                println(input)
                val data = ByteArray(4096)
                var count: Int
                count = input.read(data)
                while (count != -1) {
                    output.write(data, 0, count)
                    count = input.read(data)
                }
                output.close()
                input.close()
                connection.disconnect()
            }
            else{
                success = false
                return@Runnable
            }

        })
        thread.start()
        thread.join()
        return success

    }

    fun urlConnect(url:URL): HttpURLConnection {
        var connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.connect()
        return connection
    }

    fun downloadImage(urlList:ArrayList<String>):ByteArray? {
        var image : ByteArray? = null
        var connection : HttpURLConnection? = null
        val thread = Thread(Runnable {
            for (url in urlList) {
               connection = urlConnect(URL(url))
                if (connection!!.responseCode == HttpURLConnection.HTTP_OK) {
                    break
                } else image = null
            }
            val inputStream = BufferedInputStream(connection!!.inputStream)
            val outputStream = ByteArrayOutputStream();
            val data = ByteArray(50)
            var current = inputStream.read(data)
            while (current != -1) {
                outputStream.write(data, 0, current)
                current = inputStream.read(data)
            }
            image = outputStream.toByteArray()
            })
            thread.start()
            thread.join()
        return image
    }
}