package com.hkshopu.hk.ui.main.fragment

import android.graphics.drawable.InsetDrawable
import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.hkshopu.hk.R
import com.roan.lyde.qoqo.component.EventShopNameUpdated
import com.roan.lyde.qoqo.utils.rxjava.RxBus
import kotlinx.android.synthetic.main.dialog_fragment_des_add.*
import org.jetbrains.anko.find
import java.util.regex.Pattern

class AddNameDialogFragment : DialogFragment(), View.OnClickListener {
    companion object {
        val TAG = AddNameDialogFragment::class.java.simpleName

        /**
         * Create a new instance of MyDialogFragment, providing "num"
         * as an argument.
         */
        fun newInstance(): AddNameDialogFragment {
            val f = AddNameDialogFragment()

            // Supply num input as an argument.
            val args = Bundle()
            //args.putInt("num", num);
            f.arguments = args
            return f
        }
    }
    var et_shopName:EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //        mEventBus = EventBus.getDefault();
        setStyle(STYLE_NORMAL, R.style.Theme_App_Dialog_ShrinkScreen)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.dialog_fragment_shopname_add, container, false)
        val inset = InsetDrawable(
            ContextCompat.getDrawable(
                activity!!,
                R.drawable.dialog_fragment_background
            ), 0
        )
        dialog!!.window!!.setBackgroundDrawable(inset)
        et_shopName = v.find<EditText>(R.id.et_shopname)
        et_shopName!!.setSingleLine(true)
        et_shopName!!.setInputType(InputType.TYPE_CLASS_TEXT)

        v.findViewById<View>(R.id.btn_cancel_shopname).setOnClickListener(this)
        v.findViewById<View>(R.id.btn_confirm_shopname).setOnClickListener(this)
        val specialCharFilter =
            InputFilter { source, start, end, dest, dstart, dend ->
                val regexStr = "[`~!@#$%^&*()+=|{}':;'\",\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]"
                val pattern = Pattern.compile(regexStr)
                val matcher = pattern.matcher(source.toString())
                if (matcher.matches()) {
                    ""
                } else {
                    null
                }
            }
        et_shopName!!.setFilters(arrayOf(specialCharFilter))
        return v
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_cancel_shopname -> dismiss()
            R.id.btn_confirm_shopname -> {
                var ShopName = et_shopName!!.text.toString()
                if(!ShopName.isEmpty()){
                    RxBus.getInstance().post(EventShopNameUpdated(ShopName))
                    dismiss()
                }
            }
        }
    }
}