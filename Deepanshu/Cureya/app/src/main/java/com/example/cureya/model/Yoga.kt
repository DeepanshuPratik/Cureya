package com.example.cureya.model

class Yoga {

    var title: String? = null
    var description: String? = null
    var steps: String? = null
    var imgUrl: String? = null

    // Empty constructor for firebase serialization
    constructor()

    constructor(title: String?, description: String?,
                steps: String?, imgUrl: String?) {
        this.title = title
        this.description = description
        this.imgUrl = imgUrl
        this.steps = steps
    }
}