package pl.polsl.project.catalogex.dialogs

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.*
import pl.polsl.project.catalogex.R
import kotlinx.android.synthetic.main.dialog_text_input.*
import android.content.Context.INPUT_METHOD_SERVICE
import android.view.inputmethod.InputMethodManager


interface TextInputDialogInterface {
    fun doPositiveClick(tag:String, input:String, position: Int)
    fun doNegativeClick(tag:String, input:String, position: Int)
}

class TextInputDialog : DialogFragment() {

    var labelText: String? = null
    var position: Int = -1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_text_input, container)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        featureValueInput.text = null

        LabelFeature.text = labelText

        cancleButtonDialog.setOnClickListener{ view -> dismiss() }

        acceptButtonDialog.setOnClickListener{ view -> ( activity as TextInputDialogInterface).doPositiveClick(tag!!,featureValueInput.text.toString(),position!!); dismiss()}
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}