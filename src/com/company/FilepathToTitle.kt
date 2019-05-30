package com.company

class FilepathToTitle {

    fun toTitle(filePath : String) : String {

        val title = filePath.substring(filePath.lastIndexOf("\\") + 1, filePath.lastIndexOf("."))
        return title
    }
}