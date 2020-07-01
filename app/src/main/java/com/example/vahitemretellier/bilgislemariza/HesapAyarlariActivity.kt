package com.example.vahitemretellier.bilgislemariza

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.vahitemretellier.bilgislemariza.model.SohbetMesaj
import com.example.vahitemretellier.bilgislemariza.model.SohbetOdasi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_hesap_ayarlari.*
import kotlinx.android.synthetic.main.activity_hesap_ayarlari.button
import java.text.SimpleDateFormat
import java.util.*

//import kotlinx.android.synthetic.main.activity_sohbet_odasi.*

class HesapAyarlariActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hesap_ayarlari)

        button.setOnClickListener {
            //  if (!etNewMessage.text.toString().equals("")){
            // var yazilanMesaj=newMessage.text.toString()

            var ref =FirebaseDatabase.getInstance().reference

            var sohbetOdasiID=ref.child("sohbet_odasi").push().key

            var yeniSohbetOdasi= SohbetOdasi()

            yeniSohbetOdasi.oda_seviye="1"
            yeniSohbetOdasi.sohbetOdasi_id=sohbetOdasiID

            ref.child("sohbet_odasi").child(sohbetOdasiID!!).setValue(yeniSohbetOdasi)



            var mesajId=ref.child("sohbet_odasi").push().key

            var karsilamaMesaji=SohbetMesaj()
            karsilamaMesaji.mesajlar="Sohbet odasina hos geldiniz"
            karsilamaMesaji.timestamp=getMesajTarihi()

            ref.child("sohbet_odasi")
                    .child(sohbetOdasiID)
                    .child("sohbet_odasi_mesaj")
                    .child(mesajId!!)
                    .setValue(karsilamaMesaji)

        }
    }

    private fun getMesajTarihi(): String? {
        var sdf= SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale("tr"))
        return sdf.format(Date())
    }







}