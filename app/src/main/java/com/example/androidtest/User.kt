// User.kt 파일 내의 패키지 선언
package com.example.androidtest

import java.io.Serializable

class User(
    var id: String,
    var pw: String,
): Serializable {
    constructor(): this("","")
}
