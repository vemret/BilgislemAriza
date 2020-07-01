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
import com.google.firebase.auth.FirebaseAuth


class SifreDialogFragment : DialogFragment() {

    lateinit var emailEt: EditText
    lateinit var myContext: FragmentActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_sifre_dialog, container, false)

        myContext= activity!!
        emailEt=view.findViewById(R.id.etSifreSifirla)

        var btnIptal=view.findViewById<Button>(R.id.btnSifreIptal)
        btnIptal.setOnClickListener {
            dialog.dismiss()
        }

        var btnGonder=view.findViewById<Button>(R.id.btnSifreGonder)
        btnGonder.setOnClickListener {

            FirebaseAuth.getInstance().sendPasswordResetEmail(emailEt.text.toString())
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful){
                            Toast.makeText(myContext,"Şifre sıfırlama mailiniz gönderildi!",Toast.LENGTH_SHORT).show()
                            dialog.dismiss()
                        }else{
                            Toast.makeText(myContext,"Oopss!Hata Oluştu! :(  "+task.exception?.message,Toast.LENGTH_SHORT).show()
                        }
                    }

        }
        return view
    }

}
