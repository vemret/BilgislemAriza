package com.example.vahitemretellier.bilgislemariza

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.vahitemretellier.bilgislemariza.model.Kullanici
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_giris.*

class GirisActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_giris)

        btnKayit.setOnClickListener {
            if (etMail.text.isNotEmpty() && etPassword.text.isNotEmpty() && etPasswor2.text.isNotEmpty() && etBirim.text.isNotEmpty() && etUserName.text.isNotEmpty()){

                if (etPassword.text.toString().equals(etPasswor2.text.toString())){
                    newMember(etMail.text.toString(),etPassword.text.toString(), etUserName.text.toString(), etBirim.text.toString())
                }else{
                    Toast.makeText(this,"şifreler uyuşmuyor!!",Toast.LENGTH_SHORT).show()
                }

            }else{
                Toast.makeText(this,"Boş alanları doldurun!!",Toast.LENGTH_SHORT).show()
            }
        }
    }



    private fun newMember(mail: String, password: String, ad: String, depbirim: String ) {

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(mail,password)
                .addOnCompleteListener(object: OnCompleteListener<AuthResult>{
                    override fun onComplete(p0: Task<AuthResult>) {
                        if (p0.isSuccessful) {
                            progressBarInvisible()
                            Toast.makeText(this@GirisActivity, "İşleminiz başarıyla gerçekleşti!! ["+FirebaseAuth.getInstance().currentUser?.uid, Toast.LENGTH_SHORT).show()
                            onayMaili()//onay  maili gonderiyor

                            var databaseklenecekuser= Kullanici()
                            databaseklenecekuser.isim=etUserName.text.toString()
                            databaseklenecekuser.birim=etBirim.text.toString()
                            databaseklenecekuser.seviye="1"
                            databaseklenecekuser.kullanici_id=FirebaseAuth.getInstance().currentUser?.uid

                                FirebaseDatabase.getInstance().reference
                                        .child("kullanici")
                                        .child(FirebaseAuth.getInstance().currentUser!!.uid)
                                        .setValue(databaseklenecekuser).addOnCompleteListener { task ->

                                            if (task.isSuccessful){
                                                Toast.makeText(this@GirisActivity, "İşleminiz başarıyla gerçekleşti!! ["+FirebaseAuth.getInstance().currentUser?.uid, Toast.LENGTH_SHORT).show()
                                                FirebaseAuth.getInstance().signOut()//mail onaylanmadığı için giriş yapılmaması için
                                                girisToLogin()//login sayfasına yonlendirdi

                                            }
                                        }



                        }else{
                            progressBarInvisible()
                            Toast.makeText(this@GirisActivity, "İşleminiz yapılırken hata oluştu! :(  ["+p0.exception?.message+"]", Toast.LENGTH_SHORT).show()
                        }
                    }
                } )
    }
    private fun onayMaili(){
        var kullanici=FirebaseAuth.getInstance().currentUser
        if (kullanici != null){
            kullanici.sendEmailVerification()
                    .addOnCompleteListener(object : OnCompleteListener<Void>{
                        override fun onComplete(p0: Task<Void>) {
                            if (p0.isSuccessful){

                                Toast.makeText(this@GirisActivity, "Mail kutunuzu kontrol edin ve onay mailini aktifleştrin!", Toast.LENGTH_SHORT).show()
                            }else{

                                Toast.makeText(this@GirisActivity, "Onay maili gönderilirken hata oluştu!["+p0.exception?.message+"]", Toast.LENGTH_SHORT).show()
                            }
                        }
                    })
        }
    }
    private fun progressBarVisible(){
        progressBar.visibility=View.VISIBLE
    }
    private fun progressBarInvisible(){
        progressBar.visibility=View.INVISIBLE
    }
    private fun girisToLogin(){
        var intent= Intent(this@GirisActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
