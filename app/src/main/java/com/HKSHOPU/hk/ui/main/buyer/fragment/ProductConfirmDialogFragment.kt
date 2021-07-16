package com.HKSHOPU.hk.ui.main.buyer.fragment

import android.content.Intent
import android.graphics.drawable.InsetDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView

import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.HKSHOPU.hk.R

import com.HKSHOPU.hk.ui.user.activity.LoginActivity



class ProductConfirmDialogFragment(var orderNumber:String): DialogFragment(), View.OnClickListener {

    var signal : Boolean = false
    var order_number = orderNumber
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.Theme_App_Dialog_ShrinkScreen)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.dialog_fragment_productconfirm, container, false)
        val inset = InsetDrawable(
            ContextCompat.getDrawable(
                requireActivity(),
                R.drawable.dialog_fragment_background2
            ), 0
        )
        Log.d("ProductConfirmDialogFragment", "orderNumber：" + order_number)
        dialog!!.window!!.setBackgroundDrawable(inset)
        val number = v.findViewById<TextView>(R.id.tv_ordernumber)
        number.text = order_number
        v.findViewById<ImageView>(R.id.btn_forward).setOnClickListener(this)
        v.findViewById<ImageView>(R.id.btn_cancel).setOnClickListener(this)

        return v
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_cancel -> {

                dismiss()
            }
            R.id.btn_forward -> {

                dismiss()
            }
        }
    }

}