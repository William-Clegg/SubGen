package com.company

import java.io.Serializable

data class Contact(var name : String, var email : String?, var phone : String?) : Serializable {

    override fun toString(): String {
        return this.name
    }
}