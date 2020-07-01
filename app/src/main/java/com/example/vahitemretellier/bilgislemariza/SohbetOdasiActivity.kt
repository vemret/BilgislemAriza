package com.example.vahitemretellier.bilgislemariza

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import com.example.vahitemretellier.bilgislemariza.R.id.tekMesajRootLayout
import com.example.vahitemretellier.bilgislemariza.adapters.SohbetMesajRecyclerviewAdepter
import com.example.vahitemretellier.bilgislemariza.model.Kullanici
import com.example.vahitemretellier.bilgislemariza.model.SohbetMesaj
import com.google.android.gms.common.config.GservicesValue.init
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_sohbet_odasi.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashSet

class SohbetOdasiActivity : AppCompatActivity() {
    //lateinit var tumMesajlar:ArrayList<SohbetMesaj>
    var kullaniciseviye = 1
    //Firrebase
    var mAuthListener:FirebaseAuth.AuthStateListener? = null
    var sohbetOdaId:String=""
    var mMesajReferans : DatabaseReference?=null
    var tumMeajlar:ArrayList<SohbetMesaj>?=null
    var mesajIdSet: HashSet<String>?=null
    var myAdapter:SohbetMesajRecyclerviewAdepter?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sohbet_odasi)

        kullaniciSeviyesiGetir()
        //sohbetOdasiMesajlariGetir()

        //kullanıcının giriş vıkıc işlemlerini dinler
        baslatFirebaseAuthListener()

        sohbetOdasiBilgileri()



        init()

    }

    private fun init() {

      //  etNewMessage.setOnClickListener {
      //      rvMesajlar.smoothScrollToPosition(myAdapter!!.itemCount-1)
     //   }

        imgSentMessage.setOnClickListener {
            if (!etNewMessage.text.toString().equals("")){
                var yazilanMesaj=etNewMessage.text.toString()

                var kaydedilecekMesaj=SohbetMesaj()
                kaydedilecekMesaj.mesajlar=yazilanMesaj
                kaydedilecekMesaj.kullanici_id=FirebaseAuth.getInstance().currentUser?.uid
                kaydedilecekMesaj.timestamp=getMesajTarihi()

                var referans=FirebaseDatabase.getInstance().reference
                        .child("sohbet_odasi")
                        .child(sohbetOdaId).child("sohbet_odasi_mesaj")

                var yeniMesajId = referans.push().key
                referans.child(yeniMesajId!!)
                        .setValue(kaydedilecekMesaj)

                etNewMessage.setText("")
            }
        }
    }


    private fun getMesajTarihi(): String? {
        var sdf=SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale("tr"))
        return sdf.format(Date())
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

    private fun sohbetOdasiBilgileri() {
        sohbetOdaId="-LLmcig3_vVo_5I6q9IQ"
       // if (kullaniciseviye < 1) {
            baslatMesajListener()
        //}
    }


    private fun baslatMesajListener() {
        mMesajReferans=FirebaseDatabase.getInstance().getReference().child("sohbet_odasi").child(sohbetOdaId).child("sohbet_odasi_mesaj")

        mMesajReferans?.addValueEventListener(mValueEventListener)
    }

    var mValueEventListener:ValueEventListener=object : ValueEventListener{
        override fun onCancelled(p0: DatabaseError) {

        }

        override fun onDataChange(p0: DataSnapshot) {

            kullaniciSeviyesiGetir()

            if(kullaniciseviye == 0) {
                sohbetOdasiMesajlariGetir()
            }
        }
    }
    private fun sohbetOdasiMesajlariGetir() {

            if (tumMeajlar == null) {
                tumMeajlar = ArrayList<SohbetMesaj>()
                mesajIdSet = HashSet<String>()
            }
            mMesajReferans = FirebaseDatabase.getInstance().getReference()

            var sorgu = mMesajReferans?.child("sohbet_odasi")?.child(sohbetOdaId)?.child("sohbet_odasi_mesaj")!!
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {

                        }
                        override fun onDataChange(p0: DataSnapshot) {

                            for (tekMesaj in p0!!.children) {

                                var geciciMesaj = SohbetMesaj()
                                var kullaniciId = tekMesaj.getValue(SohbetMesaj::class.java)!!.kullanici_id

                                if (!mesajIdSet!!.contains(tekMesaj.key)) {

                                    mesajIdSet!!.add(tekMesaj.key!!)

                                    if (kullaniciId != null) {
                                        geciciMesaj.mesajlar = tekMesaj.getValue(SohbetMesaj::class.java)!!.mesajlar
                                        geciciMesaj.kullanici_id = tekMesaj.getValue(SohbetMesaj::class.java)!!.kullanici_id
                                        geciciMesaj.timestamp = tekMesaj.getValue(SohbetMesaj::class.java)!!.timestamp

                                        var kullaniciDetaylari = mMesajReferans?.child("kullanici")?.orderByKey()?.equalTo(kullaniciId)
                                        kullaniciDetaylari?.addListenerForSingleValueEvent(object : ValueEventListener {
                                            override fun onCancelled(p0: DatabaseError) {

                                            }
                                            override fun onDataChange(p0: DataSnapshot) {
                                                if (p0?.exists()!!) {
                                                    var bulunanKullanici = p0?.children?.iterator()?.next()
                                                    geciciMesaj.isim = bulunanKullanici?.getValue(Kullanici::class.java)?.isim
                                                    geciciMesaj.birim = bulunanKullanici?.getValue(Kullanici::class.java)?.birim
                                                }
                                            }

                                        })
                                        tumMeajlar?.add(geciciMesaj)
                                        myAdapter?.notifyDataSetChanged()
                                        rvMesajlar.scrollToPosition(myAdapter!!.itemCount - 1)
                                    }
                                    else {
                                        geciciMesaj.mesajlar = tekMesaj.getValue(SohbetMesaj::class.java)!!.mesajlar
                                        geciciMesaj.isim = ""
                                        geciciMesaj.birim = ""
                                        geciciMesaj.timestamp = tekMesaj.getValue(SohbetMesaj::class.java)!!.timestamp
                                        tumMeajlar?.add(geciciMesaj)
                                    }
                                }
                            }
                        }

                    })

            if (myAdapter == null) {
                initMesajListesi()
            }
       // }

    }
    private fun initMesajListesi() {
        myAdapter= SohbetMesajRecyclerviewAdepter(this,tumMeajlar!!)
        rvMesajlar.adapter=myAdapter
        rvMesajlar.layoutManager=LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        rvMesajlar.scrollToPosition(myAdapter?.itemCount!! -1)
    }

    private fun baslatFirebaseAuthListener() {
        mAuthListener=object : FirebaseAuth.AuthStateListener{
            override fun onAuthStateChanged(p0: FirebaseAuth) {

                var kullanici=p0.currentUser
                if (kullanici == null){

                    var intent=Intent(this@SohbetOdasiActivity,LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }



    override fun onStart() {
        super.onStart()
        FirebaseAuth.getInstance().addAuthStateListener(mAuthListener!!)
    }

    override fun onStop() {
        super.onStop()
        if (mAuthListener!=null){
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthListener!!)
        }
    }

    override fun onResume() {
        super.onResume()
        kullaniciKontrol()
    }

    private fun kullaniciKontrol() {
        var kullanici=FirebaseAuth.getInstance().currentUser
        if (kullanici==null){
            var intent=Intent(this@SohbetOdasiActivity,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }




}
/*private fun sohbetOdasiMesajlariGetir() {
        tumMesajlar=ArrayList<SohbetMesaj>()
        var sohbetOdaId="arizabildir"

        var ref=FirebaseDatabase.getInstance().reference

        var sorgu=ref.child("sohbet_odasi").child(sohbetOdaId).child("sohbet_odasi_mesaj")
                .addListenerForSingleValueEvent(object :ValueEventListener{
                    override fun onCancelled(p0: DatabaseError) {
                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        for (mesaj in p0!!.children){
                            var eklenecekMesaj=SohbetMesaj()
                            var kullaniciID=mesaj.getValue(SohbetMesaj::class.java)?.kullanici_id

                            if (kullaniciID!=null){
                                eklenecekMesaj.kullanici_id=kullaniciID
                                eklenecekMesaj.mesajlar=mesaj.getValue(SohbetMesaj::class.java)?.mesajlar
                                eklenecekMesaj.timestamp=mesaj.getValue(SohbetMesaj::class.java)?.timestamp
                                eklenecekMesaj.birim=mesaj.getValue(SohbetMesaj::class.java)?.birim
                                tumMesajlar.add(eklenecekMesaj)
                            }else{
                                eklenecekMesaj.mesajlar=mesaj.getValue(SohbetMesaj::class.java)?.mesajlar
                                eklenecekMesaj.timestamp=mesaj.getValue(SohbetMesaj::class.java)?.timestamp
                                tumMesajlar.add(eklenecekMesaj)
                            }
                        }
                    }

                })

    }*/