package com.company

import java.io.Serializable

data class Member(var type : String, var name : String, var addOne : String?, var addTwo : String?) : Serializable {

    var contacts : List<Contact> = ArrayList()

    var chosenContact : Contact? = null

    override fun toString(): String {
        return this.name
    }
}