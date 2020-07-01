package com.example.vahitemretellier.bilgislemariza.adapters

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.vahitemretellier.bilgislemariza.R
import com.example.vahitemretellier.bilgislemariza.model.SohbetMesaj
import kotlinx.android.synthetic.main.tek_satir_mesaj_layout.view.*

class SohbetMesajRecyclerviewAdepter(context: Context,tumMesajlar:ArrayList<SohbetMesaj>): RecyclerView.Adapter<SohbetMesajRecyclerviewAdepter.SohbetMesajViewHolder>() {

    var myContext=context
    var myTumMesajlar=tumMesajlar

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): SohbetMesajViewHolder {
        var inflater=LayoutInflater.from(myContext)
        var view=inflater.inflate(R.layout.tek_satir_mesaj_layout,parent,false)

        return SohbetMesajViewHolder(view)
    }

    override fun getItemCount(): Int {
        return myTumMesajlar.size
    }

    override fun onBindViewHolder(holder: SohbetMesajViewHolder?, position: Int) {
        var oanKiMesaj = myTumMesajlar.get(position)
        holder?.setData(oanKiMesaj,position)
    }


    inner class SohbetMesajViewHolder(itemView:View?):RecyclerView.ViewHolder(itemView){

        var tumLayout=itemView as ConstraintLayout
        var mesaj=tumLayout.tvMesaj
        var isim=tumLayout.tvMesajName
        var tarih=tumLayout.tvMesajTarih
        var birim=tumLayout.tvMesajBirim


        fun setData(oanKiMesaj: SohbetMesaj, position: Int) {

            mesaj.text=oanKiMesaj.mesajlar
            isim.text=oanKiMesaj.isim
            tarih.text=oanKiMesaj.timestamp
            birim.text=oanKiMesaj.birim

        }

    }

}