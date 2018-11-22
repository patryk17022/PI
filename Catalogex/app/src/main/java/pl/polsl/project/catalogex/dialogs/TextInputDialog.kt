package pl.polsl.project.catalogex.dialogs

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.*
import pl.polsl.project.catalogex.R
import kotlinx.android.synthetic.main.dialog_text_input.*
import android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT
import android.content.Context.INPUT_METHOD_SERVICE
import android.view.inputmethod.InputMethodManager


interface TextInputDialogInterface {
    fun doPositiveClick(tag:String, input:String, position: Int)
    fun doNegativeClick(tag:String, input:String, position: Int)
}

class TextInputDialog : DialogFragment() {

    var labelText: String? = null
    var position: Int = -1
    var activity: Activity? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_text_input, container)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        LabelFeature.text = labelText

        featureValueInput.setOnKeyListener { view, i, keyEvent ->
            if ( i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                acceptAction()
            }
            true
        }

        cancleButtonDialog.setOnClickListener{ view -> dismiss() }

        acceptButtonDialog.setOnClickListener{ view -> acceptAction()}
    }

    fun acceptAction(){
        ( activity as TextInputDialogInterface).doPositiveClick(tag!!,featureValueInput.text.toString(),position!!)
        dismiss()
    }

    override fun onResume() {
        super.onResume()
        featureValueInput.text = null
        showSoftKeyboard(featureValueInput)
    }

    //TODO NIE DZIALA
    fun showSoftKeyboard(view: View) {
        if(activity != null) {
            if (view.requestFocus()) {
                val imm = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
            }
        }
    }

}