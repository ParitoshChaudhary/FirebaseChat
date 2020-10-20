package com.example.firebasechat.models

class Users() {
    var display_name: String? = null
    var user_status: String? = null
    var profile_image: String? = null
    var thumb_img: String? = null
    var user_id: String? = null

    constructor(display_name: String, user_status: String, profile_img: String, thumb_img: String,
                user_id: String): this(){
        this.display_name = display_name
        this.user_status = user_status
        this.profile_image = profile_img
        this.thumb_img = thumb_img
        this.user_id = user_id
    }

}