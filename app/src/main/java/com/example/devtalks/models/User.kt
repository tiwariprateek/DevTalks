package com.example.devtalks.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class User(val uid:String,val usename:String,val profileImageUrl:String):Parcelable{
    constructor():this("","","")}