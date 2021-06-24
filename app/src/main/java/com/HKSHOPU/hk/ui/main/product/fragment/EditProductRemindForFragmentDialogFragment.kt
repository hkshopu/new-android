package com.HKSHOPU.hk.ui.main.product.fragment

import android.content.Intent
import android.graphics.drawable.InsetDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView

import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.HKSHOPU.hk.R
import com.HKSHOPU.hk.ui.main.product.activity.EditProductActivity

class EditProductRemindForFragmentDialogFragment(var fragment: Fragment): DialogFragment(), View.OnClickListener {


    var signal : Boolean = false

//    companion object {
//        val TAG = StoreOrNotDialogFragment::class.java.simpleName
//
//        /**
//         * Create a new instance of MyDialogFragment, providing "num"
//         * as an argument.
//         */
//        fun newInstance(): StoreOrNotDialogFragment {
//            val f = StoreOrNotDialogFragment()
//
//            // Supply num input as an argument.
//            val args = Bundle()
//            //args.putInt("num", num);
//            f.arguments = args
//            return f
//        }
//    }
    var et_shopDes:EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //        mEventBus = EventBus.getDefault();
        setStyle(STYLE_NORMAL, R.style.Theme_App_Dialog_ShrinkScreen)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.dialog_fragment_edit_product_remind, container, false)
        val inset = InsetDrawable(
            ContextCompat.getDrawable(
                activity!!,
                R.drawable.dialog_fragment_background
            ), 0
        )
        dialog!!.window!!.setBackgroundDrawable(inset)

        v.findViewById<ImageView>(R.id.btn_cancel_remind).setOnClickListener(this)
        v.findViewById<ImageView>(R.id.btn_edit_keep_goning).setOnClickListener(this)
        return v
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_cancel_remind -> {
                dismiss()
            }
            R.id.btn_edit_keep_goning->{

                var currentActivity : FragmentActivity = fragment.activity!!

                val intent = Intent(currentActivity, EditProductActivity::class.java)
                startActivity(intent)

                dismiss()

            }
        }
    }

}