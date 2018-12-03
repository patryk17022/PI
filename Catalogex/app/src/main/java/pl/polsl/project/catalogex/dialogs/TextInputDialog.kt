package pl.polsl.project.catalogex.dialogs

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.*
import pl.polsl.project.catalogex.R
import kotlinx.android.synthetic.main.dialog_text_input.*
import android.view.inputmethod.InputMethodManager
import pl.polsl.project.catalogex.interfaces.TextInputDialogInterface

@Suppress("UNUSED_ANONYMOUS_PARAMETER")
class TextInputDialog : DialogFragment() {

    private var labelText: String? = null
    private var position: Int = -1
    private var activity: Activity? = null

    fun setText(text:String?){
        this.labelText = text
    }

    fun setPosition(pos:Int){
        this.position = pos
    }

    fun setActivity(actv: Activity?){
        this.activity = actv
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_text_input, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        LabelFeature.text = labelText

        featureValueInput.setOnKeyListener { viewL, i, keyEvent ->
            if ( i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                acceptAction()
            }
            true
        }

        cancleButtonDialog.setOnClickListener{
            viewL ->
            softKeyboard(featureValueInput,true)
            dismiss()
        }

        acceptButtonDialog.setOnClickListener{ acceptAction()}
    }

    private fun acceptAction(){
        ( activity as TextInputDialogInterface).doPositiveClick(tag!!,featureValueInput.text.toString(),position)
        softKeyboard(featureValueInput,false)
        dismiss()
    }

    override fun onResume() {
        super.onResume()
        featureValueInput.text = null
        softKeyboard(featureValueInput,true)
    }

    override fun onPause() {
        softKeyboard(featureValueInput,false)
        super.onPause()
    }

    private fun softKeyboard(view: View, show:Boolean) {
        if(activity != null && view.requestFocus()) {

            val imm = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

            if(show)
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0)
            else {
                imm.hideSoftInputFromWindow(activity!!.window.decorView.rootView.windowToken, 0)
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }
    }
}