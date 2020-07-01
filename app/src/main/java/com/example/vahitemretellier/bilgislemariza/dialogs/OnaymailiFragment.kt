package com.example.vahitemretellier.bilgislemariza.dialogs

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.vahitemretellier.bilgislemariza.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth

class OnaymailiFragment : DialogFragment() {
    lateinit var emailEt:EditText
    lateinit var passwordEt:EditText
    lateinit var myContext:FragmentActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view=inflater.inflate(R.layout.fragment_onaymaili, container, false)

        emailEt=view.findViewById(R.id.etDialogMail)
        passwordEt=view.findViewById(R.id.etDialogPassword)
        myContext = activity!!

        var btnIptal=view.findViewById<Button>(R.id.btnDialogIptal)//butonu xmlden javaya donustur
        var btnGonder=view.findViewById<Button>(R.id.btnDialogGonder)
        btnIptal.setOnClickListener {
            dialog.dismiss()//kapatıyoruz
        }

        btnGonder.setOnClickListener {
            if (emailEt.text.toString().isNotEmpty() && passwordEt.text.toString().isNotEmpty()){
                girisVeOnayMailiTekrar(emailEt.text.toString(),passwordEt.text.toString())
            }else{

                Toast.makeText(myContext,"Lütfen boş alanları doldurun!",Toast.LENGTH_SHORT).show()
            }
        }
        return view
    }
    private fun girisVeOnayMailiTekrar(email: String, password: String) {

        var credential=EmailAuthProvider.getCredential(email,password)
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener {task ->
                    if (task.isSuccessful){
                        onayMailiTekrar()
                        dialog.dismiss()
                    }else{
                        Toast.makeText(myContext,"Email veya Şifre hatalı!",Toast.LENGTH_SHORT).show()
                    }
                }
    }
    private fun onayMailiTekrar() {
        var kullanici=FirebaseAuth.getInstance().currentUser
        if (kullanici != null){
            kullanici.sendEmailVerification()
                    .addOnCompleteListener(object : OnCompleteListener<Void> {
                        override fun onComplete(p0: Task<Void>) {
                            if (p0.isSuccessful){

                                Toast.makeText(myContext, "Mail kutunuzu kontrol edin ve onay mailini aktifleştrin!", Toast.LENGTH_SHORT).show()
                            }else{

                                Toast.makeText(myContext, "Onay maili gönderilirken hata oluştu!["+p0.exception?.message+"]", Toast.LENGTH_SHORT).show()
                            }
                        }
                    })
        }
    }


}
