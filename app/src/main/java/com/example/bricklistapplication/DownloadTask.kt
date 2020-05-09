package com.example.bricklistapplication

import android.webkit.URLUtil
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL


class DownloadTask(downloadUrl: String, fileName: String, filePath: File?) {
    private var downloadUrl: String? = downloadUrl
    private var downloadedFilePath : String? = filePath.toString()
    private var downloadFileName : String? = fileName

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
                input = connection.getInputStream()
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
}