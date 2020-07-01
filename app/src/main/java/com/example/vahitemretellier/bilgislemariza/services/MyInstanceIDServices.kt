package com.example.vahitemretellier.bilgislemariza.services

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService

class MyInstanceIDServices : FirebaseInstanceIdService(){

    override fun onTokenRefresh() {
        var refreshedToken:String?= FirebaseInstanceId.getInstance().getToken()

        tokenVeriTabaninaKaydet(refreshedToken)
    }

    private fun tokenVeriTabaninaKaydet(refreshedToken: String?){

        var ref= FirebaseDatabase.getInstance().reference
                .child("kullanici")
                .child(FirebaseAuth.getInstance().currentUser!!.uid)
                .child("mesaj_token")
                .setValue(refreshedToken)
    }

}