package com.example.vahitemretellier.bilgislemariza

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.vahitemretellier.bilgislemariza.model.Kullanici
import com.example.vahitemretellier.bilgislemariza.model.SohbetOdasi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var myAuthStateListener:FirebaseAuth.AuthStateListener
    var kullaniciseviye=0
    var kullanici = FirebaseAuth.getInstance().currentUser!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initAuthStateListener()
        initFCM()

        var kullanici = FirebaseAuth.getInstance().currentUser!!

        btnArizaBildir.setOnClickListener {
            var intent=Intent(this,SohbetOdasiActivity::class.java)
            startActivity(intent)

        }


    }

    private fun initFCM() {
        var tokenn=FirebaseInstanceId.getInstance().id
        tokenVeriTabaninaKaydett(tokenn)
    }

    private fun tokenVeriTabaninaKaydett(refreshedToken: String?){

        var ref= FirebaseDatabase.getInstance().reference
                .child("kullanici")
                .child(FirebaseAuth.getInstance().currentUser!!.uid)
                .child("mesaj_token")
                .setValue(refreshedToken)
    }


    private fun setKullaniciBilgileri() {

        var ref=FirebaseDatabase.getInstance().reference

        var kullanici = FirebaseAuth.getInstance().currentUser
        tvMail.text= kullanici?.email

        var sorgu=ref.child("kullanici").orderByKey().equalTo(kullanici?.uid)
        sorgu.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onDataChange(p0: DataSnapshot) {
                for (SingleSnapshot in p0!!.children){
                    var okunanKullaninici = SingleSnapshot.getValue(Kullanici::class.java)
                    tvName.text=okunanKullaninici?.isim
                    tvBirim.text=okunanKullaninici?.birim
                }
            }
        })

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.anamenu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when(item?.itemId){
            R.id.menuCikis -> {
                cikis()
                return true
            }
            R.id.menuHesapAyarlari ->{
                var intent=Intent(this,HesapAyarlariActivity::class.java)
                startActivity(intent)
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }









    private fun initAuthStateListener() {
        myAuthStateListener=object :FirebaseAuth.AuthStateListener{
            override fun onAuthStateChanged(p0: FirebaseAuth) {
                var kullannıcı=p0.currentUser

                if (kullannıcı!=null){

                }else{
                    var intent=Intent(this@MainActivity,LoginActivity::class.java)
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)

                    startActivity(intent)
                    finish()
                }
            }
        }
    }



    private fun cikis() {
        FirebaseAuth.getInstance().signOut()
    }

    override fun onResume() {
        super.onResume()
        userKontrol()
        setKullaniciBilgileri()
    }

    private fun userKontrol() {
        var kullanıcı=FirebaseAuth.getInstance().currentUser
        if (kullanıcı==null){
            var intent=Intent(this@MainActivity,LoginActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            finish()
        }
    }

    private fun kullaniciSeviyesiGetir(){
        var ref=FirebaseDatabase.getInstance().reference

        var sorgu=ref.child("kullanici").orderByKey().equalTo(FirebaseAuth.getInstance().currentUser?.uid)
        sorgu.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onDataChange(p0: DataSnapshot) {
                for (tekKayit in p0!!.children){
                    kullaniciseviye=tekKayit.getValue(Kullanici::class.java)?.seviye!!.toInt()
                }
            }
        })
    }

    override fun onStart() {
        super.onStart()
        FirebaseAuth.getInstance().addAuthStateListener(myAuthStateListener)
    }

    override fun onStop() {
        super.onStop()
        if (myAuthStateListener != null){
            FirebaseAuth.getInstance().removeAuthStateListener(myAuthStateListener)
        }
    }
}
