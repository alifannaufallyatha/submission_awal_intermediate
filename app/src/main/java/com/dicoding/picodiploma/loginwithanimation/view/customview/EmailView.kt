package com.dicoding.picodiploma.loginwithanimation.view.customview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import androidx.appcompat.widget.AppCompatEditText

class EmailView : AppCompatEditText{

    constructor(context: Context) : super(context) {
        setup()
    }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        setup()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        setup()
    }

    private fun setup(){
        this.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                var isValidEmail = false
                isValidEmail = Patterns.EMAIL_ADDRESS.matcher(text.toString().trim()).matches()

                error = if(!isValidEmail){
                    "invalid email addres"
                }else{
                    null
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })
    }

}