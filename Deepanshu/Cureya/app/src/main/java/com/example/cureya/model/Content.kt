package com.example.cureya.model

class Content {

    var title: String? = null
    var duration: String? = null
    var thumbnailUrl: String? = null
    var contentUrl: String? = null

    // Empty constructor for firebase serialization
    constructor()

    constructor(title: String?, duration: String?,
        thumbnailUrl: String?, contentUrl: String?) {
        this.title = title
        this.duration = duration
        this.thumbnailUrl = thumbnailUrl
        this.contentUrl = contentUrl
    }
}

