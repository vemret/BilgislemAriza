package com.example.vahitemretellier.bilgislemariza

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.vahitemretellier.bilgislemariza.dialogs.OnaymailiFragment
import com.example.vahitemretellier.bilgislemariza.dialogs.SifreDialogFragment
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    lateinit var mAuthStateListener : FirebaseAuth.AuthStateListener //kullanıcının giriş cıkıs yapmasını kontrol eden interface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initMyAuthStateListener()
        //Gris activity e gider
        tvKayit.setOnClickListener {
            var intent=Intent(this,GirisActivity::class.java)
            startActivity(intent)
        }

        tvTekrarOnayMaili.setOnClickListener {
            var dialogGoster= OnaymailiFragment()
            dialogGoster.show(supportFragmentManager,"gosterdialog")
        }

        tvTekrarSifre.setOnClickListener {
            var dialogSifreGoster= SifreDialogFragment()
            dialogSifreGoster.show(supportFragmentManager,"gosterdalogsifre")

        }

        btnLogin.setOnClickListener {
            if (etMail.text.isNotEmpty() && etPassword.text.isNotEmpty()){
                progressBarVisible()  //progressbar gorunur oldu

                FirebaseAuth.getInstance().signInWithEmailAndPassword(etMail.text.toString(),etPassword.text.toString())
                        .addOnCompleteListener(object :OnCompleteListener<AuthResult>{
                            override fun onComplete(p0: Task<AuthResult>) { //31.satır oluştugunda burası gerceklesecek

                                if (p0.isSuccessful){
                                    progressBarInvisible()
                                   // Toast.makeText(this@LoginActivity,"Giriş Başarıyla Gerçekleştirildi! :)  ["+FirebaseAuth.getInstance().currentUser?.email+"]",Toast.LENGTH_SHORT).show()
                                    if (!p0.result.user.isEmailVerified){
                                        FirebaseAuth.getInstance().signOut() //kullanıcı sistemden atıldı
                                    }
                                }else{
                                    progressBarInvisible()

                                    Toast.makeText(this@LoginActivity,"Hatalı Giriş!: "+p0.exception?.message,Toast.LENGTH_SHORT).show()
                                }
                            }
                        })
            } else{
                Toast.makeText(this@LoginActivity,"Boş Alanları Doldurun!!",Toast.LENGTH_SHORT).show()
            }

        }
    }

    //progressbarın gorunur olup olmaması icin yazılmıs fonksyonlar
    private fun progressBarVisible(){
        progressBarLogin.visibility=View.VISIBLE
    }
    private fun progressBarInvisible(){
        progressBarLogin.visibility=View.INVISIBLE
    }

    private fun initMyAuthStateListener(){
        mAuthStateListener = object : FirebaseAuth.AuthStateListener {
            override fun onAuthStateChanged(p0: FirebaseAuth) {
                var kullanici= p0.currentUser
                if (kullanici != null){
                    if (kullanici.isEmailVerified){
                        Toast.makeText(this@LoginActivity,"Giriş başarı ile gerçekleştirildi!!",Toast.LENGTH_SHORT).show()
                        var intent=Intent(this@LoginActivity,MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }else{
                        Toast.makeText(this@LoginActivity,"Onay mailinizi aktifleştirmeden giriş yapamazsınız!!",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        FirebaseAuth.getInstance().addAuthStateListener(mAuthStateListener)
    }

    override fun onStop() {
        super.onStop()
        FirebaseAuth.getInstance().removeAuthStateListener(mAuthStateListener)
    }
}
